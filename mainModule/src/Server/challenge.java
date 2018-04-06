package Server;

public class Challenge {
    private String playerName;
    private String game;
    private int challengeNumber;

    public Challenge(String playerName, String game, int challengeNumber){
        this.playerName = playerName;
        this.game = game;
        this.challengeNumber = challengeNumber;
    }

    public String getPlayerName(){return playerName;}

    public String getGame(){return game;}

    public int getChallengeNumber(){return challengeNumber;}

}