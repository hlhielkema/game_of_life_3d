/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * @author Hielke Hielkema
 */
public class WorldPanel extends JPanel implements MouseListener, MouseMotionListener {
    
    // Constants
    final int CELL_HEIGHT = 5;
    final int CELL_WIDTH = 5;
    
    // Private fields
    private int _worldWidth;
    private int _worldHeight;
    private Cell[][] _cells;
    private boolean _use3dEffect = true;
    private boolean _showGrid = true;
    
    public WorldPanel(int worldWidth, int worldHeight, Cell[][] cells) {
        // Copy the parameters
        this._worldWidth = worldWidth;
        this._worldHeight = worldHeight;
        this._cells = cells;
        
        // Set the size
        setSize(this._worldWidth * this.CELL_WIDTH, this._worldHeight * this.CELL_HEIGHT);
        setPreferredSize(new Dimension(this._worldWidth * this.CELL_WIDTH, this._worldHeight * this.CELL_HEIGHT));
        
        // Set the background color
        setBackground(Color.black); 
        
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
    }
    
    public void SetUse3DEffect(boolean value) {
        this._use3dEffect = value; 
    }
    
    public void SetShowGrid(boolean value) {
        this._showGrid = value;
    }
    
    @Override
    public void paintComponent(Graphics g) {
       super.paintComponent(g);

       // Draw grid
       if(_showGrid)
       {
            for(int x = 0; x < this._worldWidth; x++) {
                g.drawLine(x * this.CELL_WIDTH, 0, x * this.CELL_WIDTH, this._worldHeight * this.CELL_HEIGHT);
            }
            
            for(int y = 0; y < this._worldHeight; y++) {
                g.drawLine(0, y * this.CELL_HEIGHT, this._worldWidth * this.CELL_WIDTH, y * this.CELL_HEIGHT);
            }
       }
       
       // Draw cells
       g.setColor(Color.green);
       for(int x = 0; x < this._worldWidth; x++) {
           for(int y = 0; y < this._worldHeight; y++) {
              if (_cells[x][y].GetState()) {
                  
                  g.setColor(_cells[x][y].GetAgeColor());
                  if (_use3dEffect)
                  {
                    g.fillRect(x * this.CELL_WIDTH + 1, y * this.CELL_HEIGHT + 1, CELL_WIDTH, CELL_HEIGHT);
                    g.fillRect(x * this.CELL_WIDTH + 2, y * this.CELL_HEIGHT + 2, CELL_WIDTH, CELL_HEIGHT);
                    g.fill3DRect(x * this.CELL_WIDTH, y * this.CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, true);
                  }
                  else
                  {
                      g.fillRect(x * this.CELL_WIDTH, y * this.CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
                  }
              }
          }
       }
       
       // Draw a red border
       g.setColor(Color.green);
       g.drawRect(0, 0, this._worldWidth * this.CELL_WIDTH, this._worldHeight * this.CELL_HEIGHT);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
        // Get the x and y
        int x = e.getX();
        int y = e.getY();
        
        // Check if the click is onside the field of cells
        if(x > 0 && x < this._worldWidth * this.CELL_WIDTH && y > 0 && y < this._worldHeight * this.CELL_HEIGHT) {
            
            int cellX = x / this.CELL_WIDTH;
            int cellY = y / this.CELL_HEIGHT;
            
            if (e.getButton() == MouseEvent.BUTTON1) {
                _cells[cellX][cellY].SetState(true);
            }
            else if (e.getButton() == MouseEvent.BUTTON3) {
                _cells[cellX][cellY].SetState(false);
            }
            
            this.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
        // Get the x and y
        int x = e.getX();
        int y = e.getY();
        
        // Check if the click is onside the field of cells
        if(x > 0 && x < this._worldWidth * this.CELL_WIDTH && y > 0 && y < this._worldHeight * this.CELL_HEIGHT) {
            
            int cellX = x / this.CELL_WIDTH;
            int cellY = y / this.CELL_HEIGHT;
            
            if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
                _cells[cellX][cellY].SetState(true);
            }
            else if (e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK) {
                _cells[cellX][cellY].SetState(false);
            }
            
            this.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
}
