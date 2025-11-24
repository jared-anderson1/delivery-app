package deliveryApp.users;

import deliveryApp.managers.DriverManager;
import deliveryApp.managers.MenuManager;
import deliveryApp.managers.OrderManager;
import deliveryApp.managers.UserManager;
import deliveryApp.orders.Order;

import java.util.Optional;

/**
 * Admin class for performing manual operations on the system.
 * It delegates the actual work to the respective manager classes.
 */
public class Admin extends User {

    private final UserManager userManager;
    private final DriverManager driverManager;
    private final OrderManager orderManager;
    private final MenuManager menuManager;

    public Admin(String name, String password, UserManager userManager, DriverManager driverManager, OrderManager orderManager, MenuManager menuManager) {
        super(name, password);
        this.userManager = userManager;
        this.driverManager = driverManager;
        this.orderManager = orderManager;
        this.menuManager = menuManager;

        // The admin should also be in the user manager
        userManager.addUser(this);
    }

    // --- Manual User Operations ---

    public void suspendUser(String username) {
        Optional<User> userOpt = userManager.findUser(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setSuspended(true);
            userManager.updateUser(user); // Save the change
            System.out.println("User '" + username + "' has been suspended.");
        } else {
            System.out.println("User '" + username + "' not found.");
        }
    }

    public void resumeUser(String username) {
        Optional<User> userOpt = userManager.findUser(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setSuspended(false);
            userManager.updateUser(user); // Save the change
            System.out.println("User '" + username + "' has been resumed.");
        } else {
            System.out.println("User '" + username + "' not found.");
        }
    }

    public void viewAllUsers() {
        System.out.println("All Users:");
        userManager.getAllUsers().forEach(System.out::println);
    }

    // --- Manual Driver and Order Operations (using placeholder managers) ---

    public void assignOrderToDriver(Order order, Driver driver) {
        // This would be implemented using OrderManager and DriverManager
        System.out.println("Assigning order to driver (not yet implemented).");
    }

    public void viewAvailableDrivers() {
        System.out.println("Available Drivers:");
        // driverManager.getAvailableDrivers().forEach(System.out::println);
        System.out.println("(Not yet implemented)");
    }

    public void viewAllOrders() {
        System.out.println("All Orders:");
        // orderManager.getAllOrders().forEach(System.out::println);
        System.out.println("(Not yet implemented)");
    }
}
