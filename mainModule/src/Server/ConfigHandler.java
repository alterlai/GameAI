package Server;

import java.io.*;
import java.util.Properties;

public class ConfigHandler {

    private String filename = "config.properties";
    private Properties properties;
    private static ConfigHandler self = new ConfigHandler();
    private String ip;
    private String port;
    private String nickname;
    private boolean isAI;

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

        System.out.println("Following found in config");
        System.out.println(nickname);
        System.out.println(port);
    }

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
     * @param ip
     * @param port
     * @param nickname
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

    public static ConfigHandler getInstance() {
        return self;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean getIsAI() { return this.isAI; }
}
