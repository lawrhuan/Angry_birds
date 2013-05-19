
/**
 * @(#)RigidBox.java
 *
 * Lawrence Huang
 *
 * The RigidBox Class is a RigidBody in the shape
 * of a rectangle. 
 */

import java.util.*;
import java.awt.*;
public class RigidBox extends RigidBody
{
	int length;
	int width;
	double mInertia;
	int strength;
    public RigidBox(double x,double y,double a,int m,double e,int s,int l,int w) 
    {
    	super(x,y,a,m,e,s);
    	length=l;
    	width=w;
    	//moment of inertia
    	mInertia=m*(Math.pow(w,2)+Math.pow(l,2))/12;
    }
    
    //gets a polygon representation of this RigidBox
    //methods originally used for drawing
    public Polygon getThisPolygon()
    {
    	Polygon returnPolygon=getPolygon(positionX,positionY,length,width,positionA);
    	return returnPolygon;
    }
    public Polygon drawThisPolygon(double magnification,int displayX,int displayY)
    {
    	Polygon returnPolygon=getPolygon(positionX*magnification-displayX,displayY-magnification*(700-positionY),(int)(length*magnification),(int)(width*magnification),positionA);
    	return returnPolygon;
    }
    
    //returns x and y vertices of the Polygon of this RigidBox
    //used for collisions
    public double[] getXVertices()
    {
    	double[] returnArray=new double[4];
    	returnArray[0]=(positionX+(length*0.5)*Math.cos(positionA)-(0.5*width)*Math.sin(positionA));
    	returnArray[1]=(positionX+(length*0.5)*Math.cos(positionA)+(0.5*width)*Math.sin(positionA));
    	returnArray[2]=(positionX-(length*0.5)*Math.cos(positionA)+(0.5*width)*Math.sin(positionA));
    	returnArray[3]=(positionX-(length*0.5)*Math.cos(positionA)-(0.5*width)*Math.sin(positionA));
    	return returnArray;
    }
    public double[] getYVertices()
    {
    	double[] returnArray=new double[4];
    	returnArray[0]=(positionY+(length*0.5)*Math.sin(positionA)+(0.5*width)*Math.cos(positionA));
    	returnArray[1]=(positionY+(length*0.5)*Math.sin(positionA)-(0.5*width)*Math.cos(positionA));
    	returnArray[2]=(positionY-(length*0.5)*Math.sin(positionA)-(0.5*width)*Math.cos(positionA));
    	returnArray[3]=(positionY-(length*0.5)*Math.sin(positionA)+(0.5*width)*Math.cos(positionA));
    	return returnArray;
    }
    
    //creates a polygon
    //used for collisions
    public static Polygon getPolygon(double polyX,double polyY,int polyLength, int polyWidth,double polyAngle)
    {
    	Polygon returnPolygon=new Polygon();
    	int x=(int)(polyX+(polyLength*0.5)*Math.cos(polyAngle)-(0.5*polyWidth)*Math.sin(polyAngle));
    	int y=(int)(polyY+(polyLength*0.5)*Math.sin(polyAngle)+(0.5*polyWidth)*Math.cos(polyAngle));
    	returnPolygon.addPoint(x,y);
    	
    	x=(int)(polyX+(polyLength*0.5)*Math.cos(polyAngle)+(0.5*polyWidth)*Math.sin(polyAngle));
    	y=(int)(polyY+(polyLength*0.5)*Math.sin(polyAngle)-(0.5*polyWidth)*Math.cos(polyAngle));
    	returnPolygon.addPoint(x,y);
    	
    	x=(int)(polyX-(polyLength*0.5)*Math.cos(polyAngle)+(0.5*polyWidth)*Math.sin(polyAngle));
    	y=(int)(polyY-(polyLength*0.5)*Math.sin(polyAngle)-(0.5*polyWidth)*Math.cos(polyAngle));
    	returnPolygon.addPoint(x,y);
    	
    	x=(int)(polyX-(polyLength*0.5)*Math.cos(polyAngle)-(0.5*polyWidth)*Math.sin(polyAngle));
    	y=(int)(polyY-(polyLength*0.5)*Math.sin(polyAngle)+(0.5*polyWidth)*Math.cos(polyAngle));
    	returnPolygon.addPoint(x,y);
    	returnPolygon.invalidate();
    	
    	return returnPolygon;
    }
    	
    	
    //accessors
    public int getLength()
    {
    	return length;
    }
    public int getWidth()
    {
    	return width;
    }
    public double getMInertia()
    {
    	return mInertia;
    }
}
