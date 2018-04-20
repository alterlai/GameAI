package Server;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <H1> ListHandler</H1>
 * Handles lists
 * @author Rudolf Klijnhout
 * @version 1.0
 * @since 16-04-2018
 **/
public class ListHandler {

    /**
     * This method is used to handle the playerlist
     * @param data String the list to handle
     * @return ArrayList filled with playernames as strings
     */
    public static ArrayList<String> handlePlayerList(String data){
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(data.substring(16, data.length() - 1).split("\"")));
        for (int i = 0; i < list.size()-1; i++){
            list = correctList(i, list);
        }
        return list;
    }

    /**
     * This method is used the handle the game list
     * @param data String the list to handle
     * @return ArrayList filled with game names as strings
     */
    public static ArrayList<String> handleGamelist(String data){
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(data.substring(14, data.length() - 1).split("\"")));
        for (int i = 0; i < list.size()-1; i++){
            list = correctList(i, list);
        }
        return list;
    }

    /**
     * This method is used to counter "troll names on the server"
     * @param i int the index of the place in the list to correct
     * @param list The list to correct
     * @return list The corrected list
     */
    private static ArrayList<String> correctList(int i, ArrayList<String> list){
        if (list.get(i).equals("") || list.get(i).equals(" ") || list.get(i).equals(",") || list.get(i).equals(", ")){
            list.remove(i);
            correctList(i, list);
        }
        return list;
    }
}
