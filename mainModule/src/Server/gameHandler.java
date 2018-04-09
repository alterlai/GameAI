package Server;

import MainControllers.GameControllerInterface;
import otherControllers.GameController;

public class gameHandler {
    private GameControllerInterface gameController;
    static gameHandler gameHandlerSingelton = new gameHandler();

    private gameHandler(){};

    public static gameHandler getInstance(){return gameHandlerSingelton;}

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
