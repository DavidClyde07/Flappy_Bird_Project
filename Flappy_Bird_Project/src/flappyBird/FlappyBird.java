
package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener{
	
	////////////////////////////////////////////////////////
	// Variables
	///////////////////////////////////////////////////////
	
	
	
	//create a new static variable called flappybird, of type FlappyBird
	public static FlappyBird flappybird;
	public final int WIDTH = 700, HEIGHT = 700;  //variables for frame dimensions
	public Render render;
	public Rectangle bird;
	public int ticks, ymotion, score;
	public Random random;
	//Array List of type rectangle to store all the different columns/pipes
	public ArrayList<Rectangle> columns;
	public boolean gameOver, started ;
	
	
	//Constructor to create flappyBird objects
	public FlappyBird() {
		
		Timer timer = new Timer(20, this);
		
		JFrame jframe = new JFrame(); 
		render = new Render();
		random = new Random();
		
		jframe.setSize(WIDTH,HEIGHT);
		jframe.setVisible(true);
		jframe.setTitle("Flappy Bird");
		jframe.setResizable(false);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.addMouseListener(this);
		jframe.add(render);
		
		//Rectange() parameters are : x, y, size,size (ie here we get a 20 by 20 rectangle)
		//offsett the bird by 10 so its just off center
		bird = new Rectangle(WIDTH/2 - 10 , HEIGHT/2 - 10 , 20 , 20);
		columns = new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
	}
	
	/*
	 * Method to add columns to the screen as they are needed.
	 */
	
	public void addColumn(boolean start) {
		
		int space=300; //space between pipes
		int width =100; //width of the pipes
		int height = 50 + random.nextInt(300);//the heights of each the new columns will vary randomly
	
		if (start) {
			//Columns from the bottom of frame , height -120 so that it starts at the top of the grass
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height -120, width, height));
			//columns from the top of frame
			columns.add(new Rectangle(WIDTH + width + (columns.size()-1) * 300, 0, width, HEIGHT - height - space));
			
		}
		else {
			
			//Columns from the bottom of frame
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height -120, width, height));
			//columns from the top of frame
			columns.add(new Rectangle(columns.get(columns.size() - 1).x , 0, width, HEIGHT - height - space));
		}
		
	}
	
	//Method to make the bird jump in response to mouse clicks
	public void jump() {
		
		if(gameOver) {
			
			//Rectange() parameters are : x, y, size,size (ie here we get a 20 by 20 rectangle) 
			bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10 , 20 , 20);
			columns.clear();
			ymotion = 0;
			score = 0;
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver = false;
		}
		
		//Started is a boolean which is by default initialized to false 
		if(!started) {
			started = true;
		}
		
		else if (! gameOver) {
			
			if(ymotion >0) {
				ymotion = 0;
			}
			//set the height the bird jumps with each mouse click
			ymotion -= 7;
		}
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int speed = 10;
		ticks++ ;
		
		if(started) {
			for (int i = 0; i < columns.size() ; i++) {
				
				Rectangle column = columns.get(i);
				column.x -= speed;
			}
			
			
			if (ticks % 2 == 0 && ymotion < 15) {
				
				ymotion += 2 ;
			}
			
			for (int i = 0; i < columns.size() ; i++) {
				
				Rectangle column = columns.get(i);
				if (column.x + column.width<0) {
					columns.remove(column);
					if(column.y == 0) {
						
					addColumn(false);
					
					}
				}
			}
			
			bird.y += ymotion;
			//After the bird has moved check for a collision
			
			for (Rectangle column : columns) {
				
				//If the bird pass the center of the column then increment the score, it can only be at the middle once. Only count the columns once.column.y == 0
				if (column.y == 0 && bird.x + bird.width/2 > column.x +column.width/2 -10 && bird.x + bird.width/2 < column.x +column.width/2 +10) {
					score++;
					//System.out.println(score);
				}
				
					
				if (column.intersects(bird)) {
					gameOver = true;
					//if bird falls it cant pass through the column/pipe
					bird.x = column.x - bird.width;
				}
			}
			
			//If the birds hits the ground or the top of the sky then game over!
			if(bird.y > HEIGHT-120 || bird.y < 0) {
				gameOver = true;
			}
			
			if(gameOver) {
				bird.y = HEIGHT - 120 - bird.height;
			}
		}		
		render.repaint();
	}
	
	
	/*
	 * Method to repaint the background (ie: sky ground and grass)
	 */
	public void repaint(Graphics g) {
		// TODO Auto-generated method stub
		//System.out.println("Helloeeeeeeee");
		
		//The coordinate system starts with 0,0 in the top left
		
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT-120 , WIDTH,120 );
		
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT-120 , WIDTH,20 );
		
		//For each column in the array list columns, paint the column by calling the paintColumn method
		for (Rectangle column: columns) {
			
			paintColumn(g, column);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial" ,1,50));
		
		if (!started) {
			//System.out.println("Game Over!!!");
			g.drawString("Click To Start!", 100, HEIGHT/2 - 50);
			//gameOver = false;
		}
		
		if (gameOver) {
			//System.out.println("Game Over!!!");
			g.drawString("Game Over!!!", 100, HEIGHT/2 - 50);
			//started = false;
		}
		
		if(!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH/2 -25, 100);
		}
	}
	
	
	/*
	 * Method to repaint the columns as they are added
	 */
	public void paintColumn(Graphics g, Rectangle column) {
		
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	

	
	public static void main(String[]args) {
		//Create a new flappy bird object
		flappybird = new FlappyBird();
		
		//System.out.println("Heloo");
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		jump();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
