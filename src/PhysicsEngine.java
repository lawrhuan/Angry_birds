
/**
 * @(#)RigidWall.java
 *
 *
 * @author
 * @version 1.00 2011/5/21
 */

import java.util.*;
import java.awt.*;
public class PhysicsEngine
{
	private ArrayList<RigidBox> boxes;
	private ArrayList<RigidBall> balls;
	private ArrayList<RigidBox> walls;

	private ArrayList<RigidBall>pigs;
	private ArrayList<RigidBall>birds;
	private ArrayList<RigidBall>ballDebris;

	private ArrayList<RigidBox> clearBoxes;
	private ArrayList<RigidBall> clearBalls;

	//Used to show functionality of gravity
	//private final LVector Gravity=new LVector(0,-0.01,0);


    public PhysicsEngine(ArrayList<RigidBox> boxes,ArrayList<RigidBall> balls,ArrayList<RigidBox> walls,ArrayList<RigidBall> pigs)
    {
    	this.boxes=boxes;
    	this.ballDebris=balls;
    	this.walls=walls;
    	this.pigs=pigs;

    	birds=new ArrayList<RigidBall>();
    	this.balls=new ArrayList<RigidBall>();
    	for(RigidBall ball:ballDebris)
    	{
    		this.balls.add(ball);
    	}
    	for(RigidBall pig:pigs)
    	{
    		this.balls.add(pig);
    	}

    	//balls.get(1).addVelocity(new LVector(-5,5,0));

    	clearBoxes=new ArrayList<RigidBox>();
    	clearBalls=new ArrayList<RigidBall>();
    }
    public void update()
    {
    	for(RigidBox box:boxes)
    	{
    		//Used to show functionality of gravity
    		//box.addVelocity(Gravity);

    		//retarding force slows object down
    		box.retard();
    	}
    	for(RigidBall ball:balls)
    	{
    		//ball.addVelocity(Gravity);
    		ball.retard();
    	}

    	//Checks every two RigidBody objects in the PhysicsEngine for
    	//a collision using the overloaded collision method. Collisions
    	//are resolved using the overloaded resolve method called by the
    	//collision methods.
    	for(RigidBox box:boxes)
    	{
    		for(RigidBox otherbox:boxes)
    		{
    			if(box!=otherbox)
    			{
    				collision(box,otherbox,0);//0 is with other bodies, 1 is with wall
    			}
    		}
    		for(RigidBox wall:walls)
    		{
    			collision(wall,box,1);
    		}
    		for(RigidBall ball:balls)
    		{
    			collision(box,ball,0);
    		}
    	}
    	for(RigidBall ball:balls)
    	{
    		for(RigidBall otherball:balls)
    		{
    			if(ball!=otherball)
    			{
    				collision(ball,otherball);

    			}
    		}
    		for(RigidBox wall:walls)
    		{
    			collision(wall,ball,1);
    		}
    	}

    	for(RigidBall bird:birds)
    	{
    		if(bird.getVelocity().getMagnitude()<0.3)
    		{
    			clearBalls.add(bird);
    		}
    	}
    	for(RigidBox box:clearBoxes)
    	{
    		boxes.remove(box);
    	}
    	for(RigidBall ball:clearBalls)
    	{
    		balls.remove(ball);
    		if(ballDebris.contains(ball))
    		{
    			ballDebris.remove(ball);
    		}
    		else if(pigs.contains(ball))
    		{
    			pigs.remove(ball);
    		}
    		else if(birds.contains(ball))
    		{
    			birds.remove(ball);
    		}
    	}
    	clearBalls.clear();
    	clearBoxes.clear();

		//calls each RigidBodies update function to move them
    	for(RigidBox box:boxes)
    	{
    		box.update();
    	}
    	for(RigidBall ball:balls)
    	{
    		ball.update();
    	}
    }

    //accessors used in Gamescreen to draw RigidBodies
    public ArrayList<RigidBox> getBoxes()
    {
    	return boxes;
    }
    public ArrayList<RigidBall> getBalls()
    {
    	return ballDebris;
    }
    public ArrayList<RigidBox> getWalls()
    {
    	return walls;
    }
    public ArrayList<RigidBall> getPigs()
    {
    	return pigs;
    }
    public ArrayList<RigidBall> getBirds()
    {
    	return birds;
    }
    public boolean checkWin()
    {
    	return(pigs.size()==0);
    }
    public void addBird(RigidBall bird)
    {
    	birds.add(bird);
    	balls.add(bird);
    }


    //collision detection method for two boxes
    //type 1 indicates boxA is a wall
    public void collision(RigidBox boxA, RigidBox boxB,int type)
    {
    	//Checks if any corner of boxA is in boxB
        Polygon boxAPolygon = boxA.getThisPolygon();
        ArrayList<Double> bInAX=new ArrayList<Double>();
        ArrayList<Double> bInAY=new ArrayList<Double>();
        double[] boxBX= boxB.getXVertices();
        double[] boxBY= boxB.getYVertices();
        for (int i = 0; i < 4; i++)
        {
            if (boxAPolygon.contains(boxBX[i], boxBY[i]))
            {
                bInAX.add(boxBX[i]);
                bInAY.add(boxBY[i]);
            }
        }

		//Checks if any corner of boxB is in boxA
        Polygon boxBPolygon = boxB.getThisPolygon();
        ArrayList<Double> aInBX=new ArrayList<Double>();
        ArrayList<Double> aInBY=new ArrayList<Double>();
        double[] boxAX= boxA.getXVertices();
        double[] boxAY= boxA.getYVertices();
        for (int i = 0; i < 4; i++)
        {
            if (boxBPolygon.contains(boxAX[i], boxAY[i]))
            {
                aInBX.add(boxAX[i]);
                aInBY.add(boxAY[i]);
            }
        }

        //decided which parameters to call resolve() with
        //depending on which box's corners are colliding and how
        //many
        if(bInAX.size()==1&&aInBX.size()==0)
        {
        	resolve(boxB,boxA,bInAX.get(0),bInAY.get(0),type);
        }
        else if(bInAX.size()==0&&aInBX.size()==1)
        {
        	resolve(boxA,boxB,aInBX.get(0),aInBY.get(0),type);
        }
        //included for rigorousness but unlikely to occur
        //not resolved due to time restricitons.
        else if(bInAX.size()==1&&aInBX.size()==1)
        {
        	resolve(boxA,boxB,aInBX.get(0),aInBY.get(0),type);
        	//resolveCorners(boxB,boxA,bInAX.get(0),bInAY.get(0),aInBX.get(0),aInBY.get(0),type);
        }
        //if two corners of one box is in the other, the
        //point of collision become the average of the two corners
        else if(bInAX.size()==2&&aInBX.size()==0)
        {
        	double collisionX=(bInAX.get(0)+bInAX.get(1))/2;
        	double collisionY=(bInAY.get(0)+bInAY.get(1))/2;
        	resolve(boxB,boxA,collisionX,collisionY,type);
        }
        else if(bInAX.size()==0&&aInBX.size()==2)
        {
        	double collisionX=(aInBX.get(0)+aInBX.get(1))/2;
        	double collisionY=(aInBY.get(0)+aInBY.get(1))/2;
        	resolve(boxB,boxA,collisionX,collisionY,type);
        }
    }

    //Checks for collision between two balls
	public void collision(RigidBall ballA, RigidBall ballB)
	{
    	if(distance(ballA.getX(), ballA.getY(), ballB.getX(), ballB.getY()) <= (double)(ballA.getRadius() + ballB.getRadius()))
    	{
    		resolve(ballA,ballB);
    	}
    }

    //Checks for collision between a box and a ball.
    //type 1 indicates box is a wall
    public void collision(RigidBox box, RigidBall ball,int type)
    {
        Polygon collisionPolygon = RigidBox.getPolygon(box.getX(), box.getY(), box.getLength()+ball.getRadius()*2,box.getWidth()+ball.getRadius()*2, box.getA());
        if (collisionPolygon.contains(ball.getX(), ball.getY()))
        {

        	if(RigidBox.getPolygon(box.getX(), box.getY(), box.getLength() + ball.getRadius()*2, box.getWidth(), box.getA()).contains(ball.getX(), ball.getY())
            	|| RigidBox.getPolygon(box.getX(), box.getY(), box.getLength(), box.getWidth() + ball.getRadius()*2, box.getA()).contains(ball.getX(), ball.getY()))
            {
            	resolveEdge(box,ball,type);
            }
        	double[] xVertices=box.getXVertices();
        	double[] yVertices=box.getYVertices();

        	for(int i=0;i<4;i++)
        	{
        		if (distance(ball.getX(), ball.getY(),xVertices[i], yVertices[i]) < (double)ball.getRadius())
	            {
	                resolveCorner(box,ball,type);
	            }
        	}
		}
	}

