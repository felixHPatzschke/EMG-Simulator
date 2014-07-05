/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emgSim;

import java.awt.Color;

/**
 *
 * @author Felix Patzschke
 */
public abstract class Tafelwerk {
    
    public static final String DEFAULT_WINDOW_TITLE = "EMG-Simulator 1.0";
    public static final String DEFAULT_BDF_TITLE_1 = "Objekt ";
    public static final String DEFAULT_BDF_TITLE_2 = " - Details";
    public static final String DEFAULT_ICON_IMAGE_PATH = "letBall_02.png";
    public static final int DEFAULT_SIMULATION_X = 900, DEFAULT_SIMULATION_Y =  900;
    
    public static final double DEFAULT_TICK_SECOND_RATIO =  /*200.0  */ 600.0;
    public static final double DEFAULT_PIXEL_METER_RATIO =  /*5000.0 */ 250.0;
            
    public static final double DEFAULT_CHARGE = /*0.00001*/-0.00001, //C
                               DEFAULT_MASS = 1; //kg
    public static final int DEFAULT_RADIUS = 32; //px
    
    public static final Body DEFAULT_BODY = new emgSim.Body(DEFAULT_CHARGE, DEFAULT_MASS, false, DEFAULT_RADIUS);
    
    public static final double G = 0.0000000000667384,  /* m³/(kg s²) */
                               EPSILON = 0.000000000008854187817;  /* (A s)/(V m) */
        
    public static final Color[] DEFAULT_COLOR = {new Color(128,0,0), new Color(0,0,128), new Color(0,128,0)}; 
    
    
    //(double q, double m, Vector s, Vector v, boolean positionLocked, int r)
    /*
    public static final Body DEFAULT_BODY_1 = new Body(0.00001, 1, new Vector(1, 1+(Math.sqrt(3.0))), Vector.o, false, 24);
    public static final Body DEFAULT_BODY_2 = new Body(0.00001, 1, new Vector(3, 1+(Math.sqrt(3.0))), Vector.o, false, 24);
    public static final Body DEFAULT_BODY_3 = new Body(-0.00001, 1, new Vector(2, 1), Vector.o, false, 24);
    */
    
    public static final Body DEFAULT_BODY_1 = new Body(0.00001, 1, new Vector(1.5, (Math.sqrt(3.0))), Vector.o, false, 24);
    public static final Body DEFAULT_BODY_2 = new Body(0.00001, 1, new Vector(2.5, (Math.sqrt(3.0))), Vector.o, true, 24);
    public static final Body DEFAULT_BODY_3 = new Body(-0.00001, 1, new Vector(2, 1), Vector.o, false, 24);
    /*
    public static final Body DEFAULT_BODY_1 = new Body(0.00001, (2*Math.pow(10, 10)), new Vector(3, (Math.sqrt(3.0))), Vector.o, false, 24);
    public static final Body DEFAULT_BODY_2 = new Body(-0.00001, -(2*Math.pow(10, 10)), new Vector(3.5, (Math.sqrt(3.0))), Vector.o, false, 24);
    */
    
    public static int modulo(int a, int b){
        if(a>b){
            return modulo(a-b, b);
        }else{
            return a;
        }
    }
    
    public static Vector delta_s(Body b1, Body b2){
        return Vector.minus(b1.s, b2.s);
    }
    
    public static Vector sigma_r(Body b1, Body b2){
        
        Vector edS = Vector.e(Tafelwerk.delta_s(b1, b2));
        
        return Vector.sMult(b1.r+b2.r, edS);
        
    }
    
    public static Vector F_Coulomb(Body causingBody, Body influencedBody){
        
        double q1 = causingBody.q,
               q2 = influencedBody.q;
        
        Vector r = new Vector(
                influencedBody.s.x-causingBody.s.x,
                influencedBody.s.y-causingBody.s.y
                             );
        
        Vector e_r = new Vector(r.x/r.abs, r.y/r.abs);
        
        Vector fC = Vector.sMult((1/(4*Math.PI*Tafelwerk.EPSILON))*(q1*q2)/(Math.pow(r.abs, 2)), e_r);
        
        return fC;
        
    }
    
    public static Vector F_G(Body causingBody, Body influencedBody){
        
        double m1 = causingBody.m,
               m2 = influencedBody.m;
        
        Vector r = new Vector(
                causingBody.s.x-influencedBody.s.x,
                causingBody.s.y-influencedBody.s.y
                             );
        
        Vector e_r = new Vector(r.x/r.abs, r.y/r.abs);
        
        Vector fG = Vector.sMult(G*( (m1*m2)/(Math.pow(r.abs, 2)) ), e_r);
        
        return fG;
        
    }

/*
    public static void addF_Coulomb(Body causingBody, Body influencedBody){
        
        double q1 = causingBody.q,
               q2 = influencedBody.q;
        
        Vector r = new Vector(
                influencedBody.s.x-causingBody.s.x,
                influencedBody.s.y-causingBody.s.y
                             );
        
        Vector e_r = new Vector(r.x/r.abs, r.y/r.abs);
        
        Vector fC = Vector.sMult((1/(4*Math.PI*Tafelwerk.EPSILON))*(q1*q2)/(Math.pow(r.abs, 2)), e_r);
        
        influencedBody.F_C.add(fC);
        causingBody.F_C.add(Vector.antiVector(fC));
        
    }
    
    public static void addF_G(Body causingBody, Body influencedBody){
        
        double m1 = causingBody.m,
               m2 = influencedBody.m;
        
        Vector r = new Vector(
                causingBody.s.x-influencedBody.s.x,
                causingBody.s.y-influencedBody.s.y
                             );
        
        Vector e_r = new Vector(r.x/r.abs, r.y/r.abs);
        
        Vector fG = Vector.sMult(G*( (m1*m2)/(Math.pow(r.abs, 2)) ), e_r);
        
        influencedBody.F_G.add(fG);
        //causingBody.F_G.add(Vector.antiVector(fG));
        
    }
*/
    
    public static Vector wallCollision(Body b){
        
        
        
        return Vector.o;
        
    }
    
    /*
    public static Vector ballCollision(Koerper cB, Koerper iB){
        
    }
    */
}
