/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emgSim;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Stack;
import javax.swing.*;

/**
 *
 * @author Felix Patzschke
 */
public class SimulationPanel extends JPanel{
    
    
    public Body nextBody;
    public Center com, coc;
    public Stack<Body> obj;
    public int width, height;
    public DataFrame dFrame;
    public BodyDetailFrame bdFrame;
    public Engine e;
    public Stack<Vector>[] f_g, f_c, history;
    
    public Image img;
    public Graphics graphics;
    public short subTimeCode;
    
    public boolean vVector, fResVector, gVector, emVector, dispCoM, dispCoC, path;
    public boolean mousePressed, paused;
    public int mouseX1, mouseY1, mouseX2, mouseY2;
    
    public boolean shiftPressed, ctrlPressed, altPressed,
                   ePressed, gPressed, sPressed;
    
    public boolean nextBodyIntersect;
    //public boolean collided[][];
    
    public double fps, time;
    public long frames;
    
    public Vector eField, gField;   //Insert: v = v_0 + (g*t)   &   v = v_0 + (e*(q/m)*t) 
    public double bField;           //Insert: Lorentz-Force 
    
    
    public SimulationPanel(final Engine e){
        this.e = e;
        this.subTimeCode = 0;
        this.obj = new Stack<>();
        this.width = e.width;
        this.height = e.height;
        this.setBounds(10, 10, width, height);
        dFrame = new DataFrame();
        bdFrame = new BodyDetailFrame();
        this.com = new Center(Center.Dimension.MASS);
        this.coc = new Center(Center.Dimension.CHARGE);
        
        vVector=true;
        gVector=false;
        emVector=true;
        dispCoC = false;
        dispCoM = true;
        path = true;
        
        
        this.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent me) {
                System.out.println("mouse clicked: (" + me.getX() + "|" + me.getY() + ")");
                //phys.addBall(new Ball(newBall.getRadius(), newBall.getPosition(), newBall.getSpeed(), newBall.getFriction(), newBall.getBounce(), actualColor));//new Color(actualColor.getRed(), actualColor.getGreen(), actualColor.getBlue())));
                //System.out.println(me.getButton());
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if(!nextBodyIntersect){
                    mousePressed = true;
                }
                mouseX1 = me.getX();
                mouseY1 = me.getY();
                
                
                
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                
                if(mousePressed){
                mousePressed = false;

                
                if(!nextBodyIntersect){
                
                mouseX2 = me.getX();
                mouseY2 = me.getY();
                Vector mouse1 = new Vector(mouseX1, mouseY1);
                Vector mouse2 = new Vector(mouseX2, mouseY2);
                
                Vector v0;
                v0 = Vector.sMult(1.0/200.0, new Vector(
                 (mouseX2-mouseX1)*((e.lengthUnit)/(e.timeUnit)) ,
                 (mouseY2-mouseY1)*((e.lengthUnit)/(e.timeUnit))
                 ));
                
                
                
                nextBody.v=v0;
                
                nextBody.s=Vector.sMult(e.lengthUnit, mouse1);
                
                Body fertig = new Body(nextBody);
                
                obj.add(fertig);
                System.out.println(fertig.s.x + " | " + fertig.s.y);
                handleHistory();
                calculateForces();
                
                /*
                boolean oldCollisions[][] = collided;
                boolean newCollisions[][] = new boolean [obj.size()][obj.size()];
                for(int x=0; x<obj.size()-1; x++){
                    for(int y=0; y<obj.size()-1; y++){
                        newCollisions[x][y] = oldCollisions[x][y];
                    }
                }
                collided = newCollisions;
                */
                
                bdFrame.bodyCount = obj.size();
                
                nextBody.q = 0-(nextBody.q);
                }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseExited(MouseEvent me) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
            
            //@Override
            //public void mouseMoved(MouseEvent me) {
            //    System.out.println(me.getX() + "|" + me.getY());
            //}
            
        });

