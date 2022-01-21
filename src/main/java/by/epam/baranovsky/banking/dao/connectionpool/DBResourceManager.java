package by.epam.baranovsky.banking.dao.connectionpool;

import java.util.ResourceBundle;

public class DBResourceManager {

    private static final DBResourceManager instance = new DBResourceManager();
    private static final String RESOURCE_DB_PARAMS_BUNDLE="db";

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_DB_PARAMS_BUNDLE);

    private DBResourceManager() {}

    public static DBResourceManager getInstance(){
        return instance;
    }

    public String getValue(String key) {
        return resourceBundle.getString(key);
    }

}
