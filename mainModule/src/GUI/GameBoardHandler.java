package GUI;

import Game.AbstractBoard;
import Game.GameInterface;
import OtherControllers.GameController;
import Server.Server;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import Game.Move;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.function.Function;

public class GameBoardHandler implements Initializable, Observer, ViewActionHandler {

    @FXML
    private GridPane GameB;
    @FXML
    private Button ForfeitB;
    @FXML
    private Text Player1T;
    @FXML
    private Text Player2T;
    @FXML
    private ListView ListV;
    @FXML
    private  HBox GameHBox;

    // List of strings to use in the listview of movehistory
    private ArrayList<String> moveHistory = new ArrayList<>();

    private GameController gamecontroller;

    private int boardSize;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewHandlers.getInstance().registerHandler("GameBoardHandler", this);
    }
    public void createDisplayElements() {
        int X = this.boardSize;
        int Y = this.boardSize;

        ForfeitB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gamecontroller.forfeit();
            }
        });

        if(X > 2 || Y> 2) {
            for(int newrow = 1; newrow <= X-4; newrow++) {
                GameB.setPrefHeight( GameB.getPrefHeight() + 100);
                ListV.setPrefHeight( ListV.getPrefHeight() + 100);
                RowConstraints row = new RowConstraints();
                row.setPrefHeight(100);
                GameB.getRowConstraints().add(row);
            }

            for(int newcol = 1; newcol <= Y-4; newcol++) {
                GameB.setPrefWidth( GameB.getPrefWidth() + 100);

                ColumnConstraints colum = new ColumnConstraints();
                colum.setPrefWidth(100);
                GameB.getColumnConstraints().add(colum);
            }
        }
        ListV.setPrefHeight( ListV.getPrefHeight() + 100);
        GameHBox.setPrefSize(800, 800);
        for(int y = 0; y < Y; y++) {
            for(int x = 0; x < X; x++){
                int pos = ((y) * X)  +  x;
                Pane cell = new Pane();
                cell.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                cell.setId(String.valueOf(pos));
                cell.setStyle("-fx-border-color: black; -fx-background-color: green;");

                cell.setOnMouseClicked(event -> {

                    //cell.getChildren().add(new Circle(cell.getWidth()/2,cell.getHeight()/2, (cell.getHeight()/2) - 5, Color.BLACK));
                    gamecontroller.registerMove(pos);
                });

                GameB.add(cell,x,y);

            }
        }
    }

    @Override
    public void update(Observable o, Object arg) { //Not safe (not a representation of the board, just rebuilds it).

        GameInterface game= (GameInterface) arg;
        AbstractBoard board = game.getBoard();
        drawGrid(board);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                addToMoveHistory(game.getMoveHistory().get(game.getMoveHistory().size()-1));
            }
        });
    }


    public void drawGrid(AbstractBoard board) {
        char[] boardVals = board.getCells1D();

        ArrayList<testrun> tasks  = new ArrayList<>();
        for (int i = 0; i < this.boardSize * this.boardSize; i++) {
            Pane selectedCell = (Pane) GameB.lookup("#" + i);
            final String mark = Character.toString(boardVals[i]);
            if(mark.equals("W")) {
                tasks.add(new testrun(Color.WHITE, selectedCell));
            }
            else if(mark.equals("Z")){
                tasks.add(new testrun(Color.BLACK, selectedCell));
            }
        }
        for (testrun r : tasks) {
            Platform.runLater(r);
        }
    }

    class testrun implements Runnable {
        String mark;
        Pane pane;
        Color color;
        public testrun(Color color, Pane pane) {
            this.mark = mark;
            this.pane = pane;
            this.color = color;
        }

        @Override
        public void run() {
            pane.getChildren().removeAll();
            pane.getChildren().add(new Circle(pane.getWidth()/2,pane.getHeight()/2, (pane.getHeight()/2) - (pane.getHeight()/8), color));
           // button.setText(mark);
        }
    }

    /**
     * Add a new move to the move history to show on screen.
     * @param move
     */
    private void addToMoveHistory(Move move) {
        moveHistory.add(move.getPlayer().getName() + " moved X: " + move.getX() + " Y: " + move.getY());
        ObservableList<String> observableMoveHistory = FXCollections.observableArrayList(moveHistory);
        ListV.setItems(observableMoveHistory);
    }

    /**
     * Sets reference to the controller and sets the model/observable related variables it needs to know (before the first notify() is called)
     * @param controller
     * @param game
     */
    public void setController(GameController controller, GameInterface game) {
        this.gamecontroller = controller;
        controller.registerView(this);

        this.boardSize = game.getBoard().getSize();
        Platform.runLater( new Runnable() {

            @Override
            public void run() { //Creates the GUI elements and fills the grid with relevant information. Done on the GUI thread
                createDisplayElements();
                System.out.println("Initialized");
                drawGrid(game.getBoard());
            }
        });
    }
    @FXML
    private void forfeit(){ //Deprecated -> will never be used. Please remove.
        try {
            Server.getInstance().forfeit();
        } catch (IOException e) {
            System.out.println("Error while forfeiting.");
            e.printStackTrace();
        }
        ViewController.getInstance().activate("homeView");
        System.out.println("Player forfeit.");

    }


    public void setPlayerNames(String player1, String player2) {
        this.Player1T.setText("Player 1: " + player1);
        this.Player2T.setText("Player 2: " + player2);
    }

    public void showEndScreen(String message){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game is done!");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
                ViewController.getInstance().activate("homeView");
                ViewController.getInstance().removeView("gameView");
            }
        });

    }
}
