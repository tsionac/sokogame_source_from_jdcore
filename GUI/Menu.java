package GUI;

import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu extends JPanel{
	
	private Board board;
	
	protected JLabel steps;
	protected JLabel print_steps;
	
	protected JComboBox stages;
	
	private String[] stage_text;
	private final int lvl_num = 6; //number of stage
	
	public Menu(Board board){
		super(new FlowLayout());
		this.board = board;
		stage_text = new String[lvl_num];
		addLevelsToArray();

		
		stages = new JComboBox(stage_text);
		steps = new JLabel(board.getSteps() + "");
		print_steps = new JLabel("Steps");
		
		this.add(print_steps);
		this.add(steps);
		this.add(stages);
		
		stages.setFocusable(false);
			
	}
	
	//init the JCombobox with the lvl numbers
	private void addLevelsToArray(){
		for(int i = 0; i < lvl_num; i++)
			stage_text[i] = i + 1 + "";
	}

}
