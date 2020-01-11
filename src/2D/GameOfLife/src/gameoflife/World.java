/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
    final int WORLD_HEIGHT = 150; // 150
    final int WORLD_WIDTH = 200; // 200
    final int TICK_INTERVAL = 10; // default value
    
    // Private fields
    private Cell[][] _cells;
    private WorldPanel _worldPanel;
    private Timer _timer;
    private JButton _startButton;
    private JButton _stopButton;
    private JButton _addButton;
    private JButton _clearButton;
    private JButton _exportButton;
    private JButton _importButton;
    private JComboBox _patternBox;
    private JComboBox _surfaceBox;
    private JCheckBox _use3dCheckbox;
    private JCheckBox _showGridCheckbox;
    private JSlider _speedSlider;
    
    public World() {
       // Initialize cells
       this._cells = new Cell[WORLD_WIDTH][WORLD_HEIGHT];
       for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
               this._cells[x][y] = new Cell(false);
           }
        }
       
       // Initialize graphical user interface
       InitializeComponent();
       
       // Initalize the refresh timer
       _timer = new Timer(TICK_INTERVAL, this);
       _timer.setInitialDelay(TICK_INTERVAL);
       _timer.start(); 
       
    }
    
    private void LoadPattern(int patternId) {
        switch(patternId)
        {
            case 0:
            {
                loadRandom();
                return;
            }
            case 1:
            {        
                int[][] array = { { 10 + 1, 5 },  { 10 + 2, 5 + 1}, { 10, 5 + 2},  { 10 + 1, 5 + 2},  { 10 + 2, 5 + 2},  };
                loadPatternFromArray(array);
                return;
            }
            case 2:
            {
                int[][] array = { {1, 3}, {1, 4}, {1, 5}, {1, 21}, {1, 22}, {1, 23}, {2, 1}, {2, 2}, {2, 24}, {2, 25}, {6, 7}, {6, 8}, {6, 9}, {6, 17}, {6, 18}, {6, 19}, {7, 6}, {7, 10}, {7, 16}, {7, 20}, {8, 3}, {8, 4}, {8, 5}, {8, 11}, {8, 15}, {8, 21}, {8, 22}, {8, 23}, {9, 1}, {9, 2}, {9, 5}, {9, 11}, {9, 15}, {9, 21}, {9, 24}, {9, 25}, {10, 4}, {10, 12}, {10, 14}, {10, 22}, {11, 4}, {11, 8}, {11, 12}, {11, 14}, {11, 18}, {11, 22}, {12, 5}, {12, 8}, {12, 11}, {12, 15}, {12, 18}, {12, 21}, {13, 5}, {13, 11}, {13, 15}, {13, 21}, {14, 6}, {14, 10}, {14, 16}, {14, 20}, {15, 7}, {15, 8}, {15, 9}, {15, 17}, {15, 18}, {15, 19}, {16, 7}, {16, 19}, {17, 7}, {17, 19}, {18, 8}, {18, 18}, {19, 8}, {19, 18}, {20, 8}, {20, 18}, {21, 8}, {21, 13}, {21, 18}, {22, 8}, {22, 13}, {22, 18}, {23, 8}, {23, 18}, {24, 8}, {24, 18}, {25, 7}, {25, 19} };
                loadPatternFromArray(array);
                return;
            }
            case 3:
            {
                int[][] array = { {53, 44}, {53, 45}, {54, 44}, {54, 45}, {63, 44}, {63, 45}, {63, 46}, {64, 43}, {64, 47}, {65, 42}, {65, 48}, {66, 42}, {66, 48}, {67, 45}, {68, 43}, {68, 47}, {69, 44}, {69, 45}, {69, 46}, {70, 45}, {73, 42}, {73, 43}, {73, 44}, {74, 42}, {74, 43}, {74, 44}, {75, 41}, {75, 45}, {77, 40}, {77, 41}, {77, 45}, {77, 46}, {87, 42}, {87, 43}, {88, 42}, {88, 43} };
                loadPatternFromArray(array);
            }
        }
    }
    
    private void loadPatternFromArray(int[][] array) {
        if (array == null)
            return;
        
        for(int i = 0; i < array.length; i++) {
            this._cells[array[i][0]][array[i][1]].SetState(true);
        }
    }
    
    private void loadRandom()
    {
        Random random = new Random();
        for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
               this._cells[x][y].SetState(random.nextBoolean());
           }
        }
    }
    
    private void clearWorld() {
        for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
               this._cells[x][y].SetState(false);
           }
        }
    }
    
    private void exportWorld()
    {
        // String to store coordinates in
        String exportString = "";
        
        // Find all cells
        for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
               if(this._cells[x][y].GetState()) {
                   exportString += String.format("{%s, %s}, ", x, y);
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
    
    @SuppressWarnings("empty-statement")
    private void importWorld()
    {
        String a =JOptionPane.showInputDialog(this,"input");  
        
        if(a.length() > 0) {
            int p = -1;           
            while(++p < a.length()) {
                char ch = a.charAt(p);
                
                if (ch == '{')
                {
                    int numOneBeginIndex = p + 1;
                    while(a.charAt(++p) != ',');
                    int numOneEndIndex = p;
                    while(a.charAt(++p) == ' ');
                    int numTwoBeginIndex = p;
                    while(a.charAt(++p) != '}');
                    int numTwoEndIndex = p;
                    
                    int numOne = Integer.parseInt(a.substring(numOneBeginIndex, numOneEndIndex));
                    int numTwo = Integer.parseInt(a.substring(numTwoBeginIndex, numTwoEndIndex));
                    
                    _cells[numOne][numTwo].SetState(true);
                }
                
            }
        }
        
        this._worldPanel.repaint();
    }
    
    private void calculateNewGeneration() {
        
        // Game of life rules
        // 1. Any live cell with fewer than two live neighbours dies, as if caused by under-population.
        // 2. Any live cell with two or three live neighbours lives on to the next generation.
        // 3. Any live cell with more than three live neighbours dies, as if by overcrowding.
        // 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
        
        // Get the surface mode
        int mode = this._surfaceBox.getSelectedIndex();
        
        // Calculate the new state
        for(int x = 0; x < WORLD_WIDTH; x++) {
           for(int y = 0; y < WORLD_HEIGHT; y++) {
               
               int cellsAround = getCellsAroundCount(x,y, mode);
               Cell currentCell = this._cells[x][y];
               
               if (currentCell.GetState())
               {
                   if (cellsAround < 2 || cellsAround > 3) {
                       currentCell.SetNewState(false); // Dies
                   }
                   else if (cellsAround == 2 || cellsAround == 3) { 
                       currentCell.SetNewState(true); // Lives
                   }
               }
               else if (cellsAround == 3) {
                       currentCell.SetNewState(true); // Lives                
               }
           }
        }
         
        // Submit the changes
        for(int x = 0; x < WORLD_WIDTH; x++) {
            for(int y = 0; y < WORLD_HEIGHT; y++) {
               this._cells[x][y].SubmitNewState();
           }
        }
    }
    
    private int getCellsAroundCount(int x, int y, int mode) {
        int count = 0;
        
        // (-1,-1) (0,-1) (1,-1)
        // (-1, 0) (0, 0) (1, 0)
        // (-1, 1) (0, 1) (1, 1)

        for(int xdiff = -1; xdiff < 2; xdiff++) {
            for(int ydiff = -1; ydiff < 2; ydiff++) {
                if ((ydiff != 0 || xdiff != 0) && getStateForCell(x + xdiff, y + ydiff, mode)) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    private boolean getStateForCell(int x, int y, int mode) {
        
        // 0 = Globe
        // 1 = Horizontal Mobius
        // 2 = Vertical mobius
        // 3 = Double mobius
        
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
                break;
            }
            case 1: // Horizontal Mobius
            {
                if (x < 0) {
                    x = this.WORLD_WIDTH - 1;
                    y = (this.WORLD_HEIGHT - 1) - y;
                }
                else if (x > this.WORLD_WIDTH - 1) {
                    x = 0;
                    y = (this.WORLD_HEIGHT - 1) - y;
                }

                if (y < 0) {
                    y = this.WORLD_HEIGHT - 1;
                }
                else if (y > this.WORLD_HEIGHT - 1) {
                    y = 0;
                }
                break;
            }
            case 2: // Vertical Mobius
            {
                if (x < 0) {
                    x = this.WORLD_WIDTH - 1;
                }
                else if (x > this.WORLD_WIDTH - 1) {
                    x = 0;
                }

                if (y < 0) {
                    y = this.WORLD_HEIGHT - 1;
                    x = (this.WORLD_WIDTH - 1) - x;
                }
                else if (y > this.WORLD_HEIGHT - 1) {
                    y = 0;
                    x = (this.WORLD_WIDTH - 1) - x;
                }
                break;
            }
            case 3: // Double Mobius
            {
                if (x < 0) {
                    x = this.WORLD_WIDTH - 1;
                    y = (this.WORLD_HEIGHT - 1) - y;
                }
                else if (x > this.WORLD_WIDTH - 1) {
                    x = 0;
                    y = (this.WORLD_HEIGHT - 1) - y;
                }

                if (y < 0) {
                    y = this.WORLD_HEIGHT - 1;
                    x = (this.WORLD_WIDTH - 1) - x;
                }
                else if (y > this.WORLD_HEIGHT - 1) {
                    y = 0;
                    x = (this.WORLD_WIDTH - 1) - x;
                }
                break;
            }
            case 4: // Dead wall
            {
                if (x < 0 || x > this.WORLD_WIDTH - 1) {
                    return false;
                }

                if (y < 0 || y > this.WORLD_HEIGHT - 1) {
                    return false;
                }
  
                break;
            }
        }
        
        return this._cells[x][y].GetState();
    }
    
    private void InitializeComponent() {
        // Set size and location
        setLocation(50, 50);
        setPreferredSize(new Dimension(1000, 800));
        setSize(new Dimension(1017, 817));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.yellow);
        
        // Add the worldpanel
        this._worldPanel = new WorldPanel(this.WORLD_WIDTH, this.WORLD_HEIGHT, this._cells);
        add(this._worldPanel, BorderLayout.CENTER);
       
        // Create a panel for the buttons, sliders and other controlling components
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(600, 28));
        controlPanel.setBackground(Color.white);
        add(controlPanel, BorderLayout.SOUTH);
        
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
        this._patternBox.addItem("moving");
        this._patternBox.addItem("Nathan fantasie");
        this._patternBox.addItem("Gun 1");
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
        this._surfaceBox.addItem("Globe");
        this._surfaceBox.addItem("Horizontal mobius");
        this._surfaceBox.addItem("Vertical Mobius");
        this._surfaceBox.addItem("Double Mobius");
        this._surfaceBox.addItem("Dead wall");
        controlPanel.add(this._surfaceBox, BorderLayout.WEST);
        
        // Add 3D effect checkbox
        this._use3dCheckbox = new JCheckBox();
        this._use3dCheckbox.setText("3D effect");
        controlPanel.add(this._use3dCheckbox, BorderLayout.WEST);
        
         // Add show grid checkbox
        this._showGridCheckbox = new JCheckBox();
        this._showGridCheckbox.setText("Grid");
        controlPanel.add(this._showGridCheckbox, BorderLayout.WEST);
        
        // Add Speed Slider
        this._speedSlider = new JSlider();
        this._speedSlider.setMinimum(0);
        this._speedSlider.setMaximum(999);
        this._speedSlider.setPreferredSize(new Dimension(100, 20));
        this._speedSlider.setValue(999);
        this._speedSlider.addChangeListener(this);
        controlPanel.add(this._speedSlider, BorderLayout.WEST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this._timer) {
            calculateNewGeneration();
            this._worldPanel.SetUse3DEffect(this._use3dCheckbox.isSelected());
            this._worldPanel.SetShowGrid(this._showGridCheckbox.isSelected());
            this._worldPanel.repaint();
        }
        else if (e.getSource() == this._startButton) {
            this._timer.start();
        }
        else if (e.getSource() == this._stopButton) {
            this._timer.stop();
        }
        else if (e.getSource() == this._addButton) {
            this.LoadPattern(this._patternBox.getSelectedIndex());
            this._worldPanel.SetUse3DEffect(this._use3dCheckbox.isSelected());
            this._worldPanel.SetShowGrid(this._showGridCheckbox.isSelected());
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

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == this._speedSlider) {
            int sliderValue = this._speedSlider.getValue();
            int delay = 1000 - sliderValue;
            
            this._timer.stop();
            this._timer.setDelay(delay);
            this._timer.start();
        }
    }

    
}

