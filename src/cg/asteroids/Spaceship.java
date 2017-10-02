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
import java.util.ArrayList;
import java.util.Iterator;


/**
 *
 * @author Giskard
 */
public class Spaceship {
    PolygonObject ship;
    PolygonObject transformedShip;
    
    PolygonObject trail;
    PolygonObject transformedTrail;
    
    PolygonObject laser;
    PolygonObject transformedLaser;
    
    PolygonObject exploding;
    PolygonObject transformedExploding;
    
    PolygonObject collisionBox;
    PolygonObject transformedCB;
    
    Matrix4x4 currentTransformation = new Matrix4x4();
    
    boolean accelerating;
    boolean turningLeft;
    boolean turningRight;
    boolean firingLasers;
    boolean alive;
    
    float maxAcceleration = 5f;
    float currentAcceleration = 0f;
    
    float maxLaser = 100f;
    float currentLaser = 100f;
    float laserRegen = 0.01f;
    float laserCooldown = 200f;
    float laserCurrentCd = 0f;
    
    ArrayList<Laser> lasers;
    public enum EDirection {
        LEFT, RIGHT
    }
    
    int x;
    int y;
    int width;
    int height;
    
    public void CreateSpaceShip(){
        ship = new PolygonObject();
        trail = new PolygonObject();
        laser = new PolygonObject();
        exploding = new PolygonObject();
        lasers = new ArrayList<>();
        accelerating = false;
        firingLasers = false;
        alive = true;
        // Ship creation
        Vector4[] vertexArray = new Vector4[4];
        vertexArray[0] = new Vector4(0, 20, 0);
        vertexArray[1] = new Vector4(10, 0, 0);
        vertexArray[2] = new Vector4(0, -20, 0);
        vertexArray[3] = new Vector4(50, 0, 0);
        Edge edge = new Edge(vertexArray[0], vertexArray[1]);
        Edge edge2 = new Edge(vertexArray[2], vertexArray[1]);
        Edge edge3 = new Edge(vertexArray[0], vertexArray[3]);
        Edge edge4 = new Edge(vertexArray[2], vertexArray[3]);
        ship.addEdge(edge);
        ship.addEdge(edge2);
        ship.addEdge(edge3);
        ship.addEdge(edge4);
        
        // Trail creation
        Vector4[] vertexArrayT = new Vector4[3];
        vertexArrayT[0] = new Vector4(-30, 0, 0);
        vertexArrayT[1] = new Vector4(6, 8, 0);
        vertexArrayT[2] = new Vector4(6, -8, 0);
        Edge edgeT = new Edge(vertexArrayT[0], vertexArrayT[1]);
        Edge edgeT2 = new Edge(vertexArrayT[0], vertexArrayT[2]);
        trail.addEdge(edgeT);
        trail.addEdge(edgeT2);
        
        // Laser creation
        Vector4[] vertexArrayL = new Vector4[2];
        vertexArrayL[0] = new Vector4(50, 0, 0);
        vertexArrayL[1] = new Vector4(150, 0, 0);
        Edge edgeL = new Edge(vertexArrayL[0], vertexArrayL[1]);
        laser.addEdge(edgeL);
        
        // Explosion creation
        Vector4[] vertexArrayE = new Vector4[12];
        vertexArrayE[0] = new Vector4(-10, 10, 0);
        vertexArrayE[1] = new Vector4(-20, 20, 0);
        vertexArrayE[2] = new Vector4(-10, -8, 0);
        vertexArrayE[3] = new Vector4(-15, -17, 0);
        vertexArrayE[4] = new Vector4(10, 8, 0);
        vertexArrayE[5] = new Vector4(17, 23, 0);
        vertexArrayE[6] = new Vector4(10, -8, 0);
        vertexArrayE[7] = new Vector4(17, -23, 0);
        vertexArrayE[8] = new Vector4(-4, 4, 0);
        vertexArrayE[9] = new Vector4(-6, -2, 0);
        vertexArrayE[10] = new Vector4(5, 7, 0);
        vertexArrayE[11] = new Vector4(4, -4, 0);
        Edge edgeE = new Edge(vertexArrayE[0], vertexArrayE[1]);
        Edge edgeE2 = new Edge(vertexArrayE[2], vertexArrayE[3]);
        Edge edgeE3 = new Edge(vertexArrayE[4], vertexArrayE[5]);
        Edge edgeE4 = new Edge(vertexArrayE[6], vertexArrayE[7]);
        Edge edgeE5 = new Edge(vertexArrayE[8], vertexArrayE[9]);
        Edge edgeE6 = new Edge(vertexArrayE[8], vertexArrayE[10]);
        Edge edgeE7 = new Edge(vertexArrayE[10], vertexArrayE[11]);
        Edge edgeE8 = new Edge(vertexArrayE[9], vertexArrayE[11]);
        exploding.addEdge(edgeE);
        exploding.addEdge(edgeE2);
        exploding.addEdge(edgeE3);
        exploding.addEdge(edgeE4);
        exploding.addEdge(edgeE5);
        exploding.addEdge(edgeE6);
        exploding.addEdge(edgeE7);
        exploding.addEdge(edgeE8);
        
        Translation trans = new Translation(0, 0, 0);
        currentTransformation = Matrix4x4.times(currentTransformation, trans);
        transformedShip = PolygonObject.transformObject(ship, currentTransformation);
        //transformedShip.SetCollisionBox(0, 0, 50, 50);
        ship.SetCollisionBox(0, -20, 50, 40);
    }

