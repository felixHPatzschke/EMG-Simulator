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
public class Engine {
    
    public final int width, height;
    public Stack<Body> b;
    public Body[] b_x;
    //public long timecode;
    public boolean f_g, f_c, wallCollision, interCollision;
    
    public double timeUnit, lengthUnit;
            

    
    public Engine(int w, int h){
        this.width = w;
        this.height = h;
        //this.timecode = 0;
        this.timeUnit = 1/Tafelwerk.DEFAULT_TICK_SECOND_RATIO;
        this.lengthUnit = 1/Tafelwerk.DEFAULT_PIXEL_METER_RATIO;
        this.b = new Stack<>();
        this.f_c = true;
        this.f_g = false;
        this.wallCollision = true;
        this.interCollision = true;
            
    }
    
    
    public void actualizeTickSecondRatio(int fps){
        //this.timeUnit = 1/fps;
    }
    
    public void addBody(Body b){
        this.b.add(b);
    }
    
    public void addBody(Body b, Vector v0){
        Body x = b;
        x.v = v0;
        this.b.add(x);
    }
    
    public void tick(){
        
        //<editor-fold defaultstate="collapsed" desc="Old Tick-Method code">
        
        /*
        //b_x = new Body[b.size()];
        
        for(int i=0; i<b.size(); i++){
            //Body body = new Body(b.elementAt(i));
            for(int c=0; c<b.size(); c++){
                if(!(c==i)){
                    try{
                        if(f_c){
                            Tafelwerk.addF_Coulomb(b.elementAt(c), b.elementAt(i));
                        } else {
                            //body.F_C.add(Vector.o);
                        }
                        
                        if(f_g){
                            Tafelwerk.addF_Coulomb(b.elementAt(c), b.elementAt(i));
                        } else {
                            //body.F_G.add(Vector.o);
                        }
                    }catch(Exception e){
                        
                    }
                }
            }
            
            try{
                body.calculateAcceleration();
                body.calculateVelocity(timeUnit);
                //b.elementAt(i).calculatePosition(timeUnit);
            }catch(Exception e){
                
            }
            b_x[i] = new Body(body);
            
        }
        
        //int f_counter=0;
        /*
        for(int i_i=0; i_i<b.size(); i_i++){
            for(int i_c=0; i_c<b.size(); i_c++){
                for(int x=0; x<b.size(); x++){
                    try{
                        if(!(i_i == i_c)){
                            if(i_i == x){
                                if(f_g){
                                    F_G_Stack.add(Tafelwerk.F_G(b.elementAt(i_c), b.elementAt(i_i)));
                                    f_counter++;
                                    System.out.println("Force " + f_counter + " ( Body_" + i_c + " -> Body_" + i_i + " ) added");
                                }else{
                                    F_G_Stack.add(Vector.o);
                                }
                            }
                        }
                    }catch(Exception ex){}
                }
            }
        }
        
        try{
            //b.elementAt(0).F_G.add(Vector.sMult(1000, Vector.e_x));
            //b.elementAt(1).F_G.add(Vector.sMult(1000, Vector.e_y));
        }catch(Exception e){
            
        }
        
        for(int i=0; i<b.size(); i++){
            for(int c=0; c<b.size(); c++){
                System.out.println("an " + i + " liegt F_" + c + " an:    ( " + b.elementAt(i).F_G.elementAt(c).x + " | " + b.elementAt(i).F_G.elementAt(c).y + " )N");
            }
            System.out.println("v_" + i + " = " + b.elementAt(i).v.abs);
        }
        
        
        for(int c=0; c<b_x.length; c++){
            for(int i=0; i<b_x[c].F_G.size(); i++){
                System.out.println("Auf Körper_" + c + " wirkt F_" + i + ":   ( " + b_x[c].F_G.elementAt(i).x + " | " + b_x[c].F_G.elementAt(i).y + " )N");
            }
        }
        
        this.b.clear();
        for(int c=0; c<b_x.length; c++){
            this.b.add(new Body(b_x[c]));
        }
        /**/
        //</editor-fold>
        
        
        
        //this.timecode++;
    }
    
    public void tickBack(){
        
        
        
        //this.timecode--;
    }
    
}
