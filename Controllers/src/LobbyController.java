import GUI.ViewActionHandler;
import Server.Server;
import Server.Challenge;

public class LobbyController{
    private Server server;
    private ViewActionHandler view;

    /**
     * Handles the communication between the lobby view and the server model.
     * @param server Temporary. Server will be singleton in the future.
     * @param view
     */
    public LobbyController(Server server, ViewActionHandler view){
        this.server = server;
        this.view = view;
    }

    /**
     * Accept a certain challenge
     * @param challenge challenge that is to be accepted.
     */
    public void acceptChallenge(Challenge challenge) {
        server.acceptChallenge(challenge);
    }

    /**
     * Challenge a certain player connected to the server to a certain game.
     * @param playerName the name of the player
     * @param game the name of the game to be played
     * @throws Exception connection issues, handled by the server and it's observers. Server should be throwing more meaningful exceptions.
     */
    public void sendChallenge(String playerName, String game) throws Exception{
        server.challenge(playerName, game);
    }

    /**
     * Enter the matchmaking queue of a specified game.
     * @param game the name of the game to queue for
     * @throws Exception connection issues, handled by the server and it's observers. Server should be throwing more meaningful exceptions.
     */
    public void subscribeGame(String game) throws Exception {
        server.subscribe(game);
    }

}
