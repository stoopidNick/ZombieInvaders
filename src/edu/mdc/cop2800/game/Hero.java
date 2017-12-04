/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mdc.cop2800.game;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author student
 */
public class Hero extends Sprite implements EventHandler<KeyEvent> {
    private int health;
    private ArrayList<Weapon> weapons;
    private boolean goesEast; 
    private final int WIDTH = 2;
    private final int HEIGHT = 2;
    
    
    
    

    public Hero(String imageFile, boolean isVisible,  int initialHealth, int[][] viewportsCoords) {
        super(imageFile, isVisible, viewportsCoords);
        health = initialHealth;
        weapons = new ArrayList<>();
        goesEast = true;
    }
    

    public int getHealth() {
        return health;
    }
    
    public void decrementHealth(){
        health--;
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
        Gamev3.root.getChildren().add(weapon.getImageView());
    }

    public void removeWeapon(Weapon weapon) {        
        Gamev3.root.getChildren().remove(weapon.getImageView());
        weapons.remove(weapon);
    }
    
    public boolean hasWeapons(){
        return weapons.isEmpty();
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    @Override
    public void handle(KeyEvent ke){
        if(ke.getEventType() == KeyEvent.KEY_PRESSED) {
               //System.out.println("key pressed, frameNumber="+ frameNumber);
                imageView.setViewport(viewports.get(frameNumber));
                   frameNumber =  (++frameNumber) % (viewports.size() - 1);
                if(ke.getCode() == KeyCode.LEFT ){
                   if((int)imageView.getTranslateX() > 0) dx = -1;
                   else dx = 0;
                    goesEast = false;
                    imageView.setScaleX(-1);
                } else if(ke.getCode() == KeyCode.RIGHT ){
                    Rectangle2D viewport = viewports.get(frameNumber);
                    int width = (int) viewport.getWidth();
                     if((int)imageView.getTranslateX() < Gamev3.WIDTH - width)
                        dx = 1;
                    else dx = 0;
                    goesEast = true;
                    imageView.setScaleX(1);
                } else if(ke.getCode() == KeyCode.DOWN ){
                    Rectangle2D viewport = viewports.get(frameNumber);
                    int height = (int) viewport.getHeight();
                    if((int)imageView.getTranslateY() < Gamev3.HEIGHT - height)
                        dy = 1;
                    else dy = 0;
                } else if(ke.getCode() == KeyCode.UP ){
                    if((int)imageView.getTranslateY() > 0) dy = -1;
                    else dy = 0;
                } else if(ke.getCode() == KeyCode.SPACE) {
                    Weapon weapon;
                    int[][] weaponViewportsCoords = {
                                                        {2, 9, 20, 9}
                                                    };
                    Rectangle2D viewport = viewports.get(frameNumber);
                    int width = (int) viewport.getWidth();
                    
                    if(goesEast){
                        weapon = new Weapon(this, goesEast,"weapon.png", true, weaponViewportsCoords);
                        weapon.setPosition((int)imageView.getTranslateX()+width, 
                            (int)imageView.getTranslateY());
                    } else {
                        weapon = new Weapon(this, goesEast,"weapon.png", true, weaponViewportsCoords);
                        weapon.setPosition((int)imageView.getTranslateX()-width, 
                            (int)imageView.getTranslateY());
                    }
                    addWeapon(weapon);
                    
                }
        } else if (ke.getEventType() == KeyEvent.KEY_RELEASED){
            dx = 0;
            dy = 0;
        }
    }
    
    
    public int getWidth(){
        return WIDTH;
    }
    public int getHeight(){
        return HEIGHT;
    }
    
}
