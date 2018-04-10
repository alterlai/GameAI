package Server;

import java.util.ArrayList;
import java.util.Observable;

public class LobbyObservable extends Observable implements Runnable {
    private ArrayList<Challenge> challengesList = new ArrayList<Challenge>();
    private ArrayList<String> playerList = new ArrayList<String>();
    private static LobbyObservable lobby = new LobbyObservable();
    Server server = Server.getInstance();
    private String playerName;


    private LobbyObservable(){
    }

    public static LobbyObservable getInstance() {
        return lobby;
    }

    @Override
    public void run() {
        boolean running = true;
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
                running = false;
            }
        }
    }

    public void addChallenge(Challenge challenge){
        challengesList.add(challenge);
        setChanged();
        notifyObservers(this);
    }

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

    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers(this);
    }

    public ArrayList<Challenge> getChallengesList() {
        return challengesList;
    }

    public ArrayList<String> getPlayerList() {
        return playerList;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
}
