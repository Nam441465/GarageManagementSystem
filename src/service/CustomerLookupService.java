package service;

import dao.AppointmentDAO;
import enums.VehicleStatus;
import model.Appointment;
import model.PriceList;
import model.Service;
import model.Vehicle;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomerLookupService {

    private final VehicleService vehicleService;
    private final PriceListService priceListService;
    private final ServiceService serviceService;
    private final AppointmentDAO appointmentDAO;

    public CustomerLookupService() {
        this(
                new VehicleService(),
                new PriceListService(),
                new ServiceService(),
                new AppointmentDAO()
        );
    }

    public CustomerLookupService(
            VehicleService vehicleService,
            PriceListService priceListService,
            ServiceService serviceService) {
        this(vehicleService, priceListService, serviceService, new AppointmentDAO());
    }

    public CustomerLookupService(
            VehicleService vehicleService,
            PriceListService priceListService,
            ServiceService serviceService,
            AppointmentDAO appointmentDAO) {

        this.vehicleService = Objects.requireNonNull(
                vehicleService,
                "vehicleService is required"
        );

        this.priceListService = Objects.requireNonNull(
                priceListService,
                "priceListService is required"
        );

        this.serviceService = Objects.requireNonNull(
                serviceService,
                "serviceService is required"
        );

        this.appointmentDAO = Objects.requireNonNull(
                appointmentDAO,
                "appointmentDAO is required"
        );
    }

    public void validateVehicleTypeAndBrand(String vehicleType, String vehicleBrand) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vui lòng chọn loại xe.");
        }

        if (vehicleBrand == null) {
            throw new IllegalArgumentException("Vui lòng chọn hãng xe.");
        }
    }

    public void validateLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập biển số xe.");
        }
    }

    // ==================== Business Logic ====================

    /**
     * Tra cứu tất cả dịch vụ có giá phù hợp
     * với loại xe và hãng xe.
     */
    public List<ServicePriceResult> findServicesByVehicle(
            String vehicleType,
            String vehicleBrand) {

        if (vehicleType == null
                || vehicleType.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Vui lòng chọn loại xe.");
        }

        if (vehicleBrand == null
                || vehicleBrand.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Vui lòng chọn hãng xe.");
        }

        String type = vehicleType.trim();
        String brand = vehicleBrand.trim();

        List<PriceList> prices =
                priceListService.getAllPrices();

        List<Service> services =
                serviceService.findAll();

        return prices.stream()
                .filter(price ->
                        price.getVehicleType() != null
                                && price.getVehicleBrand() != null)
                .filter(price ->
                        price.getVehicleType()
                                .equalsIgnoreCase(type))
                .filter(price ->
                        price.getVehicleBrand()
                                .equalsIgnoreCase(brand))
                .map(price -> {

                    Service service = services.stream()
                            .filter(s ->
                                    s != null
                                            && s.getId()
                                            == price.getServiceId())
                            .findFirst()
                            .orElse(null);

                    if (service == null) {
                        return null;
                    }

                    return new ServicePriceResult(
                            service.getId(),
                            service.getServiceName(),
                            service.getDescription(),
                            price.getPrice()
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Tra cứu xe bằng biển số (tìm trong bảng Vehicle hoặc Appointment).
     */
    public Vehicle findVehicleByLicensePlate(
            String licensePlate) {

        if (licensePlate == null
                || licensePlate.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vui lòng nhập biển số xe.");
        }

        String plate = licensePlate.trim();

        // 1. Tìm trong bảng Vehicle
        Vehicle vehicle = vehicleService.findByLicensePlate(plate);

        if (vehicle != null) {
            return vehicle;
        }

        // 2. Tìm trong bảng Appointment nếu chưa có trong Vehicle
        Appointment appointment = appointmentDAO.findByLicensePlate(plate);
        if (appointment != null) {
            Vehicle tempVehicle = new Vehicle();
            tempVehicle.setId(appointment.getId());
            tempVehicle.setLicensePlate(appointment.getLicensePlate());
            tempVehicle.setVehicleBrand(appointment.getVehicleBrand());
            tempVehicle.setVehicleType(appointment.getVehicleType());
            tempVehicle.setStatus(VehicleStatus.WAITING);
            String modelDesc = "Lịch hẹn: " + appointment.getCustomerName();
            if (appointment.getCustomerPhone() != null && !appointment.getCustomerPhone().isEmpty()) {
                modelDesc += " (" + appointment.getCustomerPhone() + ")";
            }
            tempVehicle.setModel(modelDesc);
            return tempVehicle;
        }

        throw new IllegalArgumentException(
                "Không tìm thấy thông tin xe hoặc lịch hẹn với biển số: "
                        + plate);
    }

    /**
     * Kiểm tra trạng thái xe dưới dạng text
     * để hiển thị cho khách hàng.
     */
    public String getVehicleStatus(Vehicle vehicle) {

        if (vehicle == null) {
            throw new IllegalArgumentException(
                    "Thông tin xe không được để trống.");
        }

        if (vehicle.getStatus() == null) {
            return "Chưa xác định";
        }

        return switch (vehicle.getStatus()) {
            case COMPLETED ->
                    "Đã làm xong (Sẵn sàng bàn giao xe)";
            case IN_SERVICE ->
                    "Đang làm / Đang sửa chữa";
            case WAITING ->
                    "Đang chờ tiếp nhận / xử lý tại gara";
            case DELIVERED ->
                    "Đã giao xe cho khách hàng";
            case AVAILABLE ->
                    "Xe đã có thông tin trong hệ thống gara (Sẵn sàng tiếp nhận)";
        };
    }

    /**
     * DTO dùng riêng cho màn hình tra cứu giá dịch vụ.
     */
    public static class ServicePriceResult {

        private final int serviceId;
        private final String serviceName;
        private final String description;
        private final java.math.BigDecimal price;

        public ServicePriceResult(
                int serviceId,
                String serviceName,
                String description,
                java.math.BigDecimal price) {

            this.serviceId = serviceId;
            this.serviceName = serviceName;
            this.description = description;
            this.price = price;
        }

        public int getServiceId() {
            return serviceId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getDescription() {
            return description;
        }

        public java.math.BigDecimal getPrice() {
            return price;
        }
    }
}
