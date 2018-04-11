package BKEGame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import Game.Player;
import Game.Move;
import Game.GameInterface;
import Game.AbstractBoard;

public class Othello extends Observable implements GameInterface {

    private OthellloBoard board;

    private Player player1;
    private Player player2;

    /**
     * ArrayList that keeps track of successfully executed moves
     */
    private ArrayList<Move> moveHistory;

    /**
     * Remembers which player is maximizing and which player is minimizing during the minimax algorithm.
     */
    private Player maximizing;
    private Player minimizing;

    public Othello(Player player1, Player player2) {
        board = new OthellloBoard(8);
        moveHistory = new ArrayList<Move>();

        this.player1 = player1;
        this.player2 = player2;

        this.player1.setMark('Z');
        this.player2.setMark('W');

    }

    @Override
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
            OthellloBoard newState = new OthellloBoard(this.board);
            newState.playMove(move);
            int moveScore = getFinalScore(newState, !Maxer, 5, minimizing, maximizing);
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
     *        depth
     * @return
     */

    public int getFinalScore(OthellloBoard currentState, Boolean Maxer, int depth, Player player, Player opponent) {

        ArrayList<Move> Moves = currentState.getValidMoves(player);

        if (depth == 0) {
            return eval(currentState);
        }

        int bestValue;

        if (Maxer) {
            bestValue = Integer.MIN_VALUE;
        }
        else {
            bestValue = Integer.MAX_VALUE;
        }

        for (Move move : Moves) {
            OthellloBoard newState = new OthellloBoard(currentState);
            newState.playMove(move);

            int MoveScore = getFinalScore(newState, !Maxer, depth - 1, opponent, player);
            if (Maxer) {
                if (MoveScore > bestValue) {
                    bestValue = MoveScore;
                }
            }
            else {
                if (MoveScore < bestValue) {
                    bestValue = MoveScore;
                }
            }
        }
        return bestValue;

    }

    public int eval(OthellloBoard board) {
        //Todo, implement evaluation of board state. Maybe move this function to OthelloBoard.
        return 0;
    }

    @Override
    public void registerView(Observer view) {
        this.addObserver(view);
    }

    @Override
    public ArrayList<Move> getMoveHistory() {
        return this.moveHistory;
    }


    public AbstractBoard getBoard() {
        return this.board;
    }

    @Override
    public Player getPlayer1() {
        return this.player1;
    }

    @Override
    public Player getPlayer2() {
        return this.player2;
    }

    @Override
    public Boolean isValid(Move move) {
        move.makePlayable(board.getSize());
        return this.board.isValid(move);
    }

    @Override
    public Move createMove(int x, int y, Player player) {
        return new Move(x, y, board.getSize(), player);
    }

    @Override
    public void playMove(Move move) {
        move.makePlayable(board.getSize());
        board.playMove(move);
        moveHistory.add(move);
        board.print();
        setChanged();
        notifyObservers(this); //Should have a security proxy.
    }
}
