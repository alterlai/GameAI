public class GameController implements MainController {
    //Model model
    //Server server
    //View view
    //Game game

    public GameController(){
//        this.model = model;
//        view =;
//        view.createview();
//        model.init();
    }


    @Override
    public void Update_view() {

    }

    public void findMove(){
        //vraagt het model om een set te doen
        //model.Move();
    }

    public void moveSucces(){
        //wanneer de move is goed gekeurd door de server moet dit geupdate worden in de view.
    }
    public void setChosenMove(){
        //welke set heeft te tegenstander gedaan? update het model en view

    }
    public void Forfeit(){
        //wanneer onze client forfiet moet dit worden doorgestuurd naar de server
    }

    public void endGame(){
        //wanneer iemand heeft verloren zal de server deze methode aanroepen wat ervoor zorgt dat de model en view wordt geupdate

    }

}
