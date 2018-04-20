package Game;
import java.util.ArrayList;


/**
 * Abstract class that game boards have to extend. Defines and implements certain behavior.
 */
public abstract class AbstractBoard {

    protected int size; //Length of one side of the board in cells
    protected char[][] xy; //char 2 dimensional array representation of the board.

    /**
     * Creates and initaliazes a new board.
     * @param size length of one side of the board in cells
     */
    public AbstractBoard(int size) {
        this.size = size;
        this.xy = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.xy[i][j] = ' ';
            }
        }
    }

    /**
     * Overloaded constructor used when you want to create a copy of the board. Useful when you don't want to pass the reference.
     * @param old the board of which to make a copy of.
     */
    public AbstractBoard(AbstractBoard old) {
        this.size = old.getSize();
        this.xy = new char[old.getSize()][old.getSize()];
        for(int i=0; i< old.getSize(); i++)
            for(int j=0; j<old.getSize(); j++)
                xy[i][j] = old.xy[i][j];
    }


    /**
     * Prints an overview of the board on the commandline
     */
    public void print() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(" | " + this.xy[j][i] + " | ");
            }
            System.out.println("\n");
        }
    }

    /**
     * Checks whether a move is valid or not. Should only be called from GameInterface.createMove() -> wastes processing power.. Validates user input.
     * @param move
     * @return
     */
    public abstract Boolean isValid(Move move);

    /**
     * Plays the Move object. Updates the board.
     * @param move the move to play
     */
    public abstract void playMove(Move move);

    /**
     * Finds all the legal moves that the player can make in the current situation.
     * @param player
     * @return an ArrayList containing Move objects depicting moves that the player can legally play
     */
    public abstract ArrayList<Move> getValidMoves(Player player);

    /**
     *  Returns the 2 dimensional representation of the board
     */
    public char[][] getCells() {
        return this.xy;
    }

    /**
     * Returns an one dimensional representation of the board
     * @return a 1 dimensional char array
     */
    public char[] getCells1D() {
        char[] dimensionalpos = new char[this.size*this.size];
        for (int x =0; x < this.size; x++) {
            for (int y =0; y < this.size; y++) {
                dimensionalpos[y * this.size + x] = xy[x][y];
            }
        }
        return dimensionalpos;
    }

    public void setLegal(Move move) {
        this.xy[move.getX()][move.getY()] = 'L';
    }

    public int getSize() { return this.size; }


}
