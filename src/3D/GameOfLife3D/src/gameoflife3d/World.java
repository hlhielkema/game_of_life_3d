/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 * @author Hielke Hielkema
 */
public class World extends JFrame implements ActionListener, ChangeListener {
    
    // Constants
    final int WORLD_HEIGHT = 30; 
    final int WORLD_WIDTH = 30; 
    final int WORLD_DEPTH = 30;
    final int TICK_INTERVAL = 1000; // begin value
    
    // Private fields
    private Cell[][][] _cells;
    private WorldPanel _worldPanel;
    private Timer _timer;
    private Random _random;
    
    // Private components
    private JButton _startButton;
    private JButton _stopButton;
    private JButton _addButton;
    private JButton _clearButton;
    private JButton _exportButton;
    private JButton _importButton;
    private JComboBox _patternBox;
    private JComboBox _surfaceBox;
    private JSlider _speedSlider;
    //private JTextBox _
    
    /**
     *  Constructor
     */
    public World() {
       
       // Enable the fullscreenmode on osx
       this.enableOSXFullscreen(this);
        
       // Initialize cells
       this._cells = new Cell[WORLD_WIDTH][WORLD_HEIGHT][WORLD_DEPTH];
       for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
                for (int z = 0; z < WORLD_DEPTH; z++) {
                    this._cells[x][y][z] = new Cell();
                }
           }
        }
       
       // Initialize random
       this._random = new Random();
       
       // Initialize graphical user interface
       InitializeComponent();
       
       // Initalize the refresh timer
       _timer = new Timer(TICK_INTERVAL, this);
       _timer.setInitialDelay(TICK_INTERVAL);
       
       // Load some random cells
       this.loadPattern(0);
    }
    
    /** Enable fullscreen on osx
    * @param window
    */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void enableOSXFullscreen(Window window) {

        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class params[] = new Class[]{Window.class, Boolean.TYPE};
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (ClassNotFoundException e1) {
        } catch (Exception e) {

        }
    }
    
    /**
     * Load a pattern from a given id
     * @param id the id of the pattern
     */
    private void loadPattern(int id) 
    {
        /* Template:
            case ID: // NAME
            {
                int[][] array = { PATTERN };
                this.loadPatternFromArray(array);
                break;
            }
         */
        
        switch(id)
        {
            case 0: // Random
            {
                loadRandom();
                break;
            }
            case 1: // Names
            {
                int[][] array = { {1, 2, 0}, {1, 3, 0}, {1, 4, 0}, {1, 5, 0}, {1, 6, 0}, {1, 7, 0}, {1, 8, 0}, {1, 9, 0}, {1, 10, 0}, {1, 11, 0}, {1, 14, 0}, {1, 15, 0}, {1, 16, 0}, {1, 17, 0}, {1, 18, 0}, {1, 19, 0}, {1, 20, 0}, {1, 21, 0}, {1, 22, 0}, {1, 23, 0}, {2, 2, 0}, {2, 3, 0}, {2, 4, 0}, {2, 5, 0}, {2, 6, 0}, {2, 7, 0}, {2, 8, 0}, {2, 9, 0}, {2, 10, 0}, {2, 11, 0}, {2, 14, 0}, {2, 15, 0}, {2, 16, 0}, {2, 17, 0}, {2, 18, 0}, {2, 19, 0}, {2, 20, 0}, {2, 21, 0}, {2, 22, 0}, {2, 23, 0}, {3, 6, 0}, {3, 7, 0}, {3, 14, 0}, {3, 19, 0}, {3, 20, 0}, {4, 6, 0}, {4, 7, 0}, {4, 14, 0}, {4, 18, 0}, {4, 20, 0}, {4, 21, 0}, {5, 6, 0}, {5, 7, 0}, {5, 14, 0}, {5, 18, 0}, {5, 21, 0}, {5, 22, 0}, {6, 6, 0}, {6, 7, 0}, {6, 15, 0}, {6, 16, 0}, {6, 17, 0}, {6, 22, 0}, {6, 23, 0}, {7, 2, 0}, {7, 3, 0}, {7, 4, 0}, {7, 5, 0}, {7, 6, 0}, {7, 7, 0}, {7, 8, 0}, {7, 9, 0}, {7, 10, 0}, {7, 11, 0}, {8, 2, 0}, {8, 3, 0}, {8, 4, 0}, {8, 5, 0}, {8, 6, 0}, {8, 7, 0}, {8, 8, 0}, {8, 9, 0}, {8, 10, 0}, {8, 11, 0}, {8, 16, 0}, {8, 17, 0}, {8, 18, 0}, {8, 19, 0}, {8, 20, 0}, {8, 21, 0}, {9, 15, 0}, {9, 16, 0}, {9, 21, 0}, {9, 22, 0}, {10, 2, 0}, {10, 3, 0}, {10, 5, 0}, {10, 6, 0}, {10, 7, 0}, {10, 8, 0}, {10, 9, 0}, {10, 10, 0}, {10, 11, 0}, {10, 14, 0}, {10, 15, 0}, {10, 22, 0}, {10, 23, 0}, {11, 2, 0}, {11, 3, 0}, {11, 5, 0}, {11, 6, 0}, {11, 7, 0}, {11, 8, 0}, {11, 9, 0}, {11, 10, 0}, {11, 11, 0}, {11, 15, 0}, {11, 16, 0}, {11, 21, 0}, {11, 22, 0}, {12, 16, 0}, {12, 17, 0}, {12, 18, 0}, {12, 19, 0}, {12, 20, 0}, {12, 21, 0}, {13, 2, 0}, {13, 3, 0}, {13, 4, 0}, {13, 5, 0}, {13, 6, 0}, {13, 7, 0}, {13, 8, 0}, {13, 9, 0}, {13, 10, 0}, {13, 11, 0}, {13, 27, 0}, {13, 28, 0}, {14, 2, 0}, {14, 3, 0}, {14, 4, 0}, {14, 5, 0}, {14, 6, 0}, {14, 7, 0}, {14, 8, 0}, {14, 9, 0}, {14, 10, 0}, {14, 11, 0}, {14, 14, 0}, {14, 15, 0}, {14, 16, 0}, {14, 17, 0}, {14, 18, 0}, {14, 19, 0}, {14, 20, 0}, {14, 21, 0}, {14, 22, 0}, {14, 23, 0}, {14, 27, 0}, {14, 28, 0}, {15, 2, 0}, {15, 3, 0}, {15, 6, 0}, {15, 7, 0}, {15, 10, 0}, {15, 11, 0}, {15, 14, 0}, {15, 15, 0}, {15, 18, 0}, {15, 19, 0}, {15, 22, 0}, {15, 23, 0}, {15, 31, 0}, {15, 32, 0}, {16, 2, 0}, {16, 3, 0}, {16, 6, 0}, {16, 7, 0}, {16, 10, 0}, {16, 11, 0}, {16, 14, 0}, {16, 15, 0}, {16, 18, 0}, {16, 19, 0}, {16, 22, 0}, {16, 23, 0}, {16, 32, 0}, {16, 33, 0}, {17, 2, 0}, {17, 3, 0}, {17, 6, 0}, {17, 7, 0}, {17, 10, 0}, {17, 11, 0}, {17, 14, 0}, {17, 15, 0}, {17, 18, 0}, {17, 19, 0}, {17, 22, 0}, {17, 23, 0}, {17, 33, 0}, {18, 2, 0}, {18, 3, 0}, {18, 10, 0}, {18, 11, 0}, {18, 15, 0}, {18, 16, 0}, {18, 17, 0}, {18, 20, 0}, {18, 21, 0}, {18, 22, 0}, {18, 33, 0}, {19, 33, 0}, {20, 2, 0}, {20, 3, 0}, {20, 4, 0}, {20, 5, 0}, {20, 6, 0}, {20, 7, 0}, {20, 8, 0}, {20, 9, 0}, {20, 10, 0}, {20, 11, 0}, {20, 14, 0}, {20, 15, 0}, {20, 16, 0}, {20, 17, 0}, {20, 18, 0}, {20, 19, 0}, {20, 20, 0}, {20, 21, 0}, {20, 22, 0}, {20, 23, 0}, {20, 32, 0}, {20, 33, 0}, {21, 2, 0}, {21, 3, 0}, {21, 4, 0}, {21, 5, 0}, {21, 6, 0}, {21, 7, 0}, {21, 8, 0}, {21, 9, 0}, {21, 10, 0}, {21, 11, 0}, {21, 14, 0}, {21, 15, 0}, {21, 16, 0}, {21, 17, 0}, {21, 18, 0}, {21, 19, 0}, {21, 20, 0}, {21, 21, 0}, {21, 22, 0}, {21, 23, 0}, {21, 31, 0}, {21, 32, 0}, {22, 10, 0}, {22, 11, 0}, {22, 14, 0}, {22, 15, 0}, {22, 18, 0}, {22, 19, 0}, {22, 22, 0}, {22, 23, 0}, {22, 27, 0}, {22, 28, 0}, {23, 10, 0}, {23, 11, 0}, {23, 14, 0}, {23, 15, 0}, {23, 18, 0}, {23, 19, 0}, {23, 22, 0}, {23, 23, 0}, {23, 27, 0}, {23, 28, 0}, {24, 10, 0}, {24, 11, 0}, {24, 14, 0}, {24, 15, 0}, {24, 18, 0}, {24, 19, 0}, {24, 22, 0}, {24, 23, 0}, {25, 10, 0}, {25, 11, 0}, {25, 14, 0}, {25, 15, 0}, {25, 22, 0}, {25, 23, 0}, {27, 2, 0}, {27, 3, 0}, {27, 4, 0}, {27, 5, 0}, {27, 6, 0}, {27, 7, 0}, {27, 8, 0}, {27, 9, 0}, {27, 10, 0}, {27, 11, 0}, {27, 14, 0}, {27, 15, 0}, {27, 16, 0}, {27, 17, 0}, {27, 18, 0}, {27, 19, 0}, {27, 20, 0}, {27, 21, 0}, {27, 22, 0}, {27, 23, 0}, {28, 2, 0}, {28, 3, 0}, {28, 4, 0}, {28, 5, 0}, {28, 6, 0}, {28, 7, 0}, {28, 8, 0}, {28, 9, 0}, {28, 10, 0}, {28, 11, 0}, {28, 14, 0}, {28, 19, 0}, {28, 20, 0}, {29, 5, 0}, {29, 6, 0}, {29, 8, 0}, {29, 9, 0}, {29, 14, 0}, {29, 18, 0}, {29, 20, 0}, {29, 21, 0}, {30, 4, 0}, {30, 5, 0}, {30, 9, 0}, {30, 10, 0}, {30, 14, 0}, {30, 18, 0}, {30, 21, 0}, {30, 22, 0}, {31, 2, 0}, {31, 3, 0}, {31, 4, 0}, {31, 10, 0}, {31, 11, 0}, {31, 15, 0}, {31, 16, 0}, {31, 17, 0}, {31, 22, 0}, {31, 23, 0}, {33, 2, 0}, {33, 3, 0}, {33, 4, 0}, {33, 5, 0}, {33, 6, 0}, {33, 7, 0}, {33, 8, 0}, {33, 9, 0}, {33, 10, 0}, {33, 11, 0}, {33, 14, 0}, {33, 15, 0}, {34, 2, 0}, {34, 3, 0}, {34, 4, 0}, {34, 5, 0}, {34, 6, 0}, {34, 7, 0}, {34, 8, 0}, {34, 9, 0}, {34, 10, 0}, {34, 11, 0}, {34, 14, 0}, {34, 15, 0}, {35, 2, 0}, {35, 3, 0}, {35, 6, 0}, {35, 7, 0}, {35, 10, 0}, {35, 11, 0}, {35, 14, 0}, {35, 15, 0}, {35, 16, 0}, {35, 17, 0}, {35, 18, 0}, {35, 19, 0}, {35, 20, 0}, {35, 21, 0}, {35, 22, 0}, {35, 23, 0}, {36, 2, 0}, {36, 3, 0}, {36, 6, 0}, {36, 7, 0}, {36, 10, 0}, {36, 11, 0}, {36, 14, 0}, {36, 15, 0}, {36, 16, 0}, {36, 17, 0}, {36, 18, 0}, {36, 19, 0}, {36, 20, 0}, {36, 21, 0}, {36, 22, 0}, {36, 23, 0}, {37, 2, 0}, {37, 3, 0}, {37, 6, 0}, {37, 7, 0}, {37, 10, 0}, {37, 11, 0}, {37, 14, 0}, {37, 15, 0}, {38, 2, 0}, {38, 3, 0}, {38, 10, 0}, {38, 11, 0}, {38, 14, 0}, {38, 15, 0} };
                this.loadPatternFromArray(array);
                break;
            }
            case 2: // Cross
            {
                int[][] array = { {0, 0, 0}, {0, 39, 0}, {1, 1, 0}, {1, 38, 0}, {2, 2, 0}, {2, 37, 0}, {3, 3, 0}, {3, 36, 0}, {4, 4, 0}, {4, 35, 0}, {5, 5, 0}, {5, 34, 0}, {6, 6, 0}, {6, 33, 0}, {7, 7, 0}, {7, 32, 0}, {8, 8, 0}, {8, 31, 0}, {9, 9, 0}, {9, 30, 0}, {10, 10, 0}, {10, 29, 0}, {11, 11, 0}, {11, 28, 0}, {12, 12, 0}, {12, 27, 0}, {13, 13, 0}, {13, 26, 0}, {14, 14, 0}, {14, 25, 0}, {15, 15, 0}, {15, 24, 0}, {16, 16, 0}, {16, 23, 0}, {17, 17, 0}, {17, 22, 0}, {18, 18, 0}, {18, 21, 0}, {19, 19, 0}, {19, 20, 0}, {20, 19, 0}, {20, 20, 0}, {21, 18, 0}, {21, 21, 0}, {22, 17, 0}, {22, 22, 0}, {23, 16, 0}, {23, 23, 0}, {24, 15, 0}, {24, 24, 0}, {25, 14, 0}, {25, 25, 0}, {26, 13, 0}, {26, 26, 0}, {27, 12, 0}, {27, 27, 0}, {28, 11, 0}, {28, 28, 0}, {29, 10, 0}, {29, 29, 0}, {30, 9, 0}, {30, 30, 0}, {31, 8, 0}, {31, 31, 0}, {32, 7, 0}, {32, 32, 0}, {33, 6, 0}, {33, 33, 0}, {34, 5, 0}, {34, 34, 0}, {35, 4, 0}, {35, 35, 0}, {36, 3, 0}, {36, 36, 0}, {37, 2, 0}, {37, 37, 0}, {38, 1, 0}, {38, 38, 0}, {39, 0, 0}, {39, 39, 0} };
                this.loadPatternFromArray(array);
                break;
            }
            case 3: // Smile
            {
                int[][] array = { {8, 6, 0}, {8, 7, 0}, {8, 8, 0}, {8, 9, 0}, {9, 6, 0}, {9, 7, 0}, {9, 8, 0}, {9, 9, 0}, {9, 18, 0}, {10, 6, 0}, {10, 7, 0}, {10, 8, 0}, {10, 9, 0}, {10, 18, 0}, {10, 19, 0}, {11, 6, 0}, {11, 7, 0}, {11, 8, 0}, {11, 9, 0}, {11, 19, 0}, {11, 20, 0}, {12, 20, 0}, {12, 21, 0}, {13, 21, 0}, {13, 22, 0}, {14, 22, 0}, {14, 23, 0}, {15, 23, 0}, {15, 24, 0}, {16, 24, 0}, {17, 24, 0}, {18, 14, 0}, {18, 15, 0}, {18, 16, 0}, {18, 24, 0}, {19, 14, 0}, {19, 15, 0}, {19, 16, 0}, {19, 24, 0}, {20, 14, 0}, {20, 15, 0}, {20, 16, 0}, {20, 24, 0}, {21, 24, 0}, {22, 24, 0}, {23, 23, 0}, {23, 24, 0}, {24, 22, 0}, {24, 23, 0}, {25, 21, 0}, {25, 22, 0}, {26, 20, 0}, {26, 21, 0}, {27, 6, 0}, {27, 7, 0}, {27, 8, 0}, {27, 9, 0}, {27, 19, 0}, {27, 20, 0}, {28, 6, 0}, {28, 7, 0}, {28, 8, 0}, {28, 9, 0}, {28, 18, 0}, {28, 19, 0}, {29, 6, 0}, {29, 7, 0}, {29, 8, 0}, {29, 9, 0}, {29, 18, 0}, {30, 6, 0}, {30, 7, 0}, {30, 8, 0}, {30, 9, 0} };
                this.loadPatternFromArray(array);
                break;
            }
        }
    }
    
    /**
     * Load a pattern from an array
     * @param array the array to load the pattern from
     */
    private void loadPatternFromArray(int[][] array) {
        if (array == null) {
            return;
        }
        
        for(int i = 0; i < array.length; i++) {
            int x = array[i][0];
            int y = array[i][1];
            int z = array[i][2];
            
            boolean xValid = x >= 0 && x < this.WORLD_WIDTH;
            boolean yValid = y >= 0 && y < this.WORLD_HEIGHT;
            boolean zValid = z >= 0 && z < this.WORLD_DEPTH;
            
            if (xValid && yValid && zValid) {
                this._cells[x][y][z].SetState(true);
            }
        }
    }
    
    /** 
     *  Load random cells
     */
    private void loadRandom() {
        Random random = new Random();
        for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
                for (int z = 0; z < WORLD_DEPTH; z++) {
                    this._cells[x][y][z].SetState(random.nextInt() % 2 == 0);
                }
           }
        }
    }
    
    /**
     * Remove all cells
     */
    private void clearWorld() {
        for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
                for (int z = 0; z < WORLD_DEPTH; z++) {
                    this._cells[x][y][z].SetState(false);
                }
           }
        }
    }
    
    /**
     * Export all cells to the clipboard
     */
    private void exportWorld() {
        // String to store coordinates in
        String exportString = "";
        
        // Find all cells
        for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
                 for (int z = 0; z < WORLD_DEPTH; z++) {
                    if(this._cells[x][y][z].GetState()) {
                        exportString += String.format("{%s, %s, %s}, ", x, y, z);
                    }
                 }
           }
        }
        
        // Fix the ending and check  if there are items in
        if (exportString.equals("")) {
            JOptionPane.showMessageDialog(this, "no cells found.");
            return; // NO CELLS
        }
        else {
            exportString = exportString.substring(0, exportString.length() - 2);
        }

        // Place on clipboard
        Toolkit toolkit = Toolkit.getDefaultToolkit();
	Clipboard clipboard = toolkit.getSystemClipboard();
	StringSelection strSel = new StringSelection(exportString);
	clipboard.setContents(strSel, null);
        
        // Show message
        JOptionPane.showMessageDialog(this, "the cell coordinates are placed on the clipboard.");
    }
    
    /**
     * Load cells from a string
     */
    @SuppressWarnings("empty-statement")
    private void importWorld() {
        String a =JOptionPane.showInputDialog(this,"input");  
        
        try
        {
            if(a.length() > 0) {
                int p = -1;           
                while(++p < a.length()) {
                    char ch = a.charAt(p);

                    if (ch == '{')
                    {
                        // Get the start and end of the coordinates block
                        int bBeginIndex = p + 1;
                        int bEndIndex = bBeginIndex;
                        while(a.charAt(++bEndIndex) != '}') { }

                        // Cut the block out of the string
                        String block = a.substring(bBeginIndex, bEndIndex);

                        // Split the block in parts
                        String[] parts = block.split(",");

                        int x = Integer.parseInt(parts[0].trim());
                        int y = Integer.parseInt(parts[1].trim());
                        int z = 39;
                        
                        if (parts.length > 2) {
                            z = Integer.parseInt(parts[2].trim());
                        }
                        
                        _cells[x][y][0].SetState(true);
                    }

                }
            }
        }
        catch (Exception ex) {}
        
        this._worldPanel.repaint();
    }
    
    private void find3dConfig() {
        
        //          a  b  c 
        // default: 2, 3, 3
        
        // world size = 30x30x30 = 27.000
        
        final int maxA = 4;
        final int maxB = 10;
        
        final int maxGen = 500;
        
        
        for(int a = 3; a <= maxA; a++) {
           for(int b = a; b <= maxB; b++) {
               for(int c = a; c <= b; c++) {
                   this.loadRandom();
                   int g;
                   int alive = 0;
                   for(g = 0; g < maxGen; g++) {
                       alive = this.calculateNewGeneration(a, b, c);
                       
                       if (alive == 0) {
                           break;
                       }
                   }
                   
                   String result = "gen A=" + a + ", B=" + b + ", C=" + c + " ";
                   
                   if(alive == 0) {
                       result += "died after " + g + " generations";
                   }
                   else {
                       result += "is still alive after " + maxGen + "generations with " + alive + " cells (" + (((double)alive / 27000.0) * 100.0) + "% of world)";
                   }
                   
                   result += '\n';
                   
                   System.out.print(result);
                   
               }
            }
        }
        
        
    }
    
    /**
     * Calculate the next generation of cells
     */
    private void calculateNewGeneration()
    {
        // default for 2d: 2, 3, 3
        this.calculateNewGeneration(3, 7, 6);
    }
    
    /**
     * Calculate the next generation of cells with custom rules
     */
    private int calculateNewGeneration(int minAround, int maxAround, int bornAround) {
        
        // Game of life rules
        // 1. Any live cell with fewer than two live neighbours dies, as if caused by under-population.
        // 2. Any live cell with two or three live neighbours lives on to the next generation.
        // 3. Any live cell with more than three live neighbours dies, as if by overcrowding.
        // 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
        
        // Get the surface mode
        int mode = this._surfaceBox.getSelectedIndex();
        
        // Count amount of cells alive
        int alive = 0;
        
        // Loop through all cells
        for(int x = 0; x < WORLD_WIDTH; x++) {
           for(int y = 0; y < WORLD_HEIGHT; y++) {
               for (int z = 0; z < WORLD_DEPTH; z++) {
                   
                   // Count the amount of cells around
                   int cellsAround = 0;
                    for(int xdiff = -1; xdiff < 2; xdiff++) {
                        for(int ydiff = -1; ydiff < 2; ydiff++) {
                            for(int zdiff = -1; zdiff < 2; zdiff++) {
                                if ((ydiff != 0 || xdiff != 0 || zdiff != 0) && getStateForCell(x + xdiff, y + ydiff, z + zdiff, mode)) {
                                    cellsAround++;
                                }
                            }
                        }
                    }
                    
                    // default: 2, 3, 3
                    
                    // Apply the rules on the cell
                    Cell currentCell = this._cells[x][y][z];
                    if (currentCell.GetState())
                    {
                        if (cellsAround < minAround || cellsAround > maxAround) {
                            currentCell.SetNewState(false); // Dies
                        }
                        else {
                            currentCell.SetNewState(true); // Lives
                            alive++;
                        }
                    }
                    else if (cellsAround == bornAround || cellsAround == 7) {
                            currentCell.SetNewState(true); // Lives      
                            alive++;
                    }
               }
           }
        }
         
        // Submit the changes
        for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
                for (int z = 0; z < WORLD_DEPTH; z++) {
                    this._cells[x][y][z].SubmitNewState();
                }
           }
        }
        
        return alive;
    }
    
    /**
     * Get if a cell is active or not from coordinates.
     * Coordinates outside the world will be automaticly redirected to the correct one.
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @param z the z coordinate of the cell
     * @param mode the surface mode
     * @return 
     */
    private boolean getStateForCell(int x, int y, int z, int mode) {
        
        // 0 = Globe
        // 1 = Dead wall
        
        switch(mode)
        {
            case 0: // Globe
            {
                if (x < 0) {
                    x = this.WORLD_WIDTH - 1;
                }
                else if (x > this.WORLD_WIDTH - 1) {
                        x = 0;
                }

                if (y < 0) {
                    y = this.WORLD_HEIGHT - 1;
                }
                else if (y > this.WORLD_HEIGHT - 1) {
                    y = 0;
                }
                
                if (z < 0) {
                    z = this.WORLD_DEPTH - 1;
                }
                else if (z > this.WORLD_DEPTH - 1) {
                    z = 0;
                }
                 
                break;
            }
            case 1: // Dead wall
            {
                if (x < 0 || x > this.WORLD_WIDTH - 1 || y < 0 || y > this.WORLD_HEIGHT - 1 || z < 0 || z > this.WORLD_DEPTH - 1) {
                    return false;
                }
                break;
            }
            case 2: // Triple mobius
            {
                if (x < 0) {
                    x = this.WORLD_WIDTH - 1;
                    y = (this.WORLD_HEIGHT - 1) - y;
                    z = (this.WORLD_DEPTH - 1) - z;
                }
                else if (x > this.WORLD_WIDTH - 1) {
                    x = 0;
                    y = (this.WORLD_HEIGHT - 1) - y;
                    z = (this.WORLD_DEPTH - 1) - z;
                }
                
                 if (y < 0) {
                    x = (this.WORLD_WIDTH - 1) - x;
                    y = this.WORLD_HEIGHT - 1;
                    z = (this.WORLD_DEPTH - 1) - z;
                }
                else if (y > this.WORLD_HEIGHT - 1) {
                    x = (this.WORLD_WIDTH - 1) - x;
                    y = 0;
                    z = (this.WORLD_DEPTH - 1) - z;
                }
                 
                if (z < 0) {
                    x = (this.WORLD_WIDTH - 1) - x;
                    y = (this.WORLD_HEIGHT - 1) - y;
                    z = this.WORLD_DEPTH - 1;
                }
                else if (z > this.WORLD_DEPTH - 1) {
                    x = (this.WORLD_WIDTH - 1) - x;
                    y = (this.WORLD_HEIGHT - 1) - y;
                    z = 0;
                }
                
                break;
            }
            case 3: // Random wall (1%)
            {
                if (x < 0 || x > this.WORLD_WIDTH - 1 || y < 0 || y > this.WORLD_HEIGHT - 1 || z < 0 || z > this.WORLD_DEPTH - 1) {
                    return this._random.nextInt() % 100 == 0;
                }
                
                break;
            }
                
         
                
             
                
        }
        
        return this._cells[x][y][z].GetState();
    }
    
    /**
     * Initilize the components
     */
    private void InitializeComponent() {
        
        // Set size and location
        this.setTitle("Game of Life - (C) Hielke.");
        this.setLocation(50, 50);
        this.setPreferredSize(new Dimension(1000, 800));
        this.setSize(new Dimension(1017, 817));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.black);
        
        // Add the worldpanel
        this._worldPanel = new WorldPanel(this.WORLD_WIDTH, this.WORLD_HEIGHT, this.WORLD_DEPTH, this._cells);
        this.add(this._worldPanel, BorderLayout.CENTER);
       
        // Create a panel for the buttons, sliders and other controlling components
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(600, 32));
        controlPanel.setBackground(Color.white);
        this.add(controlPanel, BorderLayout.SOUTH);
        
        // Add start button
        this._startButton = new JButton();
        this._startButton.setPreferredSize(new Dimension(80, 20));
        this._startButton.setText("Start");
        this._startButton.addActionListener(this);
        controlPanel.add(this._startButton, BorderLayout.WEST);

        // Add stop button
        this._stopButton = new JButton();
        this._stopButton.setPreferredSize(new Dimension(80, 20));
        this._stopButton.setText("Stop");
        this._stopButton.addActionListener(this);
        controlPanel.add(this._stopButton, BorderLayout.WEST);
        
        // Add pattern combobox
        this._patternBox = new JComboBox();
        this._patternBox.setPreferredSize(new Dimension(100, 20));
        this._patternBox.addItem("Random");
        this._patternBox.addItem("Names");
        this._patternBox.addItem("Scross");
        this._patternBox.addItem("Smile :)");
        this._patternBox.setToolTipText("Pattern to add");
        controlPanel.add(_patternBox, BorderLayout.WEST);
        
        // Add addpatern button
        this._addButton = new JButton();
        this._addButton.setPreferredSize(new Dimension(80, 20));
        this._addButton.setText("Add");
        this._addButton.addActionListener(this);
        controlPanel.add(this._addButton, BorderLayout.WEST);
        
        // Add clear button
        this._clearButton = new JButton();
        this._clearButton.setPreferredSize(new Dimension(80, 20));
        this._clearButton.setText("Clear");
        this._clearButton.addActionListener(this);
        controlPanel.add(this._clearButton, BorderLayout.WEST);
        
        // Add export button
        this._exportButton = new JButton();
        this._exportButton.setPreferredSize(new Dimension(80, 20));
        this._exportButton.setText("Export");
        this._exportButton.addActionListener(this);
        controlPanel.add(this._exportButton, BorderLayout.WEST);
        
        // Add import button
        this._importButton = new JButton();
        this._importButton.setPreferredSize(new Dimension(80, 20));
        this._importButton.setText("Import");
        this._importButton.addActionListener(this);
        controlPanel.add(this._importButton, BorderLayout.WEST);
        
        // Add Surface combobox
        this._surfaceBox = new JComboBox();
        this._surfaceBox.setPreferredSize(new Dimension(100, 20));
        this._surfaceBox.addItem("Torus");
        this._surfaceBox.addItem("Dead wall");
        this._surfaceBox.addItem("Triple mobius");
        this._surfaceBox.addItem("Random wall");
        this._surfaceBox.setSelectedIndex(1);
        this._surfaceBox.setToolTipText("Set surface type");
        controlPanel.add(this._surfaceBox, BorderLayout.WEST);
        
        // Add Speed Slider
        this._speedSlider = new JSlider();
        this._speedSlider.setMinimum(0);
        this._speedSlider.setMaximum(999);
        this._speedSlider.setPreferredSize(new Dimension(100, 20));
        this._speedSlider.setValue(0);
        this._speedSlider.addChangeListener(this);
        controlPanel.add(this._speedSlider, BorderLayout.WEST);
    }

    /**
     * Handle actions
     * @param e the ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this._timer) {
            calculateNewGeneration();
            this._worldPanel.repaint();
        }
        else if (e.getSource() == this._startButton) {
            //find3dConfig();
            this._timer.start();
        }
        else if (e.getSource() == this._stopButton) {
            this._timer.stop();
        }
        else if (e.getSource() == this._addButton) {
            this.loadPattern(this._patternBox.getSelectedIndex());
            this._worldPanel.repaint();
        }
        else if (e.getSource() == this._clearButton) {
            clearWorld();
            this._worldPanel.repaint();
        }
        else if (e.getSource() == this._exportButton) {
            exportWorld();
        }
        else if (e.getSource() == this._importButton) {
            importWorld();
        }
    }

    /**
     * Handle state changes
     * @param e the ChangeEvent
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == this._speedSlider) {
            int sliderValue = this._speedSlider.getValue();
            int delay = 1000 - sliderValue;

            if (this._timer.isRunning()) {
                this._timer.stop();
                this._timer.setDelay(delay);
                this._timer.start();
            }
            else {
                this._timer.setDelay(delay);
            }
        }
    }
    

}

