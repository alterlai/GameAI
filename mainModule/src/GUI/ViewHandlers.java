package GUI;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jeroe on 12-4-2018.
 * Singleton object for registering viewHandlers. This makes it convenient to request view handlers.
 */
public class ViewHandlers {

    private static ViewHandlers self = new ViewHandlers();
    private static HashMap<String, ViewActionHandler> handlers = new HashMap<>();

    /**
     * Private constructor for singleton pattern
     */
    private ViewHandlers() {

    }

    /**
     * Return instance of this object.
     * @return
     */
    public static ViewHandlers getInstance() {
        return self;
    }

    /**
     * Get a handler from a view.
     * @param name
     * @return
     * @throws HandlerNotRegisterdException
     */
    public ViewActionHandler getHandler(String name) throws HandlerNotRegisterdException {
        if (handlers.get(name) == null) {
            throw new HandlerNotRegisterdException();
        }
        else {
            return handlers.get(name);
        }
    }

    /**
     * Register a handler
     * @param name  Name of the handler.
     * @param handler Handler to be registered.
     */
    public void registerHandler(String name, ViewActionHandler handler) {
        handlers.put(name, handler);
    }
}
