package service;

import dao.AppointmentServiceItemDAO;
import model.AppointmentServiceItem;

import java.math.BigDecimal;
import java.util.List;

public class AppointmentServiceItemService {

    private final AppointmentServiceItemDAO appointmentServiceItemDAO;

    public AppointmentServiceItemService() {
        this(new AppointmentServiceItemDAO());
    }

    public AppointmentServiceItemService(AppointmentServiceItemDAO appointmentServiceItemDAO) {
        this.appointmentServiceItemDAO = java.util.Objects.requireNonNull(appointmentServiceItemDAO,
                "appointmentServiceItemDAO is required");
    }

    public boolean addServiceToAppointment(int appointmentId, int serviceId, int quantity, BigDecimal unitPrice,String notes) {
        validateItem(appointmentId, serviceId, quantity, unitPrice);
        AppointmentServiceItem item = new AppointmentServiceItem(appointmentId, serviceId, quantity, unitPrice, notes);
        
        return appointmentServiceItemDAO.addAppointmentServiceItem(item);
    }

    public AppointmentServiceItem getItem(int itemId) {
        validateId(itemId, "item id");
        return appointmentServiceItemDAO.findById(itemId);
    }

    public List<AppointmentServiceItem> getAllItems() {
        return appointmentServiceItemDAO.findAll();
    }

    public List<AppointmentServiceItem> getServicesByAppointment(int appointmentId) {
        validateId(appointmentId, "appointment id");
        return appointmentServiceItemDAO.findByAppointmentId(appointmentId);
    }

    public boolean updateItem(AppointmentServiceItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Appointment service item is required");
        }
        validateId(item.getId(), "item id");
        validateItem(item.getAppointmentId(), item.getServiceId(), item.getQuantity(), item.getUnitPrice());
        appointmentServiceItemDAO.updateAppointmentServiceItem(item);
        return true;
    }

    public boolean deleteItem(int itemId) {
        validateId(itemId, "item id");
        appointmentServiceItemDAO.deleteAppointmentServiceItem(itemId);
        return true;
    }

    public BigDecimal calculateTotalForAppointment(int appointmentId) {
        validateId(appointmentId, "appointment id");
        List<AppointmentServiceItem> items = appointmentServiceItemDAO.findByAppointmentId(appointmentId);
        BigDecimal total = BigDecimal.ZERO;
        for (AppointmentServiceItem item : items) {
            total = total.add(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private void validateItem(int appointmentId, int serviceId, int quantity, BigDecimal unitPrice) {
        validateId(appointmentId, "appointment id");
        validateId(serviceId, "service id");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("Unit price must not be negative");
        }
    }

    private void validateId(int id, String field) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid " + field);
        }
    }
}
