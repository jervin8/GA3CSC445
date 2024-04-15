//CSC 445 Assignment 1
//Matthew Tennyson
//
//Description:
//  This file contains a program that reads a file at the command-line
//  containing a list of points. The points are then plotted, and line
//  segments are drawn connecting them.

import java.io.File;
import java.util.*;
import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;

public class PointProgram
{
    public static void main(String[] args) throws Exception
    {
        Scanner scanner = new Scanner(new File(args[0]));
        scanner.useDelimiter("[,\\s]+");
        int numPoints = scanner.nextInt();
        System.out.println(numPoints);

        Point2D.Double[] points = new Point2D.Double[numPoints];
        for(int i=0; i<numPoints; i++)
        {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            points[i] = new Point2D.Double(x,y);
            System.out.println(points[i]);
        }

        int numEdges = scanner.nextInt();
        ArrayList<Pair<Integer,Integer>> edges = new ArrayList<Pair<Integer,Integer>>(numEdges);
        for(int i=0; i<numEdges; i++)
        {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            Pair<Integer,Integer> p = new Pair<Integer,Integer>(a,b);
            edges.add(p);
        }

        PointPanel panel = new PointPanel(points, edges);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setTitle("CSC 445 Graph Assignment");
        frame.setSize(600,400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
