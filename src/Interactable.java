import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


/**
 * Interactable - a class that puts an iamge for peolpe to use 
 * @author Luka Lasic
 * @since 10-4-2023
 */
public class Interactable extends Pane{
    //strating postion of the task 
    private int posXBasedOnBack = -1930 + 1930 + 400;
    private int posYBasedOnBack  = -160 + 160 + 200;
    private ImageView interactable;
    private String name;

    private int stupidX = 0;
    private int stupidY = 0;

    /**
     * construcotr to put an interatable object on the players screen 
     * @param name
     */
    public Interactable(String name){
        interactable = new ImageView(name);
        interactable.setFitWidth(30);
        interactable.setFitHeight(30);
        interactable.setStyle("-fx-border-color : yellow; -fx-border-width : 2;");
        this.name = name;
        this.getChildren().addAll(interactable);
    }
    
    /** 
     * moveInteractable moving the object starting point 
     * @param x
     * @param y
     */
    public void moveInteractable(int x, int y){
        posXBasedOnBack = x;
        posYBasedOnBack = y;
    }
    
    /** 
     * update mehtod to change the objects postions as the player move in order to make it stay in the same spot 
     * @param x
     * @param y
     */
    //the x that we are getting is based on the background movment top left corner 
    // the y coord is based on teh background movements y 
    public void update(double x,double y){
        //need to alter the update statment in order correcly adjust were the task will be 
        //need to have add to it so it isnt just palce on the top left corner of the background movement 
        int xForInter = (int)x + 1930 + posXBasedOnBack;
        int yForInter = (int)y + 160 + posYBasedOnBack;
        //System.out.println("Background " +name + " X Cord " + xForInter);
        //System.out.println("Background " + name + " Y Cord " + yForInter);
        interactable.setTranslateX(xForInter);
        interactable.setTranslateY(yForInter);

    
    }
    

    
    /** 
     * sneding postion as a Point2D object 
     * @return Point2D
     */
    public Point2D getPos(){
        return new Point2D(posXBasedOnBack,posYBasedOnBack);
    }

    
    /** 
     * @return int getting objects x postion 
     */
    public int getPosXBasedOnBack() {
        return posXBasedOnBack;
    }

    
    /** 
     * @param posXBasedOnBack setting objects x postion 
     */
    public void setPosXBasedOnBack(int posXBasedOnBack) {
        this.posXBasedOnBack = posXBasedOnBack;
    }

    
    /** 
     * @return int int getting objects y postion 
     */
    public int getPosYBasedOnBack() {
        return posYBasedOnBack;
    }

    
    /** 
     * @param posYBasedOnBack
     * int setting objects y postion 
     */
    public void setPosYBasedOnBack(int posYBasedOnBack) {
        this.posYBasedOnBack = posYBasedOnBack;
    }

    
    /** 
     * @return ImageView
     * getting the image that the object is using 
     */
    public ImageView getInteractable() {
        return interactable;
    }


    
    /** 
     * @param interactable
     * setting the image for the interactable 
     */
    public void setInteractable(ImageView interactable) {
        this.interactable = interactable;
    }



    
    /** 
     * @return String
     * name of the interactable 
     */
    public String getName() {
        return name;
    }


    
    /** 
     * @param name
     * setting name of interactable 
     */
    public void setName(String name) {
        this.name = name;
    }



    
    /** 
     * @return int
     * a x that is based of the interatable postions on the gui x and y 
     */
    public int getStupidX() {
        return stupidX;
    }



    
    /** 
     * @param stupidX
     * setting the  x that is based of the interatable postions on the gui x and y 
     */
    public void setStupidX(int stupidX) {
        this.stupidX = stupidX;
    }



    
    /** 
     * @return int
     * a y that is based of the interatable postions on the gui x and y 
     */
    public int getStupidY() {
        return stupidY;
    }

    

    
    /** 
     * @param stupidXY
     * setting y that is based of the interatable postions on the gui x and y 
     * javaDoc -d javaDocHtml XML_STUFF.java
     * 
     */
    public void setStupidY(int stupidXY) {
        this.stupidY = stupidXY;
    }



    
    

}
