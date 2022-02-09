package by.epam.baranovsky.banking.dao.connectionpool;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

/**
 * Class that stores and manages JDBC connections to database.
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public final class ConnectionPool {

    private static final Integer DEFAULT_POOL_SIZE = 5;
    private static final Logger logger = Logger.getLogger(ConnectionPool.class);
    /** Singleton instance */
    private static volatile ConnectionPool instance = null;

    /**Blocking queue that stores connections that are open and free.*/
    private BlockingQueue<Connection> freeConnectionQueue;
    /**Blocking queue that stores connections that are open but taken by other objects.*/
    private BlockingQueue<Connection> givenAwayConnectionsQueue;
    /**Name of JDBC driver.*/
    private String driverName;
    /**URL of database to connect.*/
    private String databaseURL;
    /**Database connection username.*/
    private String user;
    /**Database connection password.*/
    private String password;
    /**Number of connections stored in the pool.*/
    private int poolSize;

    /**
     * Default constructor.
     * <p>
     *     Retrieves database parameters from database resource bundle.
     *     If poolsize could not be retrieved, sets it to 5.
     *     Initializes connection pool on call.
     * </p>
     * @throws ConnectionPoolException if initialization fails.
     */
    private ConnectionPool() throws ConnectionPoolException {

        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
        this.driverName = dbResourceManager.getValue(DBParameter.DB_DRIVER);
        this.databaseURL = dbResourceManager.getValue(DBParameter.DB_URL);
        this.user = dbResourceManager.getValue(DBParameter.DB_USER);
        this.password = dbResourceManager.getValue(DBParameter.DB_PASSWORD);

        try {
            this.poolSize = Integer.parseInt(dbResourceManager.getValue(DBParameter.DB_POOL_SIZE));
        } catch (NumberFormatException e) {
            poolSize = DEFAULT_POOL_SIZE;
        }

        initPoolData();
    }

    public static ConnectionPool getInstance() throws ConnectionPoolException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    /**
     * Initializes this connection pool.
     * <p>
     *     Connects to the database via driver,
     *     created a number of JDBC connections
     *     equal to poolSize variable.
     * </p>
     * @throws ConnectionPoolException if driver was not found of if JDBc connection
     * could not be opened.
     * @see PooledConnection
     */
    private void initPoolData() throws ConnectionPoolException {
        Locale.setDefault(Locale.ENGLISH);

        try{
            Class.forName(driverName);
            givenAwayConnectionsQueue = new ArrayBlockingQueue<>(poolSize);
            freeConnectionQueue = new ArrayBlockingQueue<>(poolSize);
            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(databaseURL, user, password);
                PooledConnection pooledConnection = new PooledConnection(connection);
                freeConnectionQueue.add(pooledConnection);
            }
        } catch (SQLException e) {
            throw new ConnectionPoolException("SQLException in connectionpool", e);
        } catch (ClassNotFoundException e) {
            throw new ConnectionPoolException("Can't find database driver class", e);
        }
    }

    /**
     * Shuts connection pool down.
     * @see ConnectionPool#clearConnectionQueue()  Clears connections from both queues.
     */
    public void dispose() {
        clearConnectionQueue();
    }

    /**
     * Closes all connections in both queues.
     * @see ConnectionPool#closeConnectionsQueue(BlockingQueue)
     */
    private void clearConnectionQueue() {
        try {
            closeConnectionsQueue(givenAwayConnectionsQueue);
            closeConnectionsQueue(freeConnectionQueue);
        } catch (SQLException e) {
            logger.error("Error closing the connection.", e);
        }
    }

    /**
     * Borrows connection from the pool.
     * <p>
     *  Takes an available connection from  freeConnectionQueue.
     *  If there are none available, waits until there is.
     *  Adds taken connection to givenAwayConnectionsQueue.
     * </p>
     * @return Instance of Connection that is open.
     * @throws ConnectionPoolException if connection could not be retrieved.
     */
    public Connection takeConnection() throws ConnectionPoolException {
        Connection connection;
        try {
            connection = freeConnectionQueue.take();
            givenAwayConnectionsQueue.add(connection);
        } catch (InterruptedException e) {
            throw new ConnectionPoolException(
                    "Could not take a connection.", e);
        }
        return connection;
    }

    /**
     * Closes all connections in passed queue.
     * <p>
     *     If connection has auto-commit mode disabled,
     *     commits any changes made in connection and releases any DB locks it had.
     * </p>
     * @param queue The queue of JDBC connections to clear.
     * @throws SQLException if closing connection failed, or if committing was unsuccessful.
     */
    private void closeConnectionsQueue(BlockingQueue<Connection> queue)
            throws SQLException {
        Connection connection;
        while ((connection = queue.poll()) != null) {
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            ((PooledConnection) connection).reallyClose();
        }
    }

    /**
     * Wrapper for JDBC connection that provides tools for pool management.
     * @author Baranovsky E. K.
     * @version 1.0.0
     */
    private class PooledConnection implements Connection {
        /**Connection that is wrapped.*/
        private Connection connection;

        /**
         * Constructor that accept an instance of connection.
         * <p>Enables auto commit mode.</p>
         * @param c Instance of Connection retrieved from driver.
         * @throws SQLException if turning auto-commit mode on has failed.
         */
        public PooledConnection(Connection c) throws SQLException {
            this.connection = c;
            this.connection.setAutoCommit(true);
        }

        /**
         * Releases {@code connection}'s database and JDBC resources
         * immediately instead of waiting for them to be automatically released.
         * @throws SQLException if DB access error occurs.
         */
        public void reallyClose() throws SQLException {
            connection.close();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void clearWarnings() throws SQLException {
            connection.clearWarnings();
        }

        /**
         * Puts {@code connection} that's been taken away  back to freeConnectionsQueue.
         * @throws SQLException if: <ul>
         *     <li>Connection was closed for good.</li>
         *     <li>{@code connection} was not taken away</li>
         *     <li>Removing {@code connection} from queue of given away connections failed</li>
         *     <li>Adding {@code connection} to free connection queue failed</li>
         * </ul>
         */
        @Override
        public void close() throws SQLException {
            if (connection.isClosed()) {
                throw new SQLException("Attempting to close a closed connection.");
            }
            if (connection.isReadOnly()) {
                connection.setReadOnly(false);
            }
            if (!givenAwayConnectionsQueue.remove(this)) {
                throw new SQLException("Error deleting connection from the given away connections pool.");
            }
            if (!freeConnectionQueue.offer(this)) {
                throw new SQLException(
                        "Error allocating connection in the pool.");
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void commit() throws SQLException {
            connection.commit();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Array createArrayOf(String typeName, Object[] elements)
                throws SQLException {
            return connection.createArrayOf(typeName, elements);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Clob createClob() throws SQLException {
            return connection.createClob();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NClob createNClob() throws SQLException {
            return connection.createNClob();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SQLXML createSQLXML() throws SQLException {
            return connection.createSQLXML();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Statement createStatement() throws SQLException {
            return connection.createStatement();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Statement createStatement(int resultSetType,
                                         int resultSetConcurrency) throws SQLException {
            return connection.createStatement(resultSetType,
                    resultSetConcurrency);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Statement createStatement(int resultSetType,
                                         int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            return connection.createStatement(resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Struct createStruct(String typeName, Object[] attributes)
                throws SQLException {
            return connection.createStruct(typeName, attributes);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getAutoCommit() throws SQLException {
            return connection.getAutoCommit();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getCatalog() throws SQLException {
            return connection.getCatalog();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Properties getClientInfo() throws SQLException {
            return connection.getClientInfo();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getClientInfo(String name) throws SQLException {
            return connection.getClientInfo(name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getHoldability() throws SQLException {
            return connection.getHoldability();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return connection.getMetaData();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getTransactionIsolation() throws SQLException {
            return connection.getTransactionIsolation();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return connection.getTypeMap();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SQLWarning getWarnings() throws SQLException {
            return connection.getWarnings();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isClosed() throws SQLException {
            return connection.isClosed();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isReadOnly() throws SQLException {
            return connection.isReadOnly();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isValid(int timeout) throws SQLException {
            return connection.isValid(timeout);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String nativeSQL(String sql) throws SQLException {
            return connection.nativeSQL(sql);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return connection.prepareCall(sql);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public CallableStatement prepareCall(String sql, int resultSetType,
                                             int resultSetConcurrency) throws SQLException {
            return connection.prepareCall(sql, resultSetType,
                    resultSetConcurrency);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public CallableStatement prepareCall(String sql, int resultSetType,
                                             int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            return connection.prepareCall(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PreparedStatement prepareStatement(String sql)
                throws SQLException {
            return connection.prepareStatement(sql);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PreparedStatement prepareStatement(String sql,
                                                  int autoGeneratedKeys) throws SQLException {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PreparedStatement prepareStatement(String sql,
                                                  int[] columnIndexes) throws SQLException {
            return connection.prepareStatement(sql, columnIndexes);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PreparedStatement prepareStatement(String sql,
                                                  String[] columnNames) throws SQLException {
            return connection.prepareStatement(sql, columnNames);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PreparedStatement prepareStatement(String sql,
                                                  int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return connection.prepareStatement(sql, resultSetType,
                    resultSetConcurrency);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PreparedStatement prepareStatement(String sql,
                                                  int resultSetType, int resultSetConcurrency,
                                                  int resultSetHoldability) throws SQLException {
            return connection.prepareStatement(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void rollback() throws SQLException {
            connection.rollback();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            connection.setAutoCommit(autoCommit);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setCatalog(String catalog) throws SQLException {
            connection.setCatalog(catalog);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setClientInfo(String name, String value)
                throws SQLClientInfoException {
            connection.setClientInfo(name, value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setHoldability(int holdability) throws SQLException {
            connection.setHoldability(holdability);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            connection.setReadOnly(readOnly);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Savepoint setSavepoint() throws SQLException {
            return connection.setSavepoint();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return connection.setSavepoint(name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            connection.setTransactionIsolation(level);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return connection.isWrapperFor(iface);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return connection.unwrap(iface);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void abort(Executor arg0) throws SQLException {
            connection.abort(arg0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getNetworkTimeout() throws SQLException {
            return connection.getNetworkTimeout();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getSchema() throws SQLException {
            return connection.getSchema();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void releaseSavepoint(Savepoint arg0) throws SQLException {
            connection.releaseSavepoint(arg0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void rollback(Savepoint arg0) throws SQLException {
            connection.rollback(arg0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setClientInfo(Properties arg0)
                throws SQLClientInfoException {
            connection.setClientInfo(arg0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setNetworkTimeout(Executor arg0, int arg1)
                throws SQLException {
            connection.setNetworkTimeout(arg0, arg1);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setSchema(String arg0) throws SQLException {
            connection.setSchema(arg0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
            connection.setTypeMap(arg0);
        }
    }

}