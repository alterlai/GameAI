package GUI;
import Server.Server;
import Server.LobbyObservable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;




public class LobbyViewController implements ViewActionHandler, Observer{

    @FXML private ListView<String> playerList;
    @FXML private ListView<String> gameList;
    @FXML private ComboBox<String> gameModeList;
    LobbyObservable lobby;

    public LobbyViewController(){}

    @FXML
    public void initialize() {
        //Create server
        Server server = Server.getInstance();
        try {
            server.connect();
            server.login("Jeroen");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to connect");
            alert.setHeaderText(null);
            alert.setContentText("Unable to connect to server");

            alert.showAndWait();
            e.printStackTrace();
        }
        if (server.isConnected()) {
            // Create lobby
            lobby = LobbyObservable.getInstance();
            lobby.addObserver(this);
            Thread thread = new Thread(lobby);
            thread.start();
            try {
                updateGameList(server.getGameList());
            } catch (Exception e) {
                e.printStackTrace();
            }
           // updatePlayerList(lobby.getPlayerList());
        }
    }

    /**
     * Handle start button action
     */
    @FXML
    private void handleStartEvent() {
        String selectedGame = gameList.getSelectionModel().getSelectedItem();
        String selectedMode = gameModeList.getValue();
        if (selectedGame == null) {
            //new Popup(stage, "Pleaes select a game.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to start game.");
            alert.setHeaderText(null);
            alert.setContentText("Please select a game to play.");
            alert.showAndWait();
        }
        else if (selectedMode == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to start game.");
            alert.setHeaderText(null);
            alert.setContentText("Please select a gamemode to play.");
            alert.showAndWait();
        }
        else {
            // TODO start spel
        }

    }

    public void updatePlayerList(List<String> playerArrayList) {
        ObservableList<String> observableList = FXCollections.observableArrayList(playerArrayList);
        for(String name: playerArrayList) {
            System.out.println(name);
        }
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

    @Override
    public void update(Observable o, Object arg) {
        updatePlayerList(lobby.getPlayerList());
    }
}
