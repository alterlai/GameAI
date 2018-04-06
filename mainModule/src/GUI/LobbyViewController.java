package GUI;
import Server.Server;
import Server.LobbyObservable;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
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
        Server server = new Server();
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
            lobby = new LobbyObservable(server);
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

    @FXML
    private void openSettings() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("SettingsMenu.fxml"));
            Scene menu = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Settings menu");
            stage.setScene(menu);
            stage.show();
        } catch(IOException e) {
            throw new RuntimeException("Missing SettingsMenu.fxml");
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

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updatePlayerList(lobby.getPlayerList());
            }
        });
    }
}
