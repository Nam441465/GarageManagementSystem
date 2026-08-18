package service;

import dao.AppointmentDAO;
import dao.AppointmentServiceItemDAO;
import dao.VehicleDAO;
import dao.impl.AppointmentDAOImpl;
import dao.impl.AppointmentServiceItemDAOImpl;
import dao.impl.VehicleDAOImpl;

import model.Appointment;
import model.AppointmentServiceItem;
import model.Vehicle;

import java.util.List;
import java.util.Objects;

public class AppointmentService {

        private final AppointmentDAO appointmentDAO;
        private final VehicleDAO vehicleDAO;
        private final AppointmentServiceItemDAO appointmentServiceItemDAO;

        public AppointmentService() {
                this(
                                new AppointmentDAOImpl(),
                                new VehicleDAOImpl(),
                                new AppointmentServiceItemDAOImpl());
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
                        List<AppointmentServiceItem> items) {

                if (appointment == null) {
                        throw new IllegalArgumentException(
                                        "Appointment is required.");
                }

                if (items == null || items.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "At least one service must be selected.");
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

                if (appointment.getAppointmentDate() == null) {
                        throw new IllegalArgumentException(
                                        "Appointment date and time must be selected.");
                }

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
                        return false;
                }

                for (AppointmentServiceItem item : items) {

                        if (item == null) {
                                throw new IllegalArgumentException(
                                                "Service item cannot be null.");
                        }

                        item.setAppointmentId(
                                        appointment.getId());

                        boolean itemCreated = appointmentServiceItemDAO
                                        .addAppointmentServiceItem(item);

                        if (!itemCreated) {
                                return false;
                        }
                }

                return true;
        }

        public Appointment getAppointment(int appointmentId) {

                if (appointmentId <= 0) {
                        throw new IllegalArgumentException(
                                        "Invalid appointment ID.");
                }

                return appointmentDAO.findById(appointmentId);
        }

        public List<Appointment> getAllAppointments() {
                return appointmentDAO.findAll();
        }

        public List<Appointment> getAppointmentsByCustomer(int customerId) {

                if (customerId <= 0) {
                        throw new IllegalArgumentException(
                                        "Invalid customer ID.");
                }

                return appointmentDAO.findByCustomerId(customerId);
        }

        public boolean updateAppointment(Appointment appointment) {

                if (appointment == null) {
                        throw new IllegalArgumentException(
                                        "Appointment is required.");
                }

                if (appointment.getId() <= 0) {
                        throw new IllegalArgumentException(
                                        "Invalid appointment ID.");
                }

                return appointmentDAO.updateAppointment(
                                appointment);
        }

        public boolean deleteAppointment(
                        int appointmentId) {

                if (appointmentId <= 0) {
                        throw new IllegalArgumentException(
                                        "Invalid appointment ID.");
                }

                return appointmentDAO.deleteAppointment(
                                appointmentId);
        }
}