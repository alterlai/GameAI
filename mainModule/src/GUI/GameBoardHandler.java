package GUI;

import Game.AbstractBoard;
import Game.GameInterface;
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

import Game.Move;
import java.net.URL;
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
        updateMovehistory();
    }

    @Override
    public void update(Observable o, Object arg) { //Not safe (not a representation of the board, just rebuilds it).
        GameInterface gameInterface = (GameInterface) arg;
        AbstractBoard board = gameInterface.getBoard();
        drawGrid(board);

    }

    public void drawGrid(AbstractBoard board) {
        char[] boardVals = board.getCells1D();
        for (int i = 0; i < this.boardSize * this.boardSize; i++) {
            Button selectedButton = (Button) GameB.lookup("#" + i);
            final int pos = i;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    selectedButton.setText(Character.toString(boardVals[pos]));
                }
            });
        }
    }

    public void updateMovehistory(){
        //ListV.setItems();

    }

    public void updateListview(String item){
        ListV.getItems().add(item);
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
}
