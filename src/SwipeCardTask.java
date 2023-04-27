import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Swipe Card Task 
 * 
 * @author Luka Lasic
 * @author Arian Vucelic
 * @since 20-4-2023
 */

public class SwipeCardTask extends Application {

    //Attributes

    private ProgressBar progressBar;
    private ImageView card;
    private Label swipeCountLabel;
    private int swipeCount;
    private final Image cardssss = new Image("card.png");
    private final Image readerrrr = new Image("reader.png");
    private boolean completed = false;



    
    /** 
     * method to start the game 
     * @param primaryStage
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception {
        // Create GUI components
        progressBar = new ProgressBar();
        progressBar.setProgress(0);

        card = new ImageView();
        card.setImage(cardssss);

        //Set event listener for mouse event
        card.setOnMousePressed(this::handleCardPressed);
        card.setOnMouseDragged(this::handleCardDragged);

        ImageView reader = new ImageView();
        reader.setImage(readerrrr);

        swipeCountLabel = new Label("Swipes remaining: 5");

        Button completeButton = new Button("Complete Task");
        completeButton.setDisable(true);
        completeButton.setOnAction(event -> {
            //Update swipe count label based on the number of swipes
            if (swipeCount == 5) {
                swipeCountLabel.setText("Task complete!");
            } else {
                swipeCountLabel.setText("Swipe card " + (5-swipeCount) + " more times.");
            }
        });

        // Add components to layout
        StackPane cardPane = new StackPane(card);
        cardPane.setPrefSize(300, 200);

        BorderPane layout = new BorderPane();
        layout.setCenter(cardPane);
        layout.setBottom(reader);
        layout.setTop(progressBar);
        layout.setRight(completeButton);
        layout.setLeft(swipeCountLabel);

        // Create scene and show stage
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Swipe Card Task");
        primaryStage.show();
    }

    
    /** 
     * Handle the mouse press event on the card
     * @param event
     */
    private void handleCardPressed(MouseEvent event) {
        // Start dragging the card when the player clicks on it
        card.setTranslateX(event.getX());
        card.setTranslateY(event.getY());
    }

    /**
     * Handle the mouse drag event on the card
     * @param event
     */
    private void handleCardDragged(MouseEvent event) {
        // Move the card as the player drags it
        card.setTranslateX(event.getX());
        card.setTranslateY(event.getY());

        // Check if the card is swiped successfully
        if (card.getBoundsInParent().intersects(progressBar.getBoundsInParent())) {
            swipeCount++;
            swipeCountLabel.setText("Swipes remaining: " + (5-swipeCount));
            progressBar.setProgress(swipeCount / 5.0);
            //If the task is completed, update the state and GUI components
            if (swipeCount == 5) {
                completed = true;
                card.setDisable(true);
                swipeCountLabel.setText("Task complete!");
            }
        }
    }

    //Main method
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * 
     * @return
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * 
     * @param completed
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
}