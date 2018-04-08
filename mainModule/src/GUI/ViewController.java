package GUI;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.text.View;
import java.util.HashMap;

/**
 * Singleton ViewController is used for switching scenes.
 */
public class ViewController {

    private static ViewController instance = new ViewController();
    private HashMap<String, Parent> paneMap = new HashMap<>();
    private static ViewController self = new ViewController();
    private Scene main;

    // Private constructor for singleton pattern.
    private ViewController() {}

    public void setScene(Scene main) {
        this.main = main;
    }

    public void addView(String name, Parent parent) {
        paneMap.put(name, parent);
    }

    public void removeView(String name) {
        paneMap.remove(name);
    }

    public void activate(String name) {
        main.setRoot(paneMap.get(name));
    }

    public static ViewController getInstance() {
        return self;
    }
}
