package database;

import java.sql.Connection;
import java.sql.DriverManager;
import exception.DatabaseException;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/GarageManagementSystem";
            String name = "root";
            String password = "123456";
            Connection conn = DriverManager.getConnection(url, name, password);
            return conn;
        } catch (Exception e) {
            throw new DatabaseException("Could not connect to the garage database.", e);
        }
    }
}
