package factory;

public class NotificationFactory {

    public interface NotificationChannel {
        void send(String message);
    }

    public static AppointmentNotification createAppointmentNotification() {
        return new AppointmentNotification();
    }

    public static InvoiceNotification createInvoiceNotification() {
        return new InvoiceNotification();
    }

    public static ServiceRecordNotification createServiceRecordNotification() {
        return new ServiceRecordNotification();
    }

    public static class AppointmentNotification implements NotificationChannel {
        @Override
        public void send(String message) {
            System.out.println("Appointment Notification: " + message);
        }
    }

    public static class InvoiceNotification implements NotificationChannel {
        @Override
        public void send(String message) {
            System.out.println("Invoice Notification: " + message);
        }
    }

    public static class ServiceRecordNotification implements NotificationChannel {
        @Override
        public void send(String message) {
            System.out.println("Service Record Notification: " + message);
        }
    }
}
