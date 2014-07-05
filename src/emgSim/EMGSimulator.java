/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emgSim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Felix Patzschke
 */
public class EMGSimulator extends JPanel{
    
    public void main(){
        
        this.main(Tafelwerk.DEFAULT_SIMULATION_X, Tafelwerk.DEFAULT_SIMULATION_Y);
        
    }
    
    public void main(int width, int height){
        
        final JFrame dWindow = new JFrame(Tafelwerk.DEFAULT_WINDOW_TITLE);
        
        dWindow.setSize(width+8, height+31);
        dWindow.setResizable(false);
        dWindow.setLayout(new BorderLayout());
        dWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
            dWindow.setIconImage(ImageIO.read(EMGSimulator.class.getResource(Tafelwerk.DEFAULT_ICON_IMAGE_PATH)));
        } catch (Exception ex) {}
        
        final Engine eng = new Engine(width, height);
        final SimulationPanel s = new SimulationPanel(eng);
        dWindow.add(s, BorderLayout.CENTER);
        
        s.nextBody = Tafelwerk.DEFAULT_BODY;
        s.togglePause();
        
        dWindow.setBackground(Color.white);
        
        
        
        dWindow.setVisible(true);
        //s.dFrame.setVisible(true);
        
        dWindow.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("Pressed: " + e.getExtendedKeyCode());
                /*
                 * 16 -> [shift]
                 * 17 -> [ctrl]
                 * 18 -> [alt]
                 * 
                 * 32 -> [space]
                 * 10 -> [enter]
                 * 
                 * 69 -> [e]
                 * 71 -> [g]
                 * 
                 * 80 -> [p]
                 * 76 -> [l]
                 * 84 -> [t]
                 * 
                 * 107/521 -> [+]
                 * 109/45 -> [-]
                 * 
                 * 127 -> [ENTF]
                 * 
                 * 83 -> [s]
                 */
                if(e.getExtendedKeyCode()==16){
                    s.shiftPressed = true;
                }
                if(e.getExtendedKeyCode()==17){
                    s.ctrlPressed = true;
                }
                if(e.getExtendedKeyCode()==18){
                    //s.altPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("Released: " + e.getExtendedKeyCode());
                
                if(e.getExtendedKeyCode()==32){
                    s.togglePause();
                } if(e.getExtendedKeyCode()==76){
                    s.togglePositionLock();
                } if(e.getExtendedKeyCode()==80){
                    s.togglePath();
                }
                
                //if(s.ctrlPressed){
                    if(e.getExtendedKeyCode()==107){
                        s.oneStepForth();
                    } else if(e.getExtendedKeyCode()==521){
                        s.oneStepForth();
                    } else if(e.getExtendedKeyCode()==109){
                        s.oneStepBack();
                    } else if(e.getExtendedKeyCode()==45){
                        s.oneStepBack();
                    } else if(e.getExtendedKeyCode()==84){
                        s.loadStandardTaskModel();
                    } else if(e.getExtendedKeyCode()==127){
                        s.clearSimulation();
                    }
                //}
                
                //release modifiers
                if(e.getExtendedKeyCode()==16){
                    s.shiftPressed = false;
                }
                if(e.getExtendedKeyCode()==17){
                    s.ctrlPressed = false;
                }
                if(e.getExtendedKeyCode()==18){
                    //s.altPressed = false;
                }
            }
            
        });

        
        
        while(true){
            s.repaint();
        }
    }

}
