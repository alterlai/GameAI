package Game;
import BKEGame.Board;
import Game.Move;
import Game.Player;

import java.util.ArrayList;
import java.util.Observer;


public interface Game {
    void registerView(Observer view);
    void playMove(Move move);

    ArrayList<Move> getMoveHistory();

    Board getBoard();
    Move findBestMove(Player player);

    Move createMove(int x, int y, Player player);
    Boolean isValid(Move move);

    Player getPlayer1();
    Player getPlayer2();
}