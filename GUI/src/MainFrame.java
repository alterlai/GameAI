import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MainFrame extends Application {

    Scene mainScene;
    ViewController viewController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vbox = new VBox();
        this.mainScene = new Scene(vbox, 400, 400);
        Button btn = new Button("Test button");
        btn.setOnAction(e -> {viewController.activate("tweede");});
        vbox.getChildren().add(btn);

        viewController = new ViewController(mainScene);
        viewController.addView("hoofdscherm", vbox);
        viewController.addView("tweede", tweede());
        viewController.activate("hoofdscherm");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public Pane tweede() {
        VBox vbox = new VBox();
        vbox.getChildren().add(new Label("De scene is geswitched"));
        return vbox;
    }

}
