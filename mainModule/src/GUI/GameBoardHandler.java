package GUI;

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

import BKEGame.Game;
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

        for(int x = 1; x <= X; x++) {
            for(int y = 1; y <= Y; y++){
                int nummer = ((x-1) * X)  +  y;
                Button btn = new Button( " " + nummer);
                btn.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                btn.setId(String.valueOf(nummer));

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println(btn.getId());
                        btn.setText("clicked");
                    }
                });

                GameB.add(btn,x-1,y-1);
            }
        }

        updateMovehistory();
    }

    @Override
    public void update(Observable o, Object arg) {

        Button GeselecteerdeBtn = (Button) GameB.lookup("#" + arg);
        GeselecteerdeBtn.setText("X");

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

    public void observe(Game game) {
        game.registerView(this);
    }
}
