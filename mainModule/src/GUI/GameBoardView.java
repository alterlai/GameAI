package GUI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class GameBoardView extends Application {


//     static int BoardSize = 3;

    Scene mainScene;
    ViewController viewController;


//    public void setGameboard(int grote){
//        this.BoardSize = grote;
//
//    }
//
//    public static int getGameboard(){
//
//        return (BoardSize);
//    }





    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set title bar

        primaryStage.setTitle("GameClient v0.1");
        primaryStage.getIcons().add(new Image("GUI/icon.png"));
        Parent homeView = FXMLLoader.load(getClass().getResource("GameBoard.fxml"));
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

