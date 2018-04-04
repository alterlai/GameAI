import java.util.ArrayList;

public class TicTacToe {

    Board board;

    public TicTacToe() {
        board = new Board();
    }

    /**
     * Entry point into the recursive minimaxing algorithm getFinalScore(). Determines the best next move and plays it.
     *
     * @param Maxer True when the entity making the move is the entity which is 'maxing' (i.e. the algorithm).
     */
    public void findBestMove(Boolean Maxer) {
        ArrayList<Move> Moves = board.getValidMoves(Maxer);

        int currentBestScore = Integer.MIN_VALUE;
        Move currentBestMove = null;

        for (Move move : Moves) {
            Board newState = new Board(board);
            newState.playMove(move);

            int MoveScore = getFinalScore(newState, !Maxer, 0 );
                if (MoveScore > currentBestScore) {
                    currentBestScore = MoveScore;
                    currentBestMove = move;
                }
        }

        board.playMove(currentBestMove);
        board.print();
    }

    /**
     * Plays a certain move.
     *
     * @param Move Move to play
     */
    public void playMove(Move Move) {
        board.playMove(Move);
        board.print();
    }

    /**
     * The recursive minimaxing algorithm. DFS throughout the gametree. Determines the worth of a certain move by examining all possible following gamestates.
     * @param currentState Current state of the board.
     * @param Maxer
     * @param count Used to penalize favorable gamestates that are more moves away. Favours a win in a single move over a win in two moves.
     * @return
     */

    public int getFinalScore(Board currentState, Boolean Maxer, int count) {
        count++;

        count = 0; //count caused the a.i. to start out with the wrong move (1, 1)

        ArrayList<Move> Moves = currentState.getValidMoves(Maxer);

        if (currentState.Win(true)) {
            return 10 - count;
        }
        else if (currentState.Lose(true)) {
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

            int MoveScore = getFinalScore(newState, !Maxer, count);
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

}
