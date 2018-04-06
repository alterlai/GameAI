package Server;

import java.util.ArrayList;
import java.util.Observable;

public class LobbyObservable extends Observable implements Runnable {
    private static ArrayList<Challenge> challengesList = new ArrayList<Challenge>();
    private ArrayList<String> playerList = new ArrayList<String>();
    Server server;

    public LobbyObservable(Server server){
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                playerList = server.getPlayerlist();
                setChanged();
                notifyObservers(this);
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addChallenge(Challenge challenge){
        challengesList.add(challenge);
        System.out.println(challengesList);
    }

    public static void removeChallenge(int challengeNumber){
        for (int i=0; i < challengesList.size(); i++)
            if (challengesList.get(i).getChallengeNumber() == challengeNumber){
                challengesList.remove(challengesList.get(i));
                System.out.println(challengesList);
                break;
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

    public static ArrayList<Challenge> getChallengesList() {
        return challengesList;
    }

    public ArrayList<String> getPlayerList() {
        return playerList;
    }
}
