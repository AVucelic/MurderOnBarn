import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.animation.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import javafx.scene.input.KeyEvent;

/**
 * Game2DClean - The start up for the code
 * 
 * @author Luka Lasic 
 * @author Arain Vueclic 
 * @since 25-3-2023
 * @version 1.0
 */
public class Game2DClean extends Application {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private StackPane root;


   //atr to stop multiple popups from comming up 
   private int stopRepeat = 0;


   //stage for the tasks
   private Stage taskStage = null;


   //number of taks a player needs to do 
   private int numTasks = 4;
   private int numCompletedTasks = 0;

   private static String[] args;


   //default skin for a player
   private String CREWMATE_IMAGE = "cow.png"; // file with icon for a racer for this specefic user

   private final static String CREWMATE_IMAGE2 = "duck.png";

   private final static String CREWMATE_RUNNERS = "amongusRunners.png"; // file with icon for a racer


   //mask image for reading collsion

   private final static String MASK_IMAGE = "mask2.png";

   //background iamge for the entire page
   private final static String BACKGROUND_IMAGE = "map.png";


   //the size of the window 
   private final static int WIDTH_OF_SCREEN = 800;
   private final static int HEIGHT_OF_SCREEN = 500;
  

   //this player crewmate 
   CrewmateRacer masterCrewmate = null;
   CrewmateRacer onlinePLayer = null;


   //arraylist of othercrewmates
   private ArrayList<CrewmateRacer> otherCrewmates = new ArrayList<>();


   //all th eplayer points from the server
   private ArrayList<PlayerPoint> playerPoints = null;

   // moavebale background
   MovableBackground moveablebackground = null;

   // animation timer
   AnimationTimer timer = null;
   int counter = 0;


   //movement detection atr 
   boolean moveUp = false;
   boolean moveDown = false;
   boolean moveRight = false;
   boolean moveLeft = false;

   // background /detection/collison
   Image backgroundCollison = null;
   private Collison posistion;

   //tracking which input keys are being put in
   private TrackMovement tm = new TrackMovement();


   //for the chat 
   private TextArea chatbox = new TextArea();
   private TextField message = new TextField();

   //what the players index is on the server 
   private Integer indexOfPlayrInArrayList;
   // general SOCKET attributes
   public static final int SERVER_PORT = 32001;
   private Socket socket = null;

   // IO attributes
   private ObjectInputStream ois = null;
   private ObjectOutputStream oos = null;
   private Button btnsend = null;


   //defautl scenes
   private Scene newScene = null;
   private Stage newStage = null;
   private AmongUsSettings settings = new AmongUsSettings();


   //the players name 
   private String name;

   private XML_STUFF xmlSettings = new XML_STUFF();

   //is the player an imposter 
   private boolean isImposter;

   
   /** 
    * @param _args
    */
   // main program
   public static void main(String[] _args) {
      args = _args;
      launch(args);
   }

   
   /** 
    * @param _stage
    */
   // start() method, called via launch
   public void start(Stage _stage) {

      // stage seteup
      stage = _stage;
      stage.setTitle("Among us Game");
      stage.setOnCloseRequest(
            new EventHandler<WindowEvent>() {
               public void handle(WindowEvent evt) {
                  System.exit(0);
               }
            });
      //strating screen 
      menuSreen();
      // root pane
      // initializeScene();
   }

