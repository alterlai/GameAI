package GUI;

import BKEGame.AbstractBoard;
import OtherControllers.GameController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
        updateMovehistory();
    }

    @Override
    public void update(Observable o, Object arg) { //Not safe (not a representation of the board, just rebuilds it).
        Game game = (Game) arg;
        AbstractBoard board = game.getBoard();
        //for (char[] c : ){}
        Move move = game.getMoveHistory().get(game.getMoveHistory().size() - 1);
        Button selectedButton = (Button) GameB.lookup("#" + move.getPos());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                selectedButton.setText(String.valueOf(move.getPlayer().getMark()));
            }
        });
    }



    public void updateMovehistory(){
        //ListV.setItems();

    }

    public void updateListview(String item){
        ListV.getItems().add(item);
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
}
