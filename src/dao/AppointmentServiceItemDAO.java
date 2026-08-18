package dao;

import model.AppointmentServiceItem;

import java.util.List;

public interface AppointmentServiceItemDAO extends BaseDAO<AppointmentServiceItem> {

    boolean addAppointmentServiceItem(AppointmentServiceItem item);

    AppointmentServiceItem findById(int id);

    List<AppointmentServiceItem> findAll();

    List<AppointmentServiceItem> findByAppointmentId(int appointmentId);

    boolean updateAppointmentServiceItem(AppointmentServiceItem item);

    boolean deleteAppointmentServiceItem(int id);

    @Override
    default boolean create(AppointmentServiceItem value) {
        return addAppointmentServiceItem(value);
    }

    @Override
    default AppointmentServiceItem read(int id) {
        return findById(id);
    }

    @Override
    default List<AppointmentServiceItem> readAll() {
        return findAll();
    }

    @Override
    default boolean update(AppointmentServiceItem value) {
        return updateAppointmentServiceItem(value);
    }

    @Override
    default boolean delete(int id) {
        return deleteAppointmentServiceItem(id);
    }
}
