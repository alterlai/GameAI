package BKEGame;

import Game.AbstractBoard;
import Game.Move;
import Game.Player;
import java.util.ArrayList;


public class OthellloBoard extends AbstractBoard {

    /**
     * Creates and initaliazes a Othello new board.
     * @param size length of one side of the board in cells
     */
    public OthellloBoard(int size) {
        super(size);

        //set starting stones
        xy[3][3] = 'Z';
        xy[4][3] = 'W';
        xy[3][4] = 'W';
        xy[4][4] = 'Z';
    }

    /**
     * Overloaded constructor used when you want to create a copy of the board. Useful when you don't want to pass the reference.
     * @param old the board of which to make a copy of.
     */
    public OthellloBoard(AbstractBoard old) {
        super(old);
    }

    @Override
    public Boolean isValid(Move move) {
        for (Move validMove : getValidMoves(move.getPlayer())) {
            if (move.compareTo(validMove) == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the current score of the player. Any empty squares are "given" to the winning player.
     * @param player The player of which to calculate the score of
     * @return int score - positive when winning, negative when losing, 0 when drawing.
     */
    public int playerScore(Player player) {
        int score = 0;
        char playerMark = player.getMark();
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (xy[x][y] == playerMark) {
                    score++;
                }
                else if (xy[x][y] != ' ') {
                    score--;
                }
            }
        }
        if (score > 0) { //Winner recieves all empty squares
            for (int x = 0; x < this.size; x++) {
                for (int y = 0; y < this.size; y++) {
                    if (xy[x][y] == ' ') {
                        score++;
                    }
                }
            }
        }
        return score;
    }

    @Override
    public void playMove(Move move) {
        move.makePlayable(this.getSize());
        xy[move.getX()][move.getY()] = move.getPlayer().getMark();
        flipTiles(move);
    }

    /**
     * Checks whether a certain coordinate is withinin the boundaries of the board.
     * @param x
     * @param y
     * @return true when the coordinate is found within the boundaries of the board.
     */
    public Boolean inBound(int x, int y) {
        if (x > -1 && x < this.size && y > - 1 && y < this.size) {return true;}
        return false;
    }

    /**
     * When a move is played, all incorrect marks that are found between the new correct mark and an existing correct mark have to be turned into correct marks - "flipped"
     * @param move The move for which to flip the tiles
     */
    public void flipTiles(Move move) {
        //Used to check each direction. E.g; at index 4 (xdir -> 0, ydir -> 1) the cells to flip in the path straight up are checked.
        final int xdir[] = {-1, -1, -1,  0, 0, 1, 1,  1};
        final int ydir[] = {-1,  0,  1, -1, 1, 1, 0, -1};

        char correctMark = move.getPlayer().getMark();

        path:
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
                    } break;
                }
                else { //Path checking is broken off when it becomes clear that no flips are needed
                    break;
                }
            }
        }
    }

    @Override
    public ArrayList<Move> getValidMoves(Player player) {
        ArrayList<Move> validMoves = new ArrayList<Move>();

        for (int x = 0; x < this.size; x++){
            for (int y = 0; y < this.size; y++) {
                if (xy[x][y] == player.getMark()) { //If the cell contains a mark belonging to the player, find all possible moves that would connect to this mark
                    validMoves.addAll(validMovesFrom(x, y, player));
                }
            }
        }
        return validMoves;
    }

    /**
     * Checks each direction from a certain cell (which should belong to 'player'). Returns all possible moves that would connect with this cell.
     * @param xstart
     * @param ystart
     * @param player
     * @return
     */
    private ArrayList<Move> validMovesFrom(int xstart, int ystart, Player player) {

        ArrayList<Integer> inbetween = new ArrayList<Integer>();;
        ArrayList<Move> valid = new ArrayList<Move>();

        //Used to check each direction. E.g; at index 4 (xdir -> 0, ydir -> 1) the cells to flip in the path straight up are checked.
        final int xdir[] = {-1, -1, -1, 0, 0, 1, 1, 1};
        final int ydir[] = {-1, 0, 1, -1, 1, 1, 0, -1};

        char correctMark = player.getMark();

        for (int i = 0; i < 8; i++) { //Outer for loop decides the direction to check
            inbetween = new ArrayList<Integer>();
            int x = xstart;
            int y = ystart;
            path:
            for (int j = 0; j < this.size; j++) { //Inner for loop makes sure that every possible cell in path is checked.
                x += xdir[i];
                y += ydir[i];

                if (inBound(x, y) && xy[x][y] != correctMark && xy[x][y] != ' ') { //Incorrect mark found in path
                    inbetween.add(1);
                }
                else if (inBound(x, y) &&  xy[x][y] == ' ') { //Correct mark found, found a connection between two correct marks
                    if (!inbetween.isEmpty()) { //If there are incorrect marks between the two connecting correct marks it is a valid move
                        valid.add(new Move(x, y, this.size, player));
                    }
                    break path;
                } else { //Path checking is broken off when it becomes clear that no flips are needed
                    break path;
                }
            }
        }
        return valid;
    }

    /**
     * Evaluates the current state of the board
     * @param player perspective to evaluate from
     * @return score of the board state as an int
     */
    public int evalBoard(Player player) {
        int cornerscore = 3;
        int sidescore = 2;
        int normalscore = 1;

        int score = 0;
        char mark = player.getMark();


        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                char f = xy[x][y];
                if ((x == 0 | x == 7) && f == mark) {
                    if (y == 0 | y == 7) { //corner stone
                        score += cornerscore;
                    }
                }
                else if ((x == 0 | y == 0 || x == 7 || y == 7) && f == mark) {
                    score += sidescore;
                }
                else if (f == mark) {
                    score += normalscore;
                }
            }

        }
        return score;

    }
}