import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class ViewController {

    private HashMap<String, Pane> paneMap = new HashMap<>();
    private Scene main;

    public ViewController(Scene main) {
        this.main = main;
    }

    public void addView(String name, Pane pane) {
        paneMap.put(name, pane);
    }

    public void removeView(String name) {
        paneMap.remove(name);
    }

    public void activate(String name) {
        main.setRoot(paneMap.get(name));
    }
}
