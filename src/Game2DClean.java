import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.effect.Light.Point;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.animation.*;
import java.io.*;
import java.util.*;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * AmongUSStarter with JavaFX and Threads
 * Loading imposters
 * Loading background
 * Control actors and backgrounds
 * Create many number of imposters - random controlled
 * RGB based collision
 * Collsion between two imposters
 */

/**
 * Game2DClean - The start up for the code 
 * @author Luka Lasic
 * @author Arian Vuelic 
 * @since 25-3-2023
 * @version 0.5
 */
public class Game2DClean extends Application {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private StackPane root;

   private static String[] args;

   private final static String CREWMATE_IMAGE = "cow.png"; // file with icon for a racer for this specefic user

   private final static String CREWMATE_RUNNERS = "amongusRunners.png"; // file with icon for a racer

   private final static String MASK_IMAGE = "mask2.png";

   //private final static String MASK_IMAGE_SVG = "mask2.svg";

   private final static String BACKGROUND_IMAGE = "map.png";

   private final static int WIDTH_OF_SCREEN = 800;
   private final static int HEIGHT_OF_SCREEN = 500;
   // cirlce meant to act as collison for character, or bounding circle
   private Circle collisonCircle = new Circle(35.35);

   // Crewmaes
   CrewmateRacer masterCrewmate = null;

   ArrayList<CrewmateRacer> robotCrewmates = new ArrayList<>();

   // moavebale background
   MovableBackground moveablebackground = null;

   // animation timer
   AnimationTimer timer = null;
   int counter = 0;

   boolean moveUp = false;
   boolean moveDown = false;
   boolean moveRight = false;
   boolean moveLeft = false;

   // background /detection/collison
   Image backgroundCollison = null;
   private Collison posistion;
   private TrackMovement tm = new TrackMovement();
   // main program
   public static void main(String[] _args) {
      args = _args;
      launch(args);
   }

   // start() method, called via launch
   public void start(Stage _stage) {
      // stage seteup
      stage = _stage;
      stage.setTitle("Game2D Starter");
      stage.setOnCloseRequest(
            new EventHandler<WindowEvent>() {
               public void handle(WindowEvent evt) {
                  System.exit(0);
               }
            });

      // root pane
      root = new StackPane();

      initializeScene();

   }

   // start the game scene
   public void initializeScene() {
      posistion = new Collison();
      masterCrewmate = new CrewmateRacer(true,CREWMATE_IMAGE);

      /*
       * for(int i=0; i<5;i++){
       * CrewmateRacer cr = new CrewmateRacer(false);
       * robotCrewmates.add(cr);
       * }
       */
      moveablebackground = new MovableBackground(MASK_IMAGE,BACKGROUND_IMAGE);

      // add to the root
      this.root.getChildren().add(moveablebackground);

      this.root.getChildren().add(masterCrewmate);
      // this.root.getChildren().addAll(robotCrewmates);

      // display the window
      scene = new Scene(root, 800, 500);
      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();
      // Keyboard Contro
      scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

         @Override
         public void handle(KeyEvent event) {
            
            switch (event.getCode()) {
               case UP:
                  
                  tm.setMoveUp(true);
                  moveUp = true;
                  
                  System.out.println("Up");

                  break;
               case DOWN:
               tm.setMoveDown(true);
                  moveDown = true;

                  System.out.println("DOWN");

                  break;
               case LEFT:
               tm.setMoveLeft(true);
                  moveLeft = true;

                  System.out.println("Left");

                  break;
               case RIGHT:
                  tm.setMoveRight(true);
                  moveRight = true;

                  System.out.println("Right");

                  break;

            }
         }

      });
      scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

         @Override
         public void handle(KeyEvent event) {
            switch (event.getCode()) {
               case UP:
               tm.setMoveUp(false);

                  moveUp = false;
                  System.out.println("Up realese");

                  break;
               case DOWN:
               tm.setMoveDown(false);

                  moveDown = false;
                  System.out.println("DOWN realese");

                  break;
               case LEFT:
               tm.setMoveLeft(false);

                  moveLeft = false;
                  System.out.println("Left realese");

                  break;
               case RIGHT:
               tm.setMoveRight(false);

                  moveRight = false;
                  System.out.println("Right realese");

                  break;

            }
         }
      });

      backgroundCollison = new Image(MASK_IMAGE);

      masterCrewmate.update();
      // updating the posistion of player each frame
      timer = new AnimationTimer() {

         @Override
         public void handle(long now) {
            // TODO Auto-generated method stub
            // System.out.println(counter++);
            /*
             * for(int i =0; i < robotCrewmates.size();i++){
             * robotCrewmates.get(i).update();
             * }
             */

            //

            moveablebackground.update(moveDown,moveUp,moveLeft,moveRight);

         }

      };
      timer.start();
   }
