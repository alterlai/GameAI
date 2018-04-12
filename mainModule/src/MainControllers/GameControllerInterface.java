package MainControllers;

import Game.Move;
import Game.Player;

public interface GameControllerInterface {
    public void init(Player local, Player opponent, String nameGame);

    public Move getMove() throws InterruptedException;

    public void moveSucces(Move move);

    public void endGame();

    public Player getPlayer(int number);
}
