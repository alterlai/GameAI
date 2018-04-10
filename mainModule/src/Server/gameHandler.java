package Server;

import MainControllers.GameControllerInterface;
import otherControllers.GameController;

public class GameHandler {
    private GameControllerInterface gameController;
    private static GameHandler gameHandlerSingelton = new GameHandler();

    private GameHandler(){};

    public static GameHandler getInstance(){return gameHandlerSingelton;}

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
