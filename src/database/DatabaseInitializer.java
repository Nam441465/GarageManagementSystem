package database;

import java.sql.Connection;
import java.sql.Statement;

/** Creates the application's tables when they do not yet exist. */
public final class DatabaseInitializer {
    private DatabaseInitializer() {
    }

    public static void initialize() {
        String[] statements = {
            "CREATE TABLE IF NOT EXISTS Customer (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL, phone VARCHAR(20), address VARCHAR(255), created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
            "CREATE TABLE IF NOT EXISTS Users (id INT PRIMARY KEY AUTO_INCREMENT, role ENUM('OWNER','EMPLOYEE') NOT NULL DEFAULT 'EMPLOYEE', username VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL, created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, status ENUM('ACTIVE','INACTIVE','BLOCKED') NOT NULL DEFAULT 'ACTIVE', last_login DATETIME NULL)",
            "CREATE TABLE IF NOT EXISTS Employee (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL, phone VARCHAR(20), position VARCHAR(50) NOT NULL, salary DECIMAL(12,2) NOT NULL DEFAULT 0.00, user_id INT UNIQUE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE SET NULL ON UPDATE CASCADE)",
            "CREATE TABLE IF NOT EXISTS Vehicle (id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT NOT NULL, brand VARCHAR(50) NOT NULL, vehicle_type ENUM('SEDAN','SUV','HATCHBACK','PICKUP','TRUCK','MOTORBIKE') NOT NULL, status ENUM('AVAILABLE','WAITING','IN_SERVICE','COMPLETED','DELIVERED') NOT NULL DEFAULT 'AVAILABLE', license_plate VARCHAR(20) NOT NULL UNIQUE, model VARCHAR(50) NOT NULL, year INT, color VARCHAR(30), created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(customer_id) REFERENCES Customer(id) ON DELETE CASCADE ON UPDATE CASCADE)",
            "CREATE TABLE IF NOT EXISTS Service (id INT PRIMARY KEY AUTO_INCREMENT, service_name VARCHAR(100) NOT NULL, description TEXT, category ENUM('CLEANING','MAINTENANCE','REPAIR','REPLACEMENT') NOT NULL, is_active BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
            "CREATE TABLE IF NOT EXISTS PriceList (id INT PRIMARY KEY AUTO_INCREMENT, service_id INT NOT NULL, vehicle_type ENUM('SEDAN','SUV','HATCHBACK','PICKUP','TRUCK','MOTORBIKE') NOT NULL, vehicle_brand VARCHAR(50) NOT NULL, price DECIMAL(12,2) NOT NULL, effective_from DATE NOT NULL, effective_to DATE, note VARCHAR(255), UNIQUE(service_id,vehicle_type,vehicle_brand), FOREIGN KEY(service_id) REFERENCES Service(id) ON DELETE CASCADE ON UPDATE CASCADE)",
            "CREATE TABLE IF NOT EXISTS Appointment (id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT NOT NULL, vehicle_id INT, employee_id INT, appointment_date DATETIME NOT NULL, status ENUM('PENDING','CONFIRMED','COMPLETED','CANCELLED','NO_SHOW') NOT NULL DEFAULT 'PENDING', notes TEXT, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(customer_id) REFERENCES Customer(id) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY(vehicle_id) REFERENCES Vehicle(id) ON DELETE SET NULL ON UPDATE CASCADE, FOREIGN KEY(employee_id) REFERENCES Employee(id) ON DELETE SET NULL ON UPDATE CASCADE)",
            "CREATE TABLE IF NOT EXISTS AppointmentServiceItem (id INT PRIMARY KEY AUTO_INCREMENT, appointment_id INT NOT NULL, service_id INT NOT NULL, quantity INT NOT NULL DEFAULT 1, unit_price DECIMAL(12,2) NOT NULL, notes TEXT, FOREIGN KEY(appointment_id) REFERENCES Appointment(id) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY(service_id) REFERENCES Service(id) ON DELETE RESTRICT ON UPDATE CASCADE)",
            "CREATE TABLE IF NOT EXISTS Invoice (id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT NOT NULL, employee_id INT NOT NULL, license_plate VARCHAR(20) NOT NULL, vehicle_type ENUM('SEDAN','SUV','HATCHBACK','PICKUP','TRUCK','MOTORBIKE') NOT NULL, vehicle_brand VARCHAR(50) NOT NULL, total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00, issue_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, payment_status ENUM('UNPAID','PAID') NOT NULL DEFAULT 'UNPAID', pdf_path VARCHAR(255), FOREIGN KEY(customer_id) REFERENCES Customer(id) ON DELETE RESTRICT ON UPDATE CASCADE, FOREIGN KEY(employee_id) REFERENCES Employee(id) ON DELETE RESTRICT ON UPDATE CASCADE)",
            "CREATE TABLE IF NOT EXISTS InvoiceDetail (id INT PRIMARY KEY AUTO_INCREMENT, invoice_id INT NOT NULL, service_id INT NOT NULL, service_name VARCHAR(100) NOT NULL, unit_price DECIMAL(12,2) NOT NULL, subtotal DECIMAL(12,2) NOT NULL, FOREIGN KEY(invoice_id) REFERENCES Invoice(id) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY(service_id) REFERENCES Service(id) ON DELETE RESTRICT ON UPDATE CASCADE)",
            "CREATE TABLE IF NOT EXISTS Part (id INT PRIMARY KEY AUTO_INCREMENT, part_name VARCHAR(100) NOT NULL, part_code VARCHAR(50) UNIQUE, supplier VARCHAR(100), unit_price DECIMAL(12,2) NOT NULL DEFAULT 0.00, stock_quantity INT NOT NULL DEFAULT 0, min_stock INT NOT NULL DEFAULT 0, description TEXT, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
            "CREATE TABLE IF NOT EXISTS Warranty (id INT PRIMARY KEY AUTO_INCREMENT, invoice_id INT NOT NULL, warranty_code VARCHAR(50) NOT NULL UNIQUE, start_date DATE NOT NULL, end_date DATE NOT NULL, coverage TEXT, status ENUM('ACTIVE','EXPIRED','CLAIMED') NOT NULL DEFAULT 'ACTIVE', FOREIGN KEY(invoice_id) REFERENCES Invoice(id) ON DELETE CASCADE ON UPDATE CASCADE)",
            "CREATE TABLE IF NOT EXISTS AuditLog (id INT PRIMARY KEY AUTO_INCREMENT, user_id INT, action VARCHAR(100) NOT NULL, entity_name VARCHAR(100) NOT NULL, entity_id INT, old_value JSON, new_value JSON, ip_address VARCHAR(45), device VARCHAR(255), created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(user_id) REFERENCES Users(id) ON DELETE SET NULL ON UPDATE CASCADE)",
            "CREATE TABLE IF NOT EXISTS EmployeeInvite (id INT PRIMARY KEY AUTO_INCREMENT, invite_code VARCHAR(50) NOT NULL UNIQUE, status ENUM('UNUSED','USED','EXPIRED') NOT NULL DEFAULT 'UNUSED', created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, expires_at DATETIME, created_by INT, FOREIGN KEY(created_by) REFERENCES Users(id) ON DELETE SET NULL ON UPDATE CASCADE)"
        };

        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            throw new IllegalStateException("Could not connect to the database.");
        }

