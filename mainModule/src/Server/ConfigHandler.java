package Server;

import java.io.*;
import java.util.Properties;

public class ConfigHandler {

    private String filename = "\\mainModule\\src\\resources\\config.properties";
    private Properties properties;
    private static ConfigHandler self = new ConfigHandler();
    private String ip;
    private String port;
    private String nickname;

    private ConfigHandler () {
        properties = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources/config.properties");

        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                System.err.println("Error loading config file!");
            }
        }
        else {
            System.err.println("config.properties file not found! Creating new config file");
            createNewConfig();
        }

        nickname = properties.getProperty("NICKNAME");
        port = properties.getProperty("PORT");
        ip = properties.getProperty("IP");
    }

    private void createNewConfig() {
        // NOT WORKING CURRENTLY
        try {
            PrintWriter writer = new PrintWriter("resources\\config.properties");
            writer.println("IP=localhost");
            writer.println("PORT=7789");
            writer.println("NICKNAME=NewUser");
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to create config file");
            e.printStackTrace();
        }
    }

    /**
     * Save new config file
     * @param ip
     * @param port
     * @param nickname
     */
    public void saveConfig(String ip, String port, String nickname) {
        try {
            FileOutputStream output = new FileOutputStream(System.getProperty("user.dir")+filename);
            properties.setProperty("IP", ip);
            properties.setProperty("PORT", port);
            properties.setProperty("NICKNAME", nickname);
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
}
