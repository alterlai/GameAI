package GUI;

import Game.AbstractBoard;
import Game.GameInterface;
import OtherControllers.GameController;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

import Game.Move;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.function.Function;

public class GameBoardHandler implements Initializable, Observer {

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

    // List of strings to use in the listview of movehistory
    private ArrayList<String> moveHistory = new ArrayList<>();

    private GameController gamecontroller;

    private int boardSize;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
            for(int newrow = 1; newrow <= X-3; newrow++) {
                GameB.setPrefHeight( GameB.getPrefHeight() + 100);
                RowConstraints row = new RowConstraints();
                row.setPrefHeight(100);
                GameB.getRowConstraints().add(row);
            }

            for(int newcol = 1; newcol <= Y-3; newcol++) {
                GameB.setPrefWidth( GameB.getPrefWidth() + 100);
                ColumnConstraints colum = new ColumnConstraints();
                colum.setPrefWidth(100);
                GameB.getColumnConstraints().add(colum);
            }

            moveNode(Player1T, 0);
            moveNode(Player2T, 0);
            moveNode(ListV, 1);
            moveNode(ForfeitB, GameB.getColumnConstraints().size());

        }

        for(int y = 0; y < Y; y++) {
            for(int x = 0; x < X; x++){
                int pos = ((y) * X)  +  x;
                Button btn = new Button( " ");
                btn.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                btn.setId(String.valueOf(pos));

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        gamecontroller.registerMove(pos);
                    }
                });
                System.out.println(btn);
                GameB.add(btn,x,y);
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
            Button selectedButton = (Button) GameB.lookup("#" + i);
            final String mark = Character.toString(boardVals[i]);
            tasks.add(new testrun(mark, selectedButton));
        }
        for (testrun r : tasks) {
            Platform.runLater(r);
        }
    }

    class testrun implements Runnable {
        String mark;
        Button button;
        public testrun(String mark, Button button) {
            this.mark = mark;
            this.button = button;
        }

        @Override
        public void run() {
            button.setText(mark);
        }
    }

/**
 * Add a new move to the move history to show on screen.
 * @param move
 */
private void addToMoveHistory(Move move) {
        moveHistory.add(move.getPlayer().getName() + "moved X: " + move.getX() + " Y: " + move.getY());
        ObservableList<String> observableMoveHistory = FXCollections.observableArrayList(moveHistory);
        ListV.setItems(observableMoveHistory);
        }



    private void moveNode(Node Node, int rij){
        GameB.getChildren().remove(Node);
        GameB.add(Node,GameB.getRowConstraints().size(),rij);
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
    private void Forfeit(){ //Deprecated -> will never be used. Please remove.
        System.out.println("Player forfeit.");
    }


    public void setPlayerNames(String player1, String player2) {
        this.Player1T.setText("Player 1: " + player1);
        this.Player2T.setText("Player 2: " + player2);
    }

    public void showEndScreen(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game is done!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        //ViewController.getInstance().activate("homeView");
    }
}
