package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
    private float cyklus;
    private float setinterval;
    private boolean run = false;
    private Timeline timeline;
    private Timer timerGame = new Timer(), timerEnemy = new Timer();


    @FXML
    public void countdown() {


        time = 0;
        cyklus = 0;

        player1Score.setText("0");
        player2Score.setText("0");

        level = (int) difficultySlider.getValue();
        int r = 53 - 3 * level;
        blue.setRadius(r);
        red.setRadius(r);
        red.setLayoutX(413);
        red.setLayoutY(142);

        setinterval = (float) r;
        level = (int) ((setinterval / 35) * 10);

        interval = 6 * r;

        countdownLabel.setText("" + interval / 60 + ":" + interval % 60);

        System.out.println(difficultySlider.getValue());
        if (!run) {
            timerGame.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        enemy(blue.getLayoutX(),blue.getLayoutY());
                        run = true;
                        cyklus = (float) (cyklus + 0.1);
                        time = (float) (time + 0.1);
                        //System.out.println((int) (cyklus * 10));

                        if ((int) (cyklus * 10) == level) {
                            visibleRed();
                            cyklus = 0;
                        }

                        //System.out.println(time);

                        if ((int) (time * 10) == 10) {
                            interval--;
                            String seconds;
                            if (interval % 60<10){
                                seconds = "0"+interval % 60;
                            }else seconds = ""+interval % 60;
                            countdownLabel.setText("" + interval / 60 + ":" + seconds);
                            time = 0;
                        }
                    });
                }

            }, 0, 100);
        }

        if (interval == 0)
            run = false;

    }

    private void visibleRed() {
        double randomX = Math.random() * (1000 - 2 * red.getRadius()) + red.getRadius();
        double randomY = Math.random() * (485 - 2 * red.getRadius()) + red.getRadius();

        red.setLayoutX(randomX);
        red.setLayoutY(randomY);
        red.setVisible(true);
    }

    class Delta {
        double x, y;
    }

    @FXML
    public void moveMe() {
        if (run) {
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
    public void clickRed() {
        if (run) {
            int score = Integer.parseInt(player1Score.getText());
            score++;
            player1Score.setText(String.valueOf(score));
        }
    }

    @FXML
    public void clickBlue() {
        if (run) {
            int score = Integer.parseInt(player2Score.getText());
            score++;
            player2Score.setText(String.valueOf(score));
        }
    }

    public void enemy(double x, double y) {

        if (!run) {
            timerEnemy.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        System.out.println("x="+x+" blueX="+blue.getLayoutX());
                        if ((blue.getLayoutX() >= x-blue.getRadius() && blue.getLayoutX() <= x+blue.getRadius()) || (blue.getLayoutY() >= x-blue.getRadius() && blue.getLayoutY() <= x+blue.getRadius())){
                            clickRed();
                            System.out.println("klik");
                        }

                    });
                }
            }, 1000, 150 * level);
        }
    }



}

