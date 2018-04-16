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

    private int moveCount = 0;
    private final static int earlyGame = 15;
    private final static int midGame = 45;

    private final static int earlyDepth = 5;
    private final static int midDepth = 4;
    private final static int lateDepth = 6;



    //Temporary for testing purposes
    public int calculations = 0;
    public int evalcount = 0;

    public Othello(Player player1, Player player2) {
        board = new OthellloBoard(8);
        moveHistory = new ArrayList<Move>();

        this.player1 = player1;
        this.player2 = player2;

        this.player1.setMark('W');
        this.player2.setMark('Z');

    }

    @Override
    public Move findBestMove(Player player) throws InterruptedException, ExecutionException {
        calculations = 0;
        evalcount = 0;
        long start = System.currentTimeMillis();

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
            else if (moveCount > midGame) {
                searchDepth = lateDepth;
            }

            scoreCalculator c = new scoreCalculator(move, newState, !Maxer, searchDepth, minimizing, maximizing);
            Calculations.add(c);
        }
        ExecutorService es = Executors.newFixedThreadPool(4);
        ArrayList<Thread> running = new ArrayList<>();
        for (Callable t : Calculations) {
            Futures.add(es.submit(t));
        }
        int[] scores = new int[Moves.size()];
        int i = 0;
        currentBestScore = Integer.MIN_VALUE;
        for (Future f : Futures) {
            try {
                Object o = f.get();
                Object[] obj = (Object[]) o;
                scores[i] = (Integer)obj[1];
                int moveScore = scores[i];
                if (moveScore >= currentBestScore) {
                    currentBestScore = moveScore;
                    currentBestMove = (Move) obj[0];
                }
                i++;
            }
            catch(Exception e) {
                break;
            }
        }
        es.shutdown();
        es.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println(moveCount);
/**
 System.out.println("Gametree: \n -------");
 System.out.println("    Depth: 5");
 System.out.println("    Nodes: " + calculations);
 System.out.println("    Leaves: " + evalcount);
 System.out.println("    Time: " + (System.currentTimeMillis() - start) + " ms");
 System.out.println("\n \n");
 **/
        return currentBestMove;

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
        moveCount++;
        setChanged();
        notifyObservers(this); //Should have a security proxy.
    }
}

class scoreCalculator implements Callable<Object[]> {
    int calculations = 0;
    int evalcount = 0;
    int score;

    OthellloBoard currentState;
    Boolean Maxer;
    int depth;
    Player player;
    Player opponent;

    Move belongsTo;
    public scoreCalculator(Move belongsTo, OthellloBoard currentState, Boolean Maxer, int depth, Player player, Player opponent) {
        this.currentState = currentState;
        this.Maxer = Maxer;
        this.depth = depth;
        this.player = player;
        this.opponent = opponent;
        this.belongsTo = belongsTo;
    }
    @Override
    public Object[] call() {
        score = getFinalScore(currentState, Maxer, depth, player, opponent);
        Object[] returnObj = new Object[2];
        returnObj[0] = belongsTo;
        returnObj[1] = score;

        return returnObj;
    }
    public int eval(OthellloBoard board, Player player) { //Player is a temporary parameter.
        //Todo, implement evaluation of board state. Maybe move this function to OthelloBoard.
        evalcount++;
        return board.evalBoard(player);
    }
    public int getScore() { return score;}
    /**
     * The recursive minimaxing algorithm. DFS throughout the gametree. Determines the worth of a certain move by examining all possible following gamestates.
     * @param currentState Current state of the board.
     * @param Maxer True when selecting move for the initializer (person taking the turn - maximizing), false when selecting for the initial opponent (minimizing)
     *        depth
     * @return
     */

    public int getFinalScore(OthellloBoard currentState, Boolean Maxer, int depth, Player player, Player opponent) {
        this.calculations++;
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

}