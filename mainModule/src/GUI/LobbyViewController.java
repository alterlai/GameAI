package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class LobbyViewController implements ViewActionHandler{

    @FXML private ListView<String> playerList;
    @FXML private ListView<String> gameList;
    @FXML private ComboBox<String> gameModeList;
    @FXML private HBox hbox;
    private Stage stage;

    public LobbyViewController(){}

    @FXML
    public void initialize() {
       // stage = (Stage) hbox.getScene().getWindow();
        ArrayList<String> temp = new ArrayList<>();
        temp.add("String1");
        temp.add("String2");
        updateGameList(temp);
        updateGameModeList(temp);
    }

    /**
     * Handle start button action
     */
    @FXML
    private void handleStartEvent() {
        String selectedGame = gameList.getSelectionModel().getSelectedItem();
        String selectedMode = gameModeList.getValue();
        if (selectedGame == null) {
            new Popup(stage, "Pleaes select a game.");
        }
        else if (selectedMode == null) {
            new Popup(stage, "Please select a gamemode");
        }
        else {
            // TODO start spel
        }

    }

    public void updatePlayerList(List<String> playerArrayList) {
        ObservableList<String> observableList = FXCollections.observableArrayList(playerArrayList);
        playerList.setItems(observableList);
    }

    public void updateGameList(List<String> gameArrayList) {
        ObservableList<String> observableList = FXCollections.observableArrayList(gameArrayList);
        gameList.setItems(observableList);
    }

    public void updateGameModeList(List<String> gameModeArrayList) {
        ObservableList<String> observableList = FXCollections.observableArrayList(gameModeArrayList);
        gameModeList.setItems(observableList);
    }

}
