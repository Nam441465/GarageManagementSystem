-- Sample data for Garage Management System
-- Import order is important because of foreign key constraints.

-- 1. Customer
INSERT INTO Customer (name, phone, address) VALUES
    ('Nguyễn Văn An', '0901111111', '123 Đường Lê Lợi, Quận 1, TP.HCM'),
    ('Trần Thị Bình', '0902222222', '45 Đường Phạm Ngũ Lão, Quận 1, TP.HCM'),
    ('Lê Hoàng Cường', '0903333333', '88 Đường Võ Văn Tần, Quận 3, TP.HCM');

-- 2. Users
INSERT INTO Users (role, username, password, status) VALUES
    ('OWNER', 'owner', '123456', 'ACTIVE'),
    ('EMPLOYEE', 'mechanic01', '123456', 'ACTIVE'),
    ('EMPLOYEE', 'advisor01', '123456', 'ACTIVE'),
    ('EMPLOYEE', 'cashier01', '123456', 'ACTIVE');

-- 3. Employee
INSERT INTO Employee (name, phone, position, salary, user_id) VALUES
    ('Nguyễn Văn Kỹ Thuật', '0904444444', 'MECHANIC', 18000000.00, 2),
    ('Trần Thị Tư Vấn', '0905555555', 'SERVICE_ADVISOR', 15000000.00, 3),
    ('Phạm Minh Thu Ngân', '0906666666', 'CASHIER', 12000000.00, 4);

-- 4. EmployeeInvite
INSERT INTO EmployeeInvite (invite_code, status, created_by, expires_at) VALUES
    ('INV-2026-001', 'USED', 1, DATE_ADD(NOW(), INTERVAL 30 DAY)),
    ('INV-2026-002', 'UNUSED', 1, DATE_ADD(NOW(), INTERVAL 45 DAY)),
    ('INV-2026-003', 'EXPIRED', 1, DATE_ADD(NOW(), INTERVAL -5 DAY));

-- 5. Vehicle
INSERT INTO Vehicle (customer_id, brand, vehicle_type, status, license_plate, model, year, color) VALUES
    (1, 'Toyota', 'SEDAN', 'IN_SERVICE', '30A-12345', 'Camry 2.0', 2022, 'Đen'),
    (2, 'Honda', 'SUV', 'WAITING', '51B-67890', 'CR-V', 2021, 'Trắng'),
    (3, 'Ford', 'PICKUP', 'AVAILABLE', '59C-11111', 'Ranger', 2023, 'Xám');

-- 6. Service
INSERT INTO Service (service_name, description, category, is_active) VALUES
    ('Rửa xe tổng thể', 'Dịch vụ rửa xe và vệ sinh ngoại thất', 'CLEANING', TRUE),
    ('Bảo dưỡng định kỳ', 'Thay dầu, kiểm tra hệ thống động cơ', 'MAINTENANCE', TRUE),
    ('Sửa chữa động cơ', 'Khắc phục lỗi động cơ và hệ thống nhiên liệu', 'REPAIR', TRUE),
    ('Thay lốp', 'Thay lốp mới theo tiêu chuẩn xe', 'REPLACEMENT', TRUE),
    ('Bảo dưỡng phanh', 'Kiểm tra và thay má phanh', 'MAINTENANCE', TRUE);

-- 7. PriceList
INSERT INTO PriceList (service_id, vehicle_type, vehicle_brand, price, effective_from, effective_to, note) VALUES
    (1, 'SEDAN', 'Toyota', 350000.00, '2026-01-01', NULL, 'Giá rửa xe cho xe sedan'),
    (1, 'SUV', 'Honda', 450000.00, '2026-01-01', NULL, 'Giá rửa xe cho xe SUV'),
    (2, 'SEDAN', 'Toyota', 1500000.00, '2026-01-01', NULL, 'Dịch vụ bảo dưỡng định kỳ sedan'),
    (2, 'SUV', 'Honda', 1800000.00, '2026-01-01', NULL, 'Dịch vụ bảo dưỡng định kỳ SUV'),
    (3, 'SEDAN', 'Toyota', 2500000.00, '2026-01-01', NULL, 'Sửa chữa động cơ sedan'),
    (4, 'PICKUP', 'Ford', 2200000.00, '2026-01-01', NULL, 'Thay lốp pickup'),
    (5, 'SEDAN', 'Toyota', 1300000.00, '2026-01-01', NULL, 'Bảo dưỡng phanh sedan');

-- 8. Appointment
INSERT INTO Appointment (customer_id, vehicle_id, employee_id, appointment_date, status, notes) VALUES
    (1, 1, 2, '2026-08-10 09:00:00', 'COMPLETED', 'Khách đặt lịch bảo dưỡng định kỳ'),
    (2, 2, 2, '2026-08-12 15:30:00', 'CONFIRMED', 'Khách muốn kiểm tra hệ thống phanh'),
    (3, 3, 1, '2026-08-14 08:00:00', 'PENDING', 'Đặt lịch thay lốp cho xe tải');

-- 9. AppointmentServiceItem
INSERT INTO AppointmentServiceItem (appointment_id, service_id, quantity, unit_price, notes) VALUES
    (1, 2, 1, 1500000.00, 'Bảo dưỡng định kỳ cho xe sedan'),
    (1, 5, 1, 1300000.00, 'Kiểm tra và thay má phanh'),
    (2, 5, 1, 1300000.00, 'Kiểm tra phanh trước khi đi xa'),
    (3, 4, 2, 2200000.00, 'Thay lốp cho xe pickup');

-- 10. Invoice
INSERT INTO Invoice (customer_id, employee_id, license_plate, vehicle_type, vehicle_brand, total_amount, issue_date, payment_status, pdf_path) VALUES
    (1, 1, '30A-12345', 'SEDAN', 'Toyota', 2800000.00, '2026-08-10 09:00:00', 'PAID', '/invoices/invoice-INV-0001.pdf'),
    (2, 2, '51B-67890', 'SUV', 'Honda', 1300000.00, '2026-08-12 15:30:00', 'PAID', '/invoices/invoice-INV-0002.pdf'),
    (3, 1, '59C-11111', 'PICKUP', 'Ford', 2200000.00, '2026-08-14 08:00:00', 'UNPAID', '/invoices/invoice-INV-0003.pdf');

-- 11. InvoiceDetail
INSERT INTO InvoiceDetail (invoice_id, service_id, service_name, unit_price, subtotal) VALUES
    (1, 2, 'Bảo dưỡng định kỳ', 1500000.00, 1500000.00),
    (1, 5, 'Bảo dưỡng phanh', 1300000.00, 1300000.00),
    (2, 5, 'Bảo dưỡng phanh', 1300000.00, 1300000.00),
    (3, 4, 'Thay lốp', 2200000.00, 2200000.00);

-- 13. Part
INSERT INTO Part (part_name, part_code, supplier, unit_price, stock_quantity, min_stock, description) VALUES
    ('Má phanh trước', 'PART-BR-001', 'OEM Auto', 650000.00, 20, 5, 'Má phanh xe sedan và SUV'),
    ('Lốp xe pickup 245/75R17', 'PART-TY-004', 'Tyre Pro', 2200000.00, 10, 3, 'Lốp thay thế cho xe pickup'),
    ('Dầu động cơ 5W-30', 'PART-OIL-022', 'Lubricant Co', 280000.00, 50, 12, 'Dầu động cơ cho xe sedan'),
    ('Bộ lọc dầu', 'PART-FILTER-010', 'Filter Max', 180000.00, 30, 8, 'Lọc dầu & lọc gió');

-- 14. Warranty
INSERT INTO Warranty (invoice_id, warranty_code, start_date, end_date, coverage, status) VALUES
    (1, 'WAR-20260810-001', '2026-08-10', '2026-11-10', 'Bảo hành 3 tháng cho dịch vụ bảo dưỡng và phanh', 'ACTIVE'),
    (2, 'WAR-20260812-001', '2026-08-12', '2026-11-12', 'Bảo hành 1 tháng cho kiểm tra phanh', 'ACTIVE');

-- 15. AuditLog
INSERT INTO AuditLog (user_id, action, entity_name, entity_id, old_value, new_value, ip_address, device) VALUES
    (1, 'CREATE_APPOINTMENT', 'Appointment', 1, NULL, '{"status":"PENDING"}', '192.168.1.10', 'PC-Office'),
    (1, 'CREATE_INVOICE', 'Invoice', 1, NULL, '{"payment_status":"PAID"}', '192.168.1.10', 'PC-Office'),
    (2, 'CREATE_INVOICE', 'Invoice', 1, NULL, '{"total_amount":2800000.00}', '192.168.1.15', 'Laptop-Mech');
