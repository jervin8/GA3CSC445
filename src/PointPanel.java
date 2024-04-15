//CSC 445 Assignment 1
//Matthew Tennyson
//
//Description:
//  This file contains a class that extends JPanel. It contains an array
//  of points, and plots those points so that they are scaled to the size
//  of the panel.

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

class PointPanel extends JPanel implements MouseListener
{
    private Point2D.Double[] points;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private Point[] scaledPoints;
    private ArrayList<Pair<Integer,Integer>> edges;
    private int clicked=-1;

    private final int POINT_SIZE = 30;
    private final int MARGIN = 20;

    public PointPanel(Point2D.Double[] pts, ArrayList<Pair<Integer,Integer>> edgs)
    {
        edges = edgs;
        points = pts;
        scaledPoints = new Point[pts.length];
        for(int i=0; i<pts.length; i++)
            scaledPoints[i] = new Point();

        minX = pts[0].getX();
        maxX = pts[0].getX();
        minY = pts[0].getY();
        maxY = pts[0].getY();

        for(int i=1; i<points.length; i++)
        {
            Point2D.Double p = points[i];
            if(p.getX()>maxX)
                maxX = p.getX();
            if(p.getX()<minX)
                minX = p.getX();
            if(p.getY()>maxY)
                maxY = p.getY();
            if(p.getY()<minY)
                minY = p.getY();
        }

        System.out.println("minX: " + minX);
        System.out.println("maxX: " + maxX);
        System.out.println("minY: " + minY);
        System.out.println("maxY: " + maxY);

        addMouseListener(this);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        double width = getSize().getWidth() - 2*MARGIN;
        double height = getSize().getHeight() - 2*MARGIN;

        //scale and set the point locations
        for(int i=0; i<points.length; i++)
        {
            double percentWidth = (points[i].getX()-minX)/(maxX-minX);
            int relativeX = (int)(percentWidth*width + MARGIN);

            double percentHeight = (points[i].getY()-minY)/(maxY-minY);
            int relativeY = (int)(height - percentHeight*height + MARGIN);

            scaledPoints[i].setLocation(relativeX, relativeY);
        }

        //draw the edges
        for(int i=0; i<edges.size(); i++)
        {
            Pair<Integer,Integer> pair = edges.get(i);
            int fromX = scaledPoints[pair.getKey()].x;
            int fromY = scaledPoints[pair.getKey()].y;
            int toX = scaledPoints[pair.getValue()].x;
            int toY = scaledPoints[pair.getValue()].y;
            g.drawLine(fromX, fromY, toX, toY);
        }

        //draw the points
        for(int i=0; i<points.length; i++)
        {
            if(i==clicked)
                g.setColor(Color.GREEN);
            else
                g.setColor(Color.RED);

            g.fillOval(scaledPoints[i].x-POINT_SIZE/2, scaledPoints[i].y-POINT_SIZE/2, POINT_SIZE, POINT_SIZE);

            g.setColor(Color.BLACK);
            FontMetrics font = g.getFontMetrics();
            g.drawString(i+"", scaledPoints[i].x-font.stringWidth(i+"")/2, scaledPoints[i].y+font.getHeight()/4);
        }
    }

    public void mouseClicked(MouseEvent e)
    {
        int mx = e.getX();
        int my = e.getY();

        for(int i=0; i<points.length; i++)
        {
            Point p = scaledPoints[i];

            if(Math.abs(mx-p.x)<=POINT_SIZE/2 && Math.abs(my-p.y)<=POINT_SIZE/2)
            {
                clicked = i;
                System.out.println("Point " + i + " clicked: " + points[i]);
                repaint();
            }
        }
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }
}

class Pair<K,V> extends AbstractMap.SimpleEntry<K,V>
{
    public Pair(K key, V value)
    {
        super(key,value);
    }
}
