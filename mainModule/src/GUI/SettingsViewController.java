package GUI;

import javafx.fxml.FXML;

public class SettingsViewController implements ViewActionHandler {
    @FXML private String serverip;
    @FXML private String port;
    @FXML private String nickname;

    public SettingsViewController() { }

    @FXML
    public void initialize() {
        //TODO Populate input fields with values.
    }

    @FXML
    private void apply() { }

    @FXML
    private void close() {


    }
}
