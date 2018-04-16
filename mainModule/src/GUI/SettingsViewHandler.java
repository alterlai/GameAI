package GUI;

import Server.Server;
import Server.GameMessageHandler;
import Server.ConfigHandler;
import Server.LobbyObservable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.text.View;
import java.io.IOException;

public class SettingsViewHandler implements ViewActionHandler {
    @FXML private TextField serverip;
    @FXML private TextField port;
    @FXML private TextField nickname;
    @FXML private Button cancel;
    @FXML private CheckBox AICheckbox;
    private Server server = Server.getInstance();
    private LobbyObservable lobby = LobbyObservable.getInstance();

    public SettingsViewHandler() { }

    @FXML
    public void initialize() {
        // Register the handler.
        ViewHandlers.getInstance().registerHandler("SettingsView", this);
        Server server = Server.getInstance();
        serverip.setText(server.getServerIp());
        port.setText("" +server.getServerPort());
        nickname.setText(server.getPlayerName());
        AICheckbox.setSelected(server.getIsAI());
    }

    @FXML
    private void apply() {
        boolean success = false;    // Check if the new settings are correct.
        // Regenerate new server connection
        try {
            String ipString = serverip.getText();
            int portInt = Integer.parseInt(port.getText());
            server.disconnect();
            server.setServerIp(ipString);
            server.setServerPort(portInt);
            server.connect();
            server.login(nickname.getText());
            server.setIsAI(AICheckbox.isSelected());
            success = true;
        } catch (IOException e) {
            showErrorPopup("Unable to connect to new server");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch(NumberFormatException e) {
            showErrorPopup("Invalid port number");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success){
            // Update variables in lobby view.
            try {
                server.saveConfig();
                new Thread(LobbyObservable.getInstance()).start();      // Start a new Lobby with new references.
                LobbyViewHandler lobbyView = (LobbyViewHandler) ViewHandlers.getInstance().getHandler("LobbyView");
                lobbyView.update(null, null);

            } catch (HandlerNotRegisterdException e) {
                System.err.println("Lobby view has not been loaded.");
                e.printStackTrace();
            }


            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();


        }
    }

    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unable to connect to server");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void close() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
