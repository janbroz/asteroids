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
public class Scaling extends Matrix4x4{
    public Scaling() {
        super();
    }
    
    public Scaling(double sx, double sy, double sz){
        super();
        
        matrix[0][0] = sx;
        matrix[1][1] = sy;
        matrix[2][2] = sz;
    }
    
}
