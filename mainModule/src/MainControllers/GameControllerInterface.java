package MainControllers;

import Game.Move;
import Game.Player;

/**
 * Defines the behaviour of the game controller. Allows for better decoupling, less dependency issues.
 */

public interface GameControllerInterface {

    public Move getMove() throws InterruptedException;

    public void moveSucces(Move move);

    public void endGame(int status);

    public Player getPlayer(int number);
}
