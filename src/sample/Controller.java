package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private Timer timer;
    @FXML
    private Button player1Btn, player2Btn, newGameBtn;
    @FXML
    private Label player1Score, player2Score, countdownLabel;
    @FXML
    private Slider difficultySlider;
    @FXML
    private AnchorPane arena;
    @FXML
    private Circle blue, red;

    private int interval;
    private int level;
    private float time;
    private float ciklus;
    private float setinterval;
    private boolean run =false;


    @FXML
    public void countdown(){
        run=true;
        time=0;
        ciklus=0;
        player1Score.setText("0");
        player2Score.setText("0");
        level=(int)difficultySlider.getValue();
        int r=53-3*level;
        blue.setRadius(r);
        red.setRadius(r);
        red.setLayoutX(413);
        red.setLayoutY(142);
        setinterval=(float)r;
        level=(int)((setinterval/35)*10);

        interval=6*r;


        countdownLabel.setText("" + interval / 60 + ":" + interval % 60);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            ciklus=(float) (ciklus+0.1);
            time= (float) (time+0.1);
            System.out.println((int)(ciklus*10));
            if((int)(ciklus*10)==level){
                visibleRed();
                ciklus=0;
            }
            System.out.println(time);
            if((int)(time*10)==10){
                interval--;
                countdownLabel.setText("" + interval / 60 + ":" + interval % 60);
                System.out.println("idem");
                System.out.println("" + interval / 60 + ":" + interval % 60);
                time=0;
            }
        }));
        timeline.setCycleCount(interval*10);
        timeline.play();
        if(interval==0)
        run = false;

    }




    private void visibleRed() {

        double randomX = Math.random() * (1000 - 2 * red.getRadius()) + red.getRadius();
        double randomY = Math.random() * (485 - 2 * red.getRadius()) + red.getRadius();
        red.setLayoutX(randomX);
        red.setLayoutY(randomY);
        red.setVisible(true);


    }



    class Delta { double x, y; }

    @FXML
    public void moveMe() {
        if(run==true) {
            final Delta dragDelta = new Delta();
            blue.setOnMousePressed(new EventHandler<MouseEvent>() {


                public void handle(MouseEvent mouseEvent) {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = blue.getLayoutX() - mouseEvent.getSceneX();
                    dragDelta.y = blue.getLayoutY() - mouseEvent.getSceneY();
                    blue.setCursor(Cursor.MOVE);
                }
            });
            blue.setOnMouseReleased(new EventHandler<MouseEvent>() {


                public void handle(MouseEvent mouseEvent) {
                    blue.setCursor(Cursor.HAND);
                }
            });
            blue.setOnMouseDragged(new EventHandler<MouseEvent>() {


                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getSceneX() + dragDelta.x < 1000 - blue.getRadius() && mouseEvent.getSceneX() + dragDelta.x > 0 + blue.getRadius())
                        blue.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                    if (mouseEvent.getSceneY() + dragDelta.y < 485 - blue.getRadius() && mouseEvent.getSceneY() + dragDelta.y > 0 + blue.getRadius())
                        blue.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                }
            });
            blue.setOnMouseEntered(new EventHandler<MouseEvent>() {


                public void handle(MouseEvent mouseEvent) {
                    blue.setCursor(Cursor.HAND);
                }
            });
        }
    }

    @FXML
    public void clickRed(){
        if (run==true) {
            int score = Integer.parseInt(player1Score.getText());
            score++;
            player1Score.setText(String.valueOf(score));
        }
    }

    @FXML
    public void clickBlue(){

        }







}

