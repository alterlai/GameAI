package Server;

import Game.Player;
import MainControllers.GameControllerInterface;
import OtherControllers.GameController;

public class GameHandler {
    private GameControllerInterface gameController;
    private static GameHandler gameHandlerSingelton = new GameHandler();

    private GameHandler(){};

    public static GameHandler getInstance(){return gameHandlerSingelton;}

    public void initGameController(Player starter, Player opponent, String nameGame){
        gameController = new GameController(starter, opponent, nameGame);
    };

    public GameControllerInterface getGameController() {
        return gameController;
    }

    public void removeGameController(){
        gameController = null;
    }
}
