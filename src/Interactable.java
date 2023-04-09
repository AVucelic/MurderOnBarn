import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;



public class Interactable extends Pane{
    //strating postion of the task 
    private int posXBasedOnBack = -1930 + 1930 + 400;
    private int posYBasedOnBack  = -160 + 160 + 150;
    private ImageView interactable;
    private String name;

    public Interactable(String name){
        interactable = new ImageView(name);
        interactable.setFitWidth(10);
        interactable.setFitHeight(10);
        interactable.setStyle("-fx-border-color : yellow; -fx-border-width : 2;");
        this.name = name;
        this.getChildren().addAll(interactable);
    }
    public void moveInteractable(int x, int y){
        posXBasedOnBack = x;
        posYBasedOnBack = y;
    }
    //the x that we are getting is based on the background movment top left corner 
    // the y coord is based on teh background movements y 
    public void update(double x,double y){
        //need to alter the update statment in order correcly adjust were the task will be 
        //need to have add to it so it isnt just palce on the top left corner of the background movement 
        int xForInter = (int)x + 1930 + 400;
        int yForInter = (int)y + 160 + 200;
        System.out.println("Background " +name + " X Cord " + xForInter);
        System.out.println("Background " + name + " Y Cord " + yForInter);
        interactable.setTranslateX(xForInter);
        interactable.setTranslateY(yForInter);

        posXBasedOnBack = xForInter;
        posYBasedOnBack = yForInter;
    }
    public void addHighlight(){
        interactable.setStyle("-fx-border-color: yellow; -fx-border-width: 50;");
    }
    public void removeHighlight(){
        interactable.setStyle("-fx-border-color: none; -fx-border-width: 0;");
    }

    public Point2D getPos(){
        return new Point2D(posXBasedOnBack,posYBasedOnBack);
    }
    public int getPosXBasedOnBack() {
        return posXBasedOnBack;
    }
    public void setPosXBasedOnBack(int posXBasedOnBack) {
        this.posXBasedOnBack = posXBasedOnBack;
    }
    public int getPosYBasedOnBack() {
        return posYBasedOnBack;
    }
    public void setPosYBasedOnBack(int posYBasedOnBack) {
        this.posYBasedOnBack = posYBasedOnBack;
    }
    public ImageView getInteractable() {
        return interactable;
    }
    public void setInteractable(ImageView interactable) {
        this.interactable = interactable;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    

}
