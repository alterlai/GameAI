package GUI;
import Server.Server;
import Server.Challenge;
import Server.LobbyObservable;
import Server.MessageHandler;
import Server.GameMessageHandler;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.Semaphore;


public class LobbyViewHandler implements ViewActionHandler, Observer{

    @FXML private ListView<String> playerList;
    @FXML private ListView<String> gameList;
    @FXML private ComboBox<String> challengeGameList;
    ArrayList<String> gamemodes;
    private Server server;
    LobbyObservable lobby;

    private Semaphore challengeLock = new Semaphore(1);

    public LobbyViewHandler(){}

    @FXML
    public void initialize() {
        // Register this controller to the viewHandlers.
        ViewHandlers.getInstance().registerHandler("LobbyView", this);

        //Create server
        server = Server.getInstance();
        try {
            if(!server.isConnected()){
                server.connect();
                server.login(server.getPlayerName());
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to connect");
            alert.setHeaderText(null);
            alert.setContentText("Unable to connect to server");

            alert.showAndWait();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lobby = LobbyObservable.getInstance();
        lobby.addObserver(this);
        if (server.isConnected()) {
            // Create lobby
            Thread serverThread = new Thread(server);
            serverThread.start();

            //lobby.setPlayerName(server.getPlayerName());
            Thread thread = new Thread(lobby);
            thread.start();
            try {
                updateGameList(server.getGameList());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                challengeGameList.setItems(FXCollections.observableArrayList(server.getGameList()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        showErrors(LobbyObservable.getInstance().getErrorList());
    }

    /**
     * Handle start button action
     */
    @FXML
    private void handleStartEvent() {
        String selectedGame = gameList.getSelectionModel().getSelectedItem();
        if (selectedGame == null) {
            //new Popup(stage, "Pleaes select a GameInterface.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to start GameInterface.");
            alert.setHeaderText(null);
            alert.setContentText("Please select a GameInterface to play.");
            alert.showAndWait();
        }
        else {
            try {
                server.subscribe(selectedGame);
                if (MessageHandler.lastMessageStatus()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Subscribed to GameInterface.");
                    alert.setHeaderText(null);
                    alert.setContentText("You are now subscribed to the GameInterface " + selectedGame + "\nYou will be notified when a GameInterface is ready.");
                    alert.showAndWait();
                }
                else {
                    throw new Exception("Server did not return OK");
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Unable to start GameInterface.");
                alert.setHeaderText(null);
                alert.setContentText("Unable to subscribe to GameInterface.");
                alert.showAndWait();
                e.printStackTrace();

            }
        }

    }

    // Handle challenge button
    @FXML
    private void challenge() {
        String selectedPlayer = playerList.getSelectionModel().getSelectedItem();
        String gameMode = challengeGameList.getSelectionModel().getSelectedItem();
        //Check wether a player has been selected
        if(selectedPlayer == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to start GameInterface.");
            alert.setHeaderText(null);
            alert.setContentText("Please select a player to challenge.");
            alert.showAndWait();
        }
        // Check wether a GameInterface has been selected to play
        else if(gameMode == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to start GameInterface.");
            alert.setHeaderText(null);
            alert.setContentText("Please select a GameInterface to challenge a player");
            alert.showAndWait();
        }
        else {
            try {
                server.challenge(selectedPlayer, gameMode);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Unable to start GameInterface.");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong. Please try again.");
                alert.showAndWait();
                e.printStackTrace();
            }
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
        Platform.runLater(() -> {
            challengeGameList.setItems(observableList);
            gameList.setItems(observableList);
        });
    }

    private void showErrors(ArrayList<String> errors) {
        if (errors.size() > 0) {
            String errorString = "";
            for (String error : errors) {
                errorString += error + "\n";
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error occured");
            alert.setHeaderText(null);
            alert.setContentText(errorString);
            alert.showAndWait();
        }
    }

    public void displayChallenges(List<Challenge> challenges) {
        for (int i = 0; i < challenges.size(); i++) { //Iterating through it normally so that we can remove elements during the loop. Chance of challenges reappearing despite being handled already otherwise..
            Challenge challenge = challenges.get(i);
            challenges.remove(i); //Prevents the challenge being displayed twice if the observable notifies again while notificiation is still visible

            String contentText = "User " + challenge.getPlayerName() + " has challenged you to a GameInterface of " + challenge.getGame() +"!";
            ButtonType btnPlayer = new ButtonType("Play as Player", ButtonBar.ButtonData.RIGHT);
            ButtonType btnAI = new ButtonType("Play as AI", ButtonBar.ButtonData.HELP);
            ButtonType btnNo = new ButtonType("Decline", ButtonBar.ButtonData.LEFT);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contentText, btnPlayer, btnNo,  btnAI);
            alert.setTitle("Challenge!");
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();

            // User clicked Play as AI
            if (result.get().getButtonData() == ButtonBar.ButtonData.HELP) {
                try {
                    server.setIsAI(true);
                    server.acceptChallenge(challenge);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Clicked play as Player
            if (result.get().getButtonData() == ButtonBar.ButtonData.RIGHT) {
                try {
                    server.setIsAI(false);
                    server.acceptChallenge(challenge);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            updateGameList(server.getGameList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updatePlayerList(lobby.getPlayerList());
                displayChallenges(lobby.getChallengesList());
                showErrors(LobbyObservable.getInstance().getErrorList());
            }
        });
    }
}
