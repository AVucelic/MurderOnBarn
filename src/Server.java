import java.net.*;
import java.io.*;
import java.util.*;

import javax.xml.stream.Location;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.Light.Point;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

/**
 * MyMThreadServerForRecords - a class meant to act as a server for holding
 * records
 * 
 * @author Luka Lasic
 * @since 22-3-2023
 */
public class Server extends Application implements EventHandler<ActionEvent> {
   private Stage stage; // The entire window, including title bar and borders
   private Scene scene; // Interior of window
   // Choose a pane ... VBox used here
   private VBox root = new VBox(8);

   // Socket stuff
   private ArrayList<ObjectOutputStream> outputStreams = new ArrayList<>();
   // server for the socket
   private ServerSocket sSocket = null;
   public static final int SERVER_PORT = 32001;
   int clientCounter = 1;
   // holding the orders that are sent in

   private ArrayList<PlayerPoint> oldLocations = new ArrayList<>();

   // gui atr
   private Button btnStart = null;
   private Button btnConvert = null;

   private TextField ipAddress = null;
   private TextField orderCount = null;

   private TextArea info = null;

   private int numPlayers = 0;
   private int numVotes = 0;

   private int numOfImposters = 1;

   private int numOfTotalTasks = 0;
   private int numOfCompletedTasks = 0;

   /**
    * ServerThread - a server menat to allow multiple connectioins
    */
   class ServerThread extends Thread {
      // strating the server as a thread
      public void run() {
         try {
            // making the server
            sSocket = new ServerSocket(SERVER_PORT);
         } catch (IOException ioe) {
            System.out.println("IO Exception (1): " + ioe + "\n");
            return;
         }
         // continously wating for connectioin
         while (true) {
            Socket cSocket = null;
            try {
               System.out.println("Waiting client to connect...");
               // Wait for a connection
               cSocket = sSocket.accept();
               numPlayers++;
            } catch (IOException ioe) {
               System.out.println("IO Exception (2): " + ioe + "\n");
               return;
            }
            // Start client thread
            ClientThread ct = new ClientThread(cSocket, "Client" + clientCounter);
            clientCounter++;
            ct.start();
         }

      }
   }// end of ServerThread

   // client thread
   /**
    * ClientThread - meant to handle clinet request on the server
    */
   class ClientThread extends Thread {
      private Socket cSocket = null;
      private String cName = "";
      private boolean keepGoing = true;
      private int numPlayerTasksDone = 0;
      

      private int index;
      private TextField txtClientName = new TextField();
      
      private TextField txtPLayerIndex = new TextField();
      private TextField txtPlayerName = new TextField();
      private TextField txtPlayerLoc = new TextField();
      private TextField txtisImposter = new TextField("Imposter: ");
      private TextField txtPlayerNumTasksDone = new TextField("Num Tasks Done: ");



      /**
       * Constrcutor for the clieant thread
       * 
       * @param _cSocket // the socekt of the client
       * @param _name    //Name of the client
       */
      public ClientThread(Socket _cSocket, String _name) {
         txtClientName.setEditable(false);
         txtClientName.setPrefWidth(100);
         txtPLayerIndex.setEditable(false);
         txtPLayerIndex.setPrefWidth(100);
         txtPlayerName.setEditable(false);
         txtPlayerName.setPrefWidth(100);
         txtPlayerLoc.setEditable(false);
         txtPlayerLoc.setPrefWidth(100);
         txtisImposter.setEditable(false);
         txtisImposter.setPrefWidth(100);
         txtPlayerNumTasksDone.setEditable(false);
         txtPlayerNumTasksDone.setPrefWidth(100);
         this.cSocket = _cSocket;
         this.cName = _name;
         this.index = numPlayers - 1;

         VBox serverRoot = (VBox) scene.getRoot();
         VBox box = new VBox();
         txtClientName.setText(_name);
         txtPLayerIndex.setText(""+index);
         box.getChildren().addAll(txtClientName,txtPLayerIndex,txtPlayerName,txtPlayerLoc,txtisImposter,txtPlayerNumTasksDone);
         
         synchronized(outputStreams){
            Platform.runLater(() -> {
               serverRoot.getChildren().addAll(box);
            });
         }
         
         

      }

      public void run() {
         // start off with sending movement to each player
         System.out.println(this.cName + " connected");

         info.appendText(this.cName + " connected\n");

         // IO attributes
         ObjectInputStream ois = null;
         ObjectOutputStream oos = null;
         try {
            oos = new ObjectOutputStream(cSocket.getOutputStream());
            ois = new ObjectInputStream(cSocket.getInputStream());
            // Allowing the client to keep being read until it disonnects

            outputStreams.add(oos);
            oldLocations.add(new PlayerPoint(-1930, -160, index));
            while (true) {
               Object obj = null;
               try {
                  obj = ois.readObject();
               } catch (ClassNotFoundException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
               if (obj instanceof String) {
                  String commad = (String) obj;
                  switch (commad) {
                     case "sabtoge":
                        int speed = (int)ois.readObject();
                        for (int i = 0; i < outputStreams.size(); i++) {
                           outputStreams.get(i).writeObject("changeSpeed");
                           outputStreams.get(i).close();
                           outputStreams.get(i).writeObject(speed);
                           outputStreams.get(i).close();
                        }
                        break;
                     case "addTask":
                        synchronized(outputStreams){
                           numOfCompletedTasks++;
                           numPlayerTasksDone++;
                           Platform.runLater(new Runnable() {

                              @Override
                              public void run() {
                                 txtPlayerNumTasksDone.setText("" + numPlayerTasksDone);
                              }
                              
                           });
                        }

                        break;

                     case "addName":
                        try {
                           String name = (String)ois.readObject();
                           oldLocations.get(index).setName(name);
                           boolean isImposter = (boolean)ois.readObject();
                           oldLocations.get(index).setImposter(isImposter);
                           synchronized(outputStreams){
                              Platform.runLater(new Runnable() {
   
                                 @Override
                                 public void run() {
                                    txtisImposter.appendText("" + isImposter);
                                    txtPlayerName.setText(name);
                                 }
                                 
                              });
                           }
                        } catch (ClassNotFoundException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                        }
                        break;
                     // command to begin the game by sedning
                     case "Begin":
                        synchronized(outputStreams){
                           numOfTotalTasks = 3 * numPlayers;
                        }
                        oos.writeObject((Integer) index);
                        oos.flush();

                        int randImp = (int)(Math.random() * oldLocations.size()); 
                        oldLocations.get(randImp).setImposter(true);

                        ArrayList<PlayerPoint> playerPoints = new ArrayList<>(oldLocations);
                        oos.writeObject(playerPoints);
                        oos.flush();

                        
                        break;
                     case "AddToServer":
                        String name = (String)ois.readObject();
                        String imageName = (String)ois.readObject();
                        oldLocations.get(index).setName(name);
                        oldLocations.get(index).setImageName(imageName);
                        oos.writeObject((Integer) index);
                        synchronized(outputStreams){
                           Platform.runLater(new Runnable() {

                              @Override
                              public void run() {
                                 txtisImposter.appendText("" + oldLocations.get(index).isImposter());
                                 txtPlayerName.setText(name);
                              }
                              
                           });
                        }
                        oos.flush();
                        break;
                     case "move":
                        // just send array list of old locations

                        oos.writeObject("move");
                        oos.flush();
                        oos.writeObject(oldLocations.size());
                        oos.flush();
                        for (int i = 0; i < oldLocations.size(); i++) {
                           System.out.println(oldLocations.get(i).getIndex() + "X: " + oldLocations.get(i).getX() + " Y: " + oldLocations.get(i).getY());
                           oos.reset();
                           oos.writeObject(oldLocations.get(i));
                           oos.flush();
                        }
                        
                        // ArrayList<PlayerPoint> playerPoints = new ArrayList<>(oldLocations);

                        // oos.writeObject(playerPoints);
                        // oos.flush();
                        /*
                         * for (int i = 0; i < oldLocations.size(); i++) {
                         * synchronized(outputStreams){
                         * oos.writeObject(oldLocations.get(i));
                         * oos.flush();
                         * }
                         * 
                         * 
                         * }
                         */
                        /* 
                        for (int i = 0; i < oldLocations.size(); i++) {
                           System.out.println(oldLocations.get(i).getIndex() + "old X: " + oldLocations.get(i).getX() + " old Y " + oldLocations.get(i).getY());

                        }*/
                        

                        break;
                     case "Meeting":
                        numVotes = 0;
                        for (int i = 0; i < oldLocations.size(); i++) {
                           oldLocations.get(i).setNumVotes(0);
                        }
                        for (int i = 0; i < outputStreams.size(); i++) {
                           outputStreams.get(i).writeObject("meeting");
                        }
                        break;
                     case "Chat":
                        try {
                           String message = (String) ois.readObject();
                           System.out.println(message);
                           synchronized(outputStreams){
                              Platform.runLater(new Runnable() {

                                 @Override
                                 public void run() {
                                    info.appendText(message + "\n");
                                 }
                                 
                              });
                           }
                           for (int i = 0; i < outputStreams.size(); i++) {
                              outputStreams.get(i).writeObject("addmessage");
                              outputStreams.get(i).writeObject(message);
                              outputStreams.get(i).flush();
                           }
                        } catch (ClassNotFoundException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                        }
                        break;
                     case "Vote": 
                        try {
                           String playerVoted = (String)ois.readObject();
                           for (int i = 0; i < oldLocations.size(); i++) {
                              if(oldLocations.get(i).getName().equals(playerVoted)){
                                 synchronized(outputStreams){
                                    numVotes++;
                                    oldLocations.get(i).addVotes();
                                 }
                              }
                              
                           }
                           if(numVotes == oldLocations.size()){
                              PlayerPoint votedOff = new PlayerPoint(-99, -99, -99);
                              votedOff.setNumVotes(0);
                              for (int i = 0; i < oldLocations.size(); i++) {
                                 if(oldLocations.get(i).getNumVotes() > votedOff.getNumVotes()){
                                    votedOff = oldLocations.get(i);
                                 }
                              }

                              votedOff.setAlive(false);
                              for (int i = 0; i < outputStreams.size(); i++) {
                                 outputStreams.get(i).writeObject("endmeeting");
                                 outputStreams.get(i).flush();
                                 outputStreams.get(i).writeObject(votedOff);
                                 outputStreams.get(i).flush();

                              }
                           }

                           if(endGame() == 1){
                              for (int i = 0; i < outputStreams.size(); i++) {
                                 outputStreams.get(i).writeObject("impostersWin");
                                 outputStreams.get(i).flush();
                              }
                           }else if(endGame() == 0){
                              for (int i = 0; i < outputStreams.size(); i++) {
                                 outputStreams.get(i).writeObject("playersWin");
                                 outputStreams.get(i).flush();
                              }
                           }
                        } catch (ClassNotFoundException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                        }
                        break;

                     case "newplayer":
                        for (int i = 0; i < outputStreams.size(); i++) {
                           if (i == index) {
                              continue;
                           }
                           outputStreams.get(i).writeObject("newplayer");
                           outputStreams.get(i).flush();
                        }
                        break;
                     case "kill":
                        try {
                           
                           
                           int indexDead = (int)ois.readObject();

                           oldLocations.get(indexDead).setAlive(false);
                           for(int i =0; i<oldLocations.size();i++){
                                 outputStreams.get(i).writeObject("dead");
                                 outputStreams.get(i).flush();
                                 outputStreams.get(i).writeObject(oldLocations.get(indexDead));
                                 outputStreams.get(i).flush();
                              
                           }
                           String message = oldLocations.get(index).getName() + "killed " + oldLocations.get(indexDead).getName();
                           synchronized(outputStreams){
                              Platform.runLater(new Runnable() {

                                 @Override
                                 public void run() {
                                    info.appendText(message + "\n");
                                 }
                                 
                              });
                           }
                        } catch (ClassNotFoundException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                        }
                        if(endGame() == 1){
                           for (int i = 0; i < outputStreams.size(); i++) {
                              outputStreams.get(i).writeObject("impostersWin");
                              outputStreams.get(i).flush();
                           }
                        }
                        break;
                  }
               } else if (obj instanceof CrewmateRacer) {
                  CrewmateRacer self = (CrewmateRacer) obj;
                  // sending the player posistion
               } else if (obj instanceof PlayerPoint) {
                  // sending this player posistion to other players
                  // simplify code and condesne it
                  PlayerPoint locatioin = (PlayerPoint) obj;
                  synchronized(oldLocations){
                     oldLocations.get(index).setX(locatioin.getX());
                     oldLocations.get(index).setY(locatioin.getY());
                     Platform.runLater(new Runnable() {
   
                        @Override
                        public void run() {
                           txtPlayerLoc.setText("(" +locatioin.getX() +","+  locatioin.getY() +  ")");
                        }
                        
                     });

                  }
                  System.out.println(cName + "X: " + locatioin.getX() + " Y: " + locatioin.getY());
                  for (int i = 0; i < outputStreams.size(); i++) {
                     outputStreams.get(i).writeObject(locatioin);
                     outputStreams.get(i).flush();
                     outputStreams.get(i).writeObject((Integer) index);
                     outputStreams.get(i).flush();
                     outputStreams.get(i).writeObject((Integer) numPlayers);
                     outputStreams.get(i).flush();
                  }

               }
            }
         } catch (IOException e) {
            e.printStackTrace();
         } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }

      }
   }

