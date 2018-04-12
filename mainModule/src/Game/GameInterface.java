package Game;

import java.util.ArrayList;
import java.util.Observer;
import java.util.concurrent.ExecutionException;


public interface GameInterface {
    void registerView(Observer view);
    void playMove(Move move);

    ArrayList<Move> getMoveHistory();

    AbstractBoard getBoard();
    Move findBestMove(Player player) throws InterruptedException, ExecutionException;

    Move createMove(int x, int y, Player player);
    Boolean isValid(Move move);

    Player getPlayer1();
    Player getPlayer2();
}