	//Resolves collision between two boxes
	//new velocities for each box are calculated and the
	//positions are backed up to before the collision
	public void resolve(RigidBox boxA,RigidBox boxB,double collisionX,double collisionY,int type)
	{
		double[] xVertices=boxB.getXVertices();
		double[] yVertices=boxB.getYVertices();
		double closestX=xVertices[0];
		double closestY=yVertices[0];
		double closestDistance=distance(closestX,closestY,collisionX,collisionY);
		for(int i=1;i<4;i++)
		{
			double distance=distance(xVertices[i],yVertices[i],collisionX,collisionY);
			if(distance<closestDistance)
			{
				closestDistance=distance;
				closestX=xVertices[i];
				closestY=yVertices[i];
			}
		}
		LVector[] normals=new LVector[4];
		for(int i=0;i<4;i++)
		{
			normals[i]=new LVector(Math.cos(boxB.getA()+i*(Math.PI/2)),+Math.sin(boxB.getA()+i*(Math.PI/2)),0);
		}
		LVector cornerVector=new LVector(closestX-boxB.getX(),closestY-boxB.getY(),0);
		cornerVector=cornerVector.toUnitVector();
		LVector testVector=new LVector(collisionX-boxB.getX(),collisionY-boxB.getY(),0);
		testVector=testVector.toUnitVector();
		LVector normal=new LVector(0,0,0);
		for(LVector currentNormal:normals)
		{
			if(LVector.dotProduct(currentNormal,testVector)>0&&LVector.dotProduct(currentNormal,cornerVector)>0&&
				LVector.dotProduct(currentNormal,testVector)>LVector.dotProduct(currentNormal,cornerVector))
			{
				normal=currentNormal;
			}
		}
		if(type==0)
		{
			LVector distanceA=new LVector(collisionX-boxA.getX(),collisionY-boxA.getY(),0);
			LVector distanceB=new LVector(collisionX-boxB.getX(),collisionY-boxB.getY(),0);
			double impulse=impulse(boxA,boxB,normal,distanceA,distanceB,boxA.getMInertia(),boxB.getMInertia());
			if(impulse<boxA.getStrength())
			{
				boxA.addVelocity(LVector.multiply(normal,impulse/boxA.getMass()));
				boxA.addVelocity(LVector.multiply(LVector.crossProduct(distanceA,LVector.multiply(normal,impulse)),1/boxA.getMInertia()));
				boxA.backUp();
			}
			else
			{
				clearBoxes.add(boxA);
			}
			if(impulse<boxB.getStrength())
			{
				boxB.addVelocity(LVector.multiply(normal,-impulse/boxB.getMass()));
				boxB.addVelocity(LVector.multiply(LVector.crossProduct(distanceB,LVector.multiply(normal,-impulse)),1/boxB.getMInertia()));
				boxB.backUp();
			}
			else
			{
				clearBoxes.add(boxB);
			}
		}
		if(type==1)
		{
			if(walls.contains(boxB))
			{
				RigidBox temp=boxB;
				boxB=boxA;
				boxA=temp;
				normal=LVector.multiply(normal,-1);
			}
			LVector distanceB=new LVector(collisionX-boxB.getX(),collisionY-boxB.getY(),0);
			double impulse=impulseWall(boxA,boxB,normal,distanceB,boxB.getMInertia());

			if(impulse<boxB.getStrength())
			{
				boxB.addVelocity(LVector.multiply(normal,-impulse/boxB.getMass()));
				boxB.addVelocity(LVector.multiply(LVector.crossProduct(distanceB,LVector.multiply(normal,-impulse)),1/boxB.getMInertia()));
				boxB.backUp();
			}
			else
			{
				clearBoxes.add(boxB);
			}
		}
	}

