/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emgSim;

/**
 *
 * @author Felix Patzschke
 */
class Angle {
    
    private double deg;
    
    public void setAngleDegrees(double a){
        this.deg = a;
    }
    
    public void setAngleRadiants(double r){
        this.deg = (180*r)/(Math.PI);
    }
    
    
    public double getDegrees(){
        return this.deg;
    }
    
    public double getRadiants(){
        return (this.deg/180)*Math.PI;
    }
    
}
