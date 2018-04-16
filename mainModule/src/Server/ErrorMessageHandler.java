package Server;

/**
 * <H1> ErrorMessageHandler</H1>
 * Handles error Messages from the server
 * @author Rudolf Klijnhout
 * @version 1.0
 * @since 16-04-2018
 **/
public class ErrorMessageHandler implements MessageHandlerInterface {

    /**
     * This method handels the error messages
     * @param message String the server message to handle
     */
    public static void handleMessage(String message) {
        LobbyObservable.getInstance().addError(message);
    }
}
