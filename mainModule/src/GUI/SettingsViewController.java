package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsViewController implements ViewActionHandler {
    @FXML private TextField serverip;
    @FXML private TextField port;
    @FXML private TextField nickname;
    @FXML private Button cancel;

    public SettingsViewController() { }

    @FXML
    public void initialize() {
        //TODO Populate input fields with values.
    }

    @FXML
    private void apply() { }

    @FXML
    private void close() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
