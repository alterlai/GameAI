package BKEGame;

import java.lang.reflect.Array;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.*;

import Game.Player;
import Game.Move;
import Game.GameInterface;
import Game.AbstractBoard;
import javafx.concurrent.Task;

public class Othello extends Observable implements GameInterface {

    private OthellloBoard board;

    private Player player1; //Starting player
    private Player player2; //Not-starting player

    private ArrayList<Move> moveHistory; //ArrayList that keeps track of successfully executed moves

    /**
     * Remembers which player is maximizing and which player is minimizing during the minimax algorithm.
     */
    private Player maximizing;
    private Player minimizing;


    private Player turn;


    private int moveCount = 0; //Move count used to determine phase of game.

    private final static int earlyGame = 15; //Amount of moves up to this are considered to be early game
    private final static int midGame = 50; //Amount of moves up to this are considered to be mid game

    private final static int earlyDepth = 5; //Search depth for early game phase
    private final static int midDepth = 4; //Search depth for mid game phase
    private final static int lateDepth = 6; //Search depth for late game phase


    /**
     * Creates a new instance of Othello.
     * @param player1 the player that is allowed to take the first turn
     * @param player2 the other player
     */
    public Othello(Player player1, Player player2) {
        board = new OthellloBoard(8);
        moveHistory = new ArrayList<Move>();

        this.player1 = player1;
        this.player2 = player2;

        this.player1.setMark('W'); //Starting player is 'white'
        this.player2.setMark('Z'); //Not-starting player is 'black';

        turn = player1;
    }

    /**
     * Switches turn to the other player
     */
    public void changeTurn() {
        if (turn == player1) {
            turn = player2;
        }
        else {
            turn = player1;
        }
    }
    /**
     * @param player The player that is making the move
     * @return the best move that the player can make in the current situation
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public Move findBestMove(Player player) throws InterruptedException, ExecutionException {

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

        ArrayList<Callable> Calculations = new ArrayList<>();
        ArrayList<Future> Futures = new ArrayList<>();
        for (Move move : Moves) {
            OthellloBoard newState = new OthellloBoard(this.board);
            newState.playMove(move);

            int searchDepth = earlyDepth;
            if (moveCount > earlyGame) {
                searchDepth = midDepth;
            }
            if (moveCount > midGame) {
                searchDepth = lateDepth;
            }

            scoreCalculator c = new scoreCalculator(move, newState, !Maxer, searchDepth, minimizing, maximizing);
            Calculations.add(c);
        }

        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
        ArrayList<Thread> running = new ArrayList<>();
        for (Callable t : Calculations) {
            Futures.add(es.submit(t));
        }

        currentBestScore = Integer.MIN_VALUE;
        for (Future f : Futures) {
            try {
                Object o = f.get();
                Object[] obj = (Object[]) o;
                int moveScore = (Integer)obj[1];
                if (moveScore >= currentBestScore) {
                    currentBestScore = moveScore;
                    currentBestMove = (Move) obj[0];
                }
            }
            catch(Exception e) {
                break;
            }
        }
        es.shutdown();
        es.awaitTermination(10, TimeUnit.SECONDS);

        return currentBestMove;

    }

    @Override
    public void registerView(Observer view) { this.addObserver(view); }

    @Override
    public ArrayList<Move> getMoveHistory() { return this.moveHistory; }


    public AbstractBoard getBoard() { return this.board; }

    public AbstractBoard getLegalMoveBoard() {
        ArrayList<Move> legalMoves = board.getValidMoves(turn);
        AbstractBoard tempBoard = new OthellloBoard(this.board);
        for (Move move : legalMoves) {
            tempBoard.setLegal(move);
        }
        return tempBoard;
    }

    @Override
    public Player getPlayer1() { return this.player1; }

    @Override
    public Player getPlayer2() { return this.player2; }

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
        moveCount++;
        changeTurn();
        setChanged();
        notifyObservers(this); //Should have a security proxy.
    }
}

/**
 * A Callable object which calculates the gametree from a certain move.
 */
class scoreCalculator implements Callable<Object[]> {
    private int score;
    private int depth;

    private OthellloBoard currentState;
    private Boolean Maxer;
    private Player player;
    private Player opponent;

    private Move belongsTo;

    public scoreCalculator(Move belongsTo, OthellloBoard currentState, Boolean Maxer, int depth, Player player, Player opponent) {
        this.currentState = currentState;
        this.Maxer = Maxer;
        this.depth = depth;
        this.player = player;
        this.opponent = opponent;
        this.belongsTo = belongsTo;
    }

    /**
     * Evaluates the state of the board
     * @param board the board to evaluate
     * @param player evaluates from player's perspective
     * @return the score of the board as an int
     */
    public int eval(OthellloBoard board, Player player) { //Player is a temporary parameter.
        return board.evalBoard(player);
    }


    /**
     * The recursive minimaxing algorithm. DFS throughout the gametree. Determines the worth of a certain move by examining all possible following gamestates.
     * @param currentState Current state of the board.
     * @param Maxer True when selecting move for the initializer (person taking the turn - maximizing), false when selecting for the initial opponent (minimizing)
     * @param depth Amount of layers that the tree should consist of.
     * @param player Person making a move
     * @param opponent Person not making a move
     * @return the final score of a certain node as an int
     */

    public int getFinalScore(OthellloBoard currentState, Boolean Maxer, int depth, Player player, Player opponent) {
        ArrayList<Move> Moves = currentState.getValidMoves(player);

        if (depth == 0) {
            if (Maxer) {
                return eval(currentState, player);
            }
            else {
                return eval(currentState, opponent);

            }
        }
        else if (Moves.isEmpty()) {
            if (currentState.getValidMoves(opponent).isEmpty()) { //When a stalemate has been reached the game ends.
                if (Maxer) {
                    return eval(currentState, player);
                }
                else {
                    return eval(currentState, opponent);

                }
            }
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

    @Override
    public Object[] call() {
        score = getFinalScore(currentState, Maxer, depth, player, opponent);
        Object[] returnObj = new Object[2];
        returnObj[0] = belongsTo;
        returnObj[1] = score;

        return returnObj;
    }

    public int getScore() { return score;}
}