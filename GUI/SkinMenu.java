package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SkinMenu extends JPanel implements ActionListener {
	
	private Game game;
	private JButton skin0;
	private JButton skin1;
	private JLabel lbl_background;
	
	public SkinMenu(Game game){
		this.game = game;
		
		
		lbl_background = new JLabel(new ImageIcon(new ImageIcon(this.getClass().getResource("/images/skinbackgroun.png")).getImage()));
		
		this.setLayout(null);
		
		skin0 = new JButton("Classic");
		skin1 = new JButton("Pokemon");
		
		skin0.setFocusable(false);
		skin1.setFocusable(false);
		
		add(skin0);
		add(skin1);
		
		skin0.addActionListener(this);
		skin1.addActionListener(this);
	
		//set the buttons location
		skin0.setBounds(100, 30, 120, 30);
		skin1.setBounds(260, 30, 120, 30);
		lbl_background.setBounds(0, 0, 500, 500);
		
		
		add(lbl_background);
		
		this.setSize(500,500);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		//choose the correct skin 
		if(e.getSource().equals(skin0)){
			this.setVisible(false);
			game.board.skin_num = 0;
			game.board.updateBoard(-1);
			game.buttons.setVisible(true);
			game.setSize(500, 500);
			
		}else if(e.getSource().equals(skin1)){
			this.setVisible(false);
			game.board.skin_num = 1;
			game.board.updateBoard(-1);
			game.buttons.setVisible(true);
			game.setSize(500, 500);
		}
			
		
	}

}
