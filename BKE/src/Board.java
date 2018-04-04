import java.util.ArrayList;

/**
 *Board model and Tic Tac Toe logic which should be redundant after Server is implemented.
 */
public class Board {


    public char[][] xy = new char[3][3];
    public Board() {

        xy[0][0] = 'X'; xy[1][0] = ' '; xy[2][0] = ' ';
        xy[0][1] = 'O'; xy[1][1] = 'O'; xy[2][1] = ' ';
        xy[0][2] = 'X'; xy[1][2] = ' '; xy[2][2] = ' ';


    }

    public Board(Board old) { //To prevent passing by reference, or something like that..
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                xy[i][j] = old.xy[i][j];
    }

    public Boolean Win(Boolean Maxer){
        char Player = ' ';
        if (Maxer) {
            Player = 'X';
        }
        else {
            Player = 'O';
        }

        if (xy[0][0] == Player & xy[0][1] == Player & xy[0][2] == Player) { return true; }
        if (xy[1][0] == Player & xy[1][1] == Player & xy[1][2] == Player) { return true; }
        if (xy[2][0] == Player & xy[2][1] == Player & xy[2][2] == Player) { return true; }

        if (xy[0][0] == Player & xy[1][0] == Player & xy[2][0] == Player) { return true; }
        if (xy[0][1] == Player & xy[1][1] == Player & xy[2][1] == Player) { return true; }
        if (xy[0][2] == Player & xy[1][2] == Player & xy[2][2] == Player) { return true; }

        if (xy[0][0] == Player & xy[1][1] == Player & xy[2][2] == Player) { return true; }
        if (xy[2][0] == Player & xy[1][1] == Player & xy[0][2] == Player) { return true; }

        return false;
    }

    /**
     * Checks whether the opponent has won. This should be done through Win(). This is redundant.
     * @param Maxer
     * @return Boolean that is true when the opponent has won.
     */
    public Boolean Lose(Boolean Maxer){
        char Player = ' ';
        if (Maxer) {
            Player = 'O';
        }
        else {
            Player = 'X';
        }

        if (xy[0][0] == Player & xy[0][1] == Player & xy[0][2] == Player) { return true; }
        if (xy[1][0] == Player & xy[1][1] == Player & xy[1][2] == Player) { return true; }
        if (xy[2][0] == Player & xy[2][1] == Player & xy[2][2] == Player) { return true; }

        if (xy[0][0] == Player & xy[1][0] == Player & xy[2][0] == Player) { return true; }
        if (xy[0][1] == Player & xy[1][1] == Player & xy[2][1] == Player) { return true; }
        if (xy[0][2] == Player & xy[1][2] == Player & xy[2][2] == Player) { return true; }

        if (xy[0][0] == Player & xy[1][1] == Player & xy[2][2] == Player) { return true; }
        if (xy[2][0] == Player & xy[1][1] == Player & xy[0][2] == Player) { return true; }

        return false;
    }
    public Boolean Draw(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (xy[i][j] != 'X' & xy[i][j] != 'O') {
                    return false;
                }
            }
        }
        return true;
    }
    public void print() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(" | " + xy[j][i] + " | ");
            }
            System.out.println("\n");
        }
    }
    public ArrayList<Move> getValidMoves(Boolean Maximizer) {
        ArrayList Moves = new ArrayList();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (xy[x][y] != 'X' & xy[x][y] != 'O') {
                    Moves.add(new Move(x, y, Maximizer));
                }
            }
        }
        return Moves;
    }
    public void playMove(Move move) {
        if (move.Maximizer) {
            xy[move.x][move.y] = 'X';
        }
        else {
            xy[move.x][move.y] = 'O';
        }
    }
}
