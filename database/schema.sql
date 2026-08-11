CREATE TABLE Customer(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(255)
);

CREATE TABLE Users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    role VARCHAR(50),
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE'
);

CREATE TABLE Vehicle(
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    brand VARCHAR(50),
    vehicle_type VARCHAR(30),
    status VARCHAR(30),
    license_plate VARCHAR(20) UNIQUE,
    model VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES Customer(id)
);

CREATE TABLE Service(
    id INT PRIMARY KEY AUTO_INCREMENT,
    service_name VARCHAR(50),
    price DOUBLE,
    description VARCHAR(250)
);

CREATE TABLE ServiceRecord(
    id INT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id INT,
    recordDate DATE,
    notes VARCHAR(250),
    total_cost DOUBLE,
    created_by INT,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(id),
    FOREIGN KEY (created_by) REFERENCES Users(id)
);

CREATE TABLE Employee(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    phone VARCHAR(20),
    position VARCHAR(50),
    salary DOUBLE,
    user_id INT UNIQUE,
    FOREIGN KEY(user_id) REFERENCES Users(id)
);

CREATE TABLE ServiceRecordDetail(
    id INT PRIMARY KEY AUTO_INCREMENT,
    service_record_id INT,
    service_id INT,
    quantity INT,
    price DOUBLE,
    subtotal DOUBLE,
    FOREIGN KEY (service_record_id) REFERENCES ServiceRecord(id),
    FOREIGN KEY (service_id) REFERENCES Service(id)
);

CREATE TABLE Invoice(
    id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT,
    total_amount DOUBLE,
    issueDate DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (record_id) REFERENCES ServiceRecord(id)
);

CREATE TABLE EmployeeInvite(
    id INT PRIMARY KEY AUTO_INCREMENT,
    invite_code VARCHAR(50) UNIQUE NOT NULL,
    status ENUM('UNUSED','USED') DEFAULT 'UNUSED',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO Users(role, username, password, status)
SELECT 'Owner', 'owner', '123456', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM Users WHERE role = 'Owner');

DELIMITER //

CREATE TRIGGER prevent_extra_owner
BEFORE INSERT ON Users
FOR EACH ROW
BEGIN
    IF NEW.role = 'Owner' AND EXISTS (SELECT 1 FROM Users WHERE role = 'Owner') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only one Owner account is allowed';
    END IF;
END//

CREATE TRIGGER prevent_owner_update
BEFORE UPDATE ON Users
FOR EACH ROW
BEGIN
    IF OLD.role = 'Owner' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Owner account cannot be modified';
    END IF;
END//

CREATE TRIGGER prevent_owner_delete
BEFORE DELETE ON Users
FOR EACH ROW
BEGIN
    IF OLD.role = 'Owner' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Owner account cannot be deleted';
    END IF;
END//

DELIMITER ;
