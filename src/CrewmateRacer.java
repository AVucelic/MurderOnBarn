import java.io.Serializable;

import javax.swing.Action;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


/**
 * CrewmateRacer - a class to make players for the game
 * @author Luka Lasic
 * @since 20-4-2023
 * 
 * 
 * javadoc -d  /Users/luka/Desktop/Computer_Science/Programming_2/Among_US_Project_CLEAN_UP/src -sourcepath src -subpackages .
 */
public class CrewmateRacer extends Pane implements EventHandler<ActionEvent>,Serializable {

   // this is the player postion based on its top left image
   private int racerPosX = 0;
   private int racerPosY = 0;



   private int racerPosXOnline = 0;
   private int racerPosYOnline = 0;


   private int locationOnServer;

   private boolean inMeeting;
   private ProgressBar progressBar = null;

   private ImageView aPicView = null;
   private boolean isImposter;
   private final static int WIDTH_OF_SCREEN = 800;
   private final static int HEIGHT_OF_SCREEN = 500;

   private final int STARTING_X = 400;
   private final int STARTING_Y = 250;

   private Button btnReport;
   private Button btnUse;
   private TextArea tasks;

   private Button btnKill;
   private Button btnSabatoage;

   private String toUse = "";

   private MovableBackground movement;

   private String name;

   private int backgroundPosX;
   private int backgroundPosY;


   private boolean alive;


   /**
    * CrewmateRacer - making a crewmate racer that acts as a holder for online players
    * @param isImposter
    * @param crewmateImage
    */
   public CrewmateRacer(boolean isImposter,String crewmateImage){
      //this.name = crewmateImage;
      this.isImposter = isImposter;
      racerPosX = 400;
      racerPosY = 250;

      
      aPicView = new ImageView(crewmateImage);
      aPicView.setFitWidth(50);
      aPicView.setPreserveRatio(true);
      aPicView.setTranslateX(racerPosX);
      aPicView.setTranslateY(racerPosY);

      this.getChildren().addAll(aPicView);
      this.alive =true;
      
      

   }
   /**
    * Consturcotr to use for tests 
    * @param name
    * @param isImposter
    */
   public CrewmateRacer(String name , boolean isImposter){
      this.name = name;
      this.isImposter = isImposter;
      racerPosX = 400;
      racerPosY = 250;
      this.alive = true;
   }
   /**
    * Consturcotr for the main player to make the player gui 
    * @param isImposter
    * @param crewmateImage
    * @param move
    */
   public CrewmateRacer(boolean isImposter, String crewmateImage,MovableBackground move) {
      this.alive =true;

      //this.name = crewmateImage;
      this.movement = move;
      this.isImposter = isImposter;

      //making the gui based on the image given 
      aPicView = new ImageView(crewmateImage);
      aPicView.setFitWidth(50);
      aPicView.setPreserveRatio(true);
      racerPosX = WIDTH_OF_SCREEN / 2 - 25;
      racerPosY = HEIGHT_OF_SCREEN / 2 - 25;
      aPicView.setTranslateX(racerPosX);
      aPicView.setTranslateY(racerPosY);
      StackPane layout = new StackPane();
      this.btnReport = new Button("Report");

      FlowPane btnsForUser = new FlowPane();

      btnsForUser.setPrefWidth(WIDTH_OF_SCREEN);
      btnsForUser.setPrefHeight(HEIGHT_OF_SCREEN);

      btnsForUser.setAlignment(Pos.BOTTOM_RIGHT);
      btnReport.setDisable(true);
      btnReport.setFocusTraversable(false);

      //button to report a dead body 
      btnReport.setOnAction(new EventHandler<ActionEvent>() {

         @Override
         public void handle(ActionEvent event) {
            //System.out.println("Reproting a dead body");
            meeting();
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }

      });


      //making the gui based on iff the player is an imposter or not 
      if (isImposter) {
         this.btnKill = new Button("Kill");
         this.btnSabatoage = new Button("Sabtaoge");
         this.btnKill.setDisable(false);
         btnKill.setFocusTraversable(false);
         btnSabatoage.setFocusTraversable(false);
         btnsForUser.getChildren().addAll(btnReport, btnKill, btnSabatoage);
      } else {
         // aPicView = new ImageView(CREWMATE_RUNNERS);
         this.btnUse = new Button("Use");
         this.btnUse.setOnAction(this);
         this.btnUse.setDisable(true);
         btnUse.setFocusTraversable(false);
         btnsForUser.getChildren().addAll(btnReport, btnUse);
      }

      this.tasks = new TextArea();
      tasks.appendText("Task Wires\n");
      tasks.appendText("Task Click\n");
      tasks.appendText("Task Download\n");
      tasks.setPrefWidth(100);
      tasks.setPrefHeight(50);
      tasks.setDisable(true);
      FlowPane tasksArea = new FlowPane();
      tasksArea.setPrefWidth(WIDTH_OF_SCREEN);
      tasksArea.setPrefHeight(HEIGHT_OF_SCREEN);
      this.progressBar = new ProgressBar();

      tasksArea.getChildren().addAll(progressBar,tasks);
      tasksArea.setAlignment(Pos.TOP_LEFT);

      layout.getChildren().addAll(tasksArea, btnsForUser);
      // this.root.getChildren().addAll(robotCrewmates);

      this.getChildren().add(aPicView);
      this.getChildren().addAll(layout);
   }




   
   /** 
    * update - a method to upadte the players racerpos based on their background coordiantes for their moveable 
    * and making all other players exist on the same axis 
    * @param x
    * @param y
    */
   public void update(double x,double y){
      //need to alter the update statment in order correcly adjust were the task will be 
      //need to have add to it so it isnt just palce on the top left corner of the background movement 
      
      
      
      
      
      
      
      racerPosX = (int)x + 1930 + STARTING_X - 25;
      racerPosY = (int)y + 160 + STARTING_Y - 25;
      //System.out.println("Background " +name + " X Cord " + xForInter);
      //System.out.println("Background " + name + " Y Cord " + yForInter);
      // aPicView.setTranslateX(racerPosX);
      // aPicView.setTranslateY(racerPosY);

      //System.out.println("Moving other player with main player pos: " + " X " + racerPosX  +" Y "+ racerPosY);

      
  }
  
  /** 
   * updateOnline -  A way to change the players postion from only online players
   * @param x
   * @param y
   */
  public void updateOnline(double x,double y){


      int xForInter = (int)x + STARTING_X -25;
      int yForInter = (int)y + STARTING_Y -25;
      
      
      //System.out.println("Other player moved by server pos: " + " X " + xForInter  +" Y "+ yForInter);

      aPicView.setTranslateX(xForInter);
      aPicView.setTranslateY(yForInter);

  }



   
   /** 
    * checkDistance - a way to check the distnae between a player and a task 
    * @param crewmate
    * @param task
    * @return boolean
    */
   //for the main player
 
   public static boolean checkDistance(CrewmateRacer crewmate, Interactable task) {
      double xDis = 0;
      double yDis = 0;
      synchronized (crewmate) {
         xDis = Math.pow(crewmate.getRacerPosX() - task.getPosXBasedOnBack(), 2);
         yDis = Math.pow(crewmate.getRacerPosY() - task.getPosYBasedOnBack(), 2);
      }
      double distance = Math.sqrt(xDis + yDis);
      //System.out.println("Distance between " + task.getName() + " and players posistion is:" + distance + "\n\n");
      if (distance < 40) {
         return true;
      } else {
         return false;

      }
   }

   
   /** 
    * checkDistance - a way to check the distance between two players
    * @param crewmate
    * @param other
    * @return boolean
    */
   public static boolean checkDistance(CrewmateRacer crewmate, CrewmateRacer other) {
      double xDis = 0;
      double yDis = 0;
      synchronized (crewmate) {
         xDis = Math.pow(crewmate.getRacerPosX() - other.getRacerPosX(), 2);
         yDis = Math.pow(crewmate.getRacerPosY() - other.getRacerPosY(), 2);
      }
      double distance = Math.sqrt(xDis + yDis);
      //System.out.println("Distance between " +  "Other crew mate to kill" + " and players posistion is:" + distance + "\n\n");
      if (distance < 80) {
         return true;
      } else {
         return false;

      }
   }




   
   /** 
    * @param event
    */
   @Override
   public void handle(ActionEvent event) {
      // System.out.println("DDDDD");
      // try {
      //    Thread.sleep(5000);
      // } catch (InterruptedException e) {
      //    // TODO Auto-generated catch block
      //    e.printStackTrace();
      // }
      Object obj = event.getSource();
      if (obj instanceof Button) {
         switch (toUse) {
            case "Emergency":
               System.out.println("THIS WORKS THANKS ALLAH\n\n\n\n\n\n\n\n\n");
               System.out.println("Emergency Meeting\n\n\n\n\n\n\n\n\n");
               meeting();
               break;
            case "Task1":
               System.out.println("beggining task one\n\n\n\n\n\n\n\n\n");
               break;
         }
      }
   }

