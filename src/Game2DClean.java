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
import javafx.util.Duration;
import javafx.geometry.*;
import javafx.animation.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

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
 * 
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


   private final static String CREWMATE_IMAGE2 = "duck.png";


   private final static String CREWMATE_RUNNERS = "amongusRunners.png"; // file with icon for a racer

   private final static String MASK_IMAGE = "mask2.png";

   // private final static String MASK_IMAGE_SVG = "mask2.svg";

   private final static String BACKGROUND_IMAGE = "map.png";

   private final static int WIDTH_OF_SCREEN = 800;
   private final static int HEIGHT_OF_SCREEN = 500;
   // cirlce meant to act as collison for character, or bounding circle
   private Circle collisonCircle = new Circle(35.35);

   // Crewmaes
   CrewmateRacer masterCrewmate = null;

   ArrayList<CrewmateRacer> otherCrewmates = new ArrayList<>();

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


   private TextArea chatbox = new TextArea();

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

      /*
       * for(int i=0; i<5;i++){
       * CrewmateRacer cr = new CrewmateRacer(false);
       * robotCrewmates.add(cr);
       * }
       */
      moveablebackground = new MovableBackground(MASK_IMAGE, BACKGROUND_IMAGE);
      masterCrewmate = new CrewmateRacer(true, CREWMATE_IMAGE, moveablebackground);
      CrewmateRacer dummyKill = new CrewmateRacer(false, CREWMATE_IMAGE2);

      otherCrewmates.add(dummyKill);






      // add to the root
      this.root.getChildren().add(moveablebackground);

      
      

      // Adding an interactable object
      Interactable task1 = new Interactable("amongus.png");
      EmergencyButton emergencyButton = new EmergencyButton("amongus.png");
      this.root.getChildren().addAll(task1, emergencyButton);



      this.root.getChildren().addAll(dummyKill);
      // needs to be added last
      this.root.getChildren().add(masterCrewmate);

      // display the window
      scene = new Scene(root, 800, 500);
      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();

      // Keyboard Controls
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
               default:
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
               default:
                  break;

            }
         }
      });

      backgroundCollison = new Image(MASK_IMAGE);

      // masterCrewmate.update();
      // updating the posistion of player each frame

      // allowing a a scope acces variable for the player posisiton
      Point2D playerLocation = null;
      timer = new AnimationTimer() {

         @Override
         public void handle(long now) {
            Point2D location = moveablebackground.update(moveDown, moveUp, moveLeft, moveRight);

            task1.update(location.getX(), location.getY());
            emergencyButton.update(location.getX(), location.getY());

            otherCrewmates.get(0).update(location.getX(), location.getY());
            // to detect the distance between a player and a task
            if (!masterCrewmate.isImposter()) {
               if (emergencyButton.distanceEmgButton(masterCrewmate)) {
                  synchronized (masterCrewmate) {

                     masterCrewmate.getBtnUse().setDisable(false);

                     // task.addHighlight();

                  }
                  masterCrewmate.setToUse("Emergency");
               } else if (checkDistance(masterCrewmate, task1)) {
                  synchronized (masterCrewmate) {

                     masterCrewmate.getBtnUse().setDisable(false);
                     masterCrewmate.setToUse("Task 1");
                     // task.addHighlight();

                  }
               } else {
                  masterCrewmate.getBtnUse().setDisable(true);
               }

               if (masterCrewmate.getBtnUse().isPressed()) {
                  if (masterCrewmate.getToUse().equals("Emergency")) {
                     masterCrewmate.setInMeeting(true);
                     masterCrewmate.meeting();
                     //root.getChildren().addAll(chatbox);
                     // making movement stop during a meeting

                     // possible where popup for chat will go and send to all users of the
                     // notifcaiton

                     synchronized (masterCrewmate) {
                        Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask() {
                            public void run() {
                                // Your task code here
                              masterCrewmate.setInMeeting(false);
                              masterCrewmate.notify();
                              root.getChildren().removeAll(chatbox);
                            }
                        }, 5 * 1000);
                     

                        /* for (int i = 0; i < 10; i++) {
                           try {
                              Thread.sleep(1000);
                           } catch (InterruptedException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                           }
                        }*/
                        
                     }

                     synchronized (masterCrewmate) {
                        while (masterCrewmate.isInMeeting()) {
                           try {
                              timer.wait();
                           } catch (InterruptedException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                           }
                        }
                     }

                     /*Popup popup = new Popup();
                     popup.getContent().add(chatbox);
                     popup.show(scene.getWindow(), 100, 100);
                     Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), e -> popup.hide()));
                     timeline.play();*/

                     

                     

                  }

               }

            }else{
               if(CrewmateRacer.checkDistance(masterCrewmate, dummyKill)){
                  masterCrewmate.getBtnKill().setDisable(false);
               }else{
                  masterCrewmate.getBtnKill().setDisable(true);
               }

               if(masterCrewmate.getBtnKill().isPressed()){
                  masterCrewmate.kill(dummyKill);
               }
            }

         }

      };
      timer.start();

   }

   // method to activate if the player is close enough to a task
   public boolean checkDistance(CrewmateRacer crewmate, Interactable task) {
      double xDis = 0;
      double yDis = 0;
      synchronized (masterCrewmate) {
         xDis = Math.pow(crewmate.getRacerPosX() - task.getPosXBasedOnBack(), 2);
         yDis = Math.pow(crewmate.getRacerPosY() - task.getPosYBasedOnBack(), 2);
      }
      double distance = Math.sqrt(xDis + yDis);
      System.out.println("Distance between " + task.getName() + " and players posistion is:" + distance + "\n\n");
      if (distance < 80) {
         return true;
      } else {
         return false;

      }
   }
   
}

// end class Races