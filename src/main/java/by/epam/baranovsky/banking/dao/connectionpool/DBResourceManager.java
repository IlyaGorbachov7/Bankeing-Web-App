package by.epam.baranovsky.banking.dao.connectionpool;

import java.util.ResourceBundle;

/**
 * Utility class that manages retrieving data from database parameters resource bundle.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
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
