package database;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/GarageManagementSystem";
            String name = "root";
            String password = "123456";
            Connection conn = DriverManager.getConnection(url, name, password);
            System.out.println("Connected successfully!");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}