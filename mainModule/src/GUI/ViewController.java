package GUI;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.text.View;
import java.util.HashMap;

/**
 * Singleton ViewController is used for switching between scenes.
 */
public class ViewController {

    private static ViewController instance = new ViewController();
    private HashMap<String, Parent> paneMap = new HashMap<>();
    private static ViewController self = new ViewController();
    private Scene main;

    /**
     * Private constructor for singleton pattern.
     */
    private ViewController() {}

    /**
     * Set a new scene
     * @param main scene to be shown
     */
    public void setScene(Scene main) {
        this.main = main;
    }

    /**
     * Register a view to the controller. This view can later be shown
     * @param name name of the view
     * @param parent Parent of the view.
     */
    public void addView(String name, Parent parent) {
        paneMap.put(name, parent);
    }

    /**
     * Remove a view from the list.
     * @param name name of the view to be removed.
     */
    public void removeView(String name) {
        paneMap.remove(name);
    }

    /**
     * Switch to a view.
     * @param name name of the view to be switched to.
     */
    public void activate(String name) {
        main.setRoot(paneMap.get(name));
    }

    /**
     * Get an instance of the view object. Singleton pattern.
     * @return ViewController object.
     */
    public static ViewController getInstance() {
        return self;
    }
}
