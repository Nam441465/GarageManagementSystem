CREATE TABLE Customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NULL,
    address VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE Users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role ENUM('OWNER', 'EMPLOYEE') NOT NULL DEFAULT 'EMPLOYEE',
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'INACTIVE', 'BLOCKED') NOT NULL DEFAULT 'ACTIVE',
    last_login DATETIME NULL
);
CREATE TABLE Employee (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NULL,
    position VARCHAR(50) NOT NULL,
    salary DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    user_id INT UNIQUE NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE
    SET NULL ON UPDATE CASCADE
);
CREATE TABLE EmployeeInvite (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invite_code VARCHAR(50) NOT NULL UNIQUE,
    status ENUM('UNUSED', 'USED', 'EXPIRED') NOT NULL DEFAULT 'UNUSED',
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NULL,
    created_by INT NULL,
    FOREIGN KEY (created_by) REFERENCES Users(id) ON DELETE
    SET NULL ON UPDATE CASCADE
);
CREATE TABLE Vehicle (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    brand VARCHAR(50) NOT NULL,
    vehicle_type ENUM(
        'SEDAN',
        'SUV',
        'HATCHBACK',
        'PICKUP',
        'TRUCK',
        'MOTORBIKE'
    ) NOT NULL,
    status ENUM(
        'AVAILABLE',
        'WAITING',
        'IN_SERVICE',
        'COMPLETED',
        'DELIVERED'
    ) NOT NULL DEFAULT 'AVAILABLE',
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    model VARCHAR(50) NOT NULL,
    year INT NULL,
    color VARCHAR(30) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE Service (
    id INT PRIMARY KEY AUTO_INCREMENT,
    service_name VARCHAR(100) NOT NULL,
    description TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE PriceList (
    id INT PRIMARY KEY AUTO_INCREMENT,
    service_id INT NOT NULL,
    vehicle_type ENUM(
        'SEDAN',
        'SUV',
        'HATCHBACK',
        'PICKUP',
        'TRUCK',
        'MOTORBIKE'
    ) NOT NULL,
    vehicle_brand ENUM(
        'TOYOTA',
        'HONDA',
        'FORD',
        'HYUNDAI',
        'KIA',
        'MAZDA',
        'MERCEDES',
        'BMW',
        'AUDI',
        'VINFAST',
        'MITSUBISHI',
        'NISSAN'
    ) NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    effective_from DATE NOT NULL,
    effective_to DATE NULL,
    note VARCHAR(255) NULL,
    UNIQUE (service_id, vehicle_type, vehicle_brand),
    FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE Appointment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NULL,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    license_plate VARCHAR(20) NOT NULL,
    vehicle_brand VARCHAR(50) NOT NULL,
    vehicle_type VARCHAR(50) NOT NULL,
    appointment_date DATETIME NOT NULL,
    status ENUM(
        'PENDING',
        'CONFIRMED',
        'COMPLETED',
        'CANCELLED',
        'NO_SHOW'
    ) NOT NULL DEFAULT 'PENDING',
    notes TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(id) ON DELETE
    SET NULL ON UPDATE CASCADE
);
CREATE TABLE AppointmentServiceItem (
    id INT PRIMARY KEY AUTO_INCREMENT,
    appointment_id INT NOT NULL,
    service_id INT NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    notes TEXT NULL,
    FOREIGN KEY (appointment_id) REFERENCES Appointment(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TABLE Invoice (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    employee_id INT NOT NULL,
    employee_name VARCHAR(100) NOT NULL,
    license_plate VARCHAR(20) NOT NULL,
    vehicle_type ENUM(
        'SEDAN',
        'SUV',
        'HATCHBACK',
        'PICKUP',
        'TRUCK',
        'MOTORBIKE'
    ) NOT NULL,
    vehicle_brand ENUM(
        'TOYOTA',
        'HONDA',
        'FORD',
        'HYUNDAI',
        'KIA',
        'MAZDA',
        'MERCEDES',
        'BMW',
        'AUDI',
        'VINFAST',
        'MITSUBISHI',
        'NISSAN'
    ) NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    payment_status ENUM('UNPAID', 'PAID') NOT NULL DEFAULT 'UNPAID',
    issue_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    pdf_path VARCHAR(255) NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES Employee(id) ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TABLE InvoiceDetail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT NOT NULL,
    service_id INT NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TABLE Part (
    id INT PRIMARY KEY AUTO_INCREMENT,
    part_name VARCHAR(100) NOT NULL,
    part_code VARCHAR(50) NULL UNIQUE,
    supplier VARCHAR(100) NULL,
    unit_price DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    stock_quantity INT NOT NULL DEFAULT 0,
    min_stock INT NOT NULL DEFAULT 0,
    description TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE Warranty (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT NOT NULL,
    warranty_code VARCHAR(50) NOT NULL UNIQUE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    coverage TEXT NULL,
    status ENUM('ACTIVE', 'EXPIRED', 'CLAIMED') NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (invoice_id) REFERENCES Invoice(id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE AuditLog (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NULL,
    action VARCHAR(100) NOT NULL,
    entity_name VARCHAR(100) NOT NULL,
    entity_id INT NULL,
    old_value JSON NULL,
    new_value JSON NULL,
    ip_address VARCHAR(45) NULL,
    device VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE
    SET NULL ON UPDATE CASCADE
);
CREATE INDEX idx_customer_phone ON Customer(phone);
CREATE INDEX idx_vehicle_customer_id ON Vehicle(customer_id);
CREATE INDEX idx_vehicle_license_plate ON Vehicle(license_plate);
CREATE INDEX idx_service_name ON Service(service_name);
CREATE INDEX idx_price_list_service_id ON PriceList(service_id);
CREATE INDEX idx_appointment_customer_id ON Appointment(customer_id);
CREATE INDEX idx_appointment_date ON Appointment(appointment_date);
CREATE INDEX idx_invoice_customer_id ON Invoice(customer_id);
CREATE INDEX idx_auditlog_user_id ON AuditLog(user_id);
INSERT INTO Users(role, username, password, status)
SELECT 'OWNER',
    'owner',
    '123456',
    'ACTIVE'
WHERE NOT EXISTS (
        SELECT 1
        FROM Users
        WHERE role = 'OWNER'
    );
DELIMITER // CREATE TRIGGER prevent_extra_owner BEFORE
INSERT ON Users FOR EACH ROW BEGIN IF NEW.role = 'OWNER'
    AND EXISTS (
        SELECT 1
        FROM Users
        WHERE role = 'OWNER'
    ) THEN SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Only one OWNER account is allowed';
END IF;
END // CREATE TRIGGER prevent_owner_update BEFORE
UPDATE ON Users FOR EACH ROW BEGIN IF OLD.role = 'OWNER' THEN SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'OWNER account cannot be modified';
END IF;
END // CREATE TRIGGER prevent_owner_delete BEFORE DELETE ON Users FOR EACH ROW BEGIN IF OLD.role = 'OWNER' THEN SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'OWNER account cannot be deleted';
END IF;
END // DELIMITER;