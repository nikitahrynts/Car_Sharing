package carsharing.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String url;
    private final String driver;

    public Database(String url, String driver) {
        this.url = url;
        this.driver = driver;
    }

    public Connection getConnection() {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
