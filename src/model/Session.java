package model;

public class Session {

    private static User currentUser;
    /* Customer portal is intentionally separate from the employee user session. */
    private static Customer currentCustomer;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public static void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
    }
 
    public static void logout() {
        currentUser = null;
        currentCustomer = null;
    }
}
