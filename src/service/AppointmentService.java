package service;

import dao.AppointmentDAO;
import dao.AppointmentServiceItemDAO;
import dao.VehicleDAO;

import model.Appointment;
import model.AppointmentServiceItem;
import model.Vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class AppointmentService {

        private final AppointmentDAO appointmentDAO;

        private final VehicleDAO vehicleDAO;

        private final AppointmentServiceItemDAO appointmentServiceItemDAO;

        public AppointmentService() {

                this(
                                new AppointmentDAO(),
                                new VehicleDAO(),
                                new AppointmentServiceItemDAO());
        }

        public AppointmentService(
                        AppointmentDAO appointmentDAO,
                        VehicleDAO vehicleDAO,
                        AppointmentServiceItemDAO appointmentServiceItemDAO) {

                this.appointmentDAO = Objects.requireNonNull(
                                appointmentDAO,
                                "appointmentDAO is required");

                this.vehicleDAO = Objects.requireNonNull(
                                vehicleDAO,
                                "vehicleDAO is required");

                this.appointmentServiceItemDAO = Objects.requireNonNull(
                                appointmentServiceItemDAO,
                                "appointmentServiceItemDAO is required");
        }

        public boolean createAppointment(
                        Appointment appointment,
                        List<AppointmentServiceItem> items,
                        LocalDate appointmentDate,
                        LocalTime appointmentTime) {

                validateAppointment(appointment);

                validateServiceItems(items);

                LocalDateTime appointmentDateTime = createAppointmentDateTime(
                                appointmentDate,
                                appointmentTime);

                appointment.setAppointmentDate(
                                appointmentDateTime);

                Vehicle vehicle = vehicleDAO.findByLicensePlate(
                                appointment.getLicensePlate());

                if (vehicle != null) {

                        appointment.setCustomerId(
                                        vehicle.getCustomerId());

                } else {

                        appointment.setCustomerId(null);
                }

                if (appointmentDAO.existsConflict(
                                appointment.getAppointmentDate())) {

                        throw new IllegalArgumentException(
                                        "Appointment time is already booked.");
                }

                boolean created = appointmentDAO.addAppointment(
                                appointment);

                if (!created) {

                        throw new IllegalStateException(
                                        "Cannot create appointment.");
                }

                for (AppointmentServiceItem item : items) {

                        validateServiceItem(item);

                        item.setAppointmentId(
                                        appointment.getId());

                        boolean itemCreated = appointmentServiceItemDAO
                                        .addAppointmentServiceItem(item);

                        if (!itemCreated) {

                                throw new IllegalStateException(
                                                "Cannot create appointment service item.");
                        }
                }

                return true;
        }

        private void validateAppointment(
                        Appointment appointment) {

                if (appointment == null) {

                        throw new IllegalArgumentException(
                                        "Appointment is required.");
                }

                if (appointment.getCustomerName() == null
                                || appointment.getCustomerName().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Customer name is required.");
                }

                if (appointment.getCustomerPhone() == null
                                || appointment.getCustomerPhone().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Customer phone is required.");
                }

                if (appointment.getLicensePlate() == null
                                || appointment.getLicensePlate().isBlank()) {

                        throw new IllegalArgumentException(
                                        "License plate is required.");
                }

                if (appointment.getVehicleBrand() == null
                                || appointment.getVehicleBrand().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Vehicle brand must be selected.");
                }

                if (appointment.getVehicleType() == null
                                || appointment.getVehicleType().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Vehicle type must be selected.");
                }
        }

        private void validateServiceItems(
                        List<AppointmentServiceItem> items) {

                if (items == null || items.isEmpty()) {

                        throw new IllegalArgumentException(
                                        "At least one service must be selected.");
                }

                for (AppointmentServiceItem item : items) {

                        validateServiceItem(item);
                }
        }

        private void validateServiceItem(
                        AppointmentServiceItem item) {

                if (item == null) {

                        throw new IllegalArgumentException(
                                        "Service item cannot be null.");
                }
        }

        private LocalDateTime createAppointmentDateTime(
                        LocalDate date,
                        LocalTime time) {

                if (date == null) {

                        throw new IllegalArgumentException(
                                        "Please select an appointment date.");
                }

                if (time == null) {

                        throw new IllegalArgumentException(
                                        "Please select an appointment time.");
                }

                LocalDateTime dateTime = LocalDateTime.of(date, time);

                if (dateTime.isBefore(
                                LocalDateTime.now())) {

                        throw new IllegalArgumentException(
                                        "Cannot book an appointment in the past.");
                }

                return dateTime;
        }

        public Appointment getAppointment(
                        int appointmentId) {

                validateAppointmentId(appointmentId);

                Appointment appointment = appointmentDAO.findById(appointmentId);

                if (appointment == null) {

                        throw new IllegalArgumentException(
                                        "Appointment not found.");
                }

                return appointment;
        }

        public List<Appointment> getAllAppointments() {

                List<Appointment> appointments = appointmentDAO.findAll();

                if (appointments == null) {

                        throw new IllegalStateException(
                                        "Cannot load appointments.");
                }

                return appointments;
        }

        public List<Appointment> getAppointmentsByCustomer(
                        int customerId) {

                validateCustomerId(customerId);

                List<Appointment> appointments = appointmentDAO.findByCustomerId(
                                customerId);

                if (appointments == null) {

                        throw new IllegalStateException(
                                        "Cannot load customer appointments.");
                }

                return appointments;
        }

        public boolean updateAppointment(
                        Appointment appointment) {

                if (appointment == null) {

                        throw new IllegalArgumentException(
                                        "Appointment is required.");
                }

                validateAppointmentId(
                                appointment.getId());

                validateAppointment(appointment);

                boolean updated = appointmentDAO.updateAppointment(
                                appointment);

                if (!updated) {

                        throw new IllegalStateException(
                                        "Cannot update appointment.");
                }

                return true;
        }

        public boolean deleteAppointment(
                        int appointmentId) {

                validateAppointmentId(appointmentId);

                boolean deleted = appointmentDAO.deleteAppointment(
                                appointmentId);

                if (!deleted) {

                        throw new IllegalStateException(
                                        "Cannot delete appointment.");
                }

                return true;
        }

        private void validateAppointmentId(
                        int appointmentId) {

                if (appointmentId <= 0) {

                        throw new IllegalArgumentException(
                                        "Invalid appointment ID.");
                }
        }

        private void validateCustomerId(
                        int customerId) {

                if (customerId <= 0) {

                        throw new IllegalArgumentException(
                                        "Invalid customer ID.");
                }
        }
}