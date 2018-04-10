package GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MainFrame extends Application {

    Scene mainScene;
    ViewController viewController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set title bar
        primaryStage.setTitle("GameClient v0.1");
        primaryStage.getIcons().add(new Image("GUI/icon.png"));
        primaryStage.setResizable(false);

        Parent homeView = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        //GameBoardHandler.setGameboard(8);
        Parent boardView = FXMLLoader.load(getClass().getResource("GameBoard.fxml"));

        this.mainScene = new Scene(homeView);

        // Handle shutdown.
        primaryStage.setOnCloseRequest(e -> {  //
            // TODO nice shutdown
        });


        viewController = ViewController.getInstance();
        viewController.setScene(mainScene);
        viewController.addView("homeView", homeView);
        viewController.addView("BoardView", boardView);
        viewController.activate("homeView");

        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

}