   public static void main(String[] args) {

      launch(args);

   }

   @Override
   public void handle(ActionEvent event) {
      // getting the button that pressed
      Button btn = (Button) event.getSource();
      String btnName = btn.getText();
      switch (btnName) {
         // strating up the server
         case "Start":
            // strating the server thread and setting the text
            System.out.println("MThread started...");
            info.appendText("MThread started...\n");
            ServerThread serverThread = new ServerThread();
            serverThread.start();
            btnStart.setText("Stop");

            break;
         // when clicked converting verything in arraylist to csv file
         // stopping the server
         case "Stop":
            try {
               sSocket.close();
               btnStart.setText("Start");
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

            break;

      }
   }
   public int endGame(){
      int numPlayers = 0;
      int numImposters = 0;

      for (int i = 0; i < oldLocations.size(); i++) {
         if(oldLocations.get(i).isImposter() && oldLocations.get(i).isAlive()){
            numImposters++;
         }else if(oldLocations.get(i).isAlive()){
            numPlayers++;
         }
      }
      if(numPlayers <= numImposters){
         return 1;
      }else if(numImposters == 0){
         return 0;
      }else{
         return -1;
      }
   }

   @Override
   public void start(Stage primaryStage) {
      // making the gui
      FlowPane top = new FlowPane();
      top.setAlignment(Pos.BASELINE_RIGHT);
      btnStart = new Button("Start");

      btnStart.setOnAction(this);

      top.getChildren().addAll(btnStart);

      FlowPane middle = new FlowPane();
      Label lblIp = new Label("Server IP");
      ipAddress = new TextField("127.0.0.1");
      ipAddress.setEditable(true);
      middle.getChildren().addAll(lblIp, ipAddress);

      info = new TextArea();

      root.getChildren().addAll(top, middle, info);

      stage = primaryStage; // save stage as an attribute
      stage.setTitle("GUI Starter");

      // when the window is closed convert everything from the array list in an
      // orders.obj file
      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

         @Override
         public void handle(WindowEvent event) {
            System.exit(0);
            try {
               // closing the server sockets
               sSocket.close();
               // ending the file
               System.exit(0);
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }

      }); // set the text in the title bar

      scene = new Scene(root, 800, 500); // create scene of specified size
                                         // with given layout
      stage.setScene(scene); // associate the scene with the stage
      stage.show();
   }

} // end class
