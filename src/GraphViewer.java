//CSC Graph Assignment 2
//Max Thompson
//CSC 445
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Point {
    double x, y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

class Edge {
    int start, end;

    Edge(int start, int end) {
        this.start = start;
        this.end = end;
    }
}

class PointPlotter extends JPanel {
    private final List<Point> points;
    private final List<Edge> edges;
    private double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
    private double scale;
    private final int padding = 20; 
    private final int pointDiameter = 40; 

    public PointPlotter(List<Point> points, List<Edge> edges) {
        this.points = points;
        this.edges = edges;
        calculateBounds();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    private void calculateBounds() {
        for (Point point : points) {
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        double width = getWidth() - 2 * padding - pointDiameter;
        double height = getHeight() - 2 * padding - pointDiameter;
        scale = Math.min(width / (maxX - minX), height / (maxY - minY));
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        for (Point point : points) {
            int scaledX = (int) ((point.x - minX) * scale) + padding;
            int scaledY = (int) ((maxY - point.y) * scale) + padding;
            if (Math.abs(mouseX - scaledX) < pointDiameter && Math.abs(mouseY - scaledY) < pointDiameter) {
                JOptionPane.showMessageDialog(this, "Point: (" + point.x + ", " + point.y + ")");
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        calculateBounds();
    
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics metrics = g.getFontMetrics();

        g2d.setColor(Color.BLACK); 
        for (Edge edge : edges) {
            Point start = points.get(edge.start);
            Point end = points.get(edge.end);
            int x1 = (int) ((start.x - minX) * scale) + padding;
            int y1 = (int) ((maxY - start.y) * scale) + padding;
            int x2 = (int) ((end.x - minX) * scale) + padding;
            int y2 = (int) ((maxY - end.y) * scale) + padding;
            g2d.drawLine(x1, y1, x2, y2);
        }
    

        g2d.setColor(Color.RED);
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            int x = (int) ((point.x - minX) * scale) + padding;
            int y = (int) ((maxY - point.y) * scale) + padding;
            g2d.fillOval(x - pointDiameter / 2, y - pointDiameter / 2, pointDiameter, pointDiameter);
    
            String label = Integer.toString(i);
            int labelWidth = metrics.stringWidth(label);
            int labelHeight = metrics.getHeight();
    
            g2d.setColor(Color.BLACK);
            g2d.drawString(label, x - labelWidth / 2, y + labelHeight / 2);
            g2d.setColor(Color.RED);
        }
    }
}

//creates graph viewer
public class GraphViewer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                List<Point> points = new ArrayList<>();
                List<Edge> edges = new ArrayList<>();
                readPointsAndEdgesFromFile(filePath, points, edges);
                if (!points.isEmpty() && !edges.isEmpty()) {
                    JFrame frame = new JFrame("Graph Viewer");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(1000, 800);
                    frame.setLocationRelativeTo(null);

                    PointPlotter plotter = new PointPlotter(points, edges);
                    frame.add(plotter);
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No points or edges found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static void readPointsAndEdgesFromFile(String filePath, List<Point> points, List<Edge> edges) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Find number of points in max.txt
            String line = getNextNonEmptyLine(reader);
            if (line == null) throw new IOException("File format incorrect or empty file.");
            
            int numOfPoints = Integer.parseInt(line);
            for (int i = 0; i < numOfPoints; i++) {
                line = getNextNonEmptyLine(reader);
                if (line == null) throw new IOException("Expected point data missing.");
                
                String[] parts = line.split("\\s*,\\s*");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                points.add(new Point(x, y));
            }
    
            // Read number of edges in file
            line = getNextNonEmptyLine(reader);
            if (line == null) throw new IOException("Expected edges data missing.");
            
            int numOfEdges = Integer.parseInt(line);
            for (int i = 0; i < numOfEdges; i++) {
                line = getNextNonEmptyLine(reader);
                if (line == null) throw new IOException("Expected edge connection data missing.");
                
                String[] parts = line.split("\\s+");
                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);
                edges.add(new Edge(start, end));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //ignored blank spaces in file
    private static String getNextNonEmptyLine(BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
            if (line == null) {
                return null; 
            }
            line = line.trim();
        } while (line.isEmpty());
        return line;
    }
}