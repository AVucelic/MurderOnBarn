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
import java.net.Socket;
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

   private String CREWMATE_IMAGE = "cow.png"; // file with icon for a racer for this specefic user

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
   CrewmateRacer onlinePLayer = null;

   private ArrayList<CrewmateRacer> otherCrewmates = new ArrayList<>();

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

   private TextField message = new TextField();

   private Integer indexOfPlayrInArrayList;
   // general SOCKET attributes
   public static final int SERVER_PORT = 32001;
   private Socket socket = null;

   // IO attributes
   private ObjectInputStream ois = null;
   private ObjectOutputStream oos = null;
   private Button btnsend = null;

   private Scene newScene =null;
   private Stage newStage = null;

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

      // menuSreen();
      // root pane
      initializeScene();
   }

   // connection lobby for all players
   private void waitingLobbby() {
      // making the current player
      moveablebackground = new MovableBackground(MASK_IMAGE, BACKGROUND_IMAGE);
      masterCrewmate = new CrewmateRacer(true, CREWMATE_IMAGE, moveablebackground);
      CrewmateRacer temp = new CrewmateRacer(masterCrewmate.isImposter(), masterCrewmate.getName());
      try {
         // adding the player to the arraylist on the server
         oos.writeObject("AddToServer");
         oos.flush();
         oos.writeObject(temp);
         oos.flush();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      FlowPane root = new FlowPane();

      Button btnStart = new Button("Start Game");
      // starting the actaul game
      btnStart.setOnAction(new EventHandler<ActionEvent>() {

         @Override
         public void handle(ActionEvent event) {
            //
            try {
               oos.writeObject("Begin");

               try {
                  Integer numberOfPlayers = (Integer) ois.readObject();
                  indexOfPlayrInArrayList = (Integer) ois.readObject();
                  ArrayList<CrewmateRacer> crewmatesBeforeChange = (ArrayList<CrewmateRacer>) ois.readObject();
                  otherCrewmates.remove((int) indexOfPlayrInArrayList);
                  initializeScene();
               } catch (ClassNotFoundException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

         }

      });
      VBox top = new VBox();
      top.getChildren().addAll(btnStart);
      top.setAlignment(Pos.CENTER);

      root.getChildren().addAll(top);
      root.setAlignment(Pos.CENTER);
      scene = new Scene(root, 800, 500);
      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();

   }

   // a method that is wehre the player begins and connects to the server
   private void menuSreen() {
      FlowPane root = new FlowPane();

      Button btnStart = new Button("Start");
      btnStart.setOnAction(new EventHandler<ActionEvent>() {

         @Override
         public void handle(ActionEvent event) {
            // allowing connection to the server
            TextInputDialog tid = new TextInputDialog();
            tid.setContentText("Enter Server's IP Address");
            tid.setHeaderText("Server IP");
            // getting the command the user want to execture
            Optional<String> value = tid.showAndWait();
            String ipAddress = value.get();
            // Make a connection with the server
            try {
               socket = new Socket(ipAddress, SERVER_PORT);
               ois = new ObjectInputStream(socket.getInputStream());
               oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
            waitingLobbby();

         }

      });

      Button changeCharacter = new Button("Change Character");
      changeCharacter.setOnAction(new EventHandler<ActionEvent>() {

         @Override
         public void handle(ActionEvent event) {
            while (true) {
               TextInputDialog tid = new TextInputDialog();
               tid.setContentText("Animals: cow,duck,pig, or rooster");
               tid.setHeaderText("Chose Animal");
               Optional<String> value = tid.showAndWait();
               String animal = value.get();
               String check = animal.toLowerCase();
               if (animal.equals("cow")) {
                  CREWMATE_IMAGE = "cow.png";
                  break;

               } else if (animal.equals("duck")) {
                  CREWMATE_IMAGE = "duck.png";
                  break;

               } else if (animal.equals("pig")) {
                  CREWMATE_IMAGE = "pig.png";
                  break;
               } else if (animal.equals("rooster")) {
                  CREWMATE_IMAGE = "rooster.png";
                  break;
               } else {
                  continue;
               }
            }
         }

      });

      VBox top = new VBox();
      top.getChildren().addAll(btnStart, changeCharacter);
      top.setAlignment(Pos.CENTER);

      root.getChildren().addAll(top);
      root.setAlignment(Pos.CENTER);

      // display the window
      scene = new Scene(root, 800, 500);
      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();

   }

   // start the game scene
   public void initializeScene() {

      try {
         socket = new Socket("127.0.0.1", SERVER_PORT);
         ois = new ObjectInputStream(socket.getInputStream());
         oos = new ObjectOutputStream(socket.getOutputStream());
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      
      try {
         oos.writeObject("Begin");
         Integer numberOfPlayers = (Integer) ois.readObject();
         indexOfPlayrInArrayList = (Integer) ois.readObject();
      } catch (ClassNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      // ArrayList<CrewmateRacer> othCrewmateRacers =
      // (ArrayList<CrewmateRacer>)ois.readObject();
      // otherCrewmates.remove((int)indexOfPlayrInArrayList);

      root = new StackPane();
      posistion = new Collison();

      /*
       * for(int i=0; i<5;i++){
       * CrewmateRacer cr = new CrewmateRacer(false);
       * robotCrewmates.add(cr);
       * }
       */

      // moveablebackground = new MovableBackground(MASK_IMAGE, BACKGROUND_IMAGE);
      // masterCrewmate = new CrewmateRacer(true, CREWMATE_IMAGE, moveablebackground);

      // creating a player for a differnt plaeyr to connect to

      // add to the root
      moveablebackground = new MovableBackground(MASK_IMAGE, BACKGROUND_IMAGE);
      this.root.getChildren().add(moveablebackground);

      // Adding an interactable object
      Interactable task1 = new Interactable("amongus.png");
      EmergencyButton emergencyButton = new EmergencyButton("amongus.png");
      this.root.getChildren().addAll(task1, emergencyButton);

      
      

      if (indexOfPlayrInArrayList == 0) {
         masterCrewmate = new CrewmateRacer(true, CREWMATE_IMAGE, moveablebackground);
         
         
         masterCrewmate.setLocationOnServer(indexOfPlayrInArrayList);
         onlinePLayer = new CrewmateRacer(false, CREWMATE_IMAGE2);
         onlinePLayer.setLocationOnServer(1);
      } else if (indexOfPlayrInArrayList == 1) {
         masterCrewmate = new CrewmateRacer(false,CREWMATE_IMAGE2 , moveablebackground);
         masterCrewmate.setLocationOnServer(indexOfPlayrInArrayList);
         onlinePLayer = new CrewmateRacer(true, CREWMATE_IMAGE);
         onlinePLayer.setLocationOnServer(0);
      }

      try {
         //also telling server if you are imposter or not
         oos.writeObject("addName");
         oos.flush();
         oos.writeObject(masterCrewmate.getName());
         oos.flush();
         oos.writeObject(masterCrewmate.isImposter());
         oos.flush();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      otherCrewmates.add(masterCrewmate);
      otherCrewmates.add(onlinePLayer);
      this.root.getChildren().addAll(onlinePLayer);
      // adding all crewmates to the this players pane

      /*
       * for (int i = 0; i < otherCrewmates.size(); i++) {
       * /*
       * otherCrewmates.get(i).setaPicView(new
       * ImageView(otherCrewmates.get(i).getName()));
       * otherCrewmates.get(i).getaPicView().setFitWidth(50);
       * otherCrewmates.get(i).getaPicView().setPreserveRatio(true);
       * //otherCrewmates.get(i).setRacerPosX(400);
       * //otherCrewmates.get(i).setRacerPosY(250);
       * otherCrewmates.get(i).getaPicView().setTranslateX(400);
       * otherCrewmates.get(i).getaPicView().setTranslateY(250);
       * //aPicView = new ImageView(crewmateImage);
       * //aPicView.setFitWidth(50);
       * //aPicView.setPreserveRatio(true);
       * //racerPosX = 400;
       * //racerPosY = 500;
       * //aPicView.setTranslateX(racerPosX);
       * //aPicView.setTranslateY(racerPosY);
       * 
       * otherCrewmates.get(i).getChildren().addAll(otherCrewmates.get(i).getaPicView(
       * ));
       */
      /*
       * 
       * 
       * 
       * }
       */
      // this.root.getChildren().addAll(dummyKill);

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

                  // System.out.println("Up");

                  break;
               case DOWN:
                  tm.setMoveDown(true);
                  moveDown = true;

                  // System.out.println("DOWN");

                  break;
               case LEFT:
                  tm.setMoveLeft(true);
                  moveLeft = true;

                  // System.out.println("Left");

                  break;
               case RIGHT:
                  tm.setMoveRight(true);
                  moveRight = true;

                  // System.out.println("Right");

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
                  // System.out.println("Up realese");

                  break;
               case DOWN:
                  tm.setMoveDown(false);

                  moveDown = false;
                  // System.out.println("DOWN realese");

                  break;
               case LEFT:
                  tm.setMoveLeft(false);

                  moveLeft = false;
                  // System.out.println("Left realese");

                  break;
               case RIGHT:
                  tm.setMoveRight(false);

                  moveRight = false;
                  // System.out.println("Right realese");

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
      // This is the thread for other player movement and updating their psositons

      // recveing thread
      Timer reciver = new Timer();
      TimerTask recivingInfo = new TimerTask() {

         @Override
         public void run() {
            try {
               Object obj = ois.readObject();
               if (obj instanceof String) {
                  String command = (String) obj;
                  switch (command) {
                     case "newplayer":

                     case "move":
                        int size = (int)ois.readObject();
                        ArrayList<PlayerPoint> oldLocations = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                           PlayerPoint player = (PlayerPoint)ois.readObject();
                           oldLocations.add(player);
                        }
                        //ArrayList<PlayerPoint> oldLocations = (ArrayList<PlayerPoint>) ois.readObject();
                    
                        for (int i = 0; i < oldLocations.size(); i++) {
                           System.out.println(oldLocations.get(i).getIndex() + " old X: " + oldLocations.get(i).getX() + " old Y " + oldLocations.get(i).getY());

                           int index = oldLocations.get(i).getIndex();
                           //System.out.println("Mastercrewmate INdex: "+masterCrewmate.getLocationOnServer());
                           for(int y =0; y<otherCrewmates.size();y++){
                              // System.out.println(otherCrewmates.get(y).getLocationOnServer());
                              // System.out.println(index);
                              if(otherCrewmates.get(y).getLocationOnServer() == index && 
                              otherCrewmates.get(y).getLocationOnServer() != masterCrewmate.getLocationOnServer() ){
                                 // System.out.println("THe index that is modfied: "  + index);
                                 int changeInX = (int) oldLocations.get(i).getX() - masterCrewmate.getBackgroundPosX();
                                 int changeInY = (int) oldLocations.get(i).getY() - masterCrewmate.getBackgroundPosY();
                                 otherCrewmates.get(y).updateOnline(-changeInX, -changeInY);
                                 // System.out.println("Chnage other crewmates posistion");
                              }
                           }
                           
                           
                        }
                        break;
                     case "meeting":
                        masterCrewmate.meeting();
                        Platform.runLater(() -> {
                           chatbox.setText("");
                           message.setText("");
                           newStage = new Stage();
                           VBox root = new VBox(10);

                           FlowPane middle = new FlowPane();
                           btnsend = new Button("Send");
                           
                           btnsend.setOnAction(new EventHandler<ActionEvent>() {

                              @Override
                              public void handle(ActionEvent event) {
                                 String line = masterCrewmate.getName() + message.getText() + "\n";
                                 chatbox.appendText("User: " + line + "\n");
                                 try {
                                    oos.writeObject("Chat");
                                    oos.writeObject(line);
                                    oos.flush();
                                 } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                 }

                              }

                           });

                           Button btnVote = new Button("Vote");
                           btnVote.setOnAction(new EventHandler<ActionEvent>() {

                              @Override
                              public void handle(ActionEvent event) {
                                 String line = message.getText();

                                 try {
                                    oos.writeObject("Vote");
                                    oos.flush();
                                    oos.writeObject(line);
                                    oos.flush();
                                 } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                 }
                                 btnVote.setDisable(true);
                                 
                                 
                              }
                              
                           });
                           middle.getChildren().addAll(message, btnsend,btnVote);

                           root.getChildren().addAll(chatbox, middle);
                           // create the new scene to be displayed
                           newScene = new Scene(root, 400, 300);

                           // set the scene of the new stage
                           newStage.setScene(newScene);

                           // set the title of the new stage
                           newStage.setTitle(
                                 "Chat for " + masterCrewmate.getName() + " " + masterCrewmate.getLocationOnServer());

                           // show the new stage
                           newStage.show();
                           // configure and show the new stage here
                        });
                        // create the new stage

                        break;
                     case "addmessage":

                        String message = (String) ois.readObject();
                        System.out.println(message);
                        Platform.runLater(new Runnable() {

                           @Override
                           public void run() {
                              chatbox.appendText(message);
                           }

                        });

                        break;
                     case "endmeeting":
                        PlayerPoint playerVotedOff = (PlayerPoint)ois.readObject();
                        
                        Platform.runLater(new Runnable() {

                           @Override
                           public void run() {
                              String message = playerVotedOff.getName() + "was voted off with this many votes " + playerVotedOff.getNumVotes() + " and player is Imposter:" + playerVotedOff.isImposter();
                              Alert alert = new Alert(AlertType.CONFIRMATION, message);
                              alert.setHeaderText("Player voted off");
                              alert.showAndWait();
                              newStage.close();
                           }

                        });
                        for (int i = 0; i < otherCrewmates.size(); i++) {
                           if(otherCrewmates.get(i).getLocationOnServer() == playerVotedOff.getIndex()){
                              otherCrewmates.get(i).setAlive(false);
                              System.out.println(otherCrewmates.get(i).getName() + " was voted off and killed");
                           }
                           
                        }

                        

                        break;
                     case "dead":
                        PlayerPoint deadCrewmate = (PlayerPoint)ois.readObject();
                        for (int i = 0; i < otherCrewmates.size(); i++) {
                           if(otherCrewmates.get(i).getLocationOnServer() == deadCrewmate.getIndex()){
                              System.out.println("This player has dead " + otherCrewmates.get(i).getName());
                              otherCrewmates.get(i).setAlive(false);
                              if(masterCrewmate.getLocationOnServer() == deadCrewmate.getIndex()){
                                 System.out.println("You have died");
                              }else{
                                 //this player is not killing the other player but instead on this players
                                 //screen amking them look dead 
                                 masterCrewmate.kill(otherCrewmates.get(i));
                              }
                           }
                           
                        }
                        break;
                  }

               } else if (obj instanceof PlayerPoint) {
                  PlayerPoint backgroundPos = (PlayerPoint) obj;
                  // System.out.println("Other player background pos: " + " X " + backgroundPos.getX() + " Y "
                  //             + backgroundPos.getY());
                  int index = (int) ois.readObject();
                  int numPlayers = (int) ois.readObject();
                  for (int i = 0; i < otherCrewmates.size(); i++) {
                     if (index == masterCrewmate.getLocationOnServer()) {
                        break;
                     }
                     if (index == otherCrewmates.get(i).getLocationOnServer()) {
                        int changeInX = (int) backgroundPos.getX() - masterCrewmate.getBackgroundPosX();
                        int changeInY = (int) backgroundPos.getY() - masterCrewmate.getBackgroundPosY();
                        // System.out.println("Other player background pos: " + " X " + backgroundPos.getX() + " Y "
                        //       + backgroundPos.getY());
                        // System.out.println("This player background pos: " + " X " + masterCrewmate.getBackgroundPosX()
                        //       + " Y " + masterCrewmate.getBackgroundPosY());
                        // System.out.println(otherCrewmates.get(i).getName() + "change in X coord: " + changeInX
                        //       + "change in Y coord:" + changeInY + "\n\n\n\n\n\n\n\n");
                        otherCrewmates.get(i).updateOnline(-changeInX, -changeInY);
                     }
                  }
               }
            } catch (ClassNotFoundException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

         }

      };
      reciver.scheduleAtFixedRate(recivingInfo, 10, 50);

      onlinePLayer.update(-1930, -160);

      timer = new AnimationTimer() {
         long startTime = System.nanoTime();
         int frameCount = 0;

         @Override
         public void handle(long now) {

            // Calculate elapsed time since the start of the animation loop
            long elapsedTime = now - startTime;

            // Increment frame count
            frameCount++;

            // Calculate FPS if one second has elapsed
            if (elapsedTime >= 1_000_000_000) {
               double fps = frameCount / (elapsedTime / 1_000_000_000.0);
               //System.out.println("FPS: " + fps);

               // Reset start time and frame count
               startTime = now;
               frameCount = 0;
               // delay this
               
            }

            Point2D location = moveablebackground.update(moveDown, moveUp, moveLeft, moveRight);
            masterCrewmate.setBackgroundPosX(moveablebackground.getBackgroundPosX());
            masterCrewmate.setBackgroundPosY(moveablebackground.getBackgroundPosY());
            if (moveDown || moveUp || moveLeft || moveRight) {
               // send new posistion to server

               // sending the background coordinates with its index to the server
               try {
                  if(masterCrewmate.isAlive()){
                     oos.writeObject(new PlayerPoint(masterCrewmate.getBackgroundPosX(),
                        masterCrewmate.getBackgroundPosY(), masterCrewmate.getLocationOnServer()));
                        oos.flush();
                  }
                 
                  

                  //asking for other players old locations 
                  oos.writeObject("move");
                  oos.flush();

               } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }



               // onlinePLayer.update(location.getX(), location.getY());

            }

            task1.update(location.getX(), location.getY());
            emergencyButton.update(location.getX(), location.getY());

            

            // otherCrewmates.get(0).update(location.getX(), location.getY());
            // to detect the distance between a player and a task


            //allowing one to get the report button  when they are close
            //to the emergency button or when they are close a dead crewmate
            if(!masterCrewmate.isImposter()){
               if (checkDistance(masterCrewmate, task1)) {
                  synchronized (masterCrewmate) {

                     masterCrewmate.getBtnUse().setDisable(false);
                     masterCrewmate.setToUse("Task 1");
                     // task.addHighlight();

                  }
               } else {
                  masterCrewmate.getBtnUse().setDisable(true);
               }
            }



            if (emergencyButton.distanceEmgButton(masterCrewmate) || check()) {
               synchronized (masterCrewmate) {
                  masterCrewmate.getBtnReport().setDisable(false);
                  // task.addHighlight();
               }
               masterCrewmate.setToUse("Emergency");
            }else{
               masterCrewmate.getBtnReport().setDisable(true);
               masterCrewmate.setToUse("");
            }

            
            






            if (masterCrewmate.getBtnReport().isPressed()) {
               if (masterCrewmate.getToUse().equals("Emergency")) {
                  masterCrewmate.setInMeeting(true);
                  try {
                     oos.writeObject("Meeting");
                  } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
               }
            }



            if(masterCrewmate.isImposter()){
               CrewmateRacer killable = null;
               for (int i = 0; i < otherCrewmates.size(); i++) {
                  if(masterCrewmate.getLocationOnServer() != otherCrewmates.get(i).getLocationOnServer()){
                     if(CrewmateRacer.checkDistance(masterCrewmate, otherCrewmates.get(i))){
                        masterCrewmate.getBtnKill().setDisable(false);
                        killable = otherCrewmates.get(i);
                     }else{
                        masterCrewmate.getBtnKill().setDisable(true);
                     }
                  }

                  
                  
               }

               if(masterCrewmate.getBtnKill().isPressed()){
                  masterCrewmate.kill(killable);
                  try {
                     oos.writeObject("kill");
                     oos.flush();
                     oos.writeObject(killable.getLocationOnServer());
                     oos.flush();
                  } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
                  
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
      // System.out.println("Distance between " + task.getName() + " and players
      // posistion is:" + distance + "\n\n");
      if (distance < 80) {
         return true;
      } else {
         return false;

      }
   }

   public boolean check(){
      for (int i = 0; i < otherCrewmates.size(); i++) {
         if(otherCrewmates.get(i).getLocationOnServer() != masterCrewmate.getLocationOnServer()){
            if(CrewmateRacer.checkDistance(masterCrewmate, otherCrewmates.get(i))){
               if(!otherCrewmates.get(i).isAlive()){
                  return true;
               }
            }
         }else{
            return false;
         }
      }
      return false;
   }

}

// end class Races