package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JFrame implements KeyListener, ActionListener {
	
	protected Board board;
	
	private Menu menu;
	private Menu menu2;
	
	private SkinMenu skin_menu;
	
	private JLabel lbl_background;
	
	private JButton btn_start;
	private JButton btn_reset;
	private JButton btn_select;
	private JButton btn_skins;
	private JButton btn_go_back;
	private JButton btn_undo;
	
	protected JPanel buttons;

	public Game() throws IOException {
		super("sokoban");
	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		buttons = new JPanel();
		
		buttons.setLayout(null);
		
		lbl_background = new JLabel(new ImageIcon(new ImageIcon(this.getClass().getResource("/images/background.png")).getImage()));
		
		//set buttons
		btn_skins = new JButton("Skins");
		btn_start = new JButton("START");
		btn_reset = new JButton("Reset");
		btn_select = new JButton("Select");
		btn_go_back = new JButton("Menu");
		btn_undo   =  new JButton("Undo");
		
		//set the board and the panels 
		skin_menu = new SkinMenu(this);
		board = new Board();
		menu = new Menu(board);
		menu2 = new Menu(board);
		board.requestFocus();

		//reduce the focus from the button to focus the board (game).
		btn_go_back.setFocusable(false);
		btn_start.setFocusable(false);
		btn_select.setFocusable(false);
		btn_skins.setFocusable(false);
		btn_reset.setFocusable(false);
		btn_undo.setFocusable(false);
		
		this.addKeyListener(this);
	
		//adding the 2 menus in game
		menu.add(btn_select);
		menu2.add(btn_reset);
		menu.add(btn_go_back);
		menu2.add(btn_undo);
		menu2.remove(menu2.stages);
		menu2.remove(menu2.steps);
		menu2.remove(menu2.print_steps);

		//add the buttons panel
		this.getContentPane().add(buttons);

		buttons.add(btn_start);
		buttons.add(btn_skins);
		
		//set the buttons as listeners
		btn_go_back.addActionListener(this);
		btn_skins.addActionListener(this);
		btn_start.addActionListener(this);
		btn_reset.addActionListener(this);
		btn_select.addActionListener(this);
		btn_undo.addActionListener(this);
		
		//main menu buttons settings
		buttons.setBounds(0, 0, 500, 500);
		btn_start.setBounds(200, 150, 80, 30);
		btn_skins.setBounds(200, 200, 80, 30);
		lbl_background.setBounds(0, 0, 500, 500);
		
		//background image
		buttons.add(lbl_background);
	
		//set JFrame size
		this.setSize(500, 500);

		setResizable(false);
		setVisible(true);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		//check what button was pressed and use Move function with the correct input
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			board.move(-1, 0, "Left");
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			board.move(1, 0, "Right");
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			board.move(0, -1, "Up");
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			board.move(0, 1, "Down");
		}

		if (board.checkEndGame()) { //in case the game ends pop up a massage 
			JOptionPane.showMessageDialog(this, "Bravo! you finish the stage.");
			try {
				board.resetStage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		menu.steps.setText(board.getSteps() + ""); //update the steps in the Jlabel
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//check what button was pressed and act
		if (e.getSource().equals(btn_select)) { //update the board with the same stage
			board.updateBoard(menu.stages.getSelectedIndex());
			menu.steps.setText(board.getSteps() + "");
			pack();
		} else if (e.getSource().equals(btn_reset)) { //use the reset button
			try {
				board.resetStage();
				menu.steps.setText(board.getSteps() + "");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//set visible as false to swap the panels, and make visible true to other panels 
		} else if(e.getSource().equals(btn_start)){
			buttons.setVisible(false);	
			this.setLayout(new BorderLayout()); 
			getContentPane().add(board, BorderLayout.CENTER);
			getContentPane().add(menu, BorderLayout.NORTH);
			getContentPane().add(menu2, BorderLayout.SOUTH);
			board.setVisible(true);
			menu.setVisible(true);
			menu2.setVisible(true);
			this.pack();
			//set visible as false to swap the panels, and make visible true to other panels 
		} else if(e.getSource().equals(btn_skins)){
			buttons.setVisible(false);
			this.setLayout(new BorderLayout());
			getContentPane().add(skin_menu, BorderLayout.CENTER);
			skin_menu.setVisible(true);
			//set visible as false to swap the panels, and make visible true to other panels 
		} else if(e.getSource().equals(btn_go_back)){
			board.setVisible(false);
			menu.setVisible(false);
			menu2.setVisible(false);
			buttons.setVisible(true);
			this.setSize(500, 500);
		} else if(e.getSource().equals(btn_undo)){
			if(board.getSteps() > 0){
				board.Undo();
				menu.steps.setText(board.getSteps() + "");
			}

		}

	}
}
