package dao;

import model.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentDAO extends BaseDAO<Appointment> {

    boolean addAppointment(Appointment appointment);

    Appointment findById(int id);

    List<Appointment> findAll();

    List<Appointment> findByCustomerId(int customerId);

    boolean updateAppointment(Appointment appointment);

    boolean deleteAppointment(int id);

    boolean existsConflict(LocalDateTime appointmentDate);
}