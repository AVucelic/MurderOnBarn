import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class CrewmateRacer extends Pane{
    private int racerPosX = 0;
   private int racerPosY = 0;
      private ImageView aPicView = null;
      private boolean isImposter;
      private final static int WIDTH_OF_SCREEN = 800;
    private final static int HEIGHT_OF_SCREEN = 500;

      public CrewmateRacer(boolean isImposter,String crewmateImage) {
         this.isImposter = isImposter;
         if (isImposter) {
            aPicView = new ImageView(crewmateImage);
            aPicView.setFitWidth(50);
            aPicView.setPreserveRatio(true);
            racerPosX = WIDTH_OF_SCREEN / 2 - 25;
            racerPosY = HEIGHT_OF_SCREEN / 2 - 25;
            aPicView.setTranslateX(racerPosX);
            aPicView.setTranslateY(racerPosY);
         } else {
            //aPicView = new ImageView(CREWMATE_RUNNERS);
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
         aPicView.setTranslateX(racerPosX);
         aPicView.setTranslateY(racerPosY);

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
