
/**
 * @(#)RigidBody.java
 *
 * Lawrence Huang
 *
 * The RigidBody class is a superclass for RigidBox and
 * RigidBall classes. It stores all the position and velocity
 * variables for a physical representation of a rigid body
 * ingame. The update method changes the position of the 
 * RigidBody according to the current translational and 
 * angular velocities of the RigidBody
 */


public class RigidBody 
{
	double positionX;
	double positionY;
	double positionA;
	
	double lastX;
	double lastY;
	double lastA;
	
	int strength;
	
	LVector velocity=new LVector(0,0,0);
	
	int mass;
	double elasticity;
    public RigidBody(double x,double y,double a,int m,double e,int s) 
    {
    	positionX=x;
    	lastX=x;
    	positionY=y+1;
    	lastY=y;
    	positionA=a;
    	lastA=a;
    	mass=m;
    	elasticity=e;
    	strength=s;
    }
    public void backUp()
    {
    	positionX=lastX;
    	positionY=lastY;
    	positionA=lastA;
    }
    //allows a change of velocity
    public void addVelocity(LVector v)
    {
    	velocity=LVector.add(velocity,v);
    }
    //slows the RigidBody
    public void retard()
    {
    	velocity=LVector.multiply(velocity,0.996);
    }
    
    //changes the position according to the velocity
    //also changes previous position
    public void update()
    {
    	lastX=positionX;
    	positionX+=velocity.getX();
    	lastY=positionY;
    	positionY+=velocity.getY();
    	lastA=positionA;
    	positionA-=velocity.getZ();
    	if(positionA>Math.PI*2)
    	{
    		positionA-=Math.PI*2;
    	}
    }
    
    //accessors
    public LVector getVelocity()
    {
    	return velocity;
    }
    public LVector getTVelocity()
    {
    	return new LVector(velocity.getX(),velocity.getY(),0);
    }
    public LVector getAVelocity()
    {
    	return new LVector(0,0,velocity.getZ());
    }
    public int getMass()
    {
    	return mass;
    }
    
    public double getX()
    {
    	return positionX;
    }
    public double getY()
    {
    	return positionY;
    }
    public double getA()
    {
    	return positionA;
    }
    public double getElasticity()
    {
    	return elasticity;
    }
    public int getStrength()
    {
    	return strength;
    }
    
    //setters used when creating birds
    public void setX(int x)
    {
    	positionX=x;
    }
    public void setY(int y)
    {
    	positionY=y;
    }
    
}
