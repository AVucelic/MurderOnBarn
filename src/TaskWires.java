/**
 * 
 * @ASSESSME.INTENSITY:LOW
 */
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TaskWires extends Application implements EventHandler<ActionEvent>{
    //Arays for colors and indexes
    private static final List<Integer> WIRE_ORDER = Arrays.asList(1, 3, 5, 2, 6, 4);
    private static final List<String> WIRES = Arrays.asList("Red", "Blue", "Green", "Yellow", "Purple", "Orange");


    private int wireIndex;
    private String[] wireConnections;
    private Label lblInstruction;
    private Label lblWire;
    private TextField tfWire;

    private boolean completed = false;

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        //Shuffling orders!
        Collections.shuffle(WIRE_ORDER);

        //Initialize the wire connection
        wireConnections = new String[WIRES.size()];
        for(int i = 0;i<WIRES.size();i++){
            wireConnections[i] = WIRES.get(WIRE_ORDER.get(i)-1);
        }

        //Instatiating the GUI components
        lblInstruction = new Label("Connect the wires in the following order: ");
        lblInstruction.setFont(new Font("Arial", 16));

        lblWire = new Label(wireConnections[wireIndex]);
        lblWire.setFont(new Font("Arial", 18));
        lblWire.setAlignment(Pos.CENTER);

        tfWire = new TextField();
        tfWire.setFont(new Font("Arial", 16));
        tfWire.setAlignment(Pos.CENTER);

        Button btnStart = new Button("Submit");
        btnStart.setFont(new Font("Arial", 16));
        btnStart.setOnAction(this);

        Button btnReset = new Button("Reset");
        btnReset.setFont(new Font("Arial", 16));
        btnReset.setOnAction(this);

        //Creating the layout for GUI
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(20, 20, 20, 20));
        gp.add(lblInstruction, 0, 0, 2, 1);
        gp.add(lblWire, 0, 1, 2, 1);
        gp.add(new Label("Wire: "), 0, 2);
        gp.add(tfWire, 1, 2);
        HBox btnBox = new HBox(10, btnStart, btnReset);
        btnBox.setAlignment(Pos.CENTER);
        gp.add(btnBox, 0, 3, 2, 1);
    
        //Creating the scene and stage
        Scene scene = new Scene(gp, 400, 200);
        primaryStage.setTitle("Wire Fixing Task");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @Override
    public void handle(ActionEvent event) {
        Button btn = (Button)event.getSource();
        switch(btn.getText()){
            case "Submit":
                doSubmit();
                break;
            case "Reset":
                doReset();
                break;
        }
    }

    public void doSubmit(){
        //Get the user input from the tf and trim whitespace
        String input = tfWire.getText().trim();
        //Check if the user input maches the current wire name
        if(input.equalsIgnoreCase(wireConnections[wireIndex])){
            //if it matches, go to the next one
            wireIndex++;
            // if there are more wires, go to the next one
            if(wireIndex < WIRES.size()){
                lblWire.setText(wireConnections[wireIndex]);
                tfWire.setText("");
            } 
            //If all wires are connecte, close the stage
            else{
                completed = true;
                Stage stage = (Stage) lblInstruction.getScene().getWindow();
                stage.close();
            }
        }
        //Error message for wrong wire
        else {
            Alert alert = new Alert(AlertType.ERROR, "Incorrect wire connection. Please try again");
            alert.showAndWait();
        }
    }

    public void doReset(){
        //Shuffle the order
        Collections.shuffle(WIRE_ORDER);
       
        //Initalize the wire connections
        wireConnections = new String[WIRES.size()];
        for(int i = 0; i < WIRES.size(); i++){
            wireConnections[i] = WIRES.get(WIRE_ORDER.get(i)-1);
        }

        //Reset the wire index and GUI components
        wireIndex = 0;
        lblWire.setText(wireConnections[wireIndex]);
        tfWire.setText(" ");
    }


    public boolean isCompleted() {
        return completed;
    }


    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    


    /*public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] wires = {"red", "blue", "green", "yellow", "purple", "orange"};
        int[] wireOrder = {1, 3, 5, 2, 6, 4};
        String[] wireConnections = new String[wires.length];

        shuffle(wireOrder);

        for(int i =0; i<wires.length;i++){
            wireConnections[i] = wires[wireOrder[i]-1];
        }

        System.out.println("Connect the wires in following order");
        for(String wire : wireConnections){
            System.out.println(wire + "   ");
        }
        System.out.println();

        System.out.println("Enter wire colors in the correct order");
        for(int i = 0; i<wires.length;i++){
            String color = in.next();
            if(!color.equals(wireConnections[i])){
                System.out.println("Incorrect order! try again");
                break;
            }
            if(i == wires.length -1 ){
                System.out.println("Task has been completed!");
            }
        }
    }
    private static void shuffle(int[] arr){
        for(int i = arr.length-1;i>0;i--){
            int j = (int) Math.floor(Math.random() * (i+1));
            int temp = arr[j];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }*/

}
