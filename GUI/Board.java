package GUI;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;

import Data.Cell;
import Data.LevelLoader;
import Data.PairOfCell;
import Data.Player;

public class Board extends JPanel {

	protected ArrayList<HashMap<String, ImageIcon>> image; //list of images sets (can pick many sets)
	protected Player p;									   //player coordinates
	protected LevelLoader lvl;							   //set the levels update
	protected JLabel labelHolder[][];					   //matrix of our labels
	protected Cell[][] map;								   //matrix of our map level
	protected int w, h, stage, num_of_storages = 0, skin_num = 0;  

	boolean swap_two_cells = true;   //for undo function check if we need swap 2 or 3 cells
	
	public Board() throws IOException {
		super();
		initImages(); //init the images

		p = new Player(0, 0);

		lvl = new LevelLoader();
		lvl.load("levels.txt");
		
		map = lvl.get(0);
		h = map.length;
		w = map[0].length;
		
		stage = 0; //set defoult stage 1
		labelHolder = new JLabel[h][w]; 

		this.setLayout(new GridLayout(w, h)); //setting Grid Layout

		initLabelHolder(); //set Label Holder
		updateBoard(0);	   //set the Board
		printBoard();	   //print the Board

		this.setVisible(true);

	}

	private void initImages() {
		
		image = new ArrayList<HashMap<String, ImageIcon>>();
		// Classic skin
		image.add(new HashMap<String, ImageIcon>());
		image.get(0).put("wall", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/wall.gif")).getImage()));
		image.get(0).put("box", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/box.gif")).getImage()));
		image.get(0).put("playerDown", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/player.gif")).getImage()));
		image.get(0).put("playerUp", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/player_b.gif")).getImage()));
		image.get(0).put("playerLeft", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/player_l.gif")).getImage()));
		image.get(0).put("playerRight", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/player_r.gif")).getImage()));
		image.get(0).put("floor", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/floor.gif")).getImage()));
		image.get(0).put("storage", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/storage.gif")).getImage()));
		image.get(0).put("storage_box", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/storage_box.gif")).getImage()));

		// Pokemon skin
		image.add(new HashMap<String, ImageIcon>());
		image.get(1).put("wall", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/wall.gif")).getImage()));
		image.get(1).put("box", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/box1.gif")).getImage()));
		image.get(1).put("playerDown", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/player1.gif")).getImage()));
		image.get(1).put("playerUp", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/player1_b.gif")).getImage()));
		image.get(1).put("playerLeft", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/player1_l.gif")).getImage()));
		image.get(1).put("playerRight", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/player1_r.gif")).getImage()));
		image.get(1).put("floor", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/floor1.gif")).getImage()));
		image.get(1).put("storage", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/storage1.gif")).getImage()));
		image.get(1).put("storage_box", new ImageIcon(new ImageIcon(this.getClass().getResource("/images/storage_box1.gif")).getImage()));
	}

	//get the cordinats of the cells, position string for change the player image 
	public void move(int x, int y, String position) {

		//1st case after the player is empty floor in this case we just swap 2 cells
		if (map[p.getX() + x][p.getY() + y].isEmptyFloor()) {
			addToStack(map[p.getX()][p.getY()], map[p.getX() + x][p.getY() + y]); // adding 2 cells in the stack for the undo after
			lvl.get_steps().set(stage, lvl.get_steps().get(stage) + 1); // adding + 1 for the step counter value
			checkStorageAndMove();							  // check if the player stands on the storage
			labelHolder[p.getX() + x][p.getY() + y].setIcon(image.get(skin_num).get("player" + position)); //set the player image (+ position if he go up down left or right)
			SwapCells(map[p.getX()][p.getY()], map[p.getX() + x][p.getY() + y]);
			setPlayerNewLocation(x, y);
			
		} else if (map[p.getX() + x][p.getY() + y].hasBox()) { // second case have a box on the player direction
			if (map[p.getX() + 2 * x][p.getY() + 2 * y].isEmptyFloor()) { //if after the box is an empty floor then move it
				addToStack(map[p.getX()][p.getY()], map[p.getX() + x][p.getY() + y]); //adding 2 pairs of cells for the undo function
				addToStack( map[p.getX() + x][p.getY() + y], map[p.getX() + 2 * x][p.getY() + 2 * y]);
				lvl.get_steps().set(stage, lvl.get_steps().get(stage) + 1);
				checkStorageAndMove();
				labelHolder[p.getX() + x][p.getY() + y].setIcon(image.get(skin_num).get("player" + position));
				if(!map[p.getX() + 2 * x][p.getY() + 2 * y].isStorage())	//check if the next cell after the box is storage or no and make the image swap 
					labelHolder[p.getX() + 2 * x][p.getY() + 2 * y].setIcon(image.get(skin_num).get("box"));
				else
					labelHolder[p.getX() + 2 * x][p.getY() + 2 * y].setIcon(image.get(skin_num).get("storage_box"));
				map[p.getX()][p.getY()].set_hasPlayer(false);	//swap the cells data
				map[p.getX() + x][p.getY() + y].set_hasBox(false);
				map[p.getX() + x][p.getY() + y].set_hasPlayer(true);
				map[p.getX() + 2 * x][p.getY() + 2 * y].set_hasBox(true);
				setPlayerNewLocation(x, y);	//set player new location
			} 
		}
	}

	//set the matrix label with empty JLabels
	private void initLabelHolder() {
		labelHolder = new JLabel[h][w];
		for (int m = 0; m < w; m++)
			for (int n = 0; n < h; n++)
				labelHolder[n][m] = new JLabel();
	}
	//adding the JLabels to the JPanel
	private void printBoard() {

		this.removeAll();
		this.setLayout(new GridLayout(w, h));
		for (int m = 0; m < w; m++)
			for (int n = 0; n < h; n++)
				add(labelHolder[n][m]);

	}

	//set the JLabels with the correct stage (if stage -1 we set the correct stage)
	protected void updateBoard(int stage) {

		// for update the correct stage board
		if (stage == -1)
			stage = this.stage;

		num_of_storages = 0;
		this.stage = stage;
		map = lvl.get(stage);
		h = map.length;
		w = map[0].length;

		
		this.initLabelHolder();

		for (int m = 0; m < w; m++) {
			for (int n = 0; n < h; n++) {
				if(map[n][m].isStorage())
					num_of_storages++;
				if (map[n][m].hasBox() & !map[n][m].isStorage()) {
					labelHolder[n][m].setIcon(image.get(skin_num).get("box"));
				} else if (map[n][m].hasPlayer()) {
					p.setX(map[n][m]._x);
					p.setY(map[n][m]._y);
					labelHolder[n][m].setIcon(image.get(skin_num).get("playerDown"));
				} else if (map[n][m].isEmptyFloor() & !map[n][m].isStorage()) {
					labelHolder[n][m].setIcon(image.get(skin_num).get("floor"));
				} else if (map[n][m].isStorage() & !map[n][m].hasBox()) {
					labelHolder[n][m].setIcon(image.get(skin_num).get("storage"));
				} else if (map[n][m].isStorage() & map[n][m].hasBox()) {
					labelHolder[n][m].setIcon(image.get(skin_num).get("storage_box"));
				} else if (map[n][m].isWall()) {
					labelHolder[n][m].setIcon(image.get(skin_num).get("wall"));
				} else {
					labelHolder[n][m].setIcon(image.get(skin_num).get("floor"));
				}
			}
		
		}
		printBoard();
	}

	//Checking if the player stands on Storage or not and swich the images
	private void checkStorageAndMove() {
		if (map[p.getX()][p.getY()].isStorage())
			labelHolder[p.getX()][p.getY()].setIcon(image.get(skin_num).get("storage"));
		else
			labelHolder[p.getX()][p.getY()].setIcon(image.get(skin_num).get("floor"));
	}

	//set player coordinates
	private void setPlayerNewLocation(int x, int y) {
		p.setX(map[p.getX() + x][p.getY() + y]._x);
		p.setY(map[p.getX() + x][p.getY() + y]._y);
	}

	//check if the game ends by counting the storage boxes and number of storages
	public boolean checkEndGame() {
		int counter = 0;
		for (int m = 0; m < w; m++) {
			for (int n = 0; n < h; n++) {
				if (labelHolder[n][m].getIcon().equals(image.get(skin_num).get("storage_box")))
					counter++;
			}
		}
		if (counter == num_of_storages) {
			num_of_storages = 0;
			return true;
		}
		return false;
	}

	//return the step number
	public int getSteps() {
		return lvl.get_steps().get(stage);
	}

	//reset the correct stage by using the update Board func
	public void resetStage() throws IOException {
		LevelLoader copy_lvl = new LevelLoader();
		copy_lvl.load("levels.txt");
		
		lvl.setLvl(stage, copy_lvl.get(stage));
		lvl.setSteps(stage, 0);
		num_of_storages = 0;
		updateBoard(stage);
	}
	
	//adding 2 cells to the stack for the undo button
	public void addToStack(Cell a, Cell b) {
		PairOfCell poc = new PairOfCell(a, b);
		lvl.get_prev_steps().get(stage).push(poc);
	}

	//undo the last step by pops the last 2 cells and swap them
	public void Undo(){
		if(lvl.get_prev_steps().get(stage).isEmpty())
			return;
		PairOfCell poc = lvl.get_prev_steps().get(stage).pop();
		
		if(!poc.getB().hasBox()) // in this case we pop 2 (1 pair) cells and swap them
				SwapCells(poc.getA(),poc.getB());
		else{ //in this case we pop 4 ( 2 pairs) last cells and swap the needed cells
				SwapCells(poc.getA(), poc.getB());
				PairOfCell poc2 = lvl.get_prev_steps().get(stage).pop();
				SwapCells(poc2.getA(), poc.getB());
		}
		lvl.get_steps().set(stage, lvl.get_steps().get(stage) - 1); //discount step value
		updateBoard(-1);		//update board
	}

	//input 2 cells, swap the cells data
	public void SwapCells(Cell a , Cell b ){
		
		boolean player = b.hasPlayer();
		boolean box = b.hasBox();
		b.set_hasPlayer(a.hasPlayer());
		b.set_hasBox(a.hasBox());	
		a.set_hasBox(box);
		a.set_hasPlayer(player);

	}

}