/* 
   class CrewmateRacer extends Pane {
      // players possiton in relation to the screen and not the background image
      private int racerPosX = 0;
      private int racerPosY = 0;
      private ImageView aPicView = null;
      private boolean isMaster;

      public CrewmateRacer(boolean isMaster) {
         this.isMaster = isMaster;
         if (isMaster) {
            aPicView = new ImageView(CREWMATE_IMAGE);
            aPicView.setFitWidth(50);
            aPicView.setPreserveRatio(true);
            racerPosX = WIDTH_OF_SCREEN / 2 - 25;
            racerPosY = HEIGHT_OF_SCREEN / 2 - 25;
            aPicView.setTranslateX(racerPosX);
            aPicView.setTranslateY(racerPosY);
         } else {
            aPicView = new ImageView(CREWMATE_RUNNERS);
         }

         this.getChildren().add(aPicView);
      }

      public void update() {

         /*
          * if(isMaster){//MASTER CONTROL
          * 
          * 
          * //get pixel
          * 
          * Color color = backgroundCollison.getPixelReader().getColor(racerPosX,
          * racerPosY);
          * //System.out.println("r: "+color.getRed()+",g:"+color.getGreen()+",b:"+
          * color.getBlue());
          * 
          * //get distance
          * int targetX=0;
          * int targetY=0;
          * 
          * double distance =
          * Math.sqrt(Math.pow(racerPosX-targetX,2)+Math.pow(racerPosY-targetY,2));
          * System.out.println("Distance " + distance);
          * if(color.getRed()>0.6){
          * speed = 10;
          * }else{
          * speed = 2;
          * }
          * if(moveDown){
          * racerPosY += speed;
          * }else if(moveUp){
          * racerPosY -= speed;
          * }else if(moveLeft){
          * racerPosX -= speed;
          * 
          * }else if(moveRight){
          * racerPosX += speed;
          * }
          * 
          * }else{//ALL THE OTHERS
          * racerPosX += (Math.random()-.49)*speed;
          * racerPosY += (Math.random()-.49)*speed;
          * }
          */

         // only based on screen not the background of the image
         // moving based on screen
         /* 
         aPicView.setTranslateX(racerPosX);
         aPicView.setTranslateY(racerPosY);
*/
         /*
          * if(racerPosX >root.getWidth()){
          * racerPosX = 0;
          * }else if(racerPosX < 0){
          * racerPosX = (int)root.getWidth();
          * }
          * if(racerPosY>root.getHeight()){
          * racerPosY =0;
          * }
          * else if(racerPosY < 0){
          * racerPosY = (int)root.getHeight();
          * }
          */
