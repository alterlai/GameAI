package Server;

import java.util.ArrayList;
import java.util.Observable;

public class LobbyObservable extends Observable implements Runnable {
    private ArrayList<Challenge> challengesList = new ArrayList<Challenge>();
    private ArrayList<String> playerList = new ArrayList<String>();
    private static LobbyObservable lobby = new LobbyObservable();
    Server server = Server.getInstance();


    private LobbyObservable(){
    }

    public static LobbyObservable getInstance() {
        return lobby;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ArrayList <String> tempPlayerList = server.getPlayerlist();
                if (!tempPlayerList.equals(playerList)) {
                    playerList = tempPlayerList;
                    setChanged();
                    notifyObservers(this);
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addChallenge(Challenge challenge){
        challengesList.add(challenge);
        System.out.println(challengesList);
        setChanged();
        notifyObservers(this);
    }

    public void removeChallenge(int challengeNumber) {
        for (int i = 0; i < challengesList.size(); i++) {
            if (challengesList.get(i).getChallengeNumber() == challengeNumber) {
                challengesList.remove(i);
                System.out.println(challengesList);
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
}