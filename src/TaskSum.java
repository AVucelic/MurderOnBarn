
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
/**
 * @ASSESSME.INTENSITY:LOW
 */

/**
 * Sum Task
 * 
 * @author Luka Lasic
 * @author Arian Vucelic
 * @since 20-4-2023
 */
public class TaskSum extends Application{

    //Attributes
    private int num1;
    private int num2;
    private int expectedSum;
    private Label tryAgain;
    private boolean completed = false;
    private TextField sumField;

    
    /** 
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Generate random numbers between 1 and 100
        num1 = (int) (Math.random() * 100);
        num2 = (int) (Math.random() * 100);
        //Calculate its sum
        expectedSum = num1 + num2; 

        //GUI stuff
        Label instruction = new Label("Add numbers");
        Label num1Label = new Label(Integer.toString(num1));
        Label plusLabel = new Label("+");
        Label num2Label = new Label(Integer.toString(num2));
        Label equalsLabel = new Label("=");
        tryAgain = new Label("");

        sumField = new TextField();

        Button submit = new Button("Submit");
        //Action to check answer
        submit.setOnAction(event -> checkAnswer(primaryStage));

        //Adding components to the GUI
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(25, 25, 25, 25));
        root.add(instruction, 0, 0, 4, 1);
        root.add(num1Label, 0, 1);
        root.add(plusLabel, 1, 1);
        root.add(num2Label, 2, 1);
        root.add(equalsLabel, 3, 1);
        root.add(sumField, 4, 1);
        root.add(tryAgain, 2, 2);
        root.add(submit, 2, 3);

        Scene scene = new Scene(root, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }   

    /**
     * Method to check for the answer inputed
     * @param stage
     */
    private void checkAnswer(Stage stage){
        try{
        //User's input
        int sum = Integer.parseInt(sumField.getText());
        //if statement if it is the same as correct sum
        if(sum == expectedSum){
            //If it is, task completed
            System.out.println("Task Completed!");
            tryAgain.setText("Task Completed!");
            completed = true;
            Thread.sleep(500);
            stage.close();
            //If it isn't, do it again
        } else {
            tryAgain.setText("Incorrect sum, try again!");
            System.out.println("Incorrect sum, try again!");
        }
    }
    catch(NumberFormatException | InterruptedException e){
        System.out.println("Please enter a valid number!");
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
