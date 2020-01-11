/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife3d;

import Jama.Matrix;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;

/**
 * A panel to display cells in 3D.
 * @author Hielke Hielkema
 */
public class WorldPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    
    // Private constants
    private final double CELL_SIZE_X = 20;
    private final double CELL_SIZE_Y = 20;
    private final double CELL_SIZE_Z = 20;
    
    // Private fields
    private int _worldSizeX;
    private int _worldSizeY;
    private int _worldSizeZ;
    private Cell[][][] _cells;
    private Matrix _projectionMatrix;
    private double _rX = -20.0;
    private double _rY = -8.0;
    private double _zoom = 1.0;
    
    /**
     * Constructor
     * @param worldWidth The width of the world in cells
     * @param worldHeight The height of the world in cells
     * @param worldDepth The depth of the world in cells
     * @param cells The cell array to display
     */
    public WorldPanel(int worldWidth, int worldHeight, int worldDepth, Cell[][][] cells) {
        // Copy the parameters
        this._worldSizeX = worldWidth;
        this._worldSizeY = worldHeight;
        this._worldSizeZ = worldDepth;
        this._cells = cells;
        
        // Set the background color
        setBackground(Color.black); 
        
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
        super.addMouseWheelListener(this);
        

        
        updateTransform();
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="Graphics">
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Update the transform Matrix
        this.updateTransform();
        
        double[] A = { - (_worldSizeX / 2), -(_worldSizeY / 2), -(_worldSizeZ / 2)};
        double[] B = { _worldSizeX - (_worldSizeX / 2), -(_worldSizeY / 2), -(_worldSizeZ / 2)};
        double[] C = { _worldSizeX - (_worldSizeX / 2), _worldSizeY -(_worldSizeY / 2), -(_worldSizeZ / 2)};
        double[] D = { - (_worldSizeX / 2), _worldSizeY -(_worldSizeY / 2), -(_worldSizeZ / 2)};
        
        // Translate points
        A = this.translate(A);
        B = this.translate(B);
        C = this.translate(C);
        D = this.translate(D);
        g.setColor(Color.yellow);
        this.fillPoly(g, A, B, C, D);
        
        // Create an integer to store the amount of cells alive
        int aliveCount = 0;
        
        // Draw the cells
        for(int x = 0; x < this._worldSizeX; x++) {
            for(int y = 0; y < this._worldSizeY; y++) {
                for(int z = this._worldSizeZ - 1; z >= 0; z--) {
                    if (_cells[x][y][z].GetState()) {
                        g.setColor(_cells[x][y][z].GetAgeColor());
                        this.drawBox(g, x - (this._worldSizeX / 2), y - (this._worldSizeY / 2), z - (this._worldSizeZ / 2));
                        aliveCount++;
                    }
                }
            }
        }
        
        // Draw the border
        drawBorders(g);
        
        // Draw info
        g.drawString("Game of life 3D - Hielke", 5, 15);
        g.drawString("Zoom: " + this._zoom * 100 + "%", 5, 30);
        g.drawString("Rotation: " + this._rX + ", " + this._rY + ", 0.0", 5, 45);
        g.drawString("World size: " + this._worldSizeX + "x" + this._worldSizeY + "x" + this._worldSizeZ, 5, 60);
        g.drawString("Cell size: " + this.CELL_SIZE_X + "x" + this.CELL_SIZE_Y + "x" + this.CELL_SIZE_Z, 5, 75);
        g.drawString("Cells alive: " + aliveCount, 5, 90);
    }
    
    private void drawBox(Graphics g, int x, int y, int z) {
        
        // Calculate coordinates of the points
        double[] A = { x , y, z };
        double[] B = { x + 1, y, z };
        double[] C = { x + 1, y + 1, z };
        double[] D = { x, y + 1, z };
        double[] E = { x, y, z + 1};
        double[] F = { x + 1, y, z + 1};
        double[] G = { x + 1, y + 1, z + 1};
        double[] H = { x, y + 1, z + 1};
        
        // Translate points
        A = this.translate(A);
        B = this.translate(B);
        C = this.translate(C);
        D = this.translate(D);
        E = this.translate(E);
        F = this.translate(F);
        G = this.translate(G);
        H = this.translate(H);
        
        
        // Fill sides
        fillPoly(g, A, B, C, D); // Front
        fillPoly(g, E, F, G, H); // Back
        fillPoly(g, A, B, F, E);
        fillPoly(g, B, F, G, C);
        fillPoly(g, D, E, G, H);
        fillPoly(g, A, E, H, D);
        
        // Draw lines
        g.setColor(Color.blue);

        // Front
        this.drawLine(g, A, B);
        this.drawLine(g, B, C);
        this.drawLine(g, C, D);
        this.drawLine(g, D, A);
        
        // Back
        this.drawLine(g, E, F);
        this.drawLine(g, F, G);
        this.drawLine(g, G, H);
        this.drawLine(g, H, E);
        
        // Connect
        this.drawLine(g, A, E);
        this.drawLine(g, B, F);
        this.drawLine(g, C, G);
        this.drawLine(g, D, H);
    }
    
    private void drawBorders(Graphics g) {
        
        // Calculate coordinates of the points 
        double[] center = { 0, 0, 0 };
        double[] A = { - (_worldSizeX / 2), -(_worldSizeY / 2), -(_worldSizeZ / 2)};
        double[] B = { _worldSizeX - (_worldSizeX / 2), -(_worldSizeY / 2), -(_worldSizeZ / 2)};
        double[] C = { _worldSizeX - (_worldSizeX / 2), _worldSizeY -(_worldSizeY / 2), -(_worldSizeZ / 2)};
        double[] D = { - (_worldSizeX / 2), _worldSizeY -(_worldSizeY / 2), -(_worldSizeZ / 2)};
        double[] E = { - (_worldSizeX / 2), -(_worldSizeY / 2), _worldSizeZ -(_worldSizeZ / 2)};
        double[] F = { _worldSizeX - (_worldSizeX / 2), -(_worldSizeY / 2), _worldSizeZ -(_worldSizeZ / 2)};
        double[] G = { _worldSizeX - (_worldSizeX / 2), _worldSizeY -(_worldSizeY / 2), _worldSizeZ -(_worldSizeZ / 2)};
        double[] H = { - (_worldSizeX / 2), _worldSizeY -(_worldSizeY / 2), _worldSizeZ -(_worldSizeZ / 2)};
        
        // Translate points
        center = this.translate(center);
        A = this.translate(A);
        B = this.translate(B);
        C = this.translate(C);
        D = this.translate(D);
        E = this.translate(E);
        F = this.translate(F);
        G = this.translate(G);
        H = this.translate(H);

        // Draw circle
        g.setColor(Color.blue);
        g.drawOval((int)center[0] - 3, (int)center[1] - 3, 6, 6);
        
        // Draw lines
        g.setColor(Color.red);
        this.drawLine(g, B, C);
        this.drawLine(g, F, G);
        this.drawLine(g, G, H);
        this.drawLine(g, B, F);
        this.drawLine(g, C, G);
        this.drawLine(g, E, F);
        this.drawLine(g, H, E);
        this.drawLine(g, A, E);
        this.drawLine(g, A, B);
        this.drawLine(g, D, A);
        this.drawLine(g, D, H);
        this.drawLine(g, C, D);
    }
    
    private void drawLine(Graphics g, double[] A, double[] B) {
        g.drawLine((int)A[0], (int)A[1], (int)B[0], (int)B[1]);
    }
    
    private void fillPoly(Graphics g, double[] A, double[] B, double[] C, double[] D) {
        Polygon pg = new Polygon();
        
        pg.addPoint((int)A[0], (int)A[1]);
        pg.addPoint((int)B[0], (int)B[1]);
        pg.addPoint((int)C[0], (int)C[1]);
        pg.addPoint((int)D[0], (int)D[1]);
        
        g.fillPolygon(pg);
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Matrix Math">
    /** macho transform matrix bereken algoritme
     * @author Hielke Hielkema
     */
    private void updateTransform() {
        
        // The matrix to resize
        double[][] resizeArray = {{CELL_SIZE_X * this._zoom, 0.0,                      0.0,                        0.0},
                                  {0.0,                      CELL_SIZE_Y * this._zoom, 0.0,                        0.0},
                                  {0.0,                      0.0,                       -CELL_SIZE_Z * this._zoom, 0.0},
                                  {0.0,                      0.0,                       0.0,                       1.0}};
        
        // The matrix to move
        double wDif = this.getWidth() / (this.CELL_SIZE_X * this._zoom) / 2;
        double hDif = this.getHeight() / (this.CELL_SIZE_X * this._zoom) / 2;
        double[][] moveArray = {{1.0, 0.0, 0.0, wDif},
                                {0.0, 1.0, 0.0, hDif},
                                {0.0, 0.0, 1.0, 0.0},
                                {0.0, 0.0, 0.0, 1.0}};
        
        // The matrix to rotate around the x-ax
        double[][] rotateArrayX = {{cos(this._rX), 0.0, sin(this._rX), 0.0},
                                   {0.0,           1.0, 0.0,           0.0},
                                   {sin(this._rX), 0.0, cos(this._rX), 0.0},
                                   {0.0,           0.0, 0.0,           1.0}};
        
        // The matrix to rotate around the y-ax
        double[][] rotateArrayY = {{1.1, 0.0,           0.0,            0.0},
                                   {0.0, cos(this._rY), sin(-this._rY), 0.0},
                                   {0.0, sin(this._rY), cos(this._rY),  0.0},
                                   {0.0, 0.0,           0.0,            1.0}};
        
        // Combine the matrixes to one projection matrix
        this._projectionMatrix = (new Matrix(resizeArray)).times(new Matrix(moveArray))     // Move
                                                          .times(new Matrix(rotateArrayX))  // Rotate around x-ax
                                                          .times(new Matrix(rotateArrayY))  // Rotate around y-ax
                                                          .transpose();                     // Transpose
    }
    
    /**
     * Translate the given matrix by the projection matrix
     * @param A the matrix to transform
     * @return 
     */
    private double[] translate(double[] A) {
        
        // Extend the array with 1.0, this is because we use a 4x4 matrix to translate
        double[][] array = { {A[0], A[1], A[2], 1.0} };
        
        // Apply the projection matrix and return it as a array
        return new Matrix(array).times(_projectionMatrix).getArray()[0];
    }
    
    /**
     * A shortcut to calculate sin
     * @param v the input value in degree
     * @return Math.sin(Math.toRadians(v))
     */
    private double sin(double v) {
        return Math.sin(Math.toRadians(v));
    }
    
    /**
     * A shortcut to calculate cos
     * @param v the input value in degree
     * @return Math.cos(Math.toRadians(v))
     */
    private double cos(double v) {
        return Math.cos(Math.toRadians(v));
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Mouse Handling">
    /**
     * Handle mouseDragged
     * @param e The MouseEvent
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if(e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
            this._rX = (((double)e.getX() - (this.getWidth() / 2)) / this.getWidth()) * 180;
            this._rY = (((double)e.getY() - (this.getHeight() / 2)) / this.getHeight()) * -180;
            this.updateTransform();
            this.repaint();
        }
    }
    
    /**
     * Handle mouseWheelMoved
     * @param e The MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this._zoom += (double)e.getWheelRotation() / -30;

        if(this._zoom < 0.1) {
            this._zoom = 0.1;
        }
        
        this.updateTransform();
        this.repaint();
    }
    
    
    // Not used interface implementations
    
    @Override
    public void mouseClicked(MouseEvent e) { }
    
    @Override
    public void mousePressed(MouseEvent e) { }
    
    @Override
    public void mouseReleased(MouseEvent e) { }
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
    @Override
    public void mouseMoved(MouseEvent e) { }
    
    //</editor-fold>
}
