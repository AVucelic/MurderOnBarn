import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @ASSESSME.INTENSITY:LOW
 */
public class TaskDownload extends Application implements EventHandler<ActionEvent>{
    private Stage stage;
    private Scene scene;
    private Button btnStart = new Button("Start the Download!");
    private boolean keepGoing = false;
    private ArrayList<InnerClass> progress = new ArrayList<>();
    private Label lblStart = new Label("Click Start To Start Your Task!");

    private boolean completed = false;

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(ActionEvent event) {
        Button btn = (Button)event.getSource();
        switch(btn.getText()){
            case "Start the Download!":
                doStartThread();
                break;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        stage.setTitle("Downloading Virus To Your Computer!");
        VBox root = new VBox();
        scene = new Scene(root, 200, 100);
        btnStart.setOnAction(this);

        FlowPane fp1 = new FlowPane();
        FlowPane fp3 = new FlowPane();

        fp1.setAlignment(Pos.CENTER);
        fp3.setAlignment(Pos.CENTER);

        fp1.getChildren().addAll(lblStart);
        fp3.getChildren().addAll(btnStart);
        root.getChildren().addAll(fp1);

        int i=1;

        FlowPane fp2 = new FlowPane();
        fp2.setAlignment(Pos.CENTER);
        InnerClass ic = new InnerClass(i);
        progress.add(ic);
        fp2.getChildren().addAll(ic);
        root.getChildren().addAll(fp2);
        
        root.getChildren().addAll(fp3);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public void doStartThread(){
        progress.forEach(progressCount -> {
            Thread thr1 = new Thread(progressCount);
            thr1.start();
            btnStart.setDisable(true);
        });
    }

    class InnerClass extends FlowPane implements Runnable{
        int a = 0;
        ProgressBar pb = null;
        Label lbl1;
        Label lbl2;

        public InnerClass(int a){
            super(10, 10);
            this.a = a;
            this.pb = new ProgressBar(0);
            this.lbl1 = new Label("Download" + (a+1));
            this.lbl2 = new Label("0");
            StackPane sp = new StackPane();
            sp.getChildren().addAll(pb, lbl2);
            this.getChildren().addAll(lbl1, sp);
            pb.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

        }

        @Override
        public void run() {
            System.out.println("Progress bar no" + a + " is running");
            for(int i = 0; i <=100;i++){
                try {
                    Thread.sleep((long)(Math.random()*100));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                final int fIn = i;
                javafx.application.Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        pb.setProgress(fIn/100.00);
                        lbl2.setText(fIn + "%");
                    }
                });

                if(fIn == 100){
                    javafx.application.Platform.runLater(new Runnable(){
                        @Override
                        public void run(){
                            Alert alert = new Alert(AlertType.INFORMATION, "Task Completed! Click \"OK\" to exit");
                            alert.showAndWait();
                            completed = true;

                            try {
                                Thread.sleep(1000);
                                Stage stage = (Stage) lbl1.getScene().getWindow();
                                stage.close();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            
            keepGoing = true;
            btnStart.setDisable(true);
        }
        
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
}
