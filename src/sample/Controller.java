package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    private Button player1Btn, player2Btn, newGameBtn;
    @FXML
    private Label player1Score, player2Score, countdownLabel;
    @FXML
    private Slider difficultySlider;
    @FXML
    private AnchorPane arena;
    @FXML
    private Circle blue, red, click;
    static ArrayList<Score> skore = new ArrayList<Score>();
    private int interval;
    private int level;
    private float time;
    private float cyklus;
    private float setinterval;
    private boolean run = false;
    private Timer timerGame = new Timer(), timerClick = new Timer(), timerPosition = new Timer();

    private String M="";
    private String S="";
    private String P="";


    @FXML
    public void countdown() {
        time = 0;
        cyklus = 0;

        player1Score.setText("0");
        player2Score.setText("0");

        level = (int) difficultySlider.getValue();
        int levell=(int) difficultySlider.getValue();
        int r = 53 - 3 * level;
        blue.setRadius(r);
        red.setRadius(r);
        red.setLayoutX(413);
        red.setLayoutY(142);

        setinterval = (float) r;
        level = (int) ((setinterval / 35) * 10);
        enemy();

        interval =  100;

        countdownLabel.setText("" + interval / 60 + ":" + interval % 60);

        System.out.println(difficultySlider.getValue());
        if (!run) {
            timerGame.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
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

                        } if(interval==0) {
                            timerGame.cancel();
                            run = false;
                            skore.add(new Score(levell, Integer.parseInt(player1Score.getText()) - Integer.parseInt(player2Score.getText()) * levell));
                            ulozTo();
                        }
                    });
                }

            }, 0, 100);

        }




    }

    private void ulozTo() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\skola\\du karantena\\PifPaf\\PifPaf\\src\\sample\\Tabulka.txt"));
            for (int i=0;i<skore.size();i++) {
                bw.write(skore.get(i).toString());
                bw.newLine();
            }


            bw.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(skore, new Comparator<Score>() {
            @Override
            public int compare(Score c2, Score c1) {
                return Double.compare(c1.getScore(), c2.getScore());
            }
        });

    }

    private void visibleRed() {
        double randomX = Math.random() * (1000 - 2 * red.getRadius()) + red.getRadius();
        double randomY = Math.random() * (485 - 2 * red.getRadius()) + red.getRadius();

        red.setLayoutX(randomX);
        red.setLayoutY(randomY);
        red.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\skola\\du karantena\\PifPaf\\PifPaf\\src\\sample\\Tabulka.txt"));
            String line;
            while ((line = br.readLine()) != null){
                System.out.println(line);

                skore.add(new Score(Integer.parseInt(line.substring(0,line.indexOf("|"))),Integer.parseInt(line.substring(line.indexOf("|")+1))));
                // skore.add(new Record(line.substring(0,line.indexOf("|")),Double.parseDouble(line.substring(line.indexOf("|")+1))));
            }

            Collections.sort(skore, new Comparator<Score>() {
                @Override
                public int compare(Score c2, Score c1) {
                    return Double.compare(c1.getScore(), c2.getScore());
                }
            });





        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void vypisTop10(){
        P="C.\tSkore\tUroven\n";

        int pocet=0;
        for (int i=0;i<Controller.skore.size();i++){
            if(Controller.skore.get(i).getScore()>-1) {
                pocet++;
            }

        }
        if(pocet!=0) {
            int p = 0;
            if (pocet > 10) {
                p=10;
            } else
                p=pocet;

            int c = 1;
            for (int i = 0; i < p; i++) {
                if (Controller.skore.get(i).getScore() > 0) {
                    P = P + (c) + "\t" + Controller.skore.get(i).getScore() + "\t\t" + Controller.skore.get(i).getLevel() + "\n" ;
                    S = S + Controller.skore.get(i).getScore() + "\n";
                    M = M + Controller.skore.get(i).getLevel() + "\n";
                    c++;
                } else p++;

            }


        }
        System.out.println(P);
        System.out.println(S);
        System.out.println(M);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Top10");
        alert.setHeaderText("Top10 Snaž sa viac");
        alert.setContentText(P);

        alert.showAndWait();
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
    //pripocita skore hracovi
    @FXML
    public void clickRed() {
        if (run) {
            int score = Integer.parseInt(player1Score.getText());
            score++;
            player1Score.setText(String.valueOf(score));
        }
    }
    //pripocita skore pc
    @FXML
    public void clickBlue() {
        if (run) {
            int score = Integer.parseInt(player2Score.getText());
            score++;
            player2Score.setText(String.valueOf(score));
        }
    }
    private double x,y;
    public void enemy() {
        if (!run) {
            //ak sa kruhy pretinaju klikne na hraca + hybe s ciernym kruhom
            timerClick.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        click.setRadius(10);
                        click.setLayoutX(x);
                        click.setLayoutY(y);

                        boolean intersects = Math.hypot(x - blue.getLayoutX(), y - blue.getLayoutY()) <= (click.getRadius() + blue.getRadius());//vzorec na zistenie ci sa kruhy pretinaju

                        if (intersects) {
                            clickBlue();
                            System.out.println("klik");
                        }
                    });
                }
            }, 1000, 150 * level);

            //vracia poziciu hraca s delayom
            timerPosition.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        x = blue.getLayoutX();
                        y = blue.getLayoutY();
                    });
                }
            }, 2000, 150 * level);
        }
    }


}

