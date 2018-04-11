package GUI;

import Game.AbstractBoard;
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

import Game.Game;
import Game.Move;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

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

    static int BoardSize = 3;


    public static void setGameboard(int grote){
        BoardSize = grote;
    }

    public int getGameboard(){
        return (BoardSize);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int X = getGameboard();
        int Y = getGameboard();

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

            movenode(Player1T, 0);
            movenode(Player2T, 0);
            movenode(ListV, 1);
            movenode(ForfeitB, GameB.getColumnConstraints().size());


        }

        for(int y = 0; y < Y; y++) {
            for(int x = 0; x < X; x++){
                int nummer = ((y) * X)  +  x;
                Button btn = new Button( " ");
                btn.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                btn.setId(String.valueOf(nummer));

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        gamecontroller.registerMove(nummer);
                    }
                });
                System.out.println(btn);
                GameB.add(btn,x,y);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) { //Not safe (not a representation of the board, just rebuilds it).
        Game game = (Game) arg;
        AbstractBoard board = game.getBoard();
        //for (char[] c : ){}
        Move move = game.getMoveHistory().get(game.getMoveHistory().size()-1);
        Button selectedButton = (Button) GameB.lookup("#" + move.getPos());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                selectedButton.setText(String.valueOf(move.getPlayer().getMark()));
                addToMoveHistory(game.getMoveHistory().peek());
            }
        });
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

    private void movenode(Node Node, int rij){
        GameB.getChildren().remove(Node);
        GameB.add(Node,GameB.getRowConstraints().size(),rij);
    }

    @FXML
    private void Forfeit(){
        //handlefor feit action

        //GameController.foerfeit();
        System.out.println("Player forfeit.");
    }

    public void setController(GameController controller) {
        this.gamecontroller = controller;
        controller.registerView(this);
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
