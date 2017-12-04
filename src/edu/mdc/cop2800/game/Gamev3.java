/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mdc.cop2800.game;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author student
 */
public class Gamev3 extends Application {

    public static final int FRAMES_PER_SECOND = 30;//60
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    public static final int NUM_OPPONENTS = 10;//15
    public static final int MAX_HEALTH = 100;
    public static final int MAX_HEARTS = 5;

    private Hero hero;
    private ArrayList<Opponent> opponents;
    private ArrayList<ImageView> hearts;
    private ArrayList<ImageView> diamonds;
    private Label scoreLabel;
    private Label finalLabel;

    private boolean gameLost;
    private boolean gameWon;
    private int score;

    public static Group root;
    private Scene scene;

    private static Random random;

    @Override
    public void start(Stage primaryStage) {
        initGame();

        startGameLoop();

        primaryStage.setTitle("Game Time!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initGame() {
        gameLost = false;
        gameWon = false;
        score = 0;

        random = new Random(System.currentTimeMillis());

        root = new Group();
        scene = new Scene(root, WIDTH, HEIGHT, Color.WHITE);

        int[][] heroViewportsCoords = {                    //x,y,width, height
                                                        {0, 0, 24, 36},
                                                        {47, 0, 21, 36},
                                                        {93, 0, 19, 36},
                                                        {135, 0, 28, 36},
                                                        {185, 0, 20, 36},
                                                        {232, 0, 19, 36}
                                                    };
        hero = new Hero("hero.png", true, MAX_HEALTH, heroViewportsCoords);
        hero.setPosition(5, HEIGHT / 2);
        root.getChildren().add(hero.getImageView());

        opponents = new ArrayList<>();
        int[][] opponentViewportsCoord = {
                                                        {0, 0, 23, 48},
                                                        {31, 0, 24, 48},
                                                        {63, 0, 24, 48},
                                                        {95, 0, 25, 48},
                                                        {129, 0, 24, 48},
                                                        {162, 0, 24, 48},
                                                        {194, 0, 23, 48},
                                                        {225, 0, 24, 48}
                                                    };
        for (int i = 0; i < NUM_OPPONENTS; i++) {
            Opponent opponent = new Opponent("opponent.png", true, opponentViewportsCoord);
            opponent.setPosition(getRandomHalfX(opponent.getWidth()), getRandomY(opponent.getHeight()));
            opponents.add(opponent);
            root.getChildren().add(opponent.getImageView());
        }
        
        initHeartsDisplay();
        initScoreDisplay();
        
        diamonds = new ArrayList<>();
        
        scene.setOnKeyPressed(hero);
        scene.setOnKeyReleased(hero);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void moveSprites() {
        hero.move();
        for(int j = 0; j < hero.getWeapons().size(); j++){
            hero.getWeapons().get(j).move();
        }
        for (int i = 0; i < opponents.size(); i++) {
            opponents.get(i).move();
        }
    }

    private void handleCollisions() {
         for (int i = 0; i < opponents.size(); i++) {
            if (opponents.get(i).getImageView().getBoundsInParent()
                    .intersects(hero.getImageView().getBoundsInParent())) {
                
                hero.decrementHealth();
                updateHeartsDisplay();
System.out.println("health = " + hero.getHealth()); 
                if(hero.getHealth() <= 0) {
                    gameLost = true;
                    hero.setIsDying(true);
System.out.println("You lost");     
                    break;
                }
            }
            for(int j = 0; j < hero.getWeapons().size(); j++){           
                if(hero.getWeapons().get(j).getImageView().getBoundsInParent()
                        .intersects(opponents.get(i).getImageView().getBoundsInParent())){
                    opponents.get(i).setIsDying(true);
                    hero.removeWeapon(hero.getWeapons().get(j));
                    score++;
                    updateScoreDisplay();
                    System.out.println("score = "+score);
                }
            }
        }
        
        for (int i = 0; i < opponents.size(); i++) {
            if(opponents.get(i).isDying()) {
               Gamev3.root.getChildren().remove(opponents.get(i).getImageView());
                    opponents.remove(opponents.get(i)); 
                    i--;
            } 
        }
        for(int j = 0; j < hero.getWeapons().size(); j++){ 
            if(hero.getWeapons().get(j).isDying()){
                hero.removeWeapon(hero.getWeapons().get(j));
                j--;
            }    
        }
        for (int k = 0; k < diamonds.size(); k++) {
            if(hero.getImageView().getBoundsInParent()
                    .intersects(diamonds.get(k).getBoundsInParent())){
                Gamev3.root.getChildren().remove(diamonds.get(k));
                diamonds.remove(diamonds.get(k)); 
                k--;
                score += 5;
                
                    updateScoreDisplay();
            }
        }
        
        if(opponents.isEmpty()){
            gameWon = true;                
System.out.println("You Win!"); 
        }
        
        
    }

   

    

    private void startGameLoop() {
        //game loop
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        Duration duration = Duration.millis(1000.0 / FRAMES_PER_SECOND);//Durations is 60 frames per second (per 1000 ms)
        EventHandler onFinished = (EventHandler<ActionEvent>) (ActionEvent event) -> {
            addDiamond();
            moveSprites();
            handleCollisions();
            if (gameLost || gameWon) {
                timeline.stop();
                displayFinalFrame();
                
System.out.println("You lost");
            }
        };
        KeyFrame gameFrame = new KeyFrame(duration, onFinished);
        timeline.getKeyFrames().add(gameFrame);
        timeline.play();
    }

    public static int getRandomHalfX(double offset) {
        return WIDTH / 2 + (int) offset + random.nextInt((WIDTH / 2) - (int) offset);
    }
    
    public static int getRandomX(double offset) {
        return random.nextInt(WIDTH - (int) offset);
    }

    public static int getRandomY(double offset) {
        return random.nextInt(HEIGHT - (int) offset);
    }

    private void updateScoreDisplay() {
        scoreLabel.setText("Score: " + score);
    }

    private void updateHeartsDisplay() {
        int numRedHearts = Math.abs(MAX_HEARTS * hero.getHealth() / MAX_HEALTH) ;
System.out.println("numRedHearts = " + numRedHearts );
        for(int i = numRedHearts + 1; i <= MAX_HEARTS; i++){
            hearts.get(i - 1).setImage(new Image("grayHeart.png"));
        }
    }

    private void initHeartsDisplay() {
       hearts = new ArrayList<>();
       for(int i = 1; i <= MAX_HEARTS; i++){
           Image heartImage = new Image("heart.png");
           ImageView heartImageView = new ImageView(heartImage);
           heartImageView.setTranslateY(2);
           double x = WIDTH - (4 + heartImage.getWidth()) * i;
 System.out.println("hearts: x = "+x);
           heartImageView.setTranslateX(x);
           hearts.add(heartImageView);
           root.getChildren().add(heartImageView);
           
       }
    }

    private void initScoreDisplay() {
       scoreLabel = new Label("Score: " + score);
       root.getChildren().add(scoreLabel);
    }
    
    private void displayFinalFrame() {
        if (gameWon){
            finalLabel = new Label("Game Over. \nYou Won!");
        } else{
            finalLabel = new Label("Game Over. \nYou Lost!");
        }
        finalLabel.setFont(Font.font ("Verdana", 30));
        finalLabel.setTextFill(Color.RED);
        finalLabel.setTranslateX(WIDTH / 2 - finalLabel.getWidth());
        finalLabel.setTranslateY(HEIGHT / 2 - finalLabel.getHeight());
        root.getChildren().add(finalLabel);
    }

    private void addDiamond() {
        if(random.nextDouble() < 0.008) {//1% of the time
           Image diamondImage = new Image("diamond.png");
           ImageView diamondImageView = new ImageView(diamondImage);
           diamondImageView.setTranslateY(getRandomY(diamondImage.getHeight()));
           diamondImageView.setTranslateX(getRandomX(diamondImage.getWidth()));
           diamonds.add(diamondImageView);
           root.getChildren().add(diamondImageView);
        }   
    }

}
