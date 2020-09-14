//This is so we can make our own J panel that will allow us
//to make use of double buffering.

package flappyBird;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Render extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);// we are overiding the paintComponets method from the jpanel class with our own definition
		FlappyBird.flappybird.repaint(g);
	}

	
}
