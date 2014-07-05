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
public class Center extends Body{
    
    /**
     *
     */
    public static enum Dimension{CHARGE, MASS};
    
    public Dimension dim;
    
    
    public Center(Dimension d){
        this.dim = d;
    }
    
    
    public void calculatePosition(Stack<Body> obj){
        
        Body[] b = new Body[obj.size()];
        Vector[] p = new Vector[obj.size()];
        double sum = 0.0;
        
        for(int i=0; i<obj.size(); i++){
            b[i] = new Body(obj.elementAt(i));
            if(this.dim == Dimension.CHARGE){
                p[i] = Vector.sMult(b[i].q, b[i].s);
                sum = sum + b[i].q;
            } else if(this.dim == Dimension.MASS){
                p[i] = Vector.sMult(b[i].m, b[i].s);
                sum = sum + b[i].m;
            } else {
                p[i] = Vector.o;
                sum = 1;
            }
        }
        
        this.s = Vector.sMult(1/sum, Vector.sum(p));
        
    }
    
}
