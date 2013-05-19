
/**
 * @(#)LVector.java
 *
 * Lawrence Huang
 * 
 * The LVector class allows LVector objects to be created
 * and contains static operation methods add, dotProduct
 * and crossProduct. It is used in the final project as part 
 * of the physics engine to store translational and rotational
 * velocities and calculate changes to the velocities.
 * 
 *
 */


public class LVector 
{
	//magnitudes for each direction
	//z is used as rotational velocity
	double x;
	double y;
	double z;
    public LVector(double x,double y,double z) 
    {
    	this.x=x;
    	this.y=y;
    	this.z=z;
    }
    
    //returns a vector with the same direction as current with magnitude 1
    public LVector toUnitVector()
    {
    	double magnitude=Math.pow((Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2)),0.5);
    	return new LVector(x/magnitude,y/magnitude,z/magnitude);
    }
    //adds two vectors
    public static LVector add(LVector a,LVector b)
    {
    	LVector returnVector=new LVector(a.getX()+b.getX(),a.getY()+b.getY(),a.getZ()+b.getZ());
    	return returnVector;
    }
    //multiplies a vector by a scalar to change magnitude
    public static LVector multiply(LVector vector,double magnitude)
    {
    	return new LVector(vector.getX()*magnitude,vector.getY()*magnitude,vector.getZ()*magnitude);
    }
    //returns the dotproduct of two vectors
    public static double dotProduct(LVector a,LVector b)
    {
    	return a.getX()*b.getX()+a.getY()*b.getY()+a.getZ()*b.getZ();
    }
    //returns a vector dotproduct of two vectors
    //the dotproduct formula was altered to accomodate
    //the inverted y magnitude.
    public static LVector crossProduct(LVector a,LVector b)
    {
    	double newX=a.getY()*b.getZ()-a.getZ()*b.getY();
    	double newY=a.getZ()*b.getX()-a.getX()*b.getZ();
    	double newZ=a.getX()*b.getY()-a.getY()*b.getX();
    	LVector returnLVector=new LVector(-newX,-newY,-newZ);
    	return returnLVector;
    }
    
    //accessors
    public double getX()
    {
    	return x;
    }
    public double getY()
    {
    	return y;
    }
    public double getZ()
    {
    	return z;
    }
    public double getMagnitude()
    {
    	return Math.pow((Math.pow(x,2)+Math.pow(z,2)+Math.pow(z,2)),0.5);
    }
    public String toString()
    {
    	return(x+" "+y+" "+z+" ");
    }
    
    
}
