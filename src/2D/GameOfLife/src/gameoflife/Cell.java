/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.awt.Color;

/**
 *
 * @author Hielke Hielkema
 */
public class Cell {
    
    private boolean _state;
    private boolean _newState;
    private int _age;
    
    public Cell() {
        this._state = false;
        this._age = 0;
    }
    
    public Cell(Boolean state) {
        this._state = state;
        this._newState = state;
        this._age = 0;
    }
    
    public void SubmitNewState() {
        
        if (this._state && this._newState) {
            _age++;
        }
        else {
            _age = 0;
        }
            
            
        this._state = this._newState;
    }
    
    public boolean GetState()
    {
        return this._state;
    }
    
    public void SetState(boolean state) {
        this._state = state;
        this._newState = state;
        this._age = 0;
    }
    
    public void SetNewState(boolean newState)
    {
        this._newState = newState;
    }
    
    public Color GetAgeColor() {
        
        int r = 0;
        int g = 255 - _age * 3;
        
        if (g < 50)
            g = 50;
        
        int b = 0;

        return new Color(r, g, b);

    }
}
