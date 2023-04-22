import javafx.scene.image.Image;

/**
 * @ASSESSME.INTENSITY:LOW
 */
public class TaskClickImage {
    private final Image blueRect = new Image("plavi.png");
    private final Image pinkRect = new Image("ljubicasti.png"); 
    public int imageCount;
    private int onCount = 0;
    public int points;

    public void SwitchChange(int points){
        this.points = points;
        onCount = onCount + points;
        if(onCount == imageCount){
            System.out.println("You win!");
        }
    }

}
