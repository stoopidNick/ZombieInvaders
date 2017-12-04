/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mdc.cop2800.game;

import java.util.ArrayList;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author student
 */
public abstract class Sprite {
    protected String imageFile;
    protected Image image;
    protected ImageView imageView;
    protected int dx; //next x-axis translation
    protected int dy; //next y-axis translation
    protected boolean isVisible;
    protected boolean isDying;
    
    protected int frameNumber;
    protected ArrayList<Rectangle2D> viewports;

    public Sprite(String imageFile, boolean isVisible, int[][] viewportsCoords) {
        this.image = new Image(imageFile);
        this.isVisible = isVisible;
        isDying = false;
        dx = 0;
        dy = 0;

        imageView = new ImageView(this.image);
        imageView.setCache(true);
        imageView.setSmooth(true);
        viewports = new ArrayList<>();
        for (int[] viewportsCoord : viewportsCoords) {
            viewports.add(new Rectangle2D(viewportsCoord[0], viewportsCoord[1],
                    viewportsCoord[2], viewportsCoord[3]));
        }
        frameNumber = 0;
        imageView.setViewport(viewports.get(frameNumber));
    }
    
    public void setPosition(int initialX, int initialY){ 
        imageView.setTranslateX(initialX);
        imageView.setTranslateY(initialY);
    }
    
    public void move() {
        imageView.setTranslateX(imageView.getTranslateX() + dx);
        imageView.setTranslateY(imageView.getTranslateY() + dy);
    }
    
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public boolean isIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isDying() {
        return isDying;
    }

    public void setIsDying(boolean isDying) {
        this.isDying = isDying;
    }
    
}
