package Game;

import java.util.ArrayList;
import java.util.Observer;
import java.util.concurrent.ExecutionException;


/**
 * Interface specifying the behavior to which the independent game modules have to adhere to. Each game object has to implement this interface.
 */
public interface GameInterface {
    /**
     * Used to register view observer with the game observable. The game observable updates the view.
     * @param view
     */
    void registerView(Observer view);

    /**
     * Play a certain move on the board belonging to the game
     * @param move
     */
    void playMove(Move move);

    /**
     * @return An ArrayList containing all the Move objects that have been played during the game.
     */
    ArrayList<Move> getMoveHistory();

    /**
     * @return The board belonging to the game instance
     */
    AbstractBoard getBoard();

    /**
     * Finds the best move for a certain player by using a minimax algorithm.
     * @param player The player that is making the move
     * @return The best move that the player can play in the current situation.
     */
    Move findBestMove(Player player) throws InterruptedException, ExecutionException;


    /**
     * Creates a move object that can be played in the game that implements this interface. Takes boardsize into account.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param player player that the move belongs to
     * @return a Move object that can be played in the associated game
     */
    Move createMove(int x, int y, Player player);

    /**
     * Checks whether a move is legal
     * @param move
     * @return true if the move is legal, false if it is not.
     */
    Boolean isValid(Move move);

    public AbstractBoard getLegalMoveBoard();

    Player getPlayer1();
    Player getPlayer2();
}