/* 
      }

      public ImageView getaPicView() {
         return aPicView;
      }

      public int getRacerPosX() {
         return racerPosX;
      }

      public int getRacerPosY() {
         return racerPosY;
      }

   }
*/
/* 
   private class MovableBackground extends Pane {
      // strating postion of racer in pxiels based on top left corner
      // and correlates to the window
      private int backgroundPosX = -1930;
      private int backgroundPosY = -160;
      // starting postion is based on window of 800 by 500
      private final int STARTING_X_ON_SCREEN = 400;
      private final int STARTING_Y_ON_SCREEN = 250;
      private Collison collisonDetection;
      private int playerPosistionX = 0;
      private int playerPosistionY = 0;

      private int widthOfBackground = 0;
      private int heightOfBackground = 0;
      private int speed = 10;
      private Collison collison = new Collison();
      private ImageView aPicView = null;
      private boolean isMaster;

      public MovableBackground() {

         aPicView = new ImageView(MASK_IMAGE);
         Image sizer = new Image(MASK_IMAGE);
         // SVGPath maskBackground = new SVGPath();
         // maskBackground.setContent("mask2.svg");
         widthOfBackground = (int) sizer.getWidth();
         heightOfBackground = (int) sizer.getHeight();

         this.getChildren().add(aPicView);

      }
      // When updating the movement, is is based on the top left corner of th eiamge
      // and that is being moved
      // this reulsts in the top left corner becoming negative when moving down do to
      // the player move down

      public void update() {
         int oldBackgroundPosX = backgroundPosX;
         int oldBackgroundPosY = backgroundPosY;
         int oldPlayerPosistionX = STARTING_X_ON_SCREEN - backgroundPosX;
         int oldPlayerPosistionY = STARTING_Y_ON_SCREEN - backgroundPosY;
         if (moveDown) {
            backgroundPosY -= speed;
         }else if (moveUp) {
            backgroundPosY += speed;
         }else if (moveLeft) {
            backgroundPosX += speed;
         }else if (moveRight) {
            backgroundPosX -= speed;
         }

         Color color = null;
         
         // get pixel
         playerPosistionX = STARTING_X_ON_SCREEN - backgroundPosX;
         playerPosistionY = STARTING_Y_ON_SCREEN - backgroundPosY;
         // cheking to see if background posision is negative or not
         // to make sure the color reader does not create an error
         // due to putting in a negative number
         // also to make sure the color checker is correct when player postion is postive
         System.out.println("Befroe Alter: Background image X Cord " + backgroundPosX);
         System.out.println("Befroe Alter: Background image Y Cord " + backgroundPosY);
         Collison posistion = collison.checkingCollison(moveablebackground, backgroundCollison,
               playerPosistionX,
               playerPosistionY, backgroundPosX, backgroundPosY);

         
         if(posistion.isCollided()){
            int adjustX = (int) posistion.getChangePoint().getX();
            int adjustY = (int) posistion.getChangePoint().getY();
            if (adjustX != 0 || adjustY != 0) {
               System.out.println("AdjustX: " + adjustX);
               System.out.println("AdjustY  " + adjustY);
               
               
                  backgroundPosX = backgroundPosX - adjustX;
                  backgroundPosY = backgroundPosY - adjustY;
            }else{
               backgroundPosX = oldBackgroundPosX;
               backgroundPosY = oldBackgroundPosY;
               playerPosistionX = STARTING_X_ON_SCREEN - oldBackgroundPosX;
               playerPosistionY = STARTING_Y_ON_SCREEN - oldBackgroundPosY;
            }
         }
         
         aPicView.setTranslateX(backgroundPosX);
         aPicView.setTranslateY(backgroundPosY);

         // players coordiante based on where it is on the background
         // only top left corenr for now
         System.out.println("Background image X Cord " + backgroundPosX);
         System.out.println("Background image Y Cord " + backgroundPosY);

         

         System.out.println("Players X Cord " + playerPosistionX);
         System.out.println("PLayers Y Cord " + playerPosistionY);
         color = backgroundCollison.getPixelReader().getColor(playerPosistionX, playerPosistionY);
         System.out.println("r: " + color.getRed() + ",g:" + color.getGreen() + ",b:" + color.getBlue());

         /*
          * if(masterCrewmate.getaPicView().getBoundsInParent().intersects(
          * moveablebackground.getAPicView().getBoundsInParent())){
          * System.out.println("Collison");
          * }else{
          * System.out.println("No Collison");
          * 
          * }
          */
