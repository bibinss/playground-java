package com.playground;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static ConnectionPool instance;
    private final BlockingQueue<Connection> connectionPool;
    private static final int POOL_SIZE = 10;

    private ConnectionPool(String url, String user, String password) throws SQLException {
        connectionPool = new ArrayBlockingQueue<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            connectionPool.offer(createConnection(url, user, password));
        }
    }

    private Connection createConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void returnConnection(Connection connection) {
        if (connection != null) {
            connectionPool.offer(connection);
        }
    }

    public void shutdown() throws SQLException {
        for (Connection connection : connectionPool) {
            connection.close();
        }
        connectionPool.clear();
    }
}
