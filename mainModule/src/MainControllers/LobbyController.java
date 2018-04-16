package MainControllers;

import Server.Server;

import java.io.IOException;

public class LobbyController{
    Server server;


    public LobbyController(){
        this.server = Server.getInstance();
        new Thread(server).start();
        server.run();
    }



    public void Challenge(String Tegenstander, String Game){
        //view.challenge(Tegenstander, GameInterface);
    }

    public void ChallengePlayer(String Game, String PlayerName) throws Exception {
        server.challenge(PlayerName, Game);
        //update view dat we een speller hebben uitgedaagt
    }

    public void acceptChallenge(){
        //server.acceptchallenge();
    }

    public void playAsAI() {
        // Set server bit to play as AI.
    }

    public void SubscribeGame(String Game) throws Exception {
        if(server.subscribe(Game)){
            //update view dat we in queue zitten
        }

    }

}