        try (connection; Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                statement.execute(sql);
            }
            
            // Insert default Owner account if not exists
            statement.executeUpdate("INSERT INTO Users(role, username, password, status) "
                    + "SELECT 'OWNER', 'owner', '123456', 'ACTIVE' "
                    + "WHERE NOT EXISTS (SELECT 1 FROM Users WHERE role = 'OWNER')");
            
            // Create indexes
            createIndexIfMissing(statement, "idx_vehicle_customer", "CREATE INDEX idx_vehicle_customer ON Vehicle(customer_id)");
            createIndexIfMissing(statement, "idx_appointment_customer", "CREATE INDEX idx_appointment_customer ON Appointment(customer_id)");
            createIndexIfMissing(statement, "idx_appointment_status", "CREATE INDEX idx_appointment_status ON Appointment(status)");
            createIndexIfMissing(statement, "idx_invoice_customer", "CREATE INDEX idx_invoice_customer ON Invoice(customer_id)");
            createIndexIfMissing(statement, "idx_auditlog_user", "CREATE INDEX idx_auditlog_user ON AuditLog(user_id)");
            
            createOwnerProtectionTriggers(statement);
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize the database schema.", e);
        }
    }

    private static void createOwnerProtectionTriggers(Statement statement) throws Exception {
        createTriggerIfMissing(statement, "prevent_extra_owner", "CREATE TRIGGER prevent_extra_owner "
                + "BEFORE INSERT ON Users FOR EACH ROW "
                + "BEGIN IF NEW.role = 'OWNER' AND EXISTS (SELECT 1 FROM Users WHERE role = 'OWNER') "
                + "THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only one Owner account is allowed'; END IF; END");
        createTriggerIfMissing(statement, "prevent_owner_update", "CREATE TRIGGER prevent_owner_update "
                + "BEFORE UPDATE ON Users FOR EACH ROW "
                + "BEGIN IF OLD.role = 'OWNER' THEN SIGNAL SQLSTATE '45000' "
                + "SET MESSAGE_TEXT = 'Owner account cannot be modified'; END IF; END");
        createTriggerIfMissing(statement, "prevent_owner_delete", "CREATE TRIGGER prevent_owner_delete "
                + "BEFORE DELETE ON Users FOR EACH ROW "
                + "BEGIN IF OLD.role = 'OWNER' THEN SIGNAL SQLSTATE '45000' "
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

    private static void createIndexIfMissing(Statement statement, String indexName, String sql) throws Exception {
        try {
            statement.execute(sql);
        } catch (java.sql.SQLException exception) {
            if (exception.getErrorCode() != 1061) {
                throw exception;
            }
        }
    }
}
