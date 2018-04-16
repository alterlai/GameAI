package Server;

import java.util.ArrayList;
import java.util.Observable;
/**
 * <H1> LobbyObservable</H1>
 * This is the object that is Obesrveable for the view to get updated by
 * @author Rudolf Klijnhout
 * @version 1.0
 * @since 16-04-2018
 **/
public class LobbyObservable extends Observable implements Runnable {
    private ArrayList<Challenge> challengesList = new ArrayList<Challenge>();
    private ArrayList<String> playerList = new ArrayList<String>();
    private ArrayList<String> errorList = new ArrayList<String>();
    private static LobbyObservable lobby = new LobbyObservable();
    Server server = Server.getInstance();
    private static volatile String playerName;
    private boolean running;

    /**
     * This constructor insures this is a Singelton
     */
    private LobbyObservable(){
    }

    /**
     * This method is used to get the singelton
     * @return LobbyObervable object
     */
    public static LobbyObservable getInstance() {
        return lobby;
    }

    /**
     * This method is used to request the playerlist every X amount of time.
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                ArrayList <String> tempPlayerList = server.getPlayerlist();
                tempPlayerList.remove(playerName);
                if (!tempPlayerList.equals(playerList)) {
                    playerList = tempPlayerList;
                    setChanged();
                    notifyObservers(this);
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
        }
    }

    /**
     * This method is used to register a new incoming challenge
     * @param challenge Challenge the incoming challenge
     */
    public void addChallenge(Challenge challenge){
        challengesList.add(challenge);
        setChanged();
        notifyObservers(this);
    }

    /**
     * This method is used to remove a challenge that has been cancelled by the server
     * @param challengeNumber Int the number of the challenge
     */
    public void removeChallenge(int challengeNumber) {
        for (int i = 0; i < challengesList.size(); i++) {
            if (challengesList.get(i).getChallengeNumber() == challengeNumber) {
                challengesList.remove(i);
                setChanged();
                notifyObservers(this);
                break;
            }
        }
    }

    /**
     * This method is used register a new error
     * @param error String the error message
     */
    public void addError(String error){
        errorList.add(error);
        setChanged();
        notifyObservers(this);
    }

    /**
     * Change the object state to changed
     */
    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }

    /**
     * This method is used to notify the observers
     */
    @Override
    public void notifyObservers() {
        super.notifyObservers(this);
    }

    /**
     * This method is used to get the list of challenges
     * @return ArrayList of all challenges
     */
    public ArrayList<Challenge> getChallengesList() {
        return challengesList;
    }

    /**
     * This method is used to get the list of errors
     * @return Arraylist of errors
     */
    public ArrayList<String> getErrorList(){
        return errorList;
    }

    /**
     * This method is used to get list of players
     * @return Arraylist of all playernames
     */
    public ArrayList<String> getPlayerList() {
        return playerList;
    }

    /**
     * This method is used to set the local playername
     * @param playerName String the local playername
     */
    public static void setPlayerName(String playerName){
        LobbyObservable.playerName = playerName;
    }

    /**
     * this method is used to stop the polling for the playerlist
     */
    public void stop(){running = false;}
}
