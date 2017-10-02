package cg.asteroids;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import Math.Matrix4x4;
import Math.Vector4;
import java.awt.Color;

/**
 * This class handles a collection of edges that form the object to be drawn
 * @author htrefftz
 */
public class PolygonObject {
    ArrayList<Edge> edges;
    ArrayList<Edge> collisionEdges;
    
    boolean DEBUG = false;
    public boolean hasCollision = false;
    public CollisionBox collision;
    /**
     * Constructor
     */
    public PolygonObject() {
        edges = new ArrayList<>();
    }
    
    /**
     * Add an edge to the collection of edges
     * @param edge edge to be added
     */
    public void addEdge(Edge edge) {
        edges.add(edge);
    }
    
    /**
     * Draw the lines composing the polygon
     * @param dc JPanel to draw the object on
     */
    public void drawObject(CGAsteroids dc) {
        //System.out.println(hasCollision);
        for(Edge edge: edges) {
            drawOneLine(dc, edge);
        }
        //System.out.println(collision.x0y0);
        /*
        if (hasCollision) {
            System.out.println("it has collision");
            DrawCollisionBox(dc);
        }else{
            System.out.println("Collision is fucked up");
        }
        */
        if (hasCollision) {
            //DrawCollisionBox(dc);
        }
    }

    /**
     * Ask the JPanel to draw one of the edges composing the object
     * @param dc JPanel to draw on
     * @param edge Edge to draw
     */
    public void drawOneLine(CGAsteroids dc, Edge edge) {
        int x1 = (int)edge.start.getX();
        int y1 = (int)edge.start.getY();
        int x2 = (int)edge.end.getX();
        int y2 = (int)edge.end.getY();
        
        dc.drawOneLine(x1, y1, x2, y2);
    }
    
    /**
     * Transform an object given a transformation.
     * The vertices of the object are transformed.
     * @param transformation 
     */
    public void transformObject(Matrix4x4 transformation) {
        for(Edge e: edges) {
            if(DEBUG)
                System.out.println("Before: " + e);
            e.start = Matrix4x4.times(transformation, e.start);
            e.end   = Matrix4x4.times(transformation, e.end);
            if(DEBUG)
                System.out.println("After: " + e);
        }
    }
    
    public static PolygonObject transformObject(PolygonObject po, 
            Matrix4x4 transformation) {
        
        //System.out.println(transformation);
        PolygonObject newObject = new PolygonObject();
        //System.out.println("New one is:");
        for(Edge e: po.edges) {
            //System.out.println(e.start);
            Vector4 newStart = Matrix4x4.times(transformation, e.start);
            Vector4 newEnd = Matrix4x4.times(transformation, e.end);
            Edge newEdge = new Edge(newStart, newEnd);
            newObject.addEdge(newEdge);
        }
        //System.out.println("New one is:");
        //System.out.println(transformation);
        
        if (po.hasCollision) {
            
            newObject.hasCollision = true;
            
            CollisionBox newCollision = po.collision;
            //newCollision.x0y0 = Matrix4x4.times(transformation, newCollision.x0y0);
            //System.out.println(newCollision.x0y0);
            //System.out.println(newCollision.x1y1);
            newCollision.UpdateTransform(transformation);
            newObject.collision = newCollision;
            /*
            newCollision.UpdateTransform(transformation);
            newObject.collision = newCollision;
            */
        }
        
        return newObject;
    }
    
    public void ChangeCollision(boolean coll){
        hasCollision = coll;
    }
    
    public void SetCollisionBox(int x, int y, int width, int height){
        collision = new CollisionBox(x, y, width, height);
        this.hasCollision = true;
    }
    
    void DrawCollisionBox(CGAsteroids dc){
        dc.g2d.setColor(Color.gray);
        dc.drawOneLine((int)collision.tx0y0.getX(), (int)collision.tx0y0.getY(), (int)collision.tx0y1.getX(), (int)collision.tx0y1.getY());
        dc.drawOneLine((int)collision.tx0y0.getX(), (int)collision.tx0y0.getY(), (int)collision.tx1y0.getX(), (int)collision.tx1y0.getY());
        dc.drawOneLine((int)collision.tx1y0.getX(), (int)collision.tx1y0.getY(), (int)collision.tx1y1.getX(), (int)collision.tx1y1.getY());
        dc.drawOneLine((int)collision.tx0y1.getX(), (int)collision.tx0y1.getY(), (int)collision.tx1y1.getX(), (int)collision.tx1y1.getY());
        
        //System.out.println(collision.x0y0);
    }
}
