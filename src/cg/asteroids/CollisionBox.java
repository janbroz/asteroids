/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.asteroids;

import Math.Matrix4x4;
import Math.Vector4;
import Math.Triangle;
/**
 *
 * @author Giskard
 */
public class CollisionBox {
    Vector4 x0y0;
    Vector4 x0y1;
    Vector4 x1y1;
    Vector4 x1y0;
    
    Vector4 tx0y0;
    Vector4 tx0y1;
    Vector4 tx1y1;
    Vector4 tx1y0;
    
    CollisionBox(int x, int y, int width, int height){
        x0y0 = new Vector4(x, y, 0);
        x0y1 = new Vector4(x, y+height, 0);
        x1y0 = new Vector4(x+width, y, 0);
        x1y1 = new Vector4(x+width, y+height, 0);
        
        tx0y0 = x0y0;
        tx0y1 = x0y1;
        tx1y1 = x1y1;
        tx1y0 = x1y0;
        //System.out.println(x0y0);
        //System.out.println(x1y1);
    }
    
    public void UpdateTransform(Matrix4x4 transformation){
        tx0y0 = Matrix4x4.times(transformation, x0y0);
        tx0y1 = Matrix4x4.times(transformation, x0y1);
        tx1y0 = Matrix4x4.times(transformation, x1y0);
        tx1y1 = Matrix4x4.times(transformation, x1y1);
    }
    
    public boolean CheckCollision(Vector4 point){
        boolean isInside = false;
        
        Triangle t1 = new Triangle(tx0y0.getX(), tx0y0.getY(), tx1y0.getX(), tx1y0.getY(), tx0y1.getX(), tx0y1.getY());
        isInside = t1.contains(point.getX(), point.getY());
        if (!isInside) {
            Triangle t2 = new Triangle(tx1y1.getX(), tx1y1.getY(), tx1y0.getX(), tx1y0.getY(), tx0y1.getX(), tx0y1.getY());
            isInside = t1.contains(point.getX(), point.getY());
        }
        
        //System.out.println(isInside);
        
        return isInside;
    }
    
}
