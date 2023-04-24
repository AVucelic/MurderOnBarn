
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TaskSum extends Application{

    private int num1;
    private int num2;
    private int expectedSum;
    private Label tryAgain;

    private TextField sumField;

    @Override
    public void start(Stage primaryStage) throws Exception {
        num1 = (int) (Math.random() * 100);
        num2 = (int) (Math.random() * 100);
        expectedSum = num1 + num2;

        Label instruction = new Label("Add numbers");
        Label num1Label = new Label(Integer.toString(num1));
        Label plusLabel = new Label("+");
        Label num2Label = new Label(Integer.toString(num2));
        Label equalsLabel = new Label("=");
        tryAgain = new Label("");

        sumField = new TextField();

        Button submit = new Button("Submit");
        submit.setOnAction(event -> checkAnswer(primaryStage));

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

    private void checkAnswer(Stage stage){
        try{
        int sum = Integer.parseInt(sumField.getText());
        if(sum == expectedSum){
            System.out.println("Task Completed!");
            tryAgain.setText("Task Completed!");
            Thread.sleep(500);
            stage.close();
        } else {
            tryAgain.setText("Incorrect sum, try again!");
            System.out.println("Incorrect sum, try again!");
        }
    }
    catch(NumberFormatException | InterruptedException e){
        System.out.println("Please enter a valid number!");
    }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
