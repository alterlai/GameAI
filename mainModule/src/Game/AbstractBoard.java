package Game;



import java.util.ArrayList;

public abstract class AbstractBoard {


    protected int size;
    protected char[][] xy;

    public AbstractBoard(int size) {
        this.size = size;
        this.xy = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.xy[i][j] = ' ';
            }
        }
    }

    public AbstractBoard(AbstractBoard old) { //To prevent passing by reference, or something like that..
        this.size = old.getSize();
        this.xy = new char[old.getSize()][old.getSize()];
        for(int i=0; i< old.getSize(); i++)
            for(int j=0; j<old.getSize(); j++)
                xy[i][j] = old.xy[i][j];
    }


    public void print() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(" | " + this.xy[j][i] + " | ");
            }
            System.out.println("\n");
        }
    }

    public abstract Boolean isValid(Move move);
    public abstract void playMove(Move move);

    public abstract ArrayList<Move> getValidMoves(Player player);

    public char[][] getCells() {
        return this.xy;
    }

    public char[] getCells1D() {
        char[] dimensionalpos = new char[this.size*this.size];
        for (int x =0; x < this.size; x++) {
            for (int y =0; y < this.size; y++) {
                dimensionalpos[y * this.size + x] = xy[x][y];
            }
        }
        return dimensionalpos;
    }

    public int getSize() { return this.size; }


}
