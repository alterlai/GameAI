package BKEGame;

import Game.Move;
import Game.Player;
import java.util.ArrayList;

/**
 *BKEGame.Board model and Tic Tac Toe logic
 */

public class Board {

    private Integer size = 3;
    private char[][] xy = new char[size][size];

    public Board() {
        xy[0][0] = ' '; xy[1][0] = ' '; xy[2][0] = ' ';
        xy[0][1] = ' '; xy[1][1] = ' '; xy[2][1] = ' ';
        xy[0][2] = ' '; xy[1][2] = ' '; xy[2][2] = ' ';
    }

    public Board(Board old) { //To prevent passing by reference, or something like that..
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                xy[i][j] = old.xy[i][j];
    }

    public Boolean Win(Boolean Maxer, Player player, Player opponent){
        char Player = ' ';
        if (Maxer) {
            Player = player.getMark();
        }
        else {
            Player = opponent.getMark();
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
    public Boolean Lose(Boolean Maxer, Player player, Player opponent){
        char Player = ' ';
        if (Maxer) {
            Player = opponent.getMark();
        }
        else {
            Player = player.getMark();
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
                if (xy[i][j] == ' ' & xy[i][j] == ' ') {
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
    public Boolean isValid(Move move) {
        if (xy[move.getX()][move.getY()] == ' ') {
            return true;
        }
        return false;
    }
    public void playMove(Move move) {
        move.makePlayable(this.getSize());
        xy[move.getX()][move.getY()] = move.getPlayer().getMark();
    }
  
    public ArrayList<Move> getValidMoves(Player player) {
        ArrayList Moves = new ArrayList();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (xy[x][y] == ' ' & xy[x][y] == ' ') {
                    Moves.add(new Move(x, y, this.size, player));
                }
            }
        }
        return Moves;
    }
    public int getSize() { return size; }
}
