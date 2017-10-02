/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.asteroids;


import Math.Matrix4x4;
import Math.Translation;
import Math.Vector4;
import static cg.asteroids.Spaceship.EDirection.LEFT;
import static cg.asteroids.Spaceship.EDirection.RIGHT;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JFrame;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
/**
 *
 * @author Giskard
 */
public class CGAsteroids extends JPanel implements KeyListener{

    /**
     * @param args the command line arguments
     */
    
    public static int FRAME_WIDTH = 800;
    public static int FRAME_HEIGHT = 600;
    
    public static int AXIS_SIZE = 20;
    
    Dimension size;
    Graphics2D g2d;
    PolygonObject po;
    PolygonObject transformedObject;
    Matrix4x4 currentTransformation = new Matrix4x4();
    Spaceship ship;
    
    ArrayList<Asteroid> asteroids;
    
    float maxAsteroids = 5;
    float currentAsteroids = 0;
    int score = 0;
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    // Lets simulate the actor updating
    public void TickActor() {
     final Runnable ticker = new Runnable() {
       public void run() {
           IsShipAlive();
           Update();
           repaint();
       } 
     };
     final ScheduledFuture<?> beeperHandle =
       scheduler.scheduleAtFixedRate(ticker, 0, 10, TimeUnit.MILLISECONDS);
     scheduler.schedule(new Runnable() {
       public void run() { beeperHandle.cancel(true); }
     }, 60 * 60, TimeUnit.SECONDS);
   }
    
    void IsShipAlive(){
        if (ship.alive) {
            ship.UpdateTransform();
            ship.CheckCollision(asteroids);
        }    
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        size = getSize();
        
        // Draw the polygon object
        g2d.setColor(Color.BLUE);
        // Transform the object
        //transformObject();
        
        // Apply UVN matrix
        //applyUVN();
        
        // Apply projection
        //applyProjection();

        // Draw the object
        //transformedObject.drawObject(this);
        //po.drawObject(this);
        if (ship.alive) {
            ship.DrawShip(this);
        }
        
        //ship.po.drawObject(this);
        Iterator<Asteroid> a_iter = asteroids.iterator();
        while(a_iter.hasNext()){
            Asteroid m_asteroid = a_iter.next();
            g2d.setColor(Color.green);
            m_asteroid.TickActor();
            m_asteroid.DrawAsteroid(this);
            if (m_asteroid.ttl <= m_asteroid.currentTtl || !m_asteroid.alive ) {
                if (!m_asteroid.alive) {
                    score += 10;
                }
                a_iter.remove();
                currentAsteroids--;
            }
        }
        
        DrawHUD(g);
    }
    
    public void DrawHUD(Graphics g){
        g.setColor(Color.black);
        g.drawString("SCORE:", 25, 25);
        g.drawString(String.valueOf(score), 100, 25);
    }
    
    public void Update(){
        //System.out.println(currentAsteroids);
        if (currentAsteroids < maxAsteroids) {
            Asteroid asteroid = new Asteroid();
            asteroids.add(asteroid);
            currentAsteroids++;
        }
    }
    
    public void drawOneLine(int x1, int y1, int x2, int y2) {

        x1 = x1 + size.width / 2;
        x2 = x2 + size.width / 2;

        y1 = size.height / 2 - y1;
        y2 = size.height / 2 - y2;

        g2d.drawLine(x1, y1, x2, y2);
    }
    
    private void transformObject() {
        transformedObject = PolygonObject.transformObject(po, currentTransformation);
    }
    
    public void keyReleased(KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_A) {        // Left
            ship.SetTurning(LEFT, false);
        } else if(ke.getKeyCode() == KeyEvent.VK_D) { // Right
            ship.SetTurning(RIGHT, false);
        } else if(ke.getKeyCode() == KeyEvent.VK_SPACE) {        // Left
            ship.SetAcceleration(false);
        } else if(ke.getKeyCode() == KeyEvent.VK_UP) { // Down
            ship.SetFiringLaser(false);
        }
    }
    
    public void keyPressed(KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_A) {
            ship.SetTurning(LEFT, true);// Left
            //Translation trans = new Translation(-10, 0, 0);
            //currentTransformation = Matrix4x4.times(currentTransformation, trans);
        } else if(ke.getKeyCode() == KeyEvent.VK_D) { // Right
            ship.SetTurning(RIGHT, true);// Left
            //Translation trans = new Translation(10, 0, 0);
            //currentTransformation = Matrix4x4.times(currentTransformation, trans);
        } else if(ke.getKeyCode() == KeyEvent.VK_W) { // Up
            Translation trans = new Translation(0, 10, 0);
            currentTransformation = Matrix4x4.times(currentTransformation, trans);
        } else if(ke.getKeyCode() == KeyEvent.VK_S) { // Down
            Translation trans = new Translation(0, -10, 0);
            currentTransformation = Matrix4x4.times(currentTransformation, trans);
        } else if(ke.getKeyCode() == KeyEvent.VK_SPACE) { // Down
            ship.SetAcceleration(true);
        } else if(ke.getKeyCode() == KeyEvent.VK_UP) { // Down
            ship.SetFiringLaser(true);
        } else if(ke.getKeyCode() == KeyEvent.VK_DOWN) { // Down
            ship.Shoot();
        }
    }
    
    public void keyTyped(KeyEvent ke) {
      //System.out.println("Key Typed");
    }
    
    public void InitializePo(){
        po = new PolygonObject();
        ship = new Spaceship();
        ship.CreateSpaceShip();
        
        asteroids = new ArrayList<>();
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        CGAsteroids asteroids = new CGAsteroids();
        asteroids.InitializePo();
        
        JFrame frame = new JFrame("Wire Frame Object");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add a panel called DibujarCasita3D
        frame.add(asteroids);
        // DibujarCasita will respond to the key events
        frame.addKeyListener(asteroids);
        
        frame.setSize(CGAsteroids.FRAME_WIDTH, CGAsteroids.FRAME_HEIGHT);
        // Put the frame in the middle of the window
        frame.setLocationRelativeTo(null);
        // Show the frame
        frame.setVisible(true);
        // This one makes sure the stuff is redraw.
        asteroids.TickActor();
        
    }
    
}
