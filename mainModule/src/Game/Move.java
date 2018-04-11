package Game;

import java.util.HashMap;
import java.util.Map;

public class Move implements Comparable<Move> {
    private Integer x;
    private Integer y;
    private Integer pos;

    private Player player;

    /**
     * Called through GameInterface.createMove(). Used when the 2 dimensional position is known.
     * @param x
     * @param y
     * @param boardSize size of one side of the board. Used for casting 1d to 2d.
     * @param player The player who this move belongs to.
     */
    public Move(int x, int y, int boardSize, Player player) {
        this.x = x;
        this.y = y;
        this.pos = calculate1Dimensional(x, y, boardSize);
        this.player = player;
    }

    /**
     * Called when a move is made of which only the 1 dimensional position is known
     * @param pos
     * @param player
     */
    public Move(int pos, Player player) {
        this.pos = pos;
        this.player = player;
    }

    /**
     * @param x
     * @param y
     * @param size Size of one single side, width.
     * @return 1 dimensional representation of 2 dimensional position
     */
    private int calculate1Dimensional(int x, int y, int size) {
        return y * size + x;
    }

    /**
     * Calculates the x and y position on a board of variable size based on the 1 dimensional position.
     * @param pos current 1 dimensional position
     * @param height length of one side of the board to represent the position on
     * @return Map containing the 2 dimensional representation of the 1 dimensional value
     */
    private Map<Character, Integer> calculate2Dimensional(int pos, int height) {
        Map<Character, Integer> map = new HashMap();

        int yCoord = (int)Math.floor(pos / height); //int -> should automatically floor already.
        int xCoord = pos - height * yCoord;

        map.put('x', xCoord);
        map.put('y', yCoord);

        return map;
    }

    /**
     * Sets the 2d parameters correctly.
     * @param height
     */
    public void set2DParameters(int height) {
        Map<Character, Integer> coords = calculate2Dimensional(pos, height);
        this.x = coords.get('x');
        this.y = coords.get('y');
    }

    /**
     * Has to be called before a move is played. Checks whether the 2 dimensional value is known. If not, makes sure that it it is calculated and set.
     * Possible situations: pos known without x and y known, pos known with x and y known.
     * @param height
     */
    public void makePlayable(int height) {
        if (x != null & y != null & pos != null) {
            return;
        }
        set2DParameters(height);
    }


    public int getX(){
        return x;
    }
    public int getY() {
        return y;
    }
    public Player getPlayer() {
        return player;
    }
    public int getPos(){return pos;}


    @Override
    public int compareTo(Move move) {
        if (this.x == move.getX() && this.y == move.getY() && this.player.getMark() == move.getPlayer().getMark()) {
            return 1;
        }
        return 0;
    }
}