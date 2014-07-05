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
public class Body {
    
    public double q, m;
    public Vector s, v, a, F_Res;
    //public BodyDetailFrame bdFrame = new BodyDetailFrame();
    public boolean positionLocked;
    
    public int r;
    
    public static final Body DEFAULT_BODY = new Body();
    
    
    public Body(){
        this.positionLocked = false;
    }

    public Body(Body b){
//        this.F_C = b.F_C;
//        this.F_G = b.F_G;
        this.F_Res = b.F_Res;
        this.a = b.a;
        //this.bdFrame = b.bdFrame;
        this.m = b.m;
        this.positionLocked = b.positionLocked;
        this.q = b.q;
        this.r = b.r;
        this.s = b.s;
        this.v = b.v;
        
    }
    
    public Body(double q, double m, boolean positionLocked, int r) {
        this.q = q;
        this.m = m;
        this.positionLocked = positionLocked;
        this.r = r;
//        this.F_C = new Stack<>();
//        this.F_G = new Stack<>();
        this.F_Res = new Vector();
        this.a = new Vector();
        this.v = new Vector();
        this.s = new Vector();
        //this.bdFrame = new emgSim.BodyDetailFrame();
        
    }

    public Body(double q, double m, Vector s, Vector v, boolean positionLocked, int r) {
        this.q = q;
        this.m = m;
        this.s = s;
        this.v = v;
        this.positionLocked = positionLocked;
        this.r = r;
    }
    
    
    
    
    public void calculateAcceleration(Stack<Vector> f_g, Stack<Vector> f_c){
        
        Vector[] f = new Vector[2];
        
        f[1] = Vector.sum(f_c);
        f[0] = Vector.sum(f_g);
        
        this.F_Res = Vector.sum(f);
        
        this.a = Vector.sMult(1/this.m, F_Res);
        
    }
    
    public void calculateVelocity(double deltaT){
        if(!this.positionLocked){
            Vector[] velocity = new Vector[2];
            velocity[0] = this.v;
            velocity[1] = Vector.sMult(deltaT, a);
            this.v = Vector.sum(velocity);
        } else {
            this.v = Vector.o;
        }
    }
    
    public void calculatePosition(double dT){
        if(!this.positionLocked){
            Vector[] avs = new Vector[3];
            Vector result;
        
            avs[0] = this.s;
            avs[1] = Vector.sMult(dT, v);
        //    avs[2] = Vector.sMult(Math.pow(dT, 2), a);
            avs[2] = Vector.o;
        
            result = Vector.sum(avs);
        
            this.s.x = result.x;
            this.s.y = result.y;
        } else {
            
        }
    }

/*
    public void calculateF_G(Body causingBody){
        this.F_G.add(Tafelwerk.F_G(causingBody, this));
    }
    
    public void calculateF_Coulomb(Body causingBody){
        this.F_C.add(Tafelwerk.F_Coulomb(causingBody, this));
    }
*/
    
    public void alter(){
        //this.bdFrame.setVisible(true);
    }
    
}
