package GUI;

import Server.Server;
import Server.ConfigHandler;
import Server.LobbyObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsViewHandler implements ViewActionHandler {
    @FXML private TextField serverip;
    @FXML private TextField port;
    @FXML private TextField nickname;
    @FXML private Button cancel;
    private ConfigHandler config;
    private Server server = Server.getInstance();
    private LobbyObservable lobby = LobbyObservable.getInstance();

    public SettingsViewHandler() { }

    @FXML
    public void initialize() {
        config = ConfigHandler.getInstance();
        serverip.setText(config.getIp());
        port.setText(config.getPort());
        nickname.setText(config.getNickname());
    }

    @FXML
    private void apply() {
        boolean success = false;    // Check if the new settings are correct.
        config.saveConfig(serverip.getText(), port.getText(), nickname.getText());
        // Regenerate new server connection
        try {
            String ipString = serverip.getText();
            int portInt = Integer.parseInt(port.getText());
            server.disconnect();
            server.setServerIp(ipString);
            server.setServerPort(portInt);
            server.connect();
            server.login(nickname.getText());
            success = true;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to connect to server");
            alert.setHeaderText(null);
            alert.setContentText("Unable to connect to new server");
            alert.showAndWait();
            System.err.println("Unable to disconnect. The client was probably not connected to a server.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to connect to server");
            alert.setHeaderText(null);
            alert.setContentText("Invalid port number!");
            alert.showAndWait();
        }
        if (success){
            lobby.setPlayerName(nickname.getText());
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
