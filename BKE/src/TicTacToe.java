
import game.Move;
import game.Player;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

public class TicTacToe extends Observable implements Game {

    private Board board;

    private Player player1;
    private Player player2;

    /**
     * ArrayList that keeps track of successfully executed moves
     */
    private Stack<Move> moveHistory;

    /**
     * Remembers which player is maximizing and which player is minimizing during the minimax algorithm.
     */
    private Player maximizing;
    private Player minimizing;

    /**
     * Creates a new game of Tic Tac Toe. Initializes the marks to represent the two players.
     * @param player1
     * @param player2
     */
    public TicTacToe(Player player1, Player player2) {
        board = new Board();
        moveHistory = new Stack<Move>();

        this.player1 = player1;
        this.player2 = player2;

        this.player1.setMark('X');
        this.player2.setMark('O');
    }


    /**
     * Entry point into the recursive minimaxing algorithm getFinalScore(). Determines the best next move and plays it.
     * @param player Player of which to determine the best next move.
     * @return Best next possible move
     */
    public Move findBestMove(Player player) {

        if (player == player1) {
            maximizing = player1;
            minimizing = player2;
        }
        else {
            maximizing = player2;
            minimizing = player1;
        }
        Boolean Maxer = true;

        ArrayList<Move> Moves = board.getValidMoves(player);

        int currentBestScore = Integer.MIN_VALUE;
        Move currentBestMove = null;

        for (Move move : Moves) {
            Board newState = new Board(board);
            newState.playMove(move);
            int moveScore = getFinalScore(newState, !Maxer, 0, minimizing, maximizing);
                if (moveScore > currentBestScore) {
                    currentBestScore = moveScore;
                    currentBestMove = move;
                }
        }

        return currentBestMove;
    }


    /**
     * The recursive minimaxing algorithm. DFS throughout the gametree. Determines the worth of a certain move by examining all possible following gamestates.
     * @param currentState Current state of the board.
     * @param Maxer True when selecting move for the initializer (person taking the turn - maximizing), false when selecting for the initial opponent (minimizing)
     * @param count Used to penalize favorable gamestates that are more moves away. Favours a win in a single move over a win in two moves.
     * @return
     */

    public int getFinalScore(Board currentState, Boolean Maxer, int count, Player player, Player opponent) {
        count++;

        count = 0; //count caused the a.i. to start out with the wrong move (1, 1) -> fix this

        ArrayList<Move> Moves = currentState.getValidMoves(player);

        if (currentState.Win(true, maximizing, minimizing)) {
            return 10 - count;
        }
        else if (currentState.Lose(true, maximizing, minimizing)) {
            return count - 10;
        }
        else if (currentState.Draw()) {
            return 0;
        }

        int bestValue;

        if (Maxer) {
            bestValue = Integer.MIN_VALUE;
        }
        else {
            bestValue = Integer.MAX_VALUE;
        }

        for (Move move : Moves) {
            Board newState = new Board(currentState);
            newState.playMove(move);

            int MoveScore = getFinalScore(newState, !Maxer, count, opponent, player);
                if (Maxer) {
                    if (MoveScore > bestValue) {
                        bestValue = MoveScore - count;
                    }
                }
                else {
                    if (MoveScore < bestValue) {
                        bestValue = count + MoveScore;
                    }
                }
            }
            return bestValue;

    }

    public Move createMove(int x, int y, Player player) { return new Move(x, y, board.getSize(), player); }
    public Boolean isValid(Move move) {return board.isValid(move);}

    /**
     * Plays a certain move. Only accessed through controller which means that these moves have been played on the server and are "valid"
     * @param move Move to play
     */
    public void playMove(Move move) {
        board.playMove(move);
        moveHistory.add(move);
        setChanged();
        notifyObservers(this); //Should have a security proxy.
    }

    public void registerView (Observer view) {addObserver(view);
    }

    /**
     * Getter for the moveHistory stack
     * @return moveHistory stack
     */
    public Stack<Move> getMoveHistory() {
        return moveHistory;
    }

    /**
     * @return A copy of the current board state. Not a reference to the real board, risk of entities manipulating it from outside.
     */
    @Override
    public Board getBoard() {
        return new Board(this.board);
    }


}
