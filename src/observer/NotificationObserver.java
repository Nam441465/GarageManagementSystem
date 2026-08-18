package observer;

public class NotificationObserver implements Observer {
    
    private String name;
    
    public NotificationObserver(String name) {
        this.name = name;
    }
    
    @Override
    public void update(String message) {
        System.out.println("[" + name + "] Received notification: " + message);
    }
}
