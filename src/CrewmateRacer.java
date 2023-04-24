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


   //for dummy imposter and other players
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

   public CrewmateRacer(String name , boolean isImposter){
      this.name = name;
      this.isImposter = isImposter;
      racerPosX = 400;
      racerPosY = 250;
      this.alive = true;
   }



   public void update(double x,double y){
      //need to alter the update statment in order correcly adjust were the task will be 
      //need to have add to it so it isnt just palce on the top left corner of the background movement 
      
      
      
      
      
      
      
      racerPosX = (int)x + 1930 + STARTING_X - 25;
      racerPosY = (int)y + 160 + STARTING_Y - 25;
      //System.out.println("Background " +name + " X Cord " + xForInter);
      //System.out.println("Background " + name + " Y Cord " + yForInter);
      aPicView.setTranslateX(racerPosX);
      aPicView.setTranslateY(racerPosY);

      //System.out.println("Moving other player with main player pos: " + " X " + racerPosX  +" Y "+ racerPosY);

      
  }
  public void updateOnline(double x,double y){


      int xForInter = (int)x + STARTING_X -25;
      int yForInter = (int)y + STARTING_Y -25;
      
      
      //System.out.println("Other player moved by server pos: " + " X " + xForInter  +" Y "+ yForInter);

      aPicView.setTranslateX(xForInter);
      aPicView.setTranslateY(yForInter);

  }



   //for the main player
   public CrewmateRacer(boolean isImposter, String crewmateImage,MovableBackground move) {
      this.alive =true;

      //this.name = crewmateImage;
      this.movement = move;
      this.isImposter = isImposter;
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

   public void meeting() {
      movement.setBackgroundPosX(-1930);
      movement.setBackgroundPosY(-160);
      movement.update(false, false, false, false);




   }

   public String getToUse() {
      return toUse;
   }

   public void setToUse(String toUse) {
      this.toUse = toUse;
   }

   /*
    * public void update() {
    * 
    * }
    */

   public ImageView getaPicView() {
      return aPicView;
   }

   public int getRacerPosX() {
      return racerPosX;
   }

   public int getRacerPosY() {
      return racerPosY;
   }

   public void setRacerPosX(int racerPosX) {
      this.racerPosX = racerPosX;
   }

   public void setRacerPosY(int racerPosY) {
      this.racerPosY = racerPosY;
   }

   public void setaPicView(ImageView aPicView) {
      this.aPicView = aPicView;
   }

   public boolean isImposter() {
      return isImposter;
   }

   public void setImposter(boolean isImposter) {
      this.isImposter = isImposter;
   }

   public static int getWidthOfScreen() {
      return WIDTH_OF_SCREEN;
   }

   public static int getHeightOfScreen() {
      return HEIGHT_OF_SCREEN;
   }

   public Button getBtnReport() {
      return btnReport;
   }

   public void setBtnReport(Button btnReport) {
      this.btnReport = btnReport;
   }

   public Button getBtnUse() {
      return btnUse;
   }

   public void setBtnUse(Button btnUse) {
      this.btnUse = btnUse;
   }

   public TextArea getTasks() {
      return tasks;
   }

   public void setTasks(TextArea tasks) {
      this.tasks = tasks;
   }

   public Button getBtnKill() {
      return btnKill;
   }

   public void setBtnKill(Button btnKill) {
      this.btnKill = btnKill;
   }

   public Button getBtnSabatoage() {
      return btnSabatoage;
   }

   public void setBtnSabatoage(Button btnSabatoage) {
      this.btnSabatoage = btnSabatoage;
   }

   public boolean isInMeeting() {
       return inMeeting;
   }

   public void setInMeeting(boolean inMeeting) {
      this.inMeeting = inMeeting;
   }

   public void kill(CrewmateRacer dummyKill) {
      dummyKill.getaPicView().setRotate(90);
      dummyKill.getaPicView().setOpacity(0.5);
   }

   public int getLocationOnServer() {
      return locationOnServer;
   }

   public void setLocationOnServer(int locationOnServer) {
      this.locationOnServer = locationOnServer;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }



   public int getBackgroundPosX() {
      return backgroundPosX;
   }



   public void setBackgroundPosX(int backgroundPosX) {
      this.backgroundPosX = backgroundPosX;
   }



   public int getBackgroundPosY() {
      return backgroundPosY;
   }



   public void setBackgroundPosY(int backgroundPosY) {
      this.backgroundPosY = backgroundPosY;
   }

   public boolean isAlive() {
      return alive;
   }

   public void setAlive(boolean alive) {
      this.alive = alive;
   }

   public ProgressBar getProgressBar() {
      return progressBar;
   }

   public void setProgressBar(ProgressBar progressBar) {
      this.progressBar = progressBar;
   }

   

   

   
   




}
