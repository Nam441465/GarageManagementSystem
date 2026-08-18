package factory;

import service.*;

public class ServiceFactory {

    public static UserService createUserService() {
        return new UserService();
    }

    public static CustomerService createCustomerService() {
        return new CustomerService();
    }

    public static VehicleService createVehicleService() {
        return new VehicleService();
    }

    public static AppointmentService createAppointmentService() {
        return new AppointmentService();
    }

    public static ServiceRecordService createServiceRecordService() {
        return new ServiceRecordService();
    }

    public static InvoiceService createInvoiceService() {
        return new InvoiceService();
    }

    public static EmployeeService createEmployeeService() {
        return new EmployeeService();
    }

    public static WarrantyService createWarrantyService() {
        return new WarrantyService();
    }

    public static PartService createPartService() {
        return new PartService();
    }

    public static AuditService createAuditService() {
        return new AuditService();
    }

    public static PriceListService createPriceListService() {
        return new PriceListService();
    }

    public static PdfService createPdfService() {
        return new PdfService();
    }
}
