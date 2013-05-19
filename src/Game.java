/**
 * Lawrence Huang FP
 *
 * main class for Angry Birds in Space, final project.
 * this class creates a GameScreen object and a
 * PhysicsEngine object when a level is selected.
 * RigidBody objects are read from a text file and 
 * added to the PhysicsEngine. The PhysicsEngine is updated
 * between frames at 100 frames per second.
 */

import java.util.*;
import java.io.*;

public class Game 
{
    public static void main(String[] args)
{
		//user interface and graphics
		GameScreen frame=new GameScreen();
		
		//two variables used for delay
		double lastUpdate=System.currentTimeMillis ();
		double sleepLen;
		
		//the current screen or level
		//1=start, 2=help,3=select level, 4-6 are levels,7=menu
		//8=win 9=loss
		int state=1;
		
		while(state!=0)
		{
			while(state<4)
			{
				//start, help, and level screens before
				//a level is selected
				frame.update();
				state=frame.checkMouse();
			}
			
			//creates a PhysicsEngine object used for the level and
			//adds all RigidBody objects from the appropriate text file. 
			ArrayList<RigidBall> ballArray=new ArrayList<RigidBall>();
			ArrayList<RigidBox> boxArray=new ArrayList<RigidBox>();
			ArrayList<RigidBox> wallArray=new ArrayList<RigidBox>();
			ArrayList<RigidBall> pigArray=new ArrayList<RigidBall>();
			ArrayList<RigidBall> birdArray=new ArrayList<RigidBall>();
			try
			{
				Scanner inFile=new Scanner(new BufferedReader(new FileReader(new File("RigidBodyData/balls"+(state-3)+".txt"))));
				while(inFile.hasNextLine())
				{
					String[] ballInfo=inFile.nextLine().split(" ");
					ballArray.add(new RigidBall(Double.parseDouble(ballInfo[0]),Double.parseDouble(ballInfo[1]),Double.parseDouble(ballInfo[2]),
					Integer.parseInt(ballInfo[3]),Double.parseDouble(ballInfo[4]),Integer.parseInt(ballInfo[5]),Integer.parseInt(ballInfo[6])));
				}
				inFile=new Scanner(new BufferedReader(new FileReader("RigidBodyData/pigs"+(state-3)+".txt")));
				while(inFile.hasNextLine())
				{
					String[] ballInfo=inFile.nextLine().split(" ");
					pigArray.add(new RigidBall(Double.parseDouble(ballInfo[0]),Double.parseDouble(ballInfo[1]),Double.parseDouble(ballInfo[2]),
					Integer.parseInt(ballInfo[3]),Double.parseDouble(ballInfo[4]),Integer.parseInt(ballInfo[5]),Integer.parseInt(ballInfo[6])));
				}
				inFile=new Scanner(new BufferedReader(new FileReader("RigidBodyData/boxes"+(state-3)+".txt")));
				while(inFile.hasNextLine())
				{
					String[] boxInfo=inFile.nextLine().split(" ");
					boxArray.add(new RigidBox(Double.parseDouble(boxInfo[0]),Double.parseDouble(boxInfo[1]),Double.parseDouble(boxInfo[2]),
					Integer.parseInt(boxInfo[3]),Double.parseDouble(boxInfo[4]),Integer.parseInt(boxInfo[5]),Integer.parseInt(boxInfo[6]),Integer.parseInt(boxInfo[7])));
				}
				inFile=new Scanner(new BufferedReader(new FileReader("RigidBodyData/walls"+(state-3)+".txt")));
				while(inFile.hasNextLine())
				{
					String[] wallInfo=inFile.nextLine().split(" ");
					wallArray.add(new RigidBox(Double.parseDouble(wallInfo[0]),Double.parseDouble(wallInfo[1]),Double.parseDouble(wallInfo[2]),
					Integer.parseInt(wallInfo[3]),Double.parseDouble(wallInfo[4]),Integer.parseInt(wallInfo[5]),Integer.parseInt(wallInfo[6]),Integer.parseInt(wallInfo[7])));
				}
				inFile=new Scanner(new BufferedReader(new FileReader("RigidBodyData/birds"+(state-3)+".txt")));
				while(inFile.hasNextLine())
				{
					String[] birdInfo=inFile.nextLine().split(" ");
					birdArray.add(new RigidBall(Double.parseDouble(birdInfo[0]),Double.parseDouble(birdInfo[1]),Double.parseDouble(birdInfo[2]),
					Integer.parseInt(birdInfo[3]),Double.parseDouble(birdInfo[4]),Integer.parseInt(birdInfo[5]),Integer.parseInt(birdInfo[6])));
				}
				
			}catch(IOException e)
			{
				System.out.println("missing program files");
			}
			PhysicsEngine engine=new PhysicsEngine(boxArray,ballArray,wallArray,pigArray);
			
			
			while(state>=4)
			{
				
				if(engine.checkWin()&&engine.getBirds().size()==0)
				{
					state=8;
					frame.setState(8);
				}
				else if(engine.getBirds().size()==0&&birdArray.size()==0)
				{
					state=9;
					frame.setState(9);
				}
				state=frame.checkMouse();
				frame.checkFire(engine,birdArray);
				//updates the physics engine(collisions and movement)
				//and draws the updated RigidBody positions to the screen
				engine.update();
				frame.update(engine);
				//checkKeys() changes zoom based on user input
				frame.checkKeys();
				
				
				//delay 25 milliseconds less time for system operations
				//code taken from Nettrek Example
				sleepLen=lastUpdate+10-System.currentTimeMillis();
				sleepLen=Math.max (2, sleepLen);
				delay((int)sleepLen);
				lastUpdate = System.currentTimeMillis ();
				while(state==7)
				{
					state=frame.checkMouse();
					frame.update(engine);
				}
				while(state==8)
				{
					state=frame.checkMouse();
					frame.update(engine);
				}
				while(state==9)
				{
					state=frame.checkMouse();
					frame.update(engine);
				}
			}
		}
    }
    public static void delay(int n)
    {
		try
		{
			Thread.sleep(n);
		}catch(InterruptedException e)
		{
			System.out.println("Error");
		}
	}
    
    
}