        this.addMouseWheelListener(new MouseWheelListener() {
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                //[up] -> -1.0
                if(ctrlPressed){
                    if(altPressed){
                        nextBody.q = nextBody.q-(mwe.getPreciseWheelRotation()/10000);
                    } else {
                        nextBody.q = nextBody.q-(mwe.getPreciseWheelRotation()/1000);
                    }
                                        
                }else if(shiftPressed){
                    if(altPressed){
                        nextBody.m = nextBody.m-(mwe.getPreciseWheelRotation()/10);
                    } else {
                        nextBody.m = nextBody.m-mwe.getPreciseWheelRotation();
                    }
                }else {
                    if(altPressed){
                        nextBody.r = nextBody.r-((int)(mwe.getPreciseWheelRotation())*10);
                    } else {
                        nextBody.r = nextBody.r-((int)(mwe.getPreciseWheelRotation()));;
                    }
                    
                    if(nextBody.r<2){
                        nextBody.r=2;
                    }
                    if(nextBody.r>256){
                        nextBody.r=256;
                    }
                }
                dFrame.actualizeBody(nextBody);
            }
            
        });
                
        
    }
    
    
    public void oneStepForth(){
        //e.tick();
        this.tick();
    }
    
    public void oneStepBack(){
        //e.tickBack();
        this.tickBack();
    }
    
    public void loadStandardTaskModel(){
        try{
            this.obj.add(Tafelwerk.DEFAULT_BODY_1);
            this.obj.add(Tafelwerk.DEFAULT_BODY_2);
            this.obj.add(Tafelwerk.DEFAULT_BODY_3);
        }catch(Exception ex){}
        handleHistory();
        calculateForces();
        //collided = new boolean[3][3];
    }
    
    public void togglePause(){
        this.paused = !this.paused;
        this.dFrame.togglePause();
    }
    
    public void togglePositionLock(){
        this.nextBody.positionLocked = !this.nextBody.positionLocked;
        this.dFrame.togglePositionLock();
    }
    
    public void togglePath(){
        this.path = !this.path;
        this.dFrame.togglePath();
    }
    
    public void clearSimulation(){
        this.obj.clear();
    }
    
    public void handleHistory(){
        
        
        history = new Stack[obj.size()];
        
        for(int c=0; c<obj.size(); c++){
            history[c] = new Stack<>();
        }
        
    }
    
    public void calculateForces(){
        
        f_g = new Stack[obj.size()];
        f_c = new Stack[obj.size()];
        
        for(int c=0; c<obj.size(); c++){
            f_g[c] = new Stack<>();
            f_c[c] = new Stack<>();
        }
        
        for(int i=0; i<obj.size(); i++){
            //body[i] = new Body(obj.elementAt(i));
            for(int c=0; c<obj.size(); c++){
                if(i!=c){
                    if(e.f_g){
                        f_g[i].add(Tafelwerk.F_G(obj.elementAt(c), obj.elementAt(i)));
                    } else{
                        f_g[i].add(Vector.o);
                    }
                    if(e.f_c){
                        f_c[i].add(Tafelwerk.F_Coulomb(obj.elementAt(c), obj.elementAt(i)));
                    } else{
                        f_c[i].add(Vector.o);
                    }
                    
                }
            }
            
            //b.add(body[i]);
        }
        
    }
    
    public void handleInterCollision(){
        
        if(this.e.interCollision){
            
            for(int a=0; a<this.obj.size(); a++){
                for(int b=a; b<this.obj.size(); b++){
                    if(a<b){
                        if(Vector.sum(obj.elementAt(a).s, Vector.antiVector(obj.elementAt(b).s)).abs*Tafelwerk.DEFAULT_PIXEL_METER_RATIO <= obj.elementAt(a).r+obj.elementAt(b).r){
                            //if(!(this.collided[a][b] == true)){
                                this.collide(obj.elementAt(a), obj.elementAt(b));
                                //this.collided[a][b] = true;
                                //this.pushAway(obj.elementAt(a), obj.elementAt(b));
                            //}
                        //} else {
                        //    collided[a][b] = false;
                        }
                    }
                }
            }
            
        }
        
    }
    
    public void collide(Body b1, Body b2){
        
        /*
        if(b1.positionLocked & b2.positionLocked){
            
        }else if(b1.positionLocked){
            b2.v = Vector.antiVector(b2.v);
        }else if(b2.positionLocked){
            b1.v = Vector.antiVector(b1.v);
        }else{
            
        Vector v1_1 = b1.v,
               v2_1 = b2.v,
               v1_2,
               v2_2;
            
        v1_2 = Vector.sMult(
                (1)/(b1.m + b2.m),
                Vector.sum(
                        Vector.sMult(2*b2.m, v2_1),
                        Vector.sMult(b1.m-b2.m, v1_1)
                )
        );
        
        v2_2 = Vector.sMult(
                (1)/(b1.m + b2.m),
                Vector.sum(
                        Vector.sMult(2*b1.m, v1_1),
                        Vector.sMult(b2.m-b1.m, v2_1)
                )
        );
        
        b1.v = v1_2;
        b2.v = v2_2;
        
        }
        */
        
        //Vector n, t, v1, v1n, v1t, v2, v2n, v2t;
        
        double phi_n_1, phi_t_1, phi_n_2, phi_t_2;
        
        Vector dS = Vector.minus(b2.s, b1.s);
        Vector n = Vector.e(dS);
        Vector t = new Vector(-n.y, n.x);
        Vector sR = Vector.sMult( (b1.r+b2.r)/Tafelwerk.DEFAULT_PIXEL_METER_RATIO , n);
        
        //Rausquetsch-Methode
        b1.s = Vector.sum(
                b1.s, 
                Vector.sMult(
                        (b1.m+b2.m)/(b2.m),
                        Vector.minus(dS, sR)
                )
        );
        //print("s1:", b1.s);
        
        b2.s = Vector.sum(
                b2.s, 
                Vector.sMult(
                        (b1.m+b2.m)/(b2.m),
                        Vector.minus(sR, dS)
                )
        );
        //print("s2:", b2.s);
        //Ende der Rausquetsch-Methode
        
        Vector v1 = new Vector(b1.v);
        Vector v2 = new Vector(b2.v);
        
        phi_n_1 = Vector.phi(n, v1);
        phi_t_1 = Vector.phi(v1, t);
        phi_n_2 = Vector.phi(n, v2);
        phi_t_2 = Vector.phi(v2, t);
        /*
        System.out.println("phi_n_1: " + phi_n_1);
        System.out.println("phi_t_1: " + phi_t_1);
        System.out.println("phi_n_2: " + phi_n_2);
        System.out.println("phi_t_2: " + phi_t_2);
        */
        Vector v1n = Vector.sMult(v1.abs*Math.sin(phi_t_1), n);
        Vector v1t = Vector.sMult(v1.abs*Math.sin(phi_n_1), t);
        
        Vector v2n = Vector.sMult(v2.abs*Math.sin(phi_t_2), n);
        Vector v2t = Vector.sMult(v2.abs*Math.sin(phi_n_2), t);
        
        print("v1n:", v1n);
        print("v1t:", v1t);
        System.out.println(Vector.phi(v1n, v1t));
        System.out.println();
        print("v2n:", v2n);
        print("v2t:", v2t);
        System.out.println(Vector.phi(v2n, v2t));
        
        Vector v1n_f, v2n_f;
        
        //NOT SUFFICIENT - ONLY FOR EQUAL MASS
        v1n_f = v2n;
        v2n_f = v1n;
        
        
        
        b1.v = Vector.sum(v1t, v1n_f);
        b2.v = Vector.sum(v2t, v2n_f);
        
        
        /*
        print("v1:", v1);
        print("sum: ", Vector.sum(v1t, v1n));
        */
        
        
    }
        
    public void handleWallCollision(){
        if(e.wallCollision){
            //System.out.println("colliding with walls enabled");
            for(int c=0; c<obj.size(); c++){
                if(obj.elementAt(c).s.x*Tafelwerk.DEFAULT_PIXEL_METER_RATIO-obj.elementAt(c).r<0){
                    obj.elementAt(c).v.x = -obj.elementAt(c).v.x;
                    obj.elementAt(c).s.x = obj.elementAt(c).r/Tafelwerk.DEFAULT_PIXEL_METER_RATIO;
                }
                if(obj.elementAt(c).s.y*Tafelwerk.DEFAULT_PIXEL_METER_RATIO-obj.elementAt(c).r<0){
                    obj.elementAt(c).v.y = -obj.elementAt(c).v.y;
                    obj.elementAt(c).s.y = obj.elementAt(c).r/Tafelwerk.DEFAULT_PIXEL_METER_RATIO;
                }
                if(obj.elementAt(c).s.x*Tafelwerk.DEFAULT_PIXEL_METER_RATIO+obj.elementAt(c).r>Tafelwerk.DEFAULT_SIMULATION_X){
                    obj.elementAt(c).v.x = -obj.elementAt(c).v.x;
                    obj.elementAt(c).s.x = (Tafelwerk.DEFAULT_SIMULATION_X-obj.elementAt(c).r)/Tafelwerk.DEFAULT_PIXEL_METER_RATIO;
                }
                if(obj.elementAt(c).s.y*Tafelwerk.DEFAULT_PIXEL_METER_RATIO+obj.elementAt(c).r>Tafelwerk.DEFAULT_SIMULATION_Y){
                    obj.elementAt(c).v.y = -obj.elementAt(c).v.y;
                    obj.elementAt(c).s.y = (Tafelwerk.DEFAULT_SIMULATION_Y-obj.elementAt(c).r)/Tafelwerk.DEFAULT_PIXEL_METER_RATIO;
                }
            }
        }
    }
    
    public void tick(){
        
        boolean saveHistory = false;
        
        if(subTimeCode >= this.fps | subTimeCode == 0){
            subTimeCode = 0;
            saveHistory = true;
        }
        
        for(int c=0; c<obj.size(); c++){
            obj.elementAt(c).calculateAcceleration(this.f_g[c], this.f_c[c]);
            obj.elementAt(c).calculateVelocity(e.timeUnit);
            if(saveHistory){
                history[c].add(new Vector(obj.elementAt(c).s));
                //System.out.println("history added: "+c);
            }
            obj.elementAt(c).calculatePosition(e.timeUnit);
        }
        
        this.handleInterCollision();
        this.handleWallCollision();
        this.calculateForces();
        
        this.subTimeCode++;
        //e.timecode++;
    }
    
    public void tickBack(){
        
        
        
        //e.timecode--;
    }
    
    
    @Override
    public void repaint(){
        super.repaint();
    }
    
    @Override
    public void paint(Graphics g){
        
        //FPS-Zähler by Sebastian Geisler (11SPK)
        
        if (frames == 1) {
            time = System.currentTimeMillis()/10;
        }

        if (System.currentTimeMillis() - time != 0) {
            fps = ((100 * frames) / (System.currentTimeMillis()/10 - time));

        }

        if (frames > 100) {
            frames = 0;
        }
        
        frames++;
        
        //Ende des FPS-Zählers
        
        img = createImage(e.width + 2, e.height + 2);
        graphics = img.getGraphics();

        
        if(!this.paused){
            this.tick();
            //System.out.println("tick");
            graphics.drawString("running", Tafelwerk.DEFAULT_SIMULATION_X-50, 20);
        } else {
            graphics.drawString("paused", Tafelwerk.DEFAULT_SIMULATION_X-50, 20);
        }
        //this.graphics = (Graphics2D) g;

        obj = e.b;
        int objectCount = obj.size();
        graphics.drawString("Objects: " + String.valueOf(objectCount) + "/" + (obj.capacity()), 10, 52);
        
        
        for(int c=0; c<=objectCount; c++){
            try{
                this.draw(graphics, obj.elementAt(c), String.valueOf(c), c);
                //System.out.println("object_" + c + " has been successfully drawn.");
            }catch(java.lang.ArrayIndexOutOfBoundsException aiex){
                continue;
            }catch(java.lang.NullPointerException npex){
                continue;
            }
        }
        
        
        this.coc.calculatePosition(obj);
        this.com.calculatePosition(obj);

        try{
            if(this.dispCoC){
                this.draw(coc, graphics);
            }
            if(this.dispCoM){
                this.draw(com, graphics);
            }
        }catch(Exception e){}
        graphics.setColor(Color.black);
        
        
        Vector mouse1 = new Vector(mouseX1, mouseY1);
        try{
        Vector mouse2 = new Vector(this.getMousePosition().x, this.getMousePosition().y);
        
        //INSERT INTERSECT CHECK HERE
        this.nextBodyIntersect = false;
        Vector nextS = Vector.sMult(e.lengthUnit, mouse2);
        for(int c=0; c<obj.size(); c++){
            if(Vector.sum(obj.elementAt(c).s, Vector.antiVector(nextS)).abs*Tafelwerk.DEFAULT_PIXEL_METER_RATIO <= obj.elementAt(c).r+nextBody.r){
                this.nextBodyIntersect = true;
                //System.out.println("nbi set true due to intersect with " + c);
            }
        }
        }catch(Exception ex){
            this.nextBodyIntersect = true;
        }
        
        try{
            Vector mPos = new Vector(this.getMousePosition().x, this.getMousePosition().y);
            //System.out.println("(" + mPos.x + "|" + mPos.y + ")");
            if (mousePressed) {
                try{
                    if(this.nextBodyIntersect){
                        graphics.setColor(new java.awt.Color(255, 0, 0));
                    }else{
                        graphics.setColor(new java.awt.Color(0, 0xB0, 0));
                    }
                this.drawCircle(graphics, mouse1, nextBody.r);
                this.drawArrow(graphics, mouse1, mPos, "v");
                }catch(Exception e){
                    //System.out.println("Exception caught:");
                    //e.printStackTrace();
                }
            } else {
                if(this.nextBodyIntersect){
                        graphics.setColor(new java.awt.Color(192, 0, 0));
                    }else{
                        graphics.setColor(new java.awt.Color(0, 0x80, 0));
                    }
            }
            //try{
                this.drawCircle(graphics, mPos, nextBody.r);
            //}catch(Exception ex){
                
            //}
            
            graphics.setColor(Color.BLACK);
            
            
        }catch(java.lang.NullPointerException nex){
            //System.out.println("NullPointerExcepton caught:");
            //nex.printStackTrace();
        }
        
        this.graphics.setColor(Color.black);

        graphics.drawString("FPS: " + String.valueOf(Math.round(fps)), 10, 20);
        graphics.drawString("Sub-Timecode: " + String.valueOf(this.subTimeCode+1), 10, 36);
        //grapics.setFont(...);
        if(altPressed){
            graphics.drawString("[ALT]", Tafelwerk.DEFAULT_SIMULATION_X-35, Tafelwerk.DEFAULT_SIMULATION_Y-42);
        }
        if(ctrlPressed){
            graphics.drawString("[CTRL]", Tafelwerk.DEFAULT_SIMULATION_X-46, Tafelwerk.DEFAULT_SIMULATION_Y-10);
        }
        if(shiftPressed){
            graphics.drawString("[SHIFT]", Tafelwerk.DEFAULT_SIMULATION_X-48, Tafelwerk.DEFAULT_SIMULATION_Y-26);
        }

        g.drawImage(img, 0, 0, this);
        
        
        
    }
    
    public void drawArrow(Graphics g, Vector a, Vector b, String caption){
        
        this.drawArrow(g, (int) Math.round(a.x), (int) Math.round(a.y), (int) Math.round(b.x), (int) Math.round(b.y));
        g.drawString(caption, (int) Math.round(b.x)+10, (int) Math.round(b.y)+20);
        //System.out.println("Arrow successfully drawn: (" + String.valueOf(a.x) + "|" + String.valueOf(a.y) + ") -> (" + String.valueOf(b.x) + "|" + String.valueOf(b.y) + ")");
    }
    
    public void drawDirectionalArrow(Graphics g, Vector u, Vector d, String caption){
        Vector[] s = new Vector[2];
        s[0] = u;
        s[1] = d;
        this.drawArrow(g, u, Vector.sum(s), caption);
    }
    
    public void drawCircle(Graphics g, Vector m, int r){
        g.drawOval((int) Math.round(m.x-r), (int) Math.round(m.y-r), 2*r, 2*r);
        //System.out.println("Circle successfully drawn: (" + String.valueOf(m.x) + "|" + String.valueOf(m.y) + "), r=" + String.valueOf(r));
    }
    
    public void drawCenter(Graphics g, Vector m, int r){
        g.drawRect((int) Math.round(m.x-r), (int) Math.round(m.y-r), 2*r, 2*r);
        
        g.drawRect((int) Math.round(m.x), (int) Math.round(m.y), 0, 0);
        g.drawRect((int) Math.round(m.x-1), (int) Math.round(m.y-1), 2, 2);
        
    }
    
    public void draw(Graphics g, Body b, int index){
        g.setColor(Color.BLACK);
        
        if(b.positionLocked){
            g.setColor(new java.awt.Color(0xA0, 0xA0, 0xA0));
            g.fillOval((int) Math.round(Vector.sMult(1/e.lengthUnit, b.s).x-b.r), (int) Math.round(Vector.sMult(1/e.lengthUnit, b.s).y-b.r), 2*b.r, 2*b.r);
            g.setColor(Color.black);
        }
        if(b.q>0){
            g.setColor(new java.awt.Color(255, 0, 0, 64));
        } else if(b.q==0){
            g.setColor(new java.awt.Color(255, 255, 255, 64));
        } else if(b.q<0){
            g.setColor(new java.awt.Color(0, 0, 255, 64));
        }
        g.fillOval((int) Math.round(Vector.sMult(1/e.lengthUnit, b.s).x-b.r), (int) Math.round(Vector.sMult(1/e.lengthUnit, b.s).y-b.r), 2*b.r, 2*b.r);
        this.drawCircle(g, Vector.sMult(1/e.lengthUnit, b.s), b.r);
        if(!b.positionLocked){
        if(this.vVector){
            g.setColor(new java.awt.Color(0, 128, 0  ));
            this.drawDirectionalArrow(g, Vector.sMult(1/e.lengthUnit, b.s), Vector.sMult(200*e.timeUnit/e.lengthUnit, b.v), "v");
        }
        g.setColor(Color.blue);
        if(this.emVector & obj.size()<=6){
            for(int c=0; c<=this.f_c[index].size(); c++){
                try{
                    //this.drawDirectionalArrow(g, Vector.sMult(1/e.lengthUnit, b.s), Vector.sMult(e.timeUnit*e.timeUnit/e.lengthUnit, b.F_C.elementAt(c)), "F_C" + c);
                    this.drawDirectionalArrow(g, Vector.sMult(1/e.lengthUnit, b.s), Vector.sMult(100000*e.timeUnit*e.timeUnit/e.lengthUnit, this.f_c[index].elementAt(c)), /*"F_C = " + this.f_c[index].elementAt(c).abs + "N"*/"");
                }catch(Exception ex){}
            }
        }
        g.setColor(Color.red);
        if(this.gVector & obj.size()<=6){
            for(int c=0; c<=this.f_g[index].size(); c++){
                try{
                    //this.drawDirectionalArrow(g, Vector.sMult(1/e.lengthUnit, b.s), Vector.sMult(e.timeUnit*e.timeUnit/e.lengthUnit, b.F_G.elementAt(c)), "F_G" + c);
                    this.drawDirectionalArrow(g, Vector.sMult(1/e.lengthUnit, b.s), Vector.sMult(100000*e.timeUnit*e.timeUnit/e.lengthUnit, this.f_g[index].elementAt(c)), /*"F_G = " + this.f_g[index].elementAt(c).abs + "N"*/"");
                }catch(Exception ex){}
            }
        }
        g.setColor(Color.black);
        //for(int c=0; c<=this.f_g[index].size(); c++){
            try{
                //SET NEXT LINE AS COMMENT
                this.drawDirectionalArrow(g, Vector.sMult(1/e.lengthUnit, b.s), Vector.sMult(100000*e.timeUnit*e.timeUnit/e.lengthUnit, b.F_Res), "F_Res = " + b.F_Res.abs + "N");
            }catch(Exception ex){}
        //}
        
        if(this.path){
            try{
                g.drawLine((int) Math.round(Vector.sMult(1/e.lengthUnit, b.s).x),
                           (int) Math.round(Vector.sMult(1/e.lengthUnit, b.s).y),
                           /**/
                           (int) Math.round(Vector.sMult(1/e.lengthUnit, history[index].lastElement()).x),
                           (int) Math.round(Vector.sMult(1/e.lengthUnit, history[index].lastElement()).y)
                           /**/
                           //0,0
                );
            }catch(Exception ex){}
            for(int c=0; c</*history[index].size()*/32; c++){
                try{
                    g.setColor(new java.awt.Color(c*255/32, 128 + (c*255/64), c*255/32));
                    g.drawLine((int) Math.round(Vector.sMult(1/e.lengthUnit, history[index].elementAt(history[index].size()-c)).x),
                               (int) Math.round(Vector.sMult(1/e.lengthUnit, history[index].elementAt(history[index].size()-c)).y),
                               
                               (int) Math.round(Vector.sMult(1/e.lengthUnit, history[index].elementAt(history[index].size()-(c+1))).x),
                               (int) Math.round(Vector.sMult(1/e.lengthUnit, history[index].elementAt(history[index].size()-(c+1))).y)
                    );
                    //System.out.println("drawn");
                }catch(Exception ex){}
            }
        }
        }
        g.setColor(Color.black);
    }
    
    public void draw(Center c, Graphics g){
        if(c.dim == Center.Dimension.CHARGE){
            g.setColor(Color.blue);
        } else if(c.dim == Center.Dimension.MASS){
            g.setColor(Color.red);
        }
        
        this.drawCenter(g, Vector.sMult(1/e.lengthUnit, c.s), 5);
        
    }
    
    public void draw(Graphics g, Body b, String caption, int index){
        this.draw(g, b, index);
        //g.drawString(caption, (int) Math.round(Vector.sMult(1/e.lengthUnit, b.s).x), (int) Math.round(Vector.sMult(1/e.lengthUnit, b.s).y)+10);
    }
    
    /*
     * Code by Sebastian Geisler (11SPK)
     */
    void drawArrow(Graphics g1, int x1, int y1, double x2, double y2) {
            int ARR_SIZE = 4;

            Graphics2D g = (Graphics2D) g1.create();
            double dx = x2 - x1, dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx*dx + dy*dy);
            AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
            at.concatenate(AffineTransform.getRotateInstance(angle));
            g.transform(at);

            // Draw horizontal arrow starting in (0, 0)
            g.drawLine(0, 0, len, 0);
            g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                          new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
    /*
     * End of Code by Sebastian Geisler (11SPK)
     */

    public static void print(String s, Vector v){
        System.out.println(s + " (" + v.x + " | " + v.y + ")");
    }
    
}
