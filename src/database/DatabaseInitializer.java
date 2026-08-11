package database;

import java.sql.Connection;
import java.sql.Statement;

/** Creates the application's tables when they do not yet exist. */
public final class DatabaseInitializer {
    private DatabaseInitializer() {
    }

    public static void initialize() {
        String[] statements = {
            "CREATE TABLE IF NOT EXISTS Customer (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), phone VARCHAR(20), address VARCHAR(255))",
            "CREATE TABLE IF NOT EXISTS Users (id INT PRIMARY KEY AUTO_INCREMENT, role VARCHAR(50), username VARCHAR(50) UNIQUE, password VARCHAR(100), created_date DATETIME DEFAULT CURRENT_TIMESTAMP, status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE')",
            "CREATE TABLE IF NOT EXISTS Employee (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), phone VARCHAR(20), position VARCHAR(50), salary DOUBLE, user_id INT UNIQUE, FOREIGN KEY(user_id) REFERENCES Users(id))",
            "CREATE TABLE IF NOT EXISTS Vehicle (id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT, brand VARCHAR(50), vehicle_type VARCHAR(30), status VARCHAR(30), license_plate VARCHAR(20) UNIQUE, model VARCHAR(50), FOREIGN KEY(customer_id) REFERENCES Customer(id))",
            "CREATE TABLE IF NOT EXISTS Service (id INT PRIMARY KEY AUTO_INCREMENT, service_name VARCHAR(50), price DOUBLE, description VARCHAR(250))",
            "CREATE TABLE IF NOT EXISTS ServiceRecord (id INT PRIMARY KEY AUTO_INCREMENT, vehicle_id INT, recordDate DATE, notes VARCHAR(250), total_cost DOUBLE, created_by INT, FOREIGN KEY(vehicle_id) REFERENCES Vehicle(id), FOREIGN KEY(created_by) REFERENCES Users(id))",
            "CREATE TABLE IF NOT EXISTS ServiceRecordDetail (id INT PRIMARY KEY AUTO_INCREMENT, service_record_id INT, service_id INT, quantity INT, price DOUBLE, subtotal DOUBLE, FOREIGN KEY(service_record_id) REFERENCES ServiceRecord(id), FOREIGN KEY(service_id) REFERENCES Service(id))",
            "CREATE TABLE IF NOT EXISTS Invoice (id INT PRIMARY KEY AUTO_INCREMENT, record_id INT, total_amount DOUBLE, issueDate DATE DEFAULT (CURRENT_DATE), FOREIGN KEY(record_id) REFERENCES ServiceRecord(id))",
            "CREATE TABLE IF NOT EXISTS EmployeeInvite (id INT PRIMARY KEY AUTO_INCREMENT, invite_code VARCHAR(50) UNIQUE NOT NULL, status ENUM('UNUSED','USED') DEFAULT 'UNUSED', created_date DATETIME DEFAULT CURRENT_TIMESTAMP)"
        };

        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            throw new IllegalStateException("Could not connect to the database.");
        }

        try (connection; Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                statement.execute(sql);
            }
            statement.executeUpdate("INSERT INTO Users(role, username, password, status) "
                    + "SELECT 'Owner', 'owner', '123456', 'ACTIVE' "
                    + "WHERE NOT EXISTS (SELECT 1 FROM Users WHERE role = 'Owner')");
            createOwnerProtectionTriggers(statement);
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize the database schema.", e);
        }
    }

    private static void createOwnerProtectionTriggers(Statement statement) throws Exception {
        createTriggerIfMissing(statement, "prevent_extra_owner", "CREATE TRIGGER prevent_extra_owner "
                + "BEFORE INSERT ON Users FOR EACH ROW "
                + "BEGIN IF NEW.role = 'Owner' AND EXISTS (SELECT 1 FROM Users WHERE role = 'Owner') "
                + "THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only one Owner account is allowed'; END IF; END");
        createTriggerIfMissing(statement, "prevent_owner_update", "CREATE TRIGGER prevent_owner_update "
                + "BEFORE UPDATE ON Users FOR EACH ROW "
                + "BEGIN IF OLD.role = 'Owner' THEN SIGNAL SQLSTATE '45000' "
                + "SET MESSAGE_TEXT = 'Owner account cannot be modified'; END IF; END");
        createTriggerIfMissing(statement, "prevent_owner_delete", "CREATE TRIGGER prevent_owner_delete "
                + "BEFORE DELETE ON Users FOR EACH ROW "
                + "BEGIN IF OLD.role = 'Owner' THEN SIGNAL SQLSTATE '45000' "
                + "SET MESSAGE_TEXT = 'Owner account cannot be deleted'; END IF; END");
    }

    private static void createTriggerIfMissing(Statement statement, String name, String sql) throws Exception {
        try {
            statement.execute(sql);
        } catch (java.sql.SQLException exception) {
            if (exception.getErrorCode() != 1359) {
                throw exception;
            }
        }
    }
}
