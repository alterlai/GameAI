package Server;

import Game.Player;
import MainControllers.GameControllerInterface;
import OtherControllers.GameController;

/**
 * <H1> Gamehandler</H1>
 * Handles the init of new games
 * @author Rudolf Klijnhout
 * @version 1.0
 * @since 16-04-2018
 **/

public class GameHandler {
    private GameControllerInterface gameController;
    private static GameHandler gameHandlerSingelton = new GameHandler();

    private GameHandler(){};

    public static GameHandler getInstance(){return gameHandlerSingelton;}

    /**
     * Inits a new game controler
     * @param starter Player starting player
     * @param opponent Player player 2
     * @param nameGame String the name of the game
     */
    public void initGameController(Player starter, Player opponent, String nameGame){
        gameController = new GameController(starter, opponent, nameGame);
    };

    /**
     * This method is used to get the gameController object
     * @return GameControllerInterface gamecontroller
     */
    public GameControllerInterface getGameController() {
        return gameController;
    }

    /**
     * This method is used to delete a game controller.
     */
    public void removeGameController(){
        gameController = null;
    }
}
