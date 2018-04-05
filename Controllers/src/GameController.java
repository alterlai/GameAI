public class GameController implements MainController {
    //Model model
    //Server server
    //View view
    //Game game
    //chosenMove

    //Move move;

    public GameController(){
//        this.model = model;
//        view =;
//        view.createview();
//        model.init();
    }


    @Override
    public void Update_view() {

    }

    public void findMove() throws InterruptedException {
//        move=null;
//        if(playerIsAi){
//            move = model.getmove();
//        }
//        else{
//            view.yourturn();
//            wait();
//
//        }
//        return move;

    }

    public void moveSucces(){
        //wanneer de move is goed gekeurd door de server moet dit geupdate worden in het gamemodel.
        //game.player(move);

    }

    public void Forfeit(){
        //wanneer onze client forfiet moet dit worden doorgestuurd naar de server
    }

    public void endGame(){
        //wanneer iemand heeft verloren zal de server deze methode aanroepen wat ervoor zorgt dat de model en view wordt geupdate

    }

    public void  registermove(){
        //update de move variable in de controller;
        //this.move = move;

    }

}
