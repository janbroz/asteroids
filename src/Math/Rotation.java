/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;

/**
 *
 * @author Giskard
 */
public class Rotation extends Matrix4x4{
    public Rotation() {
        super();
    }
    
    public Rotation(double angle, int axis){
        super();
        
        if (axis == 0) {
            // Rotate around z
            matrix[0][0] = Math.cos(angle);
            matrix[0][1] = -(Math.sin(angle));
            matrix[1][0] = Math.sin(angle);
            matrix[1][1] = Math.cos(angle);
        }else if(axis == 1){
            // Rotate around x
            matrix[1][1] = Math.cos(angle);
            matrix[2][1] = -(Math.sin(angle));
            matrix[1][2] = Math.sin(angle);
            matrix[2][2] = Math.cos(angle);
            
        }else{
            // Rotate around y
            matrix[0][0] = Math.cos(angle);
            matrix[0][2] = -(Math.sin(angle));
            matrix[2][0] = Math.sin(angle);
            matrix[2][2] = Math.cos(angle);
        }
                
        
        
    }  
}
