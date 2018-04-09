package GUI;

import javafx.application.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Locale;


public class GameFrame extends Application {


    Scene mainScene;
    ViewController viewController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set title bar
        primaryStage.setTitle("GameClient v0.1");
        primaryStage.getIcons().add(new Image("GUI/icon.png"));
        Parent homeView = FXMLLoader.load(getClass().getResource("GameBoard.fxml"));


        Button btn = new Button();
        btn.setText("Say 'Hello World'");





        primaryStage.setResizable(false);
        this.mainScene = new Scene(homeView);


        // Handle shutdown.
        primaryStage.setOnCloseRequest(e -> {  //
            // TODO nice shutdown
        });


        viewController = ViewController.getInstance();
        viewController.setScene(mainScene);
        viewController.addView("homeView", homeView);
        viewController.activate("homeView");


        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

}