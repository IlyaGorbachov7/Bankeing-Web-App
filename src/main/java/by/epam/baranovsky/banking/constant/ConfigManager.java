package by.epam.baranovsky.banking.constant;

import java.util.ResourceBundle;

public class ConfigManager {

    private static final ConfigManager instance = new ConfigManager();
    private static final String CONFIG_BUNDLE="config";
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle(CONFIG_BUNDLE);

    private ConfigManager(){}

    public static ConfigManager getInstance(){
        return instance;
    }

    public String getValue(String key) {
        return resourceBundle.getString(key);
    }
}
