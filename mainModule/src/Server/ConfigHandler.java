package Server;

import java.io.*;
import java.util.Properties;

/**
 * @author Jeroen
 * This class is responsible for all communication between the config file and the application.
 */
public class ConfigHandler {

    private String filename = "config.properties";
    private Properties properties;
    private static ConfigHandler self = new ConfigHandler();
    private String ip;
    private String port;
    private String nickname;
    private boolean isAI;

    /**
     * Initiation of the confighandler. Load the config file. Creates a new config file if none exists.
     */
    private ConfigHandler () {
        properties = new Properties();

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                System.err.println("Error loading config file!");
            }
        }
        else {
            System.err.println("config.properties file not found! Creating new config file");
            properties = createNewConfig();
        }

        nickname = properties.getProperty("NICKNAME");
        port = properties.getProperty("PORT");
        ip = properties.getProperty("IP");
        isAI = Boolean.parseBoolean(properties.getProperty("ISAI"));
    }

    /**
     * Creates a new config file if none exists.
     * @return config properties.
     */
    private Properties createNewConfig() {
        // NOT WORKING CURRENTLY
        try {
            Properties properties = new Properties();
            properties.setProperty("IP", "localhost");
            properties.setProperty("PORT","7789");
            properties.setProperty("NICKNAME","NewUser");
            properties.setProperty("ISAI", "false");

            File file = new File(filename);
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, null);
            return properties;
        } catch (IOException e) {
            System.err.println("Unable to create config file");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save new config file
     * @param ip IP string to be saved.
     * @param port Port string to be saved.
     * @param nickname Nickname string to be saved.
     * @param isAI Boolean value whether to play as AI or not.
     */
    public void saveConfig(String ip, String port, String nickname, boolean isAI) {
        String tempstring = "";
        if (isAI) tempstring = "true"; else tempstring = "false";   // Handle boolean input
        try {
            Properties properties = new Properties();
            FileOutputStream output = new FileOutputStream(filename);
            properties.setProperty("IP", ip);
            properties.setProperty("PORT", port);
            properties.setProperty("NICKNAME", nickname);
            properties.setProperty("ISAI", tempstring);
            properties.store(output, null);
        } catch (IOException e) {
            System.err.println("Unable to save config file");
            e.printStackTrace();
        }


    }

    /**
     * Get an instance of the ConfigHandler object.
     * @return instance of confighandler object.
     */
    public static ConfigHandler getInstance() {
        return self;
    }

    /**
     * Get the current IP stored in the config file.
     * @return String current IP.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Get the current port stored in the config file.
     * @return String current port.
     */
    public String getPort() {
        return port;
    }

    /**
     * Get the current nickname stored in the config file.
     * @return String current nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Get the current boolean value whether to play as AI or not.
     * @return Boolean play as AI or not.
     */
    public boolean getIsAI() { return this.isAI; }
}
