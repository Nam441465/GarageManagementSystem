package service;

import dao.AppointmentDAO;
import dao.AppointmentItemDAO;
import dao.EmployeeDAO;

import model.Appointment;
import model.AppointmentItem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final EmployeeDAO employeeDAO;
    private final AppointmentItemDAO appointmentItemDAO;

    public AppointmentService() {
        this(
                new AppointmentDAO(),
                new EmployeeDAO(),
                new AppointmentItemDAO());
    }

    public AppointmentService(
            AppointmentDAO appointmentDAO,
            EmployeeDAO employeeDAO,
            AppointmentItemDAO appointmentItemDAO) {

        this.appointmentDAO = Objects.requireNonNull(
                appointmentDAO,
                "appointmentDAO is required");

        this.employeeDAO = Objects.requireNonNull(
                employeeDAO,
                "employeeDAO is required");

        this.appointmentItemDAO = Objects.requireNonNull(
                appointmentItemDAO,
                "appointmentItemDAO is required");
    }

    public boolean createAppointment(
            Appointment appointment,
            List<AppointmentItem> items,
            LocalDate appointmentDate,
            LocalTime appointmentTime) {

        validateAppointment(appointment);
        validateServiceItems(items);

        LocalDateTime dateTime = createAppointmentDateTime(
                appointmentDate,
                appointmentTime);

        appointment.setAppointmentDate(
                appointmentDate.atStartOfDay());

        appointment.setAppointmentTime(
                appointmentDate.atTime(appointmentTime));

        if (!isTimeSlotAvailable(dateTime)) {
            throw new IllegalArgumentException(
                    "This appointment time is fully booked.");
        }

        boolean created = appointmentDAO.addAppointment(appointment);

        if (!created) {
            throw new IllegalStateException(
                    "Không thể tạo lịch hẹn.");
        }

        for (AppointmentItem item : items) {

            validateServiceItem(item);

            item.setAppointmentId(
                    appointment.getId());

            boolean itemCreated = appointmentItemDAO
                    .addAppointmentItem(item);

            if (!itemCreated) {
                throw new IllegalStateException(
                        "Không thể lưu hạng mục dịch vụ của lịch hẹn.");
            }
        }

        return true;
    }

    public boolean isTimeSlotAvailable(
            LocalDateTime appointmentDateTime) {

        if (appointmentDateTime == null) {
            throw new IllegalArgumentException(
                    "Ngày và giờ hẹn không được để trống.");
        }

        int employeeCount = employeeDAO.countEmployees();

        int appointmentCount = appointmentDAO.countAppointmentsAtTime(
                appointmentDateTime.toLocalDate().atStartOfDay(),
                appointmentDateTime);

        return employeeCount > appointmentCount;
    }

    public int getEmployeeCount() {
        return employeeDAO.countEmployees();
    }

    public int getAppointmentCountAtTime(
            LocalDateTime appointmentDateTime) {

        if (appointmentDateTime == null) {
            throw new IllegalArgumentException(
                    "Ngày và giờ hẹn không được để trống.");
        }

        return appointmentDAO.countAppointmentsAtTime(
                appointmentDateTime.toLocalDate().atStartOfDay(),
                appointmentDateTime);
    }

    public Appointment getAppointment(
            int appointmentId) {

        validateAppointmentId(appointmentId);

        Appointment appointment = appointmentDAO.findById(appointmentId);

        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy lịch hẹn.");
        }

        return appointment;
    }

    public List<Appointment> getAllAppointments() {

        List<Appointment> appointments = appointmentDAO.findAll();

        if (appointments == null) {
            throw new IllegalStateException(
                    "Không thể tải danh sách lịch hẹn.");
        }

        return appointments;
    }

    public boolean updateAppointment(
            Appointment appointment) {

        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Thông tin lịch hẹn không được để trống.");
        }

        validateAppointmentId(
                appointment.getId());

        validateAppointment(appointment);

        appointmentDAO.update(appointment);

        return true;
    }

    public boolean deleteAppointment(
            int appointmentId) {

        validateAppointmentId(appointmentId);

        appointmentDAO.delete(appointmentId);

        return true;
    }

    private void validateAppointment(
            Appointment appointment) {

        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Thông tin lịch hẹn không được để trống.");
        }

        if (appointment.getCustomerName() == null
                || appointment.getCustomerName().isBlank()) {

            throw new IllegalArgumentException(
                    "Tên khách hàng không được để trống.");
        }

        if (appointment.getCustomerPhone() == null
                || appointment.getCustomerPhone().isBlank()) {

            throw new IllegalArgumentException(
                    "Số điện thoại khách hàng không được để trống.");
        }

        if (appointment.getLicensePlate() == null
                || appointment.getLicensePlate().isBlank()) {

            throw new IllegalArgumentException(
                    "Biển số xe không được để trống.");
        }

        if (appointment.getVehicleBrand() == null) {
            throw new IllegalArgumentException(
                    "Vui lòng chọn hãng xe.");
        }

        if (appointment.getVehicleType() == null) {
            throw new IllegalArgumentException(
                    "Vui lòng chọn loại xe.");
        }
    }

    private void validateServiceItems(
            List<AppointmentItem> items) {

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Vui lòng chọn ít nhất một dịch vụ.");
        }

        for (AppointmentItem item : items) {
            validateServiceItem(item);
        }
    }

    private void validateServiceItem(
            AppointmentItem item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "Hạng mục dịch vụ không hợp lệ.");
        }

        if (item.getServiceId() <= 0) {
            throw new IllegalArgumentException(
                    "Mã dịch vụ không hợp lệ.");
        }
    }

    private LocalDateTime createAppointmentDateTime(
            LocalDate date,
            LocalTime time) {

        if (date == null) {
            throw new IllegalArgumentException(
                    "Vui lòng chọn ngày hẹn.");
        }

        if (time == null) {
            throw new IllegalArgumentException(
                    "Vui lòng chọn giờ hẹn.");
        }

        LocalDateTime dateTime = LocalDateTime.of(date, time);

        if (dateTime.isBefore(
                LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Không thể đặt lịch hẹn trong quá khứ.");
        }

        return dateTime;
    }

    private void validateAppointmentId(
            int appointmentId) {

        if (appointmentId <= 0) {
            throw new IllegalArgumentException(
                    "Mã lịch hẹn không hợp lệ.");
        }
    }
}