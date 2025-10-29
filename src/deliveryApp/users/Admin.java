package deliveryApp.users;

import deliveryApp.orders.Order;
import java.util.*;

public class Admin extends User {
    private PriorityQueue<Driver> availableDrivers;
    private Queue<Order> orders;

    public Admin(String name, String password) {
        super(name, password);
        availableDrivers = new PriorityQueue<>();
        orders = new LinkedList<>();
    }

    public void addDriver(Driver driver) {
        availableDrivers.add(driver);
    }

    // Assigns a driver to an order
    public void assignOrder(Order order) {
        Driver bestDriver = availableDrivers.poll();
        if (bestDriver != null) {
            bestDriver.setAvailable(false);
            order.updateStatus("Accepted");
            System.out.println("Order assigned to: " + bestDriver.getName());
        } else {
            System.out.println("No available drivers right now.");
        }
        orders.add(order);
    }

    public void viewOrders() {
        if (orders.isEmpty()) System.out.println("No orders found.");
        else orders.forEach(System.out::println);
    }

    public void viewDrivers() {
        if (availableDrivers.isEmpty()) {
            System.out.println("No available drivers.");
        } else {
            availableDrivers.forEach(System.out::println);
        }
    }
}