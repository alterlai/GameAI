package Server;

/**
 * <H1> Challenge</H1>
 * An object that holds a challenge
 * @author Rudolf Klijnhout
 * @version 1.0
 * @since 16-04-2018
 **/
public class Challenge {
    private String playerName;
    private String game;
    private int challengeNumber;

    /**
     * Constructor
     * @param playerName String the name of the playerchallenge
     * @param game String the name of the game
     * @param challengeNumber Int the id of the challenge
     */
    public Challenge(String playerName, String game, int challengeNumber){
        this.playerName = playerName;
        this.game = game;
        this.challengeNumber = challengeNumber;
    }

    /**
     * This method is used to get the playername
     * @return String playername
     */
    public String getPlayerName(){return playerName;}

    /**
     * This method is used to get the game
     * @return String name of the game
     */
    public String getGame(){return game;}

    /**
     * This method is used to get the challenge id
     * @return int challenge id
     */
    public int getChallengeNumber(){return challengeNumber;}

}