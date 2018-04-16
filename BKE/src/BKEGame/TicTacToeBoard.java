package BKEGame;

import Game.AbstractBoard;
import Game.Move;
import Game.Player;
import java.util.ArrayList;

/**
 * Tic tac toe board and game logic
 */

public class TicTacToeBoard extends AbstractBoard {


    /**
     * Creates and initaliazes a new Tic Tac Toe board.
     * @param size length of one side of the board in cells
     */
    public TicTacToeBoard(int size) {
        super(size);
    }

    /**
     * Overloaded constructor used when you want to create a copy of the board. Useful when you don't want to pass the reference.
     * @param old the board of which to make a copy of.
     */
    public TicTacToeBoard(AbstractBoard old) { //To prevent passing by reference, or something like that..
        super(old);
    }

    /**
     * Checks whether a certain player has won
     * @param Maxer
     * @param player
     * @param opponent
     * @return true if the player has won, false if not.
     */
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

    /**
     * Checks whether the game has reached a draw state.
     * @return true when it is a draw, false when it's not.
     */
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

    @Override
    public Boolean isValid(Move move) {
        if (xy[move.getX()][move.getY()] == ' ') {
            return true;
        }
        return false;
    }

    @Override
    public void playMove(Move move) {
        move.makePlayable(this.getSize());
        xy[move.getX()][move.getY()] = move.getPlayer().getMark();
    }

    @Override
    public ArrayList<Move> getValidMoves(Player player) {
        ArrayList Moves = new ArrayList();
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (xy[x][y] == ' ' & xy[x][y] == ' ') {
                    Moves.add(new Move(x, y, this.size, player));
                }
            }
        }
        return Moves;
    }

}
