package MainControllers;

import Game.Move;
import Game.Player;

public interface GameControllerInterface {
    public void init(Player opponent);

    public Move getMove(Player player) throws InterruptedException;

    public void moveSucces(Move move);

    public void endGame();
}
