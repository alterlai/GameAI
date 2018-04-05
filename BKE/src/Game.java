import game.Move;
import game.Player;

import java.util.Observer;
import java.util.Stack;

public interface Game {
    void registerView(Observer view);
    void playMove(Move move);

    Stack<Move> getMoveHistory();

    Board getBoard();
    Move findBestMove(Player player);
}
