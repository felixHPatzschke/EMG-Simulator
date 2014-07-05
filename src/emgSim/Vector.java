/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emgSim;

import java.util.Stack;

/**
 *
 * @author Felix Patzschke
 */
public class Vector {

    
    public double x,y,abs;
    public Angle phi;
    public String name;
    public int index;
    
    public static final Vector o = new Vector(0,0);
    public static final Vector e_x = new Vector(1,0), 
                               e_y = new Vector(0,1);

    
    public Vector(){
        this.x = 0;
        this.y = 0;
        this.abs = 0;
        this.phi = new Angle();
        phi.setAngleRadiants(0);
    }
    
    public Vector(double x, double y){
        this.x = x;
        this.y = y;
        this.abs = Math.sqrt( Math.pow(x, 2)+Math.pow(y, 2) );
        try{
            this.phi.setAngleRadiants(Math.acos(x/abs));
        }catch(Exception ex){
            
        }
    }
    
    public Vector(double value, Angle phi){
        double val = value;
        while(phi.getDegrees()>=180){
            phi.setAngleDegrees(phi.getDegrees()-180);
            val = val*(-1);
        }
        this.abs = val;                                 //Überprüfen!!!
        
        //this.phi = phi;
        this.x = Math.cos(phi.getRadiants());
        this.y = Math.sin(phi.getRadiants());
    }
    
    public Vector(Vector v){
        this.x = v.x;
        this.y = v.y;
        this.abs = v.abs;
        this.phi = v.phi;
        
    }
    
    
    public static Vector sum(Vector[] summand){
        double xs=0, ys=0;
        
        for(int c=0; c<summand.length; c++){
            xs = xs+summand[c].x;
            ys = ys+summand[c].y;
        }
        
        return new Vector(xs,ys);
    }
    
    public static Vector sum(Vector a, Vector b){
        return new Vector(a.x+b.x, a.y+b.y);
    }
    
    public static Vector sum(Stack<Vector> s) {
        double xs=0, ys=0;
        
        for(int c=0; c<s.size(); c++){
            xs = xs+s.elementAt(c).x;
            ys = ys+s.elementAt(c).y;
        }
        
        return new Vector(xs,ys);
    }

    public static Vector minus(Vector a, Vector b){
        
        return Vector.sum(a, Vector.antiVector(b));
        
    }
    
    public void minus(Vector b){
        
        Vector a = new Vector(Vector.minus(this, b));
        
        this.abs = a.abs;
        this.x = a.x;
        this.y = a.y;
        
        
    }
    
    public static Vector sMult(double n, Vector a){
        return new Vector(n*a.x, n*a.y);
    }
    
    public static Vector antiVector(Vector v){
        return Vector.sMult(-1, v);
    }
    
    public static double skalarProdukt(Vector v1, Vector v2){
        return v1.x*v2.x + v1.y*v2.y;
    }
    
    public static double phi(Vector v1, Vector v2){
        return Math.acos(Vector.skalarProdukt(v1, v2)/(v1.abs*v2.abs));
    }
    
    public static Vector e(Vector v){
        
        double x,y;
        
        x = v.x/v.abs;
        y = v.y/v.abs;
        
        Vector e_v = new Vector(x,y);
        
        return e_v;
        
    }
    
    
    
}