   // a method that is wehre the player begins and connects to the server
   /*
    * menuSreen - a method to create a start up screen with a menu
    */
   private void menuSreen() {
      FlowPane root = new FlowPane();
      //button to strat the intial connection to the server and ask for the players name
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

            tid.setContentText("Name you want");
            tid.setHeaderText("Your Name:");
            // getting the command the user want to execture
            Optional<String> valueNext = tid.showAndWait();
            name = valueNext.get();

            // Make a connection with the server
            try {
               socket = new Socket(ipAddress, SERVER_PORT);
               ois = new ObjectInputStream(socket.getInputStream());
               oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
            //puts the players into a waiting lobby waiting for other players
            waitingLobbby();

         }

      });


      //chagning the charcerts deafult player 
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
      
      //chagning the deafult settings 
      Button btnSettings = new Button("Settings");
      btnSettings.setOnAction(new EventHandler<ActionEvent>() {

         @Override
         public void handle(ActionEvent event) {
            Stage settingsStage = new Stage();
            VBox settingsRoot = new VBox();
            xmlSettings.readXML();
            Label lblPortNum = new Label("Port Number:");
            TextField txtIpNum = new TextField("" + xmlSettings.player.getIpPORT());

            Label lblKillCoolDown = new Label("KillCoolDown: ");
            TextField txtKillCoolDown = new TextField("" + xmlSettings.player.getKillCoolDown());

            Label lblKillDis = new Label("KillDistance: ");
            TextField txtKillDistance = new TextField("" + xmlSettings.player.getKillDistance());

            Label lblSpeed = new Label("Speed:");
            TextField txtSpeed = new TextField("" + xmlSettings.player.getPlayerSpeed());

            Label lblIpAddres = new Label("Ip Address:");
            TextField txtIpAddress = new TextField("IP" + xmlSettings.player.getServerIP());

            Button btnSave = new Button("Save Changes");
            settingsRoot.getChildren().addAll(lblPortNum, txtIpNum, lblKillCoolDown, txtKillCoolDown, lblKillDis,
                  txtKillDistance, lblSpeed, txtSpeed, lblIpAddres, txtIpAddress,btnSave);

            

            btnSave.setOnAction(new EventHandler<ActionEvent>() {

               @Override
               public void handle(ActionEvent event) {
                  int ipPort = Integer.parseInt(txtIpNum.getText().trim());
                  int killCool = Integer.parseInt(txtKillCoolDown.getText().trim());
                  int killDis = Integer.parseInt(txtKillDistance.getText().trim());
                  int speed = Integer.parseInt(txtSpeed.getText().trim());
                  String ipAddress = txtIpAddress.getText().trim();
                  xmlSettings.player.setIpPORT(ipPort);
                  xmlSettings.player.setKillCoolDown(killCool);
                  xmlSettings.player.setKillDistance(killDis);
                  xmlSettings.player.setPlayerSpeed(speed);
                  xmlSettings.player.setServerIP(ipAddress);
                  settingsStage.close();
               }

            });

            Scene settingsScene = new Scene(settingsRoot, 400, 300);
            settingsStage.setScene(settingsScene);
            settingsStage.show();
         }

      });

      VBox top = new VBox();
      top.getChildren().addAll(btnStart, changeCharacter, btnSettings);
      top.setAlignment(Pos.CENTER);

      root.getChildren().addAll(top);
      root.setAlignment(Pos.CENTER);

      // display the window
      scene = new Scene(root, 800, 500);
      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();

   }

   // connection lobby for all players
   private void waitingLobbby() {
      // making the current player
      // moveablebackground = new MovableBackground(MASK_IMAGE, BACKGROUND_IMAGE);
      // masterCrewmate = new CrewmateRacer(true, CREWMATE_IMAGE, moveablebackground);

      int playerIndex = 0;
      //putting the player on the server 
      try {
         // adding the player to the arraylist on the server
         oos.writeObject("AddToServer");
         oos.flush();
         oos.writeObject(name);
         oos.flush();
         oos.writeObject(CREWMATE_IMAGE);
         oos.flush();

         playerIndex = (int) ois.readObject();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      FlowPane root = new FlowPane();

      Button btnStart = new Button("Start Game");
      // starting the actaul game
      btnStart.setOnAction(new EventHandler<ActionEvent>() {
         
         @Override
         public void handle(ActionEvent event) {
            //strating the game 
            try {
               oos.writeObject("Begin");
               indexOfPlayrInArrayList = (Integer) ois.readObject();
               playerPoints = (ArrayList<PlayerPoint>) ois.readObject();
               for (int i = 0; i < playerPoints.size(); i++) {
                  if (playerPoints.get(i).getIndex() == indexOfPlayrInArrayList && playerPoints.get(i).isImposter()) {
                     isImposter = true;
                  }

               }
               //making the game start 
               initializeScene();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            } catch (ClassNotFoundException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
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

   // start the game scene
   /*
    * initializeScene() -  method to start up the game 
    */
   public void initializeScene() {

      // try {
      // socket = new Socket("127.0.0.1", SERVER_PORT);
      // ois = new ObjectInputStream(socket.getInputStream());
      // oos = new ObjectOutputStream(socket.getOutputStream());
      // } catch (IOException e) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }

      // try {
      // oos.writeObject("Begin");
      // indexOfPlayrInArrayList = (Integer) ois.readObject();
      // } catch (ClassNotFoundException e) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // } catch (IOException e) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }

      //creating a palce for all the elements 
      root = new StackPane();
      posistion = new Collison();

      // creating a player for a differnt plaeyr to connect to

      // add to the root
      //creating a movable background 
      moveablebackground = new MovableBackground(MASK_IMAGE, BACKGROUND_IMAGE);

      this.root.getChildren().add(moveablebackground);

      // Adding an interactable object
      Interactable task1 = new Interactable("amongus.png");
      task1.setStupidX(400);
      task1.setStupidY(200);
      Interactable task2 = new Interactable("amongus.png");
      task2.moveInteractable(-750, -100);
      task2.setStupidX(1505);
      task2.setStupidY(555);
      Interactable task3 = new Interactable("amongus.png");
      task3.setStupidX(-845);
      task3.setStupidY(565);
      task3.moveInteractable(1600, -100);

      //making an emergency button 
      EmergencyButton emergencyButton = new EmergencyButton("emg.png");
      Interactable task4 = new Interactable("amongus.png");
      task4.setStupidX(739);
      task4.setStupidY(-339);
      task4.moveInteractable(0, 800);
      this.root.getChildren().addAll(task1, task2, task3, task4, emergencyButton);

      // if (indexOfPlayrInArrayList == 0) {
      // masterCrewmate = new CrewmateRacer(true, CREWMATE_IMAGE, moveablebackground);

      // masterCrewmate.setLocationOnServer(indexOfPlayrInArrayList);
      // onlinePLayer = new CrewmateRacer(false, CREWMATE_IMAGE2);
      // onlinePLayer.setLocationOnServer(1);
      // } else if (indexOfPlayrInArrayList == 1) {
      // masterCrewmate = new CrewmateRacer(false,CREWMATE_IMAGE2 ,
      // moveablebackground);
      // masterCrewmate.setLocationOnServer(indexOfPlayrInArrayList);
      // onlinePLayer = new CrewmateRacer(true, CREWMATE_IMAGE);
      // onlinePLayer.setLocationOnServer(0);
      // }

      // try {
      // //also telling server if you are imposter or not
      // oos.writeObject("addName");
      // oos.flush();
      // oos.writeObject(masterCrewmate.getName());
      // oos.flush();
      // oos.writeObject(masterCrewmate.isImposter());
      // oos.flush();
      // } catch (IOException e) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }


      //making the master crewmate for this player
      masterCrewmate = new CrewmateRacer(isImposter, CREWMATE_IMAGE,moveablebackground);
      // moveablebackground);
      // masterCrewmate = new CrewmateRacer(false, CREWMATE_IMAGE, moveablebackground);



      masterCrewmate.setLocationOnServer(indexOfPlayrInArrayList);
      masterCrewmate.setName(name);
      otherCrewmates.add(masterCrewmate);



      //Adding all the player from the server to this players game 
      for (int i = 0; i < playerPoints.size(); i++) {
         if (playerPoints.get(i).getIndex() != indexOfPlayrInArrayList) {
            CrewmateRacer temp = new CrewmateRacer(playerPoints.get(i).isImposter(),
                  playerPoints.get(i).getImageName());
            temp.setName(playerPoints.get(i).getName());
            temp.setLocationOnServer(playerPoints.get(i).getIndex());
            otherCrewmates.add(temp);
            this.root.getChildren().addAll(temp);

         }

      }

      // otherCrewmates.add(onlinePLayer);
      // this.root.getChildren().addAll(onlinePLayer);

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

      // updating the posistion of player each frame

      // This is the thread for other player movement and updating their psositons

      // putting all the other players on the strating posistion
      for (int i = 0; i < otherCrewmates.size(); i++) {
         if (otherCrewmates.get(i).getLocationOnServer() != indexOfPlayrInArrayList) {
            otherCrewmates.get(i).update(-1930, -160);
         }

      }
      // recveing thread from server 
      Timer reciver = new Timer();
      TimerTask recivingInfo = new TimerTask() {

         @Override
         public void run() {
            try {
               //always wating for a message from the server 
               Object obj = ois.readObject();
               if (obj instanceof String) {
                  //getting a command from the server
                  String command = (String) obj;
               
                  switch (command) {
                     //when an imposter sabtoage everyone else change the speed
                     case "changeSpeed":
                        synchronized(playerPoints){
                           int speed = (int) ois.readObject();
                           moveablebackground.setSpeed(speed);
                        }
                        
                        break;
                     case "newplayer":
                        break;
                     case "annoyingPop":
                        if(!masterCrewmate.isImposter()){
                           
                              
                              Platform.runLater(new Runnable() {

                                 @Override
                                 public void run() {
                                    Alert annoyingPop = new Alert(AlertType.WARNING, "Shut Down the Game or Imposters Win");
                                    annoyingPop.setHeaderText("Imposter Prank");
                                    annoyingPop.showAndWait();
                                 }
                                 
                              });
                              
                                 
                              
                           
                        }
                        break;
                     //method to update other plyaers posstion based on their old positions 
                     case "move":

                        int size = (int) ois.readObject();
                        ArrayList<PlayerPoint> oldLocations = new ArrayList<>();

                        //recving the list of player points from the server 
                        for (int i = 0; i < size; i++) {
                           PlayerPoint player = (PlayerPoint) ois.readObject();
                           oldLocations.add(player);
                        }
                        // ArrayList<PlayerPoint> oldLocations = (ArrayList<PlayerPoint>)
                        // ois.readObject();


                        //looking through the old locations and updating other players movements 
                        for (int i = 0; i < oldLocations.size(); i++) {
                           System.out.println(oldLocations.get(i).getIndex() + " old X: " + oldLocations.get(i).getX()
                                 + " old Y " + oldLocations.get(i).getY());

                           int index = oldLocations.get(i).getIndex();
                           // System.out.println("Mastercrewmate INdex:
                           // "+masterCrewmate.getLocationOnServer());
                           
                           //looking through the crewmates to correctly change the players posistion
                           for (int y = 0; y < otherCrewmates.size(); y++) {
                              // System.out.println(otherCrewmates.get(y).getLocationOnServer());
                              // System.out.println(index);
                              if (otherCrewmates.get(y).getLocationOnServer() == index &&
                                    otherCrewmates.get(y).getLocationOnServer() != masterCrewmate
                                          .getLocationOnServer()) {
                                 // System.out.println("THe index that is modfied: " + index);
                                 int changeInX = (int) oldLocations.get(i).getX() - masterCrewmate.getBackgroundPosX();
                                 int changeInY = (int) oldLocations.get(i).getY() - masterCrewmate.getBackgroundPosY();
                                 otherCrewmates.get(y).updateOnline(-changeInX, -changeInY);
                                 otherCrewmates.get(y).update(oldLocations.get(i).getX(), oldLocations.get(i).getY());
                                 // System.out.println("Chnage other crewmates posistion");
                              }
                           }

                        }
                        break;
                     //calling a meeting if a dead body is reported or emergency button is pressed
                     case "meeting":
                        if (stopRepeat == 0) {
                           //puttig player in the right posistion 
                           masterCrewmate.meeting();
                           

                           //making a chat popup to send informatioin between players
                           Platform.runLater(() -> {
                              chatbox.setText("");
                              message.setText("");
                              newStage = new Stage();
                              VBox root = new VBox(10);

                              FlowPane middle = new FlowPane();
                              btnsend = new Button("Send");
                              //button to send a message to others 
                              btnsend.setOnAction(new EventHandler<ActionEvent>() {

                                 @Override
                                 public void handle(ActionEvent event) {
                                    String line = masterCrewmate.getName() + message.getText() + "\n";
                                    // chatbox.appendText("User: " + line + "\n");
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
                              //button to vote for a player
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
                              middle.getChildren().addAll(message, btnsend, btnVote);

                              root.getChildren().addAll(chatbox, middle);
                              // create the new scene to be displayed
                              newScene = new Scene(root, 400, 300);

                              // set the scene of the new stage
                              newStage.setScene(newScene);

                              // set the title of the new stage
                              newStage.setTitle(
                                    "Chat for " + masterCrewmate.getName() + " "
                                          + masterCrewmate.getLocationOnServer());

                              // show the new stage
                              newStage.show();
                              stopRepeat++;
                              // configure and show the new stage here
                           });
                           // create the new stage
                        }
                        break;
                     //getting the message from the server from other players
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
                     //when all players voted the meeting will end 
                     case "endmeeting":
                        stopRepeat = 0;
                        //getting the player that was voted off
                        PlayerPoint playerVotedOff = (PlayerPoint) ois.readObject();

                        Platform.runLater(new Runnable() {

                           @Override
                           public void run() {
                              String message = playerVotedOff.getName() + "was voted off with this many votes "
                                    + playerVotedOff.getNumVotes() + " and player is Imposter:"
                                    + playerVotedOff.isImposter();
                              Alert alert = new Alert(AlertType.CONFIRMATION, message);
                              alert.setHeaderText("Player voted off");
                              alert.showAndWait();
                              newStage.close();
                           }

                        });
                        //Finding the player that matches the index of the player that was voted off 
                        for (int i = 0; i < otherCrewmates.size(); i++) {
                           if (otherCrewmates.get(i).getLocationOnServer() == playerVotedOff.getIndex()) {
                              otherCrewmates.get(i).setAlive(false);
                              System.out.println(otherCrewmates.get(i).getName() + " was voted off and killed");
                              if(otherCrewmates.get(i).getLocationOnServer() == masterCrewmate.getLocationOnServer()){
                                 masterCrewmate.getaPicView().setOpacity(0.5);
                              }else{
                                 masterCrewmate.kill(otherCrewmates.get(i));
                              }
                              
                              
                           }

                        }

                        break;
                     //a player is killed by an imposter and the message is sent to all other players
                     //making the player killed on the this players game 
                     case "dead":
                        PlayerPoint deadCrewmate = (PlayerPoint) ois.readObject();
                        for (int i = 0; i < otherCrewmates.size(); i++) {
                           if (otherCrewmates.get(i).getLocationOnServer() == deadCrewmate.getIndex()) {
                              
                              System.out.println("This player has dead " + otherCrewmates.get(i).getName());
                              otherCrewmates.get(i).setAlive(false);

                              if (masterCrewmate.getLocationOnServer() == deadCrewmate.getIndex()) {
                                 System.out.println("You have died");
                                 masterCrewmate.getaPicView().setOpacity(0.5);
                              } else {
                                 // this player is not killing the other player but instead on this players
                                 // screen amking them look dead
                                 masterCrewmate.kill(otherCrewmates.get(i));
                              }
                           }

                        }
                        Alert alert = new Alert(AlertType.INFORMATION, "This player has dead " + deadCrewmate.getName());
                        alert.setHeaderText("Somone has Dead");
                        alert.showAndWait();
                        break;
                     //a end case for when the imposters win
                     case "impostersWin":
                        Platform.runLater(new Runnable() {

                           @Override
                           public void run() {
                              Alert alert = new Alert(AlertType.INFORMATION, "The imposters win");
                              alert.showAndWait();

                           }

                        });

                        break;
                     //a end case if the players win
                     case "playersWin":
                        Platform.runLater(new Runnable() {
                           @Override
                           public void run() {
                              Alert alert = new Alert(AlertType.INFORMATION, "The players win");
                              alert.showAndWait();
                           }

                        });
                        break;
                  }
               
                  //getting othe players movement so they move on the current players screen 
               } else if (obj instanceof PlayerPoint) {
                  PlayerPoint backgroundPos = (PlayerPoint) obj;
                  // System.out.println("Other player background pos: " + " X " +
                  // backgroundPos.getX() + " Y "
                  // + backgroundPos.getY());
                  int index = (int) ois.readObject();
                  int numPlayers = (int) ois.readObject();
                  for (int i = 0; i < otherCrewmates.size(); i++) {
                     if (index == masterCrewmate.getLocationOnServer()) {
                        break;
                     }
                     if (index == otherCrewmates.get(i).getLocationOnServer()) {
                        int changeInX = (int) backgroundPos.getX() - masterCrewmate.getBackgroundPosX();
                        int changeInY = (int) backgroundPos.getY() - masterCrewmate.getBackgroundPosY();
                        // System.out.println("Other player background pos: " + " X " +
                        // backgroundPos.getX() + " Y "
                        // + backgroundPos.getY());
                        // System.out.println("This player background pos: " + " X " +
                        // masterCrewmate.getBackgroundPosX()
                        // + " Y " + masterCrewmate.getBackgroundPosY());
                        // System.out.println(otherCrewmates.get(i).getName() + "change in X coord: " +
                        // changeInX
                        // + "change in Y coord:" + changeInY + "\n\n\n\n\n\n\n\n");
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
      reciver.scheduleAtFixedRate(recivingInfo, 10, 10);


      //handleing the enitre normal running of the game 
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
               // System.out.println("FPS: " + fps);

               // Reset start time and frame count
               startTime = now;
               frameCount = 0;

            }

            //updating the player posisiton and detetecting collison 
            Point2D location = moveablebackground.update(moveDown, moveUp, moveLeft, moveRight);
            //settiing the background pos for the main player
            masterCrewmate.setBackgroundPosX(moveablebackground.getBackgroundPosX());
            masterCrewmate.setBackgroundPosY(moveablebackground.getBackgroundPosY());
            if (moveDown || moveUp || moveLeft || moveRight) {

               // send new posistion to server
               masterCrewmate.update(location.getX(), location.getY());
               // sending the background coordinates with its index to the server
               try {
                  if (masterCrewmate.isAlive()) {
                     oos.writeObject(new PlayerPoint(masterCrewmate.getBackgroundPosX(),
                           masterCrewmate.getBackgroundPosY(), masterCrewmate.getLocationOnServer()));
                     oos.flush();
                  }

                  // asking for other players old locations
                  oos.writeObject("move");
                  oos.flush();

               } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }

               // onlinePLayer.update(location.getX(), location.getY());

            }

            //moving tasks and buttons to stay in the same place
            task1.update(location.getX(), location.getY());
            task2.update(location.getX(), location.getY());
            task3.update(location.getX(), location.getY());
            task4.update(location.getX(), location.getY());

            emergencyButton.update(location.getX(), location.getY());

            // otherCrewmates.get(0).update(location.getX(), location.getY());
            // to detect the distance between a player and a task

            // allowing one to get the report button when they are close
            // to the emergency button or when they are close a dead crewmate

            // for normal crewmates tasks
            if (!masterCrewmate.isImposter()) {
               if (checkDistance(masterCrewmate, task1)) {
                  synchronized (masterCrewmate) {

                     masterCrewmate.getBtnUse().setDisable(false);
                     masterCrewmate.setToUse("Task 1");
                     // task.addHighlight();

                  }
               } else if (checkDistance(masterCrewmate, task2)) {
                  synchronized (masterCrewmate) {

                     masterCrewmate.getBtnUse().setDisable(false);
                     masterCrewmate.setToUse("Task 2");
                     // task.addHighlight();

                  }

               } else if (checkDistance(masterCrewmate, task3)) {
                  synchronized (masterCrewmate) {

                     masterCrewmate.getBtnUse().setDisable(false);
                     masterCrewmate.setToUse("Task 3");
                     // task.addHighlight();

                  }

               } else if (checkDistance(masterCrewmate, task4)) {
                  synchronized (masterCrewmate) {

                     masterCrewmate.getBtnUse().setDisable(false);
                     masterCrewmate.setToUse("Task 4");
                     // task.addHighlight();

                  }

               } else {
                  masterCrewmate.getBtnUse().setDisable(true);
               }


               //when the button i avalibale to be pusehd and the player
               //is the near a task they can then activate that task 
               if (masterCrewmate.getBtnUse().isPressed()) {

                  taskStage = new Stage();

                  if (masterCrewmate.getToUse().equals("Task 1")) {
                     TaskWires task = new TaskWires();
                     taskStage.setOnHiding(new EventHandler<WindowEvent>() {

                        @Override
                        public void handle(WindowEvent event) {
                           if (task.isCompleted()) {
                              try {
                                 //adding to a counter on the server 
                                 oos.writeObject("addTask");
                                 oos.flush();
                              } catch (IOException e) {
                                 // TODO Auto-generated catch block
                                 e.printStackTrace();
                              }
                              numCompletedTasks++;
                              double total = (double) numCompletedTasks / numTasks;
                              Platform.runLater(new Runnable() {

                                 @Override
                                 public void run() {
                                    masterCrewmate.getProgressBar().setProgress(total);
                                 }

                              });
                           }
                           stopRepeat = 0;
                        }

                     });
                     try {
                        if (stopRepeat == 0) {
                           task.start(taskStage);
                           stopRepeat++;
                        }

                     } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }
                  } else if (masterCrewmate.getToUse().equals("Task 2")) {
                     TaskDownload task = new TaskDownload();
                     taskStage.setOnHiding(new EventHandler<WindowEvent>() {

                        @Override
                        public void handle(WindowEvent event) {
                           if (task.isCompleted()) {
                              try {
                                 oos.writeObject("addTask");
                                 oos.flush();
                              } catch (IOException e) {
                                 // TODO Auto-generated catch block
                                 e.printStackTrace();
                              }
                              numCompletedTasks++;
                              double total = (double) numCompletedTasks / numTasks;
                              Platform.runLater(new Runnable() {

                                 @Override
                                 public void run() {
                                    masterCrewmate.getProgressBar().setProgress(total);
                                 }

                              });
                           }
                           stopRepeat = 0;
                        }

                     });
                     try {
                        if (stopRepeat == 0) {
                           task.start(taskStage);
                           stopRepeat++;
                        }

                     } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }
                  } else if (masterCrewmate.getToUse().equals("Task 3")) {
                     TaskSum task = new TaskSum();
                     taskStage.setOnHiding(new EventHandler<WindowEvent>() {

                        @Override
                        public void handle(WindowEvent event) {
                           if (task.isCompleted()) {
                              try {
                                 oos.writeObject("addTask");
                                 oos.flush();
                              } catch (IOException e) {
                                 // TODO Auto-generated catch block
                                 e.printStackTrace();
                              }
                              numCompletedTasks++;
                              double total = (double) numCompletedTasks / numTasks;
                              Platform.runLater(new Runnable() {

                                 @Override
                                 public void run() {
                                    masterCrewmate.getProgressBar().setProgress(total);
                                 }

                              });
                           }
                           stopRepeat = 0;
                        }

                     });
                     try {
                        if (stopRepeat == 0) {
                           task.start(taskStage);
                           stopRepeat++;
                        }
                     } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }

                  } else if (masterCrewmate.getToUse().equals("Task 4")) {
                     SwipeCardTask task = new SwipeCardTask();
                     taskStage.setOnHiding(new EventHandler<WindowEvent>() {

                        @Override
                        public void handle(WindowEvent event) {
                           if (task.isCompleted()) {
                              try {
                                 oos.writeObject("addTask");
                                 oos.flush();
                              } catch (IOException e) {
                                 // TODO Auto-generated catch block
                                 e.printStackTrace();
                              }
                              numCompletedTasks++;
                              double total = (double) numCompletedTasks / numTasks;
                              Platform.runLater(new Runnable() {

                                 @Override
                                 public void run() {
                                    masterCrewmate.getProgressBar().setProgress(total);
                                 }

                              });
                           }
                           stopRepeat = 0;
                        }

                     });
                     try {
                        if (stopRepeat == 0) {
                           task.start(taskStage);
                           stopRepeat++;
                        }
                     } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                     }

                  }

               }
            }

            // for reproting a dead body or emergency button
            if (emergencyButton.distanceEmgButton(masterCrewmate) || check()) {
               synchronized (masterCrewmate) {
                  masterCrewmate.getBtnReport().setDisable(false);
                  // task.addHighlight();
               }
               masterCrewmate.setToUse("Emergency");
            } else {
               masterCrewmate.getBtnReport().setDisable(true);
            }


            //reporting a creating a metting where a player could be voted off
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


            //only for imposters
            //code to kill other players 
            if (masterCrewmate.isImposter()) {
               CrewmateRacer killable = null;
               for (int i = 0; i < otherCrewmates.size(); i++) {
                  if (masterCrewmate.getLocationOnServer() != otherCrewmates.get(i).getLocationOnServer()) {
                     if (CrewmateRacer.checkDistance(masterCrewmate, otherCrewmates.get(i))) {
                        masterCrewmate.getBtnKill().setDisable(false);
                        killable = otherCrewmates.get(i);
                     } else {
                        masterCrewmate.getBtnKill().setDisable(true);
                     }
                  }

               }
               //killing a differnt player and reporting that to the server 
               if (masterCrewmate.getBtnKill().isPressed()) {
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
               if(masterCrewmate.getBtnSabatoage2().isPressed()){
                  try {
                     oos.writeObject("sabtoge2");
                     oos.flush();
                  } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
                  
               }

               //the imposter is always able to press the the sabtaoge button in order to limit player speed 
               if (masterCrewmate.getBtnSabatoage().isPressed()) {
                  try {
                     oos.writeObject("sabtoge");
                     oos.flush();
                     oos.writeObject(moveablebackground.getSpeed() / 2);
                     oos.flush();
                     Timer slow = new Timer();
                     TimerTask slowLimit = new TimerTask() {

                        @Override
                        public void run() {
                           try {
                              oos.writeObject("sabtoge");
                              oos.flush();
                              oos.writeObject(moveablebackground.getSpeed() * 2);
                              oos.flush();
                           } catch (IOException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                           }

                        }

                     };
                     slow.schedule(slowLimit, 20000);

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

   
   /** 
    * checkDistance checking distance between two game objects 
    * @param crewmate
    * @param task
    * @return boolean
    */
   // method to activate if the player is close enough to a task
   public boolean checkDistance(CrewmateRacer crewmate, Interactable task) {
      double xDis = 0;
      double yDis = 0;
      synchronized (masterCrewmate) {
         xDis = Math.pow(crewmate.getRacerPosX() - task.getStupidX(), 2);
         yDis = Math.pow(crewmate.getRacerPosY() - task.getStupidY(), 2);
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

   
   /** 
    * check - seeing if other crewmates on the server are alive for player to kill 
    * @return boolean
    */
   public boolean check() {
      for (int i = 0; i < otherCrewmates.size(); i++) {
         if (otherCrewmates.get(i).getLocationOnServer() != masterCrewmate.getLocationOnServer()) {
            if (CrewmateRacer.checkDistance(masterCrewmate, otherCrewmates.get(i))) {
               if (!otherCrewmates.get(i).isAlive()) {
                  return true;
               }
            }
         } else {
            return false;
         }
      }
      return false;
   }

}

// end class Races