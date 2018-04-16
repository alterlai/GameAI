package Server;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <H1> ListHandler</H1>
 * Handles all lists
 * @author Rudolf Klijnhout
 * @version 1.0
 * @since 16-04-2018
 **/
public class ListHandler {

    public static ArrayList<String> handlePlayerList(String data){
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(data.substring(16, data.length() - 1).split("\"")));
        for (int i = 0; i < list.size()-1; i++){
            list = correctList(i, list);
        }
        return list;
    }

    public static ArrayList<String> handleGamelist(String data){
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(data.substring(14, data.length() - 1).split("\"")));
        for (int i = 0; i < list.size()-1; i++){
            list = correctList(i, list);
        }
        return list;
    }

    private static ArrayList<String> correctList(int i, ArrayList<String> list){
        if (list.get(i).equals("") || list.get(i).equals(" ") || list.get(i).equals(",") || list.get(i).equals(", ")){
            list.remove(i);
            correctList(i, list);
        }
        return list;
    }
}
