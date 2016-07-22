/**
 * File: Breakout.java
 * -------------------
 * Name:Munisha
 * Section Leader:
 * Munisha Choudhary
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.event.*;

public class MyBreakOut extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 40;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Separation between bricks */
	private static final int LIFE_SEP = 3;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
/**Pause or Delay in game*/
	private static final int DELAY=17;
/** Starting velocity of ball*/
	private static final double START_VEL=3.0;
	
	public void run() 
	{	
		setUp();
		Start=false;
		while(Start==false)
		{
			//infinite loop until Start is true
		}
		
		for (int turn=0; turn<NTURNS; turn++)
		{	
			playGame();
			
			if(wins()) break;	
			
			else if(loose())
			{
				remove(life[NTURNS-turn-1]);	//decrement life by 1
				remove(ball);
				
				lose= new GLabel("LOSE!!");
				lose.setFont("SansSerif-36");
				lose.setColor(Color.RED);
				add(lose,(gameBoardX+WIDTH/2)-lose.getWidth()/2,HEIGHT/2+lose.getHeight());
				
				pause(2000);			//delay before adding new ball
				
				remove(lose);
				addBall();	
				
					
			}
			
		}
		remove(ball);					
		End= new GLabel("GAME OVER");
		End.setFont("SansSerif-36");
		End.setColor(Color.RED);
		add(End,(gameBoardX+WIDTH/2)-End.getWidth()/2,HEIGHT/2+End.getHeight());
	}
	
	private void setUp()
	{
			
		//Add box for game
		gameBoard =new GRect(APPLICATION_WIDTH,APPLICATION_HEIGHT);
		gameBoardX=getWidth()/2+APPLICATION_WIDTH/2;
		gameBoardY=(getHeight()/2-APPLICATION_HEIGHT/3);
		add(gameBoard,gameBoardX,gameBoardY);
	
		//add bricks
		double x=gameBoardX+BRICK_SEP/2;
		double y=gameBoardY+BRICK_Y_OFFSET;
		for(int i=0; i<NBRICK_ROWS;i++)
		{
			for (int j=0; j<NBRICKS_PER_ROW;j++)
			{			
				bricks= new GRect(x,y,BRICK_WIDTH,BRICK_HEIGHT);
				bricks.setFilled(true);
				if((i==0) || (i ==1))
				{
					bricks.setFillColor(Color.RED);
				} 
				if((i==2) || (i ==3))
				{
					bricks.setFillColor(Color.ORANGE);
				} 
					if((i==4) || (i ==5))
				{
					bricks.setFillColor(Color.YELLOW);
				} 
				if((i==6) || (i ==7))
				{
					bricks.setFillColor(Color.GREEN);
				} 
				if((i==8) || (i ==9))
				{
					bricks.setFillColor(Color.CYAN);
				} 
				add(bricks);
				x=x+BRICK_WIDTH+BRICK_SEP;
			}
			y=y+BRICK_HEIGHT+BRICK_SEP;
			x=gameBoardX+BRICK_SEP/2;
			}
			
		//Count the number of Bricks
		CountBricks=(NBRICK_ROWS*NBRICKS_PER_ROW);
			
		//add paddle
		paddle= new GRect(PADDLE_WIDTH ,PADDLE_HEIGHT);
		paddle.setFilled(true);
		double paddle_x=gameBoardX+(WIDTH-PADDLE_WIDTH)/2;
		double paddle_y=gameBoardY+(HEIGHT-PADDLE_Y_OFFSET);
		add(paddle,paddle_x,paddle_y);
			
		//addBall
		addBall();
			
		Lives();
		
		// velocities Starting values
		vx=rgen.nextDouble(1.0,3.0); //randomly generate velocity of ball in x axis
		if(rgen.nextBoolean(0.5)) vx=-vx; //make vx negative half the time
		vy =START_VEL;
			
		startLabel= new GLabel("Click to Play");
		startLabel.setFont("SansSerif-36");
		startLabel.setColor(Color.BLACK);
		add(startLabel,(gameBoardX+WIDTH/2)-startLabel.getWidth()/2,HEIGHT/2+startLabel.getHeight());
			
		//call when mouse is clicked
		addMouseListeners();
			
	}
	
	public void mouseClicked(MouseEvent e)
	{
		Start=true;
		remove(startLabel);
			
	}
		
	//add Lives
	private void Lives()
	{	
		double lifeX=gameBoardX;
		double lifeY=gameBoardY-2.5*BALL_RADIUS;
		for(int i=0; i<NTURNS; i++)
		{
			life[i] = new GOval(2*BALL_RADIUS,2*BALL_RADIUS);
			life[i].setFilled(true);
			life[i].setColor(Color.BLACK);
			add(life[i],lifeX,lifeY);
			lifeX=lifeX+2*BALL_RADIUS+LIFE_SEP;
		}
			
	}

	//add ball
	private void addBall()
	{	
		ball= new GOval (2*BALL_RADIUS,2*BALL_RADIUS);
		ball.setFilled(true);
		add(ball,(gameBoardX+WIDTH/2)-BALL_RADIUS,(gameBoardY+HEIGHT/2));
	}
	
	//set the boundation of paddle
	public void mouseDragged(MouseEvent e)
	{
		if(paddle != null)
		{
			double x=e.getX();	
			if(x >(gameBoardX)&& (x<=((gameBoardX+APPLICATION_WIDTH)-PADDLE_WIDTH)))
			{
				paddle.move(x-paddle.getX(), 0);
			}

		}
	}
	
	private void playGame()
	{
		while(!(gameOver()))
		{
			MoveBall();
			CheckForCollision();
			pause(DELAY);
		}
	}

	private void MoveBall()
	{	
		ball.move(vx, vy);	
		if((ball.getY()+ball.getHeight())>=(gameBoardY+HEIGHT)) //hit base of gameBoard
		{
			 vy=-vy;															//negative the velocity
		}
		else if(ball.getY()<=gameBoardY)										 //hit top of gameBoard
		{
			vy=-vy;
		}
			
		else if((ball.getX()+ball.getWidth())>=(gameBoardX+WIDTH))                //hit the rightside	
		{	
			vx=-vx;
		}
		else if((ball.getX()<=gameBoardX))										//hit the left side
		{
			vx=-vx;
		}
	}
	
	private void CheckForCollision()
	{
		GObject collider = getCollidingObject();
		AudioClip bricksClip= MediaTools.loadAudioClip("bounce.au");
		if (collider==paddle)
		{	

			vy=-vy;
		}
		else if ((collider == gameBoard))
		{
			
		}
		else 
		{	
			bricksClip.play();
			remove (collider);
			vy=-vy;
			CountBricks =CountBricks-1;
			if(CountBricks==0)
			{
				wins= new GLabel("WINNER");
				wins.setFont("SansSerif-36");
				wins.setColor(Color.MAGENTA);
				add(wins,(gameBoardX+WIDTH/2)-wins.getWidth()/2,gameBoardY+wins.getHeight());
			}
		}
	}
	
	//Returns the colliding objects
	 private GObject getCollidingObject()
	 {
		 GObject collideObj = null;
		 
		 if(getElementAt(ball.getX()+2*BALL_RADIUS,ball.getY()+2*BALL_RADIUS)!=null)		//hit at bottom right corner of ball
		 {	
			 collideObj=getElementAt(ball.getX()+2*BALL_RADIUS,ball.getY()+2*BALL_RADIUS);
		 }
		 else if(getElementAt(ball.getX(),ball.getY()+2*BALL_RADIUS)!=null)					//hit at bottom left corner of ball
		 {
			 collideObj=getElementAt(ball.getX(),ball.getY()+2*BALL_RADIUS);				
		 }
		 else if(getElementAt(ball.getX()+2*BALL_RADIUS,ball.getY())!=null)					//hit at top right corner of ball
		 {
			 collideObj=getElementAt(ball.getX()+2*BALL_RADIUS,ball.getY());
		 }
		 else if(getElementAt(ball.getX(),ball.getY())!=null)								//hit at top left corner of ball
		 {	
		 collideObj=getElementAt(ball.getX(),ball.getY());
	     }
	 
		 return collideObj;
		 
	}
	 	
	private boolean gameOver()
	{
		return(loose()|| wins());
	}
	 	
	private boolean loose()
	{	
		return((ball.getY()+ball.getHeight())>=(gameBoardY+HEIGHT));
	}
	 	
	private boolean wins()
	{
		return((CountBricks==0));
	}
	 	
//Instance Variables
	GLabel startLabel,wins,lose,End;
	private boolean Start;
	private GRect gameBoard;
	private GRect bricks,paddle;
	private GOval ball;
	private double gameBoardX,gameBoardY;
	private double vx, vy; //velocity of ball
	private int CountBricks;
	private GOval[] life= new GOval[NTURNS];
	private RandomGenerator rgen = RandomGenerator.getInstance();
	

}

