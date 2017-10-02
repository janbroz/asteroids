/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.asteroids;

import Math.Matrix4x4;
import Math.Rotation;
import Math.Translation;
import Math.Vector4;
import java.awt.Color;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Giskard
 */
public class Asteroid {
    PolygonObject asteroid;
    PolygonObject transformedAsteroid;
       
    Matrix4x4 currentTransformation = new Matrix4x4();
    float movementSpeed = 2f;
    float ttl = 1500f;
    float currentTtl = 0f;
    boolean alive = true;
    
    public Asteroid(int x, int y, Rotation rot){
        
    }
    
    public Asteroid(){
        asteroid = new PolygonObject();
        
        
        Vector4[] vertexArray = new Vector4[10];
        vertexArray[0] = new Vector4(-10, 20, 0);
        vertexArray[1] = new Vector4(10, 30, 0);
        vertexArray[2] = new Vector4(27, 15, 0);
        vertexArray[3] = new Vector4(25, 3, 0);
        vertexArray[4] = new Vector4(12, -5, 0);
        vertexArray[5] = new Vector4(-8, -3, 0);
        vertexArray[6] = new Vector4(-3, 6, 0);
        vertexArray[7] = new Vector4(9, 25, 0);
        vertexArray[8] = new Vector4(12, 12, 0);
        vertexArray[9] = new Vector4(17, 3, 0);
        Edge edge = new Edge(vertexArray[0], vertexArray[1]);
        Edge edge2 = new Edge(vertexArray[1], vertexArray[2]);
        Edge edge3 = new Edge(vertexArray[2], vertexArray[3]);
        Edge edge4 = new Edge(vertexArray[3], vertexArray[4]);
        Edge edge5 = new Edge(vertexArray[4], vertexArray[5]);
        Edge edge6 = new Edge(vertexArray[5], vertexArray[0]);
        Edge edge7 = new Edge(vertexArray[6], vertexArray[7]);
        Edge edge8 = new Edge(vertexArray[8], vertexArray[9]);
        asteroid.addEdge(edge);
        asteroid.addEdge(edge2);
        asteroid.addEdge(edge3);
        asteroid.addEdge(edge4);
        asteroid.addEdge(edge5);
        asteroid.addEdge(edge6);
        asteroid.addEdge(edge7);
        asteroid.addEdge(edge8);
        
        asteroid.SetCollisionBox(-10, -5, 37, 35);
        int x_rand = ThreadLocalRandom.current().nextInt(-400, 400 + 1);
        int y_rand = ThreadLocalRandom.current().nextInt(-400, 400 + 1);
        Translation trans = new Translation(x_rand,y_rand,0);
        
        double random = 0.1 + Math.random() * (5 - 0.1);
        Rotation rot = new Rotation(random, 0);
        
        currentTransformation = Matrix4x4.times(currentTransformation, trans);
        currentTransformation = Matrix4x4.times(currentTransformation, rot);
        transformedAsteroid = PolygonObject.transformObject(asteroid, currentTransformation);
        
        
        Random rand = new Random();
        float result = rand.nextFloat() * (0.1f - 3f) + 0.1f;
        movementSpeed = result;
        //System.out.println("Created an asteroid");
    }
    
    public void DrawAsteroid(CGAsteroids ast){
        ast.g2d.setColor(Color.magenta);
        transformedAsteroid.drawObject(ast);
    }
    
    public void UpdateTransform(){
        Translation trans = new Translation(movementSpeed,0,0);
        currentTransformation = Matrix4x4.times(currentTransformation, trans);
        transformedAsteroid = PolygonObject.transformObject(asteroid, currentTransformation);
    }
    
    public void TickActor(){
        UpdateTransform();
        currentTtl++;
        
    }
    
}