    public void DrawShip(CGAsteroids ast){
        if (alive) {
            if (accelerating) {
                transformedShip.drawObject(ast);
                ast.g2d.setColor(Color.red);
                transformedTrail.drawObject(ast);
            }else{
                transformedShip.drawObject(ast);
            }
            if (firingLasers) {
                ast.g2d.setColor(Color.green);
                transformedLaser.drawObject(ast);
            }
        }else{
            ast.g2d.setColor(Color.red);
            transformedExploding.drawObject(ast);
        }
        
        DrawLasers(ast);
    }
    
    public void SetAcceleration(boolean newAccel){
        accelerating = newAccel;
    }
    
    public void UpdateTransform(){
        UpdateLasers();
        UpdateAcceleration();
        UpdateRotation();
        Translation trans = new Translation(currentAcceleration, 0, 0);
        currentTransformation = Matrix4x4.times(currentTransformation, trans);
        
        
        transformedShip = PolygonObject.transformObject(ship, currentTransformation);
        transformedTrail = PolygonObject.transformObject(trail, currentTransformation);
        transformedLaser = PolygonObject.transformObject(laser, currentTransformation);
        transformedExploding = PolygonObject.transformObject(exploding, currentTransformation);
        //System.out.println("I'm a leaf on the wind"); 
    }
    
    void UpdateAcceleration(){
        if (accelerating) {
            currentAcceleration = clamp(currentAcceleration + 0.05f, 0, maxAcceleration);
        }else{
            currentAcceleration = clamp(currentAcceleration - 0.02f, 0, maxAcceleration);
        }
        //System.out.println(currentAcceleration); 
    }
    
    void UpdateRotation(){
        if (turningLeft) {
            Rotation rot = new Rotation(0.03, 0);
            currentTransformation = Matrix4x4.times(currentTransformation, rot);
            //System.out.println("Liberal"); 
        }
        if (turningRight) {
            Rotation rot = new Rotation(-0.03, 0);
            currentTransformation = Matrix4x4.times(currentTransformation, rot);
            //System.out.println("Conservators"); 
        }
    }
    
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
    
    public void SetTurning(EDirection index, boolean isAccel){
        switch(index){
            case LEFT: turningLeft = isAccel;
                break;
            case RIGHT: turningRight = isAccel;
                break;
            default: break;
        }
    }
    
    public void SetFiringLaser(boolean firing){
        firingLasers = firing;
    }
    
    public void Shoot(){
        Laser shoot = new Laser(currentTransformation);
        lasers.add(shoot);
    }
    
    void UpdateLasers(){
        Iterator<Laser> l_iter = lasers.iterator();
        while(l_iter.hasNext()){
            Laser m_laser = l_iter.next();
            m_laser.TickLaser();
            if (m_laser.currentTtl >= m_laser.ttl || !m_laser.alive) {
                l_iter.remove();
            }
        }
    }
    
    void DrawLasers(CGAsteroids ast){
        Iterator<Laser> l_iter = lasers.iterator();
        while(l_iter.hasNext()){
            Laser m_laser = l_iter.next();
            ast.g2d.setColor(Color.green);
            m_laser.transformedLaser.drawObject(ast);
        }
        /*
        for(Laser m_laser: lasers) {
            ast.g2d.setColor(Color.green);
            m_laser.transformedLaser.drawObject(ast);
        }
        */
    }
    
    public void CheckCollision(ArrayList<Asteroid> asteroids){
        boolean crashed = false;
        for(Asteroid ast : asteroids){
            crashed = transformedShip.collision.CheckCollision(ast.transformedAsteroid.collision.tx0y0);
            if (crashed) 
                alive = false; 
            crashed = transformedShip.collision.CheckCollision(ast.transformedAsteroid.collision.tx0y1);
            if (crashed) 
                alive = false;
            crashed = transformedShip.collision.CheckCollision(ast.transformedAsteroid.collision.tx1y0);
            if (crashed) 
                alive = false;
            crashed = transformedShip.collision.CheckCollision(ast.transformedAsteroid.collision.tx1y1);
            if (crashed) {
                alive = false;
            }
        }
        
        //crashed = transformedShip.collision.CheckCollision(new Vector4(100, 0,0));
        if (crashed) {
            alive = false;
        }else{
            for(Laser lsr : lasers){
                lsr.CheckCollision(asteroids);
            }
        }
    }    
}