/*
      }

      public ImageView getAPicView() {
         return aPicView;
      }

      public void setSpeed(int speed) {
         this.speed = speed;
      }

      public int getSpeed() {
         return speed;
      }

      public int getPlayerPosistionX() {
         return playerPosistionX;
      }

      public int getPlayerPosistionY() {
         return playerPosistionY;
      }

      public void setPlayerPosistionX(int playerPosistionX) {
         this.playerPosistionX = playerPosistionX;
      }

      public void setPlayerPosistionY(int playerPosistionY) {
         this.playerPosistionY = playerPosistionY;
      }
   }
   */
  /** 
   class Collison {
      private boolean collided;
      private Point2D changePoint;

      

      public void setCollided(boolean collided) {
         this.collided = collided;
      }



      public void setChangePoint(Point2D changePoint) {
         this.changePoint = changePoint;
      }



      public boolean isCollided() {
         return collided;
      }



      public Point2D getChangePoint() {
         return changePoint;
      }



      public Collison(boolean collided, Point2D changePoint) {
         this.collided = collided;
         this.changePoint = changePoint;
      }



      public Collison() {
      }



      public Collison checkingCollison(MovableBackground mv, Image mask, int playerX, int playerY,
            int moveBackgroundX, int moveBackgroundY) {
         // System.out.println("Players X Cord in Collsion " + playerX);
         // System.out.println("PLayers Y Cord in Collsion " + playerY);
          
         Color collisonColor = mask.getPixelReader().getColor(playerX, playerY);
         this.collided = false;
         
         // checking for collsion based on the center of the image
         // need to be imporved
         if (collisonColor.getRed() > 0.90) {
            System.out.println("Collison collison");
            collided = true;
            // trying to implement stop and slide

            if (moveDown) {
               boolean slideLeft = false;
               boolean slideRight = false;
               PixelReader reader = mask.getPixelReader();

               if (mask.getPixelReader().getColor(playerX + 25, playerY).getRed() > 0.90) {
                  System.out.println("Color of right: " + reader.getColor(playerX + 25, playerY).getRed());
                  slideLeft = true;
                  // System.out.println("Slide Left: True");
               }

               if (mask.getPixelReader().getColor(playerX - 25, playerY).getRed() > 0.90) {
                  System.out.println("Color of left: " + reader.getColor(playerX - 25, playerY).getRed());
                  slideRight = true;
                  // System.out.println("Slide Right: True);
               }

               if (slideLeft && slideRight) {
                  System.out.println("Both Slide: True");
                  return new Collison(collided, new Point2D(0,-10)) ;// moveablebackground.getSpeed());


               } else if (slideRight) {


                  System.out.println("Slide RIGHT IS ACTIVE Slide: True\n\n\n\n\n\n");
                  boolean escape = false;
                  // trying to do slide
                  // getting the colliosn point based on players current posistion
                  Point2D collisionPoint = new Point2D(playerX, playerY);
                  Point2D point2 = null;
                  // where the player is going to be
                  Point2D goal = new Point2D(playerX, playerY + 10);
                  
                  for (int y = playerY + 5; y > playerY - 5; y--) {
                     for (int x = playerX; x < playerX + 10; x++) {
                        System.out.println("X Coord: " +x+ " Y coord:"+y);
                        if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y - 1).getGreen() > 0.6) {

                           // getting the point where ther is a red pixel right next to a green one
                           point2 = new Point2D(x+1, y-1);
                           escape = true;
                           break;
                        }

                     }
                     if (escape)
                        break;
                  }
                  /*
                  int y = (int)goal.getY();
                  int x = (int)goal.getX();
                  while(escape){

                     if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y + 1).getGreen() > 0.6) {

                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x+1, y+1);
                        escape = false;
                     }
                     x++;
                     
                  } */
