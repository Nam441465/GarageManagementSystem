package service;

import factory.NotificationFactory;

public class NotificationService {

    public void notifyAppointmentCreated(String appointmentDetails) {
        send(NotificationFactory.createAppointmentNotification(), "New appointment created: " + appointmentDetails);
    }

    public void notifyAppointmentUpdated(String appointmentDetails) {
        send(NotificationFactory.createAppointmentNotification(), "Appointment updated: " + appointmentDetails);
    }

    public void notifyInvoiceCreated(String invoiceDetails) {
        send(NotificationFactory.createInvoiceNotification(), "New invoice created: " + invoiceDetails);
    }

    public void notifyInvoicePaid(String invoiceDetails) {
        send(NotificationFactory.createInvoiceNotification(), "Invoice paid: " + invoiceDetails);
    }

    public void notifyServiceRecordCompleted(String serviceDetails) {
        send(NotificationFactory.createServiceRecordNotification(), "Service record completed: " + serviceDetails);
    }

    public void notifyServiceRecordCreated(String serviceDetails) {
        send(NotificationFactory.createServiceRecordNotification(), "New service record created: " + serviceDetails);
    }

    public void notifyLowStockAlert(String partName, int currentStock, int minStock) {
        System.out.println("ALERT: " + partName + " stock is low. Current: " + currentStock + ", Minimum: " + minStock);
    }

    private void send(NotificationFactory.NotificationChannel channel, String message) {
        channel.send(message);
    }
}
