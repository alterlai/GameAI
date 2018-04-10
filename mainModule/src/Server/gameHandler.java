package Server;

import MainControllers.GameControllerInterface;
import otherControllers.GameController;

public class GameHandler {
    private GameControllerInterface gameController;
    private static Server.GameHandler gameHandlerSingelton = new Server.GameHandler();

    private GameHandler(){};

    public static Server.GameHandler getInstance(){return gameHandlerSingelton;}

    public void initGameController(){
        if(gameController == null){
            gameController = new GameController();
        }
    };

    public GameControllerInterface getGameController() {
        return gameController;
    }

    public void removeGameController(){
        gameController = null;
    }
}