/* 
                  Point2D wall = point2;
                  if(wall == null){
                     System.out.println("STOPP");
                     return new Collison(collided, new Point2D(0, 10)) ;
                  }
                  // wall.add(point2);
                  // collisionPoint.subtract(goal);
                  // double length =
                  // collisionPoint.distance(point2);//collisionPoint.dotProduct(wall);

                  /*
                   * We are just making the player move in the driection of the slope of the wall
                   */

                 /*  Point2D collisonToWall = wall.subtract(collisionPoint);



                  // double projection = collisonToWall.dotProduct(goal.subtract(collisionPoint))
                  // ;

                  double length = 0;
                  // int slideX = (int)length;//(int)(moveBackgroundX + length);
                  // int slideY = (int)-length;//(int)((moveBackgroundY+10) - length);]

                  return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY())) ;

               }else if(slideLeft){
                  System.out.println("Slide RIGHT IS ACTIVE Slide: True\n\n\n\n\n\n");
                  boolean escape = false;
                  // trying to do slide
                  // getting the colliosn point based on players current posistion
                  Point2D collisionPoint = new Point2D(playerX, playerY);
                  Point2D point2 = null;
                  // where the player is going to be
                  Point2D goal = new Point2D(playerX, playerY + 10);
                  
                  for (int y = playerY + 5; y > playerY - 5; y--) {
                     for (int x = playerX; x > playerX -10; x--) {
                        System.out.println("X Coord: " +x+ " Y coord:"+y);
                        System.out.println("R: " + reader.getColor(x, y).getRed());
                        System.out.println("G: " + reader.getColor(x - 1, y - 1).getGreen());
                        if (reader.getColor(x, y).getRed() > 0.8 && reader.getColor(x - 1, y - 1).getGreen() > 0.6) {

                           // getting the point where ther is a red pixel right next to a green one
                           point2 = new Point2D(x-1, y-1);
                           escape = true;
                           break;
                        }

                     }
                     if (escape)
                        break;
                  }
                  /*
                  int y = (int)goal.getY();
                  int x = (int)goal.getX();
                  while(escape){

                     if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y + 1).getGreen() > 0.6) {

                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x+1, y+1);
                        escape = false;
                     }
                     x++;
                     
                  } */

                  /* Point2D wall = point2;
                  if(wall == null){
                     System.out.println("STOPP");
                     return new Collison(collided, new Point2D(0, 10)) ;
                  }
                  // wall.add(point2);
                  // collisionPoint.subtract(goal);
                  // double length =
                  // collisionPoint.distance(point2);//collisionPoint.dotProduct(wall);

                  /*
                   * We are just making the player move in the driection of the slope of the wall
                   */

                  /* Point2D collisonToWall = wall.subtract(collisionPoint);



                  // double projection = collisonToWall.dotProduct(goal.subtract(collisionPoint))
                  // ;

                  double length = 0;
                  // int slideX = (int)length;//(int)(moveBackgroundX + length);
                  // int slideY = (int)-length;//(int)((moveBackgroundY+10) - length);]

                  return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY())) ;
                     
               } else{
                  return new Collison(collided, new Point2D(0, 10)) ;
               }


               


            } else if (moveUp) {

               boolean slideLeft = false;
               boolean slideRight = false;
               PixelReader reader = mask.getPixelReader();
               // determing that if the player is mving up if the image should slide left or
               // right
               if (mask.getPixelReader().getColor(playerX + 25, playerY).getRed() > 0.90) {
                  System.out.println("Color of right: " + reader.getColor(playerX + 25, playerY).getRed());
                  slideLeft = true;
                  // System.out.println("Slide Left: True");
               }
               if (mask.getPixelReader().getColor(playerX - 25, playerY).getRed() > 0.90) {
                  System.out.println("Color of left: " + reader.getColor(playerX - 25, playerY).getRed());
                  slideRight = true;
                  // System.out.println("Slide Right: True\n\n\n\n\n\n");
               }

               if (slideLeft && slideRight) {
                  System.out.println("Both Slide: True\n\n\n\n\n\n");
                  return new Collison(collided, new Point2D(0, 10));// moveablebackground.getSpeed());


               } else if (slideLeft) {

                  System.out.println("Slide Left IS ACTIVE Slide: True\n\n\n\n\n\n");
                  boolean escape = false;
                  // trying to do slide
                  // getting the colliosn point based on players current posistion
                  Point2D collisionPoint = new Point2D(playerX, playerY);
                  Point2D point2 = null;
                  // where the player is going to be
                  Point2D goal = new Point2D(playerX, playerY - 1);

                  //getting vectir beacsue by subtracting to oiits is the same as getting the slope in dx and dy
                  Point2D vectorToGoal = collisionPoint.subtract(goal);
                  System.out.println("Vector to Goal dx:" + vectorToGoal.getX() + " dy: " + vectorToGoal.getY() );

                  for (int x = playerX; x > playerX - 10; x--) {
                     for (int y = playerY - 1; y < playerY + 1; y++) {
                        System.out.println("X Coord: " +x+ " Y coord:"+y);
                        if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x - 1, y + 1).getGreen() > 0.6) {

                           // getting the point where ther is a red pixel right next to a green one
                           point2 = new Point2D(x-1, y+1);
                           escape = true;
                           break;
                        }

                     }
                     if (escape)
                        break;
                  }


                  Point2D wall = point2;
                  if(wall == null){
                     return new Collison(collided, new Point2D(0, 10));
                  }
                  Point2D vectorToWall =wall.subtract(collisionPoint);
                  Point2D collisonToWall = wall.subtract(collisionPoint);


                  /* 
                  System.out.println("Vector to Wall dx:" + vectorToWall.getX() + " dy: " + vectorToWall.getY() )
                  // wall.add(point2);
                  // collisionPoint.subtract(goal);
                  double length = vectorToGoal.dotProduct(vectorToWall);
                  int newX = (int)(goal.getX() + length * wall.getX());
                  int newY = (int)(goal.getY() + length * wall.getY()); 
                  System.out.println("Scaller value changing to Wall dx:" + length);
                  System.out.println("Scaller change point to Wall x:" + newX + " y: " + newY );

                  Point2D newLocation = new Point2D(length, length);
                  System.out.println("Vector to Wall dx:" + newLocation.getX() + " dy: " + newLocation.getY() );

                  // collisionPoint.distance(point2);//collisionPoint.dotProduct(wall);
                  Point2D collisonToWall = wall.subtract(collisionPoint);
                  System.out.println("collisonToWall dx:" + collisonToWall.getX() + " dy: " + collisonToWall.getY() );

                  // double projection = collisonToWall.dotProduct(goal.subtract(collisionPoint))
                  // ;
                  Point2D vectorToGoalGPT = goal.subtract(collisionPoint);
                  Point2D vectorToWallGPT = wall.subtract(collisionPoint);
                  double dotProduct = vectorToGoal.dotProduct(vectorToWall);
                  double lengthGPT = dotProduct / vectorToWall.magnitude();
                  Point2D newLocationGPT = collisionPoint.add(vectorToWall.normalize().multiply(length));
                  System.out.println("New to Wall dx:" + newLocationGPT.getX() + " dy: " + newLocationGPT.getY() );
                  Point2D vecotrMove = newLocationGPT.subtract(collisionPoint);
                  System.out.println("Move vector dx:" + vecotrMove.getX() + " dy: " + vecotrMove.getY() );

                  
                  Point2D newLocationVector = vectorToWall.normalize().multiply(length);

                  // Calculate the new location by subtracting the vector from the collision point
                  Point2D newLocation2 = collisionPoint.subtract(newLocationVector);
                  System.out.println("New to Wall dx:" + newLocation2.getX() + " dy: " + newLocation2.getY());
                  
                  // Calculate the adjustments needed to move the player to the new location
                  int adjustX = (int)(newLocation2.getX() - playerX);
                  int adjustY = (int)(newLocation2.getY() - playerY);
                  System.out.println("AdjustX based off dot: " + adjustX);
                  System.out.println("AdjustY based off dot: " + adjustY);
                  // int slideX = (int)length;//(int)(moveBackgroundX + length);
                  // int slideY = (int)-length;//(int)((moveBackgroundY+10) - length);]
                  */
                 /*  return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));
                  //return vecotrMove;

                  
               } else if (slideRight) {


                  System.out.println("Slide RIGHT IS ACTIVE Slide: True\n\n\n\n\n\n");
                  boolean escape = false;
                  // trying to do slide
                  // getting the colliosn point based on players current posistion
                  Point2D collisionPoint = new Point2D(playerX, playerY);
                  Point2D point2 = null;
                  // where the player is going to be
                  Point2D goal = new Point2D(playerX, playerY - 10);
                  
                  for (int x = playerX; x < playerX + 10; x++) {
                     for (int y = playerY - 5; y < playerY + 5; y++) {
                        System.out.println("X Coord: " +x+ " Y coord:"+y);
                        if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y + 1).getGreen() > 0.6) {

                           // getting the point where ther is a red pixel right next to a green one
                           point2 = new Point2D(x+1, y+1);
                           escape = true;
                           break;
                        }

                     }
                     if (escape)
                        break;
                  }
                  /*
                  int y = (int)goal.getY();
                  int x = (int)goal.getX();
                  while(escape){

                     if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y + 1).getGreen() > 0.6) {

                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x+1, y+1);
                        escape = false;
                     }
                     x++;
                     
                  } */

              /*     Point2D wall = point2;
                  if(wall == null){
                     System.out.println("STOPP");
                     return new Collison(collided,new Point2D(0, 10));
                  }
                  // wall.add(point2);
                  // collisionPoint.subtract(goal);
                  // double length =
                  // collisionPoint.distance(point2);//collisionPoint.dotProduct(wall);

                  /*
                   * We are just making the player move in the driection of the slope of the wall
                   */

               /*    Point2D collisonToWall = wall.subtract(collisionPoint);



                  // double projection = collisonToWall.dotProduct(goal.subtract(collisionPoint))
                  // ;

                  double length = 0;
                  // int slideX = (int)length;//(int)(moveBackgroundX + length);
                  // int slideY = (int)-length;//(int)((moveBackgroundY+10) - length);]

                  return new Collison(collided,new Point2D(collisonToWall.getX(), collisonToWall.getY()));

               }

            } else if (moveLeft) {
               
               boolean slideUp = false;
               boolean slideDown = false;
               PixelReader reader = mask.getPixelReader();
               // determing that if the player is mving up if the image should slide left or
               // right
               if (mask.getPixelReader().getColor(playerX, playerY + 25).getRed() > 0.90) {
                  System.out.println("Color of right: " + reader.getColor(playerX + 25, playerY).getRed());
                  slideUp = true;
                  // System.out.println("Slide Left: True");
               }
               if (mask.getPixelReader().getColor(playerX, playerY - 25).getRed() > 0.90) {
                  System.out.println("Color of left: " + reader.getColor(playerX - 25, playerY).getRed());
                  slideDown = true;
                  // System.out.println("Slide Right: True\n\n\n\n\n\n");
               }

               if (slideUp && slideDown) {
                  System.out.println("Both Slide: True\n\n\n\n\n\n");
                  return new Collison(collided, new Point2D(10, 0));// moveablebackground.getSpeed());


               } else if (slideUp) {

                  System.out.println("Slide UP IS ACTIVE Slide: True\n\n\n\n\n\n");
                  boolean escape = false;
                  // trying to do slide
                  // getting the colliosn point based on players current posistion
                  Point2D collisionPoint = new Point2D(playerX, playerY);
                  Point2D point2 = null;
                  // where the player is going to be
                  Point2D goal = new Point2D(playerX-10, playerY);

                  //getting vectir beacsue by subtracting to oiits is the same as getting the slope in dx and dy


                  for (int x = playerX + 5; x > playerX - 5; x--) {
                     for (int y = playerY - 5; y < playerY+5; y++) {
                        System.out.println("X Coord: " +x+ " Y coord:"+y);
                        System.out.println("R: " + reader.getColor(x, y).getRed());
                        System.out.println("G: " + reader.getColor(x + 1, y - 1).getGreen());
                        if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 2 , y-1).getGreen() > 0.5) {
                           //try {
                              System.out.println("THE GOD IS DIED");
                              //Thread.sleep(1000);
                              
                           //} catch (InterruptedException e) {
                              // TODO Auto-generated catch block
                              //e.printStackTrace();
                           //}
                           // getting the point where ther is a red pixel right next to a green one
                           point2 = new Point2D(x+2, y-1);
                           escape = true;
                           break;
                        }

                     }
                     if (escape)
                        break;
                  }


                  Point2D wall = point2;
                  if(wall == null){
                     return new Collison(collided, new Point2D(10,0));
                  }
                  Point2D vectorToWall =wall.subtract(collisionPoint);
                  Point2D collisonToWall = wall.subtract(collisionPoint);
                  System.out.println("collisonToWall dx:" + collisonToWall.getX() + " dy: " + collisonToWall.getY() );


                  
                  
                  
                  return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));
                 

                  
               } else if (slideDown) {


                  System.out.println("Slide RIGHT IS ACTIVE Slide: True\n\n\n\n\n\n");
                  boolean escape = false;
                  // trying to do slide
                  // getting the colliosn point based on players current posistion
                  Point2D collisionPoint = new Point2D(playerX, playerY);
                  Point2D point2 = null;
                  // where the player is going to be
                  Point2D goal = new Point2D(playerX-10, playerY);
                  
                  for (int x = playerX; x < playerX + 10; x++) {
                     for (int y = playerY - 5; y < playerY + 5; y++) {
                        System.out.println("X Coord: " +x+ " Y coord:"+y);
                        if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y + 1).getGreen() > 0.6) {

                           // getting the point where ther is a red pixel right next to a green one
                           point2 = new Point2D(x+1, y+1);
                           escape = true;
                           break;
                        }

                     }
                     if (escape)
                        break;
                  }
                 

                  Point2D wall = point2;
                  if(wall == null){
                     System.out.println("STOPP");
                     return new Collison(collided,new Point2D(0, 10));
                  }
                 

                  /*
                   * We are just making the player move in the driection of the slope of the wall
                   */

             /*      Point2D collisonToWall = wall.subtract(collisionPoint);


                  double length = 0;
            

                  return new Collison(collided,new Point2D(collisonToWall.getX(), collisonToWall.getY()));
               }
            } else if (moveRight) {
               boolean slideUp = false;
               boolean slideDown = false;
               PixelReader reader = mask.getPixelReader();
               // determing that if the player is mving up if the image should slide left or
               // right
               if (mask.getPixelReader().getColor(playerX, playerY - 25 ).getRed() > 0.90) {
                  System.out.println("Color of right: " + reader.getColor(playerX + 25, playerY).getRed());
                  slideDown = true;
                  // System.out.println("Slide Left: True");
               }
               if (mask.getPixelReader().getColor(playerX, playerY + 25).getRed() > 0.90) {
                  System.out.println("Color of left: " + reader.getColor(playerX - 25, playerY).getRed());
                  slideUp = true;
                  // System.out.println("Slide Right: True\n\n\n\n\n\n");
               }

               if (slideUp && slideDown) {
                  System.out.println("Both Slide: True\n\n\n\n\n\n");
                  return new Collison(collided, new Point2D(-10, 0));// moveablebackground.getSpeed());


               } else if (slideDown) {

                  System.out.println("Slide UP IS ACTIVE Slide: True\n\n\n\n\n\n");
                  boolean escape = false;
                  // trying to do slide
                  // getting the colliosn point based on players current posistion
                  Point2D collisionPoint = new Point2D(playerX, playerY);
                  Point2D point2 = null;
                  // where the player is going to be
                  Point2D goal = new Point2D(playerX+10, playerY);

                  //getting vectir beacsue by subtracting to oiits is the same as getting the slope in dx and dy


                        for (int y = playerY - 5; y < playerY + 5; y++) {
                           for (int x = playerX; x < playerX + 10; x++) {
                                 System.out.println("X Coord: " +x+ " Y coord:"+y);
                                 System.out.println("R: " + reader.getColor(x, y).getRed());
                                 System.out.println("G: " + reader.getColor(x - 2 , y + 2 ).getGreen());
                              if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x-2, y + 2).getGreen() > 0.6) {
      
                                 // getting the point where ther is a red pixel right next to a green one
                                 point2 = new Point2D(x-2, y+1);
                                 escape = true;
                                 break;
                              }
      
                           }
                           if (escape)
                              break;
                        }


                  Point2D wall = point2;
                  if(wall == null){
                     return new Collison(collided, new Point2D(-10,0));
                  }
                  Point2D vectorToWall =wall.subtract(collisionPoint);
                  Point2D collisonToWall = wall.subtract(collisionPoint);
                  System.out.println("collisonToWall dx:" + collisonToWall.getX() + " dy: " + collisonToWall.getY() );


                  
                  
                  
                  return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));
                 

                  
               } else if (slideUp) {


                  System.out.println("Slide RIGHT IS ACTIVE Slide: True\n\n\n\n\n\n");
                  boolean escape = false;
                  // trying to do slide
                  // getting the colliosn point based on players current posistion
                  Point2D collisionPoint = new Point2D(playerX, playerY);
                  Point2D point2 = null;
                  // where the player is going to be
                  Point2D goal = new Point2D(playerX-10, playerY);
                  
                  for (int y = playerY - 10; y < playerY + 5; y++) {
                     for (int x = playerX; x < playerX + 10; x++) {
                        System.out.println("X Coord: " +x+ " Y coord:"+y);
                        System.out.println("R: " + reader.getColor(x, y).getRed());
                        System.out.println("G: " + reader.getColor(x - 1, y - 1).getGreen());
                        if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x-1, y - 1).getGreen() > 0.5) {

                           // getting the point where ther is a red pixel right next to a green one
                           point2 = new Point2D(x-1, y-1);
                           escape = true;
                           break;
                        }

                     }
                     if (escape)
                        break;
                  }
                 

                  Point2D wall = point2;
                  if(wall == null){
                     System.out.println("STOPP");
                     return new Collison(collided,new Point2D(-10,0));
                  }
                 

                  /*
                   * We are just making the player move in the driection of the slope of the wall
                   */

                 /*  Point2D collisonToWall = wall.subtract(collisionPoint);


                  double length = 0;
            

                  return new Collison(collided,new Point2D(collisonToWall.getX(), collisonToWall.getY()));
               }

            }

         } else {
            System.out.println("No collison");

         }
         return new Collison(collided, new Point2D(0, 0));

      }*/
}

 // end class Races