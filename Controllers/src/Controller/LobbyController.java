/**package Controller;
import Server.*;

import java.io.IOException;

public class LobbyController{
    //Model model
    Server server;
    //View view

    public LobbyController(){

//        this.model = model;
//        view =;
//        view.createview();
//        model.init();
        this.server = new Server();
        new Thread(server).start();
        server.run();
    }



    public void Challenge(String Tegenstander, String Game){
        //view.challenge(Tegenstander, Game);
    }

    public void ChallengePlayer(String Game, String PlayerName) throws IOException {
        server.challenge(PlayerName, Game);
        //update view dat we een speller hebben uitgedaagt
    }

    public void acceptChallenge(){
        //server.acceptchallenge();
    }

    public void SubscribeGame(String Game) throws IOException {
        if(server.subscribe(Game)){
            //update view dat we in queue zitten
        }

    }

}**/
