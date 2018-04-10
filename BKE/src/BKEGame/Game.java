package BKEGame;
import Game.Move;
import Game.Player;
import java.util.Observer;
import java.util.Stack;

public interface Game {
    void registerView(Observer view);
    void playMove(Move move);

    Stack<Move> getMoveHistory();

    Board getBoard();
    Move findBestMove(Player player);

    Move createMove(int x, int y, Player player);
    Boolean isValid(Move move);

    Player getPlayer1();
    Player getPlayer2();
}