	//Resolves collision between a box and a ball, with the ball
	//hitting the edge of the box
	public void resolveEdge(RigidBox box,RigidBall ball,int type)
	{
		double[] xVertices=box.getXVertices();
		double[] yVertices=box.getYVertices();
		double closestX=xVertices[0];
		double closestY=yVertices[0];
		double closestDistance=distance(closestX,closestY,ball.getX(),ball.getY());
		for(int i=1;i<4;i++)
		{
			double distance=distance(xVertices[i],yVertices[i],ball.getX(),ball.getY());
			if(distance<closestDistance)
			{
				closestDistance=distance;
				closestX=xVertices[i];
				closestY=yVertices[i];
			}
		}
		LVector[] normals=new LVector[4];
		for(int i=0;i<4;i++)
		{
			normals[i]=new LVector(Math.cos(box.getA()+i*(Math.PI/2)),+Math.sin(box.getA()+i*(Math.PI/2)),0);
		}
		LVector cornerVector=new LVector(closestX-box.getX(),closestY-box.getY(),0);
		cornerVector=cornerVector.toUnitVector();
		LVector testVector=new LVector(ball.getX()-box.getX(),ball.getY()-box.getY(),0);
		testVector=testVector.toUnitVector();
		LVector normal=new LVector(0,0,0);
		for(LVector currentNormal:normals)
		{
			if(LVector.dotProduct(currentNormal,testVector)>0&&LVector.dotProduct(currentNormal,cornerVector)>0&&
				LVector.dotProduct(currentNormal,testVector)>LVector.dotProduct(currentNormal,cornerVector))
			{
				normal=LVector.multiply(currentNormal,-1);
			}
		}
		if(type==0)
		{
			LVector distanceB=LVector.multiply(normal,ball.getRadius());
			LVector distanceA=new LVector(-box.getX()+distanceB.getX()+ball.getX(),-box.getY()+distanceB.getY()+ball.getY(),0);
			double impulse=impulse(box,ball,normal,distanceA,distanceB,box.getMInertia(),ball.getMInertia());

			if(impulse<box.getStrength())
			{
				box.addVelocity(LVector.multiply(normal,impulse/box.getMass()));
				box.addVelocity(LVector.multiply(LVector.crossProduct(distanceA,LVector.multiply(normal,impulse)),1/box.getMInertia()));
				box.backUp();
			}
			else
			{
				clearBoxes.add(box);
			}
			if(impulse<ball.getStrength())
			{
				ball.addVelocity(LVector.multiply(normal,-impulse/ball.getMass()));
				ball.backUp();
			}
			else
			{
				clearBalls.add(ball);
			}
		}
		if(type==1)
		{
			LVector distanceB=LVector.multiply(normal,ball.getRadius());
			double impulse=impulseWall(box,ball,normal,distanceB,ball.getMInertia());
			if(impulse<ball.getStrength())
			{
				ball.addVelocity(LVector.multiply(normal,-impulse/ball.getMass()));
				ball.backUp();
			}
			else
			{
				clearBalls.add(ball);
			}
		}


	}
	//resolves if a ball hits the corner of a box
	public void resolveCorner(RigidBox box,RigidBall ball, int type)
	{
		double[] xVertices=box.getXVertices();
		double[] yVertices=box.getYVertices();
		double closestX=xVertices[0];
		double closestY=yVertices[0];
		double closestDistance=distance(closestX,closestY,ball.getX(),ball.getY());
		for(int i=1;i<4;i++)
		{
			double distance=distance(xVertices[i],yVertices[i],ball.getX(),ball.getY());
			if(distance<closestDistance)
			{
				closestDistance=distance;
				closestX=xVertices[i];
				closestY=yVertices[i];
			}
		}
		LVector normal=(new LVector(closestX-ball.getX(),closestY-ball.getY(),0)).toUnitVector();
		if(type==0)
		{
			LVector distanceA=new LVector(closestX-box.getX(),closestY-box.getY(),0);
			LVector distanceB=new LVector(closestX-ball.getX(),closestY-ball.getY(),0);
			double impulse=impulse(box,ball,normal,distanceA,distanceB,box.getMInertia(),ball.getMInertia());
			if(impulse<box.getStrength())
			{
				box.addVelocity(LVector.multiply(normal,impulse/box.getMass()));
				box.addVelocity(LVector.multiply(LVector.crossProduct(distanceA,LVector.multiply(normal,impulse)),1/box.getMInertia()));
				box.backUp();
			}
			else
			{
				clearBoxes.add(box);
			}
			if(impulse<ball.getStrength())
			{
				ball.addVelocity(LVector.multiply(normal,-impulse/ball.getMass()));
				ball.backUp();
			}
			else
			{
				clearBalls.add(ball);
			}
		}
		if(type==1)
		{
			LVector distanceB=new LVector(closestX-ball.getX(),closestY-ball.getY(),0);
			double impulse=impulseWall(box,ball,normal,distanceB,ball.getMInertia());
			if(impulse<ball.getStrength())
			{
				ball.addVelocity(LVector.multiply(normal,-impulse/ball.getMass()));
				ball.backUp();
			}
			else
			{
				clearBalls.add(ball);
			}
		}
	}

