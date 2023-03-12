package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static final String PROPERTY_FILE_PATH = "src/test/resources/application.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(PROPERTY_FILE_PATH);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getConfigValue(String key) {
        return ((System.getProperty(key) == null) ? properties.getProperty(key) : System.getProperty(key));
    }

}
