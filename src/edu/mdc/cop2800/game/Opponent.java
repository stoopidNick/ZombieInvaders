/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mdc.cop2800.game;

import java.util.ArrayList;
import javafx.geometry.Rectangle2D;

/**
 *
 * @author student
 */
public class Opponent extends Sprite {
    
    private final int WIDTH = 4;
    private final int HEIGHT = 4;
    
    public Opponent(String imageFile, boolean isVisible, int[][] viewportsCoords) {
        super(imageFile, isVisible, viewportsCoords);
        dx = -1;
        dy = 0;
    }
    
    @Override
    public void move(){
        
        super.move();
        if(imageView.getTranslateX() < 0){
            imageView.setTranslateX(Gamev3.WIDTH);
        }
        
         imageView.setViewport(viewports.get(frameNumber));
        frameNumber =  (++frameNumber) % (viewports.size()-1);
    }
   
    public int getWidth(){
        return WIDTH;
    }
    public int getHeight(){
        return HEIGHT;
    }
    
}
