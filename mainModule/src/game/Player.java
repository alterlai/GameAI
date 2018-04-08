package game;

public class Player {
    private char mark = ' ';
    private Boolean AI;
    private String name;

    public Player(String name) {
        this.name = name;
    }

    /**
     * Overloaded method used when a player has AI.
     * @param AI Used in the GameController to decide whether to wait for user input or to calculate a move with the minimax algorithm.
     */
    public Player(String name, Boolean AI) {
        this.name = name;
        this.AI = AI;
    }

    /**
     *
     * @return Returns true when the player object is making moves based on an algorithm and not on user input.
     */
    public Boolean isAI() {
        return this.AI;
    }

    /**
     * @param mark Used to distinguish who occupied a certain spot. Not set in constructor because Player() may be called outside of game modules.
     */
    public void setMark(char mark) {
        this.mark = mark;
    }

    public char getMark() {
        return mark;
    }
    public String getName() {return name;}
}
