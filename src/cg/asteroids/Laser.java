/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.asteroids;

import Math.Matrix4x4;
import Math.Translation;
import Math.Vector4;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Giskard
 */
public class Laser {
    PolygonObject laser;
    PolygonObject transformedLaser;
    
    Matrix4x4 currentTransformation;
    float movementSpeed = 5f;
    float ttl = 150f;
    float currentTtl = 0f;
    boolean alive = true;
    
    public Laser(Matrix4x4 transform){
        laser = new PolygonObject();
        
        Vector4[] vertexArray = new Vector4[4];
        vertexArray[0] = new Vector4(0, 2, 0);
        vertexArray[1] = new Vector4(30, 2, 0);
        vertexArray[2] = new Vector4(0, -2, 0);
        vertexArray[3] = new Vector4(30, -2, 0);
        Edge edge = new Edge(vertexArray[0], vertexArray[1]);
        Edge edge2 = new Edge(vertexArray[0], vertexArray[2]);
        Edge edge3 = new Edge(vertexArray[1], vertexArray[3]);
        Edge edge4 = new Edge(vertexArray[2], vertexArray[3]);
        laser.addEdge(edge);
        laser.addEdge(edge2);
        laser.addEdge(edge3);
        laser.addEdge(edge4);
        
        laser.SetCollisionBox(0, -2, 30, 4);
        
        currentTransformation = transform;
        Translation trans = new Translation(45, 0, 0);
        currentTransformation = Matrix4x4.times(currentTransformation, trans);
        transformedLaser = PolygonObject.transformObject(laser, currentTransformation);
    }
    
    public void TickLaser(){
        currentTtl++;
        Translation trans = new Translation(10, 0, 0);
        currentTransformation = Matrix4x4.times(currentTransformation, trans);
        
        transformedLaser = PolygonObject.transformObject(laser, currentTransformation);
        //System.out.println(currentTtl);
    }
    
    public void CheckCollision(ArrayList<Asteroid> asteroids){
        
        
        boolean crashed = false;
        for(Asteroid ast : asteroids){
            crashed = ast.transformedAsteroid.collision.CheckCollision(transformedLaser.collision.tx0y0);
            if(crashed)
                ast.alive = false;
            crashed = ast.transformedAsteroid.collision.CheckCollision(transformedLaser.collision.tx0y1);
            if(crashed)
                ast.alive = false;
            crashed = ast.transformedAsteroid.collision.CheckCollision(transformedLaser.collision.tx1y0);
            if(crashed)
                ast.alive = false;
            crashed = ast.transformedAsteroid.collision.CheckCollision(transformedLaser.collision.tx1y1);
            if(crashed)
                ast.alive = false;
            /*
            crashed = transformedLaser.collision.CheckCollision(ast.transformedAsteroid.collision.tx0y0);
            if (crashed){ 
                alive = false;
                System.out.println("killed an arab");
            }
            crashed = transformedLaser.collision.CheckCollision(ast.transformedAsteroid.collision.tx0y1);
            if (crashed){ 
                alive = false;
                System.out.println("killed an arab");
            }
            crashed = transformedLaser.collision.CheckCollision(ast.transformedAsteroid.collision.tx1y0);
            if (crashed){ 
                alive = false; 
                System.out.println("killed an arab");
            }
            crashed = transformedLaser.collision.CheckCollision(ast.transformedAsteroid.collision.tx1y1);
            if (crashed) {
                alive = false;
                ast.alive = false;
                System.out.println("killed an arab");
            }
            */
        }
        
        //crashed = transformedShip.collision.CheckCollision(new Vector4(100, 0,0));
        if (crashed) {
            alive = false;
        }
        
        
    }
}
