
/**
 * @(#)RigidBall.java
 *
 * The RigidBall Class is a RigidBody in the shape
 * of a circle. Used for pigs, birds, and debris
 */


public class RigidBall extends RigidBody
{
	int radius;
	double mInertia;
    public RigidBall(double x,double y,double a,int m,double e,int s,int r) 
    {
    	super(x,y,a,m,e,s);
    	radius=r;
    	//moment of inertia
    	mInertia=2*m*Math.pow(r,2)/5;
    	
    }
    
    //accessors
    public int getRadius()
    {
    	return radius;
    }
    public double getMInertia()
    {
    	return mInertia;
    }
}
