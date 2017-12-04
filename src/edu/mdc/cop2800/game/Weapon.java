/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mdc.cop2800.game;

import javafx.scene.transform.Translate;

/**
 *
 * @author student
 */
public class Weapon extends Sprite {
    private Hero hero;
    private boolean goesEast;

    public Weapon(Hero hero, boolean goesEast,  String imageFile, boolean isVisible, int[][] viewportsCoords) {
        super(imageFile, isVisible, viewportsCoords);        
        if(goesEast){
            dx = 3;
        } else {
            dx = -3;           
            imageView.setScaleX(-1);
        }
        dy = 0;
        this.hero = hero;
        this.goesEast = goesEast;
    }
    
    @Override
    public void move(){
        super.move();
        if( (goesEast && imageView.getTranslateX() > Gamev3.WIDTH)
                || (!goesEast && imageView.getTranslateX() < 0)
        ){
           hero.removeWeapon(this);
        }
    }
    
   
}
