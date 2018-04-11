package BKEGame;

import Game.AbstractBoard;
import Game.Move;
import Game.Player;
import java.util.ArrayList;


public class OthellloBoard extends AbstractBoard {

    public OthellloBoard(int size) {
        super(size);

        //set starting stones..
        xy[3][3] = 'Z';
        xy[4][3] = 'W';
        xy[3][4] = 'W';
        xy[4][4] = 'Z';

        //Test run..
        print();

        Player temp = new Player("Han");
        temp.setMark('Z');

        Player temp2 = new Player("Han");
        temp2.setMark('W');

        ArrayList<Move> moves = getValidMoves(temp);
        for (Move move : moves) {
            System.out.println(isValid(move));
            //xy[move.getX()][move.getY()] = 'U';
            System.out.println(move.getX() + ", " + move.getY());
        }

        playMove(new Move(2, 4, size, temp));
        playMove(new Move(2, 5, size, temp2));
        playMove(new Move(5, 3, size, temp));
        playMove(new Move(2, 3, size, temp2));
        //playMove(new Move(6, 3, size, temp2));
        print();

    }

    public OthellloBoard(AbstractBoard old) { //Creates a copy, used when you don't want to pass the reference
        super(old);
    }

    /**
     * Checks whether a move is valid or not. Should only be called from Game.createMove() -> wastes processing power.. Validates user input.
     * @param move
     * @return
     */
    public Boolean isValid(Move move) {
        for (Move validMove : getValidMoves(move.getPlayer())) {
            if (move.compareTo(validMove) == 1) {
                return true;
            }
        }
        return false;
    }

    public void playMove(Move move) {
        move.makePlayable(this.getSize());
        xy[move.getX()][move.getY()] = move.getPlayer().getMark();
        flipTiles(move);
    }

    public Boolean inBound(int x, int y) {
        if (x > 0 && x < this.size && y > 0 && y < this.size) {return true;}
        return false;
    }

    public void flipTiles(Move move) {

        //Used to check each direction. E.g; at index 4 (xdir -> 0, ydir -> 1) the cells to flip in the path straight up are checked.
        final int xdir[] = {-1, -1, -1,  0, 0, 1, 1,  1};
        final int ydir[] = {-1,  0,  1, -1, 1, 1, 0, -1};

        char correctMark = move.getPlayer().getMark();

        for ( int i = 0; i < 8; i++) { //Outer for loop decides the direction to check
            int x = move.getX();
            int y = move.getY();
            ArrayList<Integer[]> flip = new ArrayList<Integer[]>();

            for (int j = 0; j < size; j++) { //Inner for loop makes sure that every possible cell in path is checked.
                x += xdir[i];
                y += ydir[i];
                    if (inBound(x, y) && xy[x][y] != correctMark && xy[x][y]!= ' ') { //Would need flipping if correctMark is found later on in the path
                        flip.add(new Integer[]{x, y});
                    }
                    else if (inBound(x, y) && xy[x][y] == correctMark) { //Correct mark found - "sandwhich" identified - flip all the cells inbetween.
                        for (Integer[] cell : flip) {
                            xy[cell[0]][cell[1]] = correctMark;
                        }
                    }
                    else { //Path checking is broken off when it becomes clear that no flips are needed
                        break;
                    }
            }
        }
    }

    public ArrayList<Move> getValidMoves(Player player) {
        ArrayList<Move> validMoves = new ArrayList<Move>();

        for (int x = 0; x < size; x++){ //for column in xy
            for (int y = 0; y < size; y++) {
                if (xy[x][y] != ' ') {
                    char cellMark = xy[x][y];
                    //Check each direction
                    if (xy[x + 1][y] == ' ' && player.getMark() != cellMark) {
                        validMoves.add(new Move(x + 1, y, this.size, player));
                    }
                    if (xy[x - 1][y] == ' ' && player.getMark() != cellMark) {
                        validMoves.add(new Move(x - 1, y, this.size, player));
                    }
                    if (xy[x][y + 1] == ' ' && player.getMark() != cellMark) {
                        validMoves.add(new Move(x, y + 1, this.size, player));
                    }
                    if (xy[x][y - 1] == ' ' && player.getMark() != cellMark) {
                        validMoves.add(new Move(x, y - 1, this.size, player));
                    }
                }
            }
        }
        return validMoves;
    }
}
