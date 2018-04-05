import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;


public class MainFrame extends Application {

    Scene mainScene;
    ViewController viewController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent homeView = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        this.mainScene = new Scene(homeView);

        viewController = new ViewController(mainScene);
        viewController.addView("homeView", homeView);

        //test code
        VBox vbox = new VBox();
        viewController.addView("tweede", vbox);

        viewController.activate("homeView");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

}