   /**
    * meeting - putting the player in a deafult posisiton 
    */
   public void meeting() {
      movement.setBackgroundPosX(-1930);
      movement.setBackgroundPosY(-160);
      movement.update(false, false, false, false);




   }

   
   /** 
    * toUse - what the button task should be set to 
    * @return String
    */
   public String getToUse() {
      return toUse;
   }

   
   /** 
    * setting what the button should be used for 
    * @param toUse
    */
   public void setToUse(String toUse) {
      this.toUse = toUse;
   }

   
   /** 
    * getting the plyaer image 
    * @return ImageView
    */
   public ImageView getaPicView() {
      return aPicView;
   }

   
   /** 
    * getting the player x based off the gui 
    * @return int
    */
   public int getRacerPosX() {
      return racerPosX;
   }

   
   /** 
    * getting the players y based off  the gui
    * @return int
    */
   public int getRacerPosY() {
      return racerPosY;
   }

   
   /** 
    * setting the players x based off the gui
    * @param racerPosX
    */
   public void setRacerPosX(int racerPosX) {
      this.racerPosX = racerPosX;
   }

   
   /** 
    * setting the players y based off the gui 
    * @param racerPosY
    */
   public void setRacerPosY(int racerPosY) {
      this.racerPosY = racerPosY;
   }

   
   /** 
    * setting the iamge that this player will use
    * @param aPicView
    */
   public void setaPicView(ImageView aPicView) {
      this.aPicView = aPicView;
   }

   
   /** 
    * a boolean to check if the player is an imposter or not
    * @return boolean
    */
   public boolean isImposter() {
      return isImposter;
   }

   
   /** 
    * setting if the player is an imposter 
    * @param isImposter
    */
   public void setImposter(boolean isImposter) {
      this.isImposter = isImposter;
   }

   
   /** 
    * getting the width of the statc attribute 
    * @return int
    */
   public static int getWidthOfScreen() {
      return WIDTH_OF_SCREEN;
   }

   
   /** 
    * getting the height of the static attribute
    * @return int
    */
   public static int getHeightOfScreen() {
      return HEIGHT_OF_SCREEN;
   }

   
   /** 
    * getting the gui element that reports dead bodies and meetings
    * @return Button
    */
   public Button getBtnReport() {
      return btnReport;
   }

   
   /** 
    * setting the btn report gui element
    * @param btnReport
    */
   public void setBtnReport(Button btnReport) {
      this.btnReport = btnReport;
   }

   
   /** 
    * getting the gui element responsible for using tasks
    * @return Button
    */
   public Button getBtnUse() {
      return btnUse;
   }

   
   /** 
    * setting the btn use gui element
    * @param btnUse
    */
   public void setBtnUse(Button btnUse) {
      this.btnUse = btnUse;
   }

   
   /** 
    * getting the tasks area that should hold all the task that need to be done 
    * @return TextArea
    */
   public TextArea getTasks() {
      return tasks;
   }

   
   /** 
    * setting the tasks chat that should all the text that should be done 
    * @param tasks
    */
   public void setTasks(TextArea tasks) {
      this.tasks = tasks;
   }

   
   /** 
    * a button for imposters to kill othe r players and getting that button
    * @return Button
    */
   public Button getBtnKill() {
      return btnKill;
   }

   
   /** 
    * setting the kill button for imposters 
    * @param btnKill
    */
   public void setBtnKill(Button btnKill) {
      this.btnKill = btnKill;
   }

   
   /** 
    * imposter button to sabotage other players 
    * @return Button
    */
   public Button getBtnSabatoage() {
      return btnSabatoage;
   }

   
   /** 
    * setting the imposter sabtoge button
    * @param btnSabatoage
    */
   public void setBtnSabatoage(Button btnSabatoage) {
      this.btnSabatoage = btnSabatoage;
   }

   
   /** 
    * getting if the player is in a meeting 
    * @return boolean
    */
   public boolean isInMeeting() {
       return inMeeting;
   }

   
   /** 
    * setting if the player is in a meeting 
    * @param inMeeting
    */
   public void setInMeeting(boolean inMeeting) {
      this.inMeeting = inMeeting;
   }

   
   /** 
    * rotating a player in order to mkae them seem like they are dead 
    * @param dummyKill
    */
   public void kill(CrewmateRacer dummyKill) {
      dummyKill.getaPicView().setRotate(90);
      dummyKill.getaPicView().setOpacity(0.5);
   }

   
   /** 
    * getting the plyaers location on the server
    * @return int
    */
   public int getLocationOnServer() {
      return locationOnServer;
   }

   
   /** 
    * setting the players location on the server 
    * @param locationOnServer
    */
   public void setLocationOnServer(int locationOnServer) {
      this.locationOnServer = locationOnServer;
   }

   
   /** 
    * getting the players name 
    * @return String
    */
   public String getName() {
      return name;
   }

   
   /** 
    * setting the players name
    * @param name
    */
   public void setName(String name) {
      this.name = name;
   }



   
   /** 
    * get the player background pos x 
    * @return int
    */
   public int getBackgroundPosX() {
      return backgroundPosX;
   }



   
   /** 
    * setting the players background x
    * @param backgroundPosX
    */
   public void setBackgroundPosX(int backgroundPosX) {
      this.backgroundPosX = backgroundPosX;
   }



   
   /** 
    * getting the background y
    * @return int
    */
   public int getBackgroundPosY() {
      return backgroundPosY;
   }



   
   /** 
    * setting the backgorund y 
    * @param backgroundPosY
    */
   public void setBackgroundPosY(int backgroundPosY) {
      this.backgroundPosY = backgroundPosY;
   }

   
   /** 
    * getting if the player is alive or not
    * @return boolean
    */
   public boolean isAlive() {
      return alive;
   }

   
   /** 
    * setting if the player is alive or not
    * @param alive
    */
   public void setAlive(boolean alive) {
      this.alive = alive;
   }

   
   /** 
    * @return ProgressBar
    */
   public ProgressBar getProgressBar() {
      return progressBar;
   }

   
   /** 
    * @param progressBar
    */
   public void setProgressBar(ProgressBar progressBar) {
      this.progressBar = progressBar;
   }

   

   

   
   




}
