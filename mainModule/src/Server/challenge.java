package Server;

public class challenge {
    private String playerName;
    private String game;
    private int challengeNumber;

    public challenge (String playerName, String game, int challengeNumber){
        this.playerName = playerName;
        this.game = game;
        this.challengeNumber = challengeNumber;
    }

    public String getPlayerName(){return playerName;}

    public String getGame(){return game;}

    public int getChallengeNumber(){return challengeNumber;}
}