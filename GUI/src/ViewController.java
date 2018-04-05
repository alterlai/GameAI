import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;

public class ViewController {

    private HashMap<String, Parent> paneMap = new HashMap<>();
    private Scene main;

    public ViewController(Scene main) {
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
}
