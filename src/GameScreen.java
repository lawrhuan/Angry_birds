
/**
 * Lawrence Huang FP
 *
 * The GameScreen class draws a representation
 * of the objects in the physics engine to the screen.
 * It is also an interface for the mouse and keyboard.
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.util.*;

class GameScreen extends JFrame implements MouseMotionListener, MouseListener, KeyListener
{
	//variables for mouse and keyboard interface
	private int mx,my,mb;
	private int releasedB;
	private boolean []keys;

	private boolean pulled;
	private LVector pullPosition;

	ArrayList<RigidBall> birds=new ArrayList<RigidBall>();

	//import all images needed in the game
	private Image dbImage;
	Image startBackground=new ImageIcon("Images/StartBackground.png").getImage();
	Image helpBackground=new ImageIcon("Images/HelpBackground.png").getImage();
	Image levelBackground=new ImageIcon("Images/LevelBackground.png").getImage();
	Image levelTwoBackground=new ImageIcon("Images/LevelTwoBackground.png").getImage();
	Image menuBackground=new ImageIcon("Images/MenuBackground.png").getImage();
	Image play1=new ImageIcon("Images/Play1.png").getImage();
	Image play2=new ImageIcon("Images/Play2.png").getImage();
	Image play3=new ImageIcon("Images/Play3.png").getImage();
	Image help1=new ImageIcon("Images/Help1.png").getImage();
	Image help2=new ImageIcon("Images/Help2.png").getImage();
	Image help3=new ImageIcon("Images/Help3.png").getImage();
	Image back1=new ImageIcon("Images/Back1.png").getImage();
	Image back2=new ImageIcon("Images/Back2.png").getImage();
	Image back3=new ImageIcon("Images/Back3.png").getImage();
	Image one1=new ImageIcon("Images/One1.png").getImage();
	Image one2=new ImageIcon("Images/One2.png").getImage();
	Image one3=new ImageIcon("Images/One3.png").getImage();
	Image two1=new ImageIcon("Images/Two1.png").getImage();
	Image two2=new ImageIcon("Images/Two2.png").getImage();
	Image two3=new ImageIcon("Images/Two3.png").getImage();
	Image three1=new ImageIcon("Images/Three1.png").getImage();
	Image three2=new ImageIcon("Images/Three2.png").getImage();
	Image three3=new ImageIcon("Images/Three3.png").getImage();
	Image menu1=new ImageIcon("Images/Menu1.png").getImage();
	Image menu2=new ImageIcon("Images/Menu2.png").getImage();
	Image menu3=new ImageIcon("Images/Menu3.png").getImage();
	Image exit1=new ImageIcon("Images/Exit1.png").getImage();
	Image exit2=new ImageIcon("Images/Exit2.png").getImage();
	Image exit3=new ImageIcon("Images/Exit3.png").getImage();
	Image resume1=new ImageIcon("Images/Resume1.png").getImage();
	Image resume2=new ImageIcon("Images/Resume2.png").getImage();
	Image resume3=new ImageIcon("Images/Resume3.png").getImage();
	Image wallImage=new ImageIcon("Images/Wall.png").getImage();
	Image metalBox=new ImageIcon("Images/MetalBox.png").getImage();
	Image glassBox=new ImageIcon("Images/GlassBox.png").getImage();
	Image metalBall=new ImageIcon("Images/MetalBall.png").getImage();
	Image glassBall=new ImageIcon("Images/GlassBall.png").getImage();
	Image pigImage=new ImageIcon("Images/Pig.png").getImage();
	Image birdImage=new ImageIcon("Images/Bird.png").getImage();
	Image birdGhost=new ImageIcon("Images/BirdGhost.png").getImage();
	Image missionAccomplished=new ImageIcon("Images/WinText.png").getImage();
	Image missionFailed=new ImageIcon("Images/LoseText.png").getImage();
	Image sling1=new ImageIcon("Images/Sling1.png").getImage();
	Image sling2=new ImageIcon("Images/Sling2.png").getImage();

	private Graphics dbg;
	private String col="";
	//the current screen or level.
	//changed in through user input in this class
	//state variable in main class is kept updated
	int state=1;

	//keep track of the computer's screen position
	//and magnification in relation to the PhysicsEngine
	//coordinates. Magnification is between 0.5 and 1 with 1
	//as the true magnification. Position is the top left corner
	//of the screen on the PhysicsEngine
	double magnification;
	int screenPositionX;
	int screenPositionY;

    public GameScreen() {
		super("Basic Frame");

		addKeyListener(this);
		addMouseListener (this);
		addMouseMotionListener(this);
		keys = new boolean[2000];
		setSize(1000,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		magnification=0.5;
		screenPositionX=0;
		screenPositionY=-700;
    }

    //The mouse controls movement between screens and levels.
    //Checks if the mouse has clicked a button and changes
    //both this state and Game class state
    public void setState(int state)
    {
    	this.state=state;
    }
    
    //checks if the bird is stretch or fired
    public void checkFire(PhysicsEngine engine,ArrayList<RigidBall> birdArray)
    {
    	birds=birdArray;
    	if(birdArray.size()>0)
    	{
    		double mouseX=(mx+screenPositionX)/magnification;
    		double mouseY=(my-screenPositionY)/magnification+700;

			//bird is stretched and ready to fire
    		if(mb==1&&PhysicsEngine.distance(mouseX,mouseY,200,0)<25&&pulled==false)
	    	{
	    		pulled=true;
	    		//vector relates mouse position to bird center
	    		pullPosition=new LVector(200-mouseX,0-mouseY,0);
	    	}
	    	if(mb==0&&pulled==true)
	    	{
	    		pulled=false;
	    		if(PhysicsEngine.distance(mouseX,mouseY,200-pullPosition.getX(),0-pullPosition.getY())>25)
	    		{
	    			//limits the mouse x and y to within 100 pixels(magnification 1)of original position
	    			double limitedX,limitedY;
	    			if(PhysicsEngine.distance(mouseX,mouseY,200-pullPosition.getX(),0-pullPosition.getY())>100)
	    			{
		    			LVector distanceVector=new LVector(200-(mouseX+pullPosition.getX()),0-mouseY+pullPosition.getY(),0);
		    			distanceVector=LVector.multiply(distanceVector.toUnitVector(),100);
		    			limitedX=200-distanceVector.getX();
		    			limitedY=0-distanceVector.getY();
	    			}
	    			else
	    			{
	    				limitedX=mouseX+pullPosition.getX();
	    				limitedY=mouseY+pullPosition.getY();
	    			}

					//gets velocity vector for bird when released
	    			double pulldistance=PhysicsEngine.distance(limitedX,limitedY,200,0);//
	    			LVector velocity=new LVector(200-limitedX,0-limitedY,0);
	    			velocity=velocity.toUnitVector();
	    			double magnitude=0.0015*(Math.pow(pulldistance,2));
	    			velocity=LVector.multiply(velocity,magnitude);
					
					//sets bird characteristics and fires bird
	    			birdArray.get(0).setX((int)(limitedX));
	    			birdArray.get(0).setY((int)(limitedY));
	    			birdArray.get(0).addVelocity(velocity);
					
					//bird moved from GameScreen to PhysicsEngine
	    			engine.addBird(birdArray.get(0));
	    			birdArray.remove(birdArray.get(0));



	    		}
	    		else
	    		{
	    			//misfire
	    			pulled=false;
	    		}
	    	}
    	}
    }
    
    //check button presses
    //changes the state and returns the state
    //used to change state in GameScreen class
    public int checkMouse()
    {
    	int returnState=state;
    	if(releasedB==1)
    	{
    		if(state==1)
    		{
    			if(300<mx&&mx<450&&500<my&&my<575)
    			{
    				state=3 ;
    			}
    			if(550<mx&&mx<700&&500<my&&my<575)
    			{
    				state=2;
    			}
    		}
    		else if(state==2)
    		{
    			if(50<mx&&mx<200&&550<my&&my<625)
    			{
    				state=1;
    			}
    		}
    		else if(state==3)
    		{
    			if(50<mx&&mx<200&&550<my&&my<625)
    			{
    				state=1;
    			}
    			if(225<mx&&mx<375&&200<my&&my<375)
    			{
    				state=4;
    			}
    			if(425<mx&&mx<575&&200<my&&my<375)
    			{
    				state=5;
    			}
    			if(625<mx&&mx<775&&200<my&&my<375)
    			{
    				state=6;
    			}
    		}
    		else if(state>3&&state<7)
    		{
    			if(10<mx&&mx<160&&30<my&&my<105)
    			{
    				state=7;
    			}
    		}
    		else if(state==7)
    		{
    			if(425<mx&&mx<575&&250<my&&my<325)
    			{
    				state=4;
    			}
    			if(425<mx&&mx<575&&350<my&&my<425)
    			{
    				state=1;
    			}
    		}
    		else if(state==8||state==9)
    		{
    			if(425<mx&&mx<575&&350<my&&my<425)
    			{
    				state=1;
    			}
    		}
    	}
    	//method is called by main Game to check state
    	returnState=state;
    	releasedB=0;
    	return returnState;
    }

    //Keys control zoom during a level.
    //Changes the zoom according to the keys pressed and makes sure
    //the player does not zoom outside intended area.
    //The PhysicsEngine is a 2000 by 1400 area with the top left corner
    //at 0,-700. The screen in a 1000 by 700 area with the top left corner
    //at 0,0. confusion caused by poor planning noted
    public void checkKeys()
    {
    	//keys 81 and 69, Q and E, control zoom. Changes
    	//in screen position are for zoom to focus on the
    	//current center of the screen
    	if(getKeys()[81]==true)
		{
			screenPositionX-=1000*magnification;
			screenPositionY-=700*magnification;
			magnification-=0.01;
    		magnification=Math.max(0.5,magnification);
    		screenPositionX+=1000*magnification;
    		screenPositionY+=700*magnification;
		}
		if(getKeys()[69]==true)
		{
			screenPositionX-=1000*magnification;
			screenPositionY-=700*magnification;
			magnification+=0.01;
    		magnification=Math.min(1,magnification);
    		screenPositionX+=1000*magnification;
    		screenPositionY+=700*magnification;
		}

		//WASD controls the position of the screen
		//in relation to the PhysicsEngine coordinates.
		if(getKeys()[65]==true)
		{
			screenPositionX-=10;
		}
		if(getKeys()[68]==true)
		{
			screenPositionX+=10;
		}
		if(getKeys()[87]==true)
		{
			screenPositionY+=10;
		}
		if(getKeys()[83]==true)
		{
			screenPositionY-=10;
		}
		//makes sure the screen position does not show outside of 2000 by 1400
		//area of the PhysicsEngine according to the magnification.
		screenPositionX=Math.max(0,screenPositionX);
    	screenPositionY=Math.max(700,screenPositionY);
    	screenPositionX=Math.min((int)((magnification-0.5)*2000),screenPositionX);
    	screenPositionY=Math.min((int)(1400*magnification),screenPositionY);

    }

    //Interface
	public int []getMouse(){
		int []pos=new int[2];
		pos[0]=mx;
		pos[1]=my;
		return pos;
	}
	public int getButton(){
		return mb;
	}

	public boolean []getKeys(){
		return keys;
	}

	private void updateMouse(MouseEvent e){
		mx = e.getX();
		my = e.getY();
	}

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
     	updateMouse(e);
     	releasedB=1;
     	mb = 0;
    }
    public void mouseClicked(MouseEvent e){
    	updateMouse(e);
    	mb = 0;
    }
    public void mouseDragged(MouseEvent e){
    	updateMouse(e);
	}
    public void mouseMoved(MouseEvent e)
    {
    		updateMouse(e);
    }
    public void mousePressed(MouseEvent e){
    	updateMouse(e);
    	mb = e.getButton();
	}

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    //Draws to the screen when not in a level
    //calls different draw methods depending
    //on the current state
    public void update()
    {
		Graphics g = getGraphics();
		if(dbImage == null){
			dbImage = createImage(getWidth(), getHeight());
			dbg = dbImage.getGraphics();
		}
		switch(state)
		{
			case 1:drawStartScreen(dbg);break;
			case 2:drawInstructionsScreen(dbg);break;
			case 3:drawLevelsScreen(dbg);break;
		}
		g.drawImage(dbImage,0,0,null);
	}

    //draws during the level
    //uses the information from the PhysicsEngine
  	public void update(PhysicsEngine engine){
		Graphics g = getGraphics();
		if(dbImage == null){
			dbImage = createImage(getWidth(), getHeight());
			dbg = dbImage.getGraphics();
		}

		drawLevel(dbg,engine);
		g.drawImage(dbImage,0,0,null);
	}

	//Various draw methods for non-level screens
	//draws starting screen
	public void drawStartScreen(Graphics g)
	{
		g.drawImage(startBackground,0,0,1000,700,this);
		if(300<mx&&mx<450&&500<my&&my<575)
		{
			if(mb==1)
			{
				g.drawImage(play3,300,500,150,75,this);
			}
			else
			{
				g.drawImage(play2,300,500,150,75,this);
			}
		}
		else
		{
			g.drawImage(play1,300,500,150,75,this);
		}
		if(550<mx&&mx<700&&500<my&&my<575)
		{
			if(mb==1)
			{
				g.drawImage(help3,550,500,150,75,this);
			}
			else
			{
				g.drawImage(help2,550,500,150,75,this);
			}
		}
		else
		{
			g.drawImage(help1,550,500,150,75,this);
		}
	}
	//draws help screen
	public void drawInstructionsScreen(Graphics g)
	{

		g.drawImage(helpBackground,0,0,1000,700,this);
		if(50<mx&&mx<200&&550<my&&my<625)
		{
			if(mb==1)
			{
				g.drawImage(back3,50,550,150,75,this);
			}
			else
			{
				g.drawImage(back2,50,550,150,75,this);
			}
		}
		else
		{
			g.drawImage(back1,50,550,150,75,this);
		}
	}
	
	//draws level selection screen
	public void drawLevelsScreen(Graphics g)
	{
		g.drawImage(levelBackground,0,0,1000,700,this);
		if(50<mx&&mx<200&&550<my&&my<625)
		{
			if(mb==1)
			{
				g.drawImage(back3,50,550,150,75,this);
			}
			else
			{
				g.drawImage(back2,50,550,150,75,this);
			}
		}
		else
		{
			g.drawImage(back1,50,550,150,75,this);
		}
		if(225<mx&&mx<375&&200<my&&my<375)
		{
			if(mb==1)
			{
				g.drawImage(one3,225,200,150,150,this);
			}
			else
			{
				g.drawImage(one2,225,200,150,150,this);
			}

		}
		else
		{
			g.drawImage(one1,225,200,150,150,this);
		}
		if(425<mx&&mx<575&&200<my&&my<375)
		{
			if(mb==1)
			{
				g.drawImage(two3,425,200,150,150,this);
			}
			else
			{
				g.drawImage(two2,425,200,150,150,this);
			}
		}
		else
		{
			g.drawImage(two1,425,200,150,150,this);
		}
		if(625<mx&&mx<775&&200<my&&my<375)
		{
			if(mb==1)
			{
				g.drawImage(three3,625,200,150,150,this);
			}
			else
			{
				g.drawImage(three2,625,200,150,150,this);
			}

		}
		else
		{
			g.drawImage(three1,625,200,150,150,this);
		}

	}

	//draw method for level screen
	//uses information from the PhysicsEngine
	public void drawLevel(Graphics g,PhysicsEngine engine)
	{
		Graphics2D g2d=(Graphics2D)g;
		g.drawImage(levelTwoBackground,(int)(-screenPositionX*magnification),(int)(screenPositionY-1400*magnification),(int)(2000*magnification),(int)(1400*magnification),this);
		g.drawImage(wallImage,(int)(-250*magnification-screenPositionX),(int)(-610*magnification+screenPositionY),250,20,this);
		g.drawImage(sling1,(int)((180*magnification)-screenPositionX),(int)(screenPositionY-(715*magnification)),(int)(40*magnification),(int)(115*magnification),this);
		
		for(RigidBox box:engine.getBoxes())
		{
			AffineTransform trans = new AffineTransform();

			trans.translate((int)(box.getX()*magnification-screenPositionX-box.getLength()/2*magnification),
			screenPositionY-magnification*(700-box.getY())-box.getWidth()/2*magnification);

			trans.rotate(box.getA(),box.getLength()/2*magnification,box.getWidth()/2*magnification);
			trans.scale(box.getLength()*magnification/200,box.getWidth()*magnification/50);
			//g.fillPolygon(wall.drawThisPolygon(magnification,screenPositionX,screenPositionY));
			if(box.getStrength()<450)
			{
				g2d.drawImage(glassBox,trans, this);
			}
			else
			{
				g2d.drawImage(metalBox,trans, this);
			}
		}
		for(RigidBox wall:engine.getWalls())
		{
			AffineTransform trans = new AffineTransform();

			trans.translate((int)(wall.getX()*magnification-screenPositionX-wall.getLength()/2*magnification),
			screenPositionY-magnification*(700-wall.getY())-wall.getWidth()/2*magnification);

			trans.rotate(wall.getA(),wall.getLength()/2*magnification,wall.getWidth()/2*magnification);
			trans.scale(wall.getLength()*magnification/2000,wall.getWidth()*magnification/100);
			//g.fillPolygon(wall.drawThisPolygon(magnification,screenPositionX,screenPositionY));
			g2d.drawImage(wallImage,trans, this);
		}
		for(RigidBall ball:engine.getBalls())
		{
			int x=(int)((ball.getX()*magnification-ball.getRadius()*magnification)-screenPositionX);
			int y=(int)(screenPositionY-magnification*(700-(ball.getY()-ball.getRadius()*magnification)));
			//g.fillOval(x,y,(int)(ball.getRadius()*2*magnification),(int)(ball.getRadius()*2*magnification));
			if(ball.getStrength()<400)
			{
				g.drawImage(glassBall,x,y,(int)(ball.getRadius()*2*magnification),(int)(ball.getRadius()*2*magnification),this);
			}
			else
			{
				g.drawImage(metalBall,x,y,(int)(ball.getRadius()*2*magnification),(int)(ball.getRadius()*2*magnification),this);
			}
		}
		for(RigidBall pig: engine.getPigs())
		{
			int x=(int)((pig.getX()*magnification-pig.getRadius()*magnification)-screenPositionX);
			int y=(int)(screenPositionY-magnification*(700-(pig.getY()-pig.getRadius()*magnification)));
			g.drawImage(pigImage,x,y,(int)(pig.getRadius()*2*magnification),(int)(pig.getRadius()*2*magnification),this);
		}
		for(RigidBall bird: engine.getBirds())
		{
			int x=(int)((bird.getX()*magnification-bird.getRadius()*magnification)-screenPositionX);
			int y=(int)(screenPositionY-magnification*(700-(bird.getY()-bird.getRadius()*magnification)));
			g.drawImage(birdImage,x,y,(int)(bird.getRadius()*2*magnification),(int)(bird.getRadius()*2*magnification),this);
		}


		//if the player still has unfired birds
		if(birds.size()>0)
		{
			if(pulled==false)
			{
				//draws bird in resting position
				int x=(int)((200*magnification-25*magnification)-screenPositionX);
				int y=(int)(screenPositionY-(magnification*700+25*magnification));
				g.drawImage(birdImage,x,y,(int)(25*2*magnification),(int)(25*2*magnification),this);
			}
			else
			{
				//draws bird according to user stretch
				double mouseX=(mx+screenPositionX)/magnification;
    			double mouseY=(my-screenPositionY)/magnification+700;
    			double distance=PhysicsEngine.distance(mouseX,mouseY,200-pullPosition.getX(),0-pullPosition.getY());
    			int x,y;
    			//prevents elastic from being stretched too much
    			if(distance>100)
    			{
    				LVector distanceVector=new LVector(mouseX-(200-pullPosition.getX()),mouseY+pullPosition.getY(),0);
    				distanceVector=LVector.multiply(distanceVector.toUnitVector(),100);
    				x=(int)(((200+distanceVector.getX())*magnification-25*magnification)-screenPositionX);
					y=(int)(screenPositionY-magnification*((700-distanceVector.getY())+25*magnification));

    			}
    			else
    			{
    				x=(int)(mx+pullPosition.getX()*magnification-25*magnification);
					y=(int)Math.round((my+pullPosition.getY()*magnification-25*magnification));
    			}
    			//draws elastic in background
    			g.setColor(new Color(80,20,5));
    			Polygon backElastic=new Polygon();
				backElastic.addPoint((int)((210*magnification)-screenPositionX),(int)(screenPositionY-(692*magnification)));
				backElastic.addPoint((int)((210*magnification)-screenPositionX),(int)(screenPositionY-(703*magnification)));
				backElastic.addPoint((int)(x+6*magnification),(int)(y+20*magnification));
				backElastic.addPoint((int)(x+6*magnification),(int)(y+35*magnification));
				g.fillPolygon(backElastic);

				//draw bird in slingshot
				g.drawImage(birdImage,x,y,(int)(25*2*magnification),(int)(25*2*magnification),this);
				
				//draw elastic in foreground
				Polygon frontElastic=new Polygon();
				frontElastic.addPoint((int)((190*magnification)-screenPositionX),(int)(screenPositionY-(692*magnification)));
				frontElastic.addPoint((int)((190*magnification)-screenPositionX),(int)(screenPositionY-(703*magnification)));
				frontElastic.addPoint((int)(x+6*magnification),(int)(y+20*magnification));
				frontElastic.addPoint((int)(x+6*magnification),(int)(y+35*magnification));
				g.fillPolygon(frontElastic);
			}
		}
		//draw sling in foreground
		g.drawImage(sling2,(int)((180*magnification)-screenPositionX),(int)(screenPositionY-(715*magnification)),(int)(40*magnification),(int)(115*magnification),this);
		for(int i=0;i<birds.size();i++)
		{
			//draw birds left indicator
			g.drawImage(birdGhost,i*60+175,40,50,50,this);
		}


		//draw instructions for menu, win and lose screens
		if(state>6)
		{
			g.drawImage(menu1,10,30,150,75,this);
			g.drawImage(menuBackground,0,0,1000,700,this);
			//win screen
			if(state==8)
			{
				g.drawImage(missionAccomplished,250,250,500,75,this);
			}
			//lose screen
			else if(state==9)
			{
				g.drawImage(missionFailed,250,250,500,75,this);
			}
			//menu screen
			else if(state==7)
			{
				if(425<mx&&mx<575&&250<my&&my<325)
				{
					if(mb==1)
					{
						g.drawImage(resume3,425,250,150,75,this);
					}
					else
					{
						g.drawImage(resume2,425,250,150,75,this);
					}

				}
				else
				{
					g.drawImage(resume1,425,250,150,75,this);
				}
			}
			if(425<mx&&mx<575&&350<my&&my<425)
			{
				if(mb==1)
				{
					g.drawImage(exit3,425,350,150,75,this);
				}
				else
				{
					g.drawImage(exit2,425,350,150,75,this);
				}

			}
			else
			{
				g.drawImage(exit1,425,350,150,75,this);
			}
		}
		else
		{
			if(10<mx&&mx<160&&30<my&&my<105)
			{
				if(mb==1)
				{
					g.drawImage(menu3,10,30,150,75,this);
				}
				else
				{
					g.drawImage(menu2,10,30,150,75,this);
				}

			}
			else
			{
				g.drawImage(menu1,10,30,150,75,this);
			}
		}
	}
}