	//resolve the collision between two balls
	public void resolve(RigidBall ballA,RigidBall ballB)
	{
		LVector normal=new LVector(ballA.getX()-ballB.getX(),ballA.getY()-ballB.getY(),0).toUnitVector();
		LVector ballADistance=LVector.multiply(normal,ballB.getRadius());
		LVector ballBDistance=LVector.multiply(normal,-ballA.getRadius());
		double impulse=impulse(ballA,ballB,normal,ballADistance,ballBDistance,ballA.getMInertia(),ballB.getMInertia());
		if(impulse<ballA.getStrength())
		{
			ballA.addVelocity(LVector.multiply(normal,impulse/ballA.getMass()));
			ballA.backUp();
		}
		else
		{
			clearBalls.add(ballA);
		}
		if(impulse<ballB.getStrength())
		{
			ballB.addVelocity(LVector.multiply(normal,-impulse/ballB.getMass()));
			ballB.backUp();
		}
		else
		{
			clearBalls.add(ballB);
		}
	}


	public static double distance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    }
	 //impulse calculation formula
	    public double impulse(RigidBody bodyA, RigidBody bodyB,LVector normal,LVector distanceA,LVector distanceB,double inertiaA,double inertiaB)
    {
    	double elasticity=(bodyA.getElasticity()+bodyB.getElasticity())/2;
    	LVector ivA=LVector.add(bodyA.getTVelocity(),LVector.crossProduct(bodyA.getAVelocity(),distanceA));
    	LVector ivB=LVector.add(bodyB.getTVelocity(),LVector.crossProduct(bodyB.getAVelocity(),distanceB));

    	LVector impactVelocity=LVector.add(ivA,LVector.multiply(ivB,-1));
    	double returnDouble=(LVector.dotProduct(LVector.multiply(impactVelocity,-(1+elasticity)),normal))/
    		(1/(double)bodyA.getMass()+1/(double)bodyB.getMass()+
    			LVector.dotProduct(LVector.crossProduct(distanceA,normal),LVector.crossProduct(distanceA,normal))/inertiaA+
    				LVector.dotProduct(LVector.crossProduct(distanceB,normal),LVector.crossProduct(distanceB,normal))/inertiaB);

   		return returnDouble;
    }
	//impulse calculation formula for an immovable object
	    public double impulseWall(RigidBody bodyA,RigidBody bodyB,LVector normal,LVector distanceB,double inertiaB)
    {
    	double elasticity=(bodyA.getElasticity()+bodyB.getElasticity())/2;
    	LVector impactVelocity=LVector.multiply(LVector.add(bodyB.getTVelocity(),LVector.crossProduct(bodyB.getAVelocity(),distanceB)),-1);
    	double returnDouble=(LVector.dotProduct(LVector.multiply(impactVelocity,-(1+elasticity)),normal))/
    		(1/(double)bodyB.getMass()+LVector.dotProduct(LVector.crossProduct(distanceB,normal),LVector.crossProduct(distanceB,normal))/inertiaB);

   		return returnDouble;
    }
}
