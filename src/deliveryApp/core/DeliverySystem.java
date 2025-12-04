package deliveryApp.core;

import deliveryApp.users.*;
import deliveryApp.menu.MenuItem;
import deliveryApp.orders.*;

import java.util.*;

/**
 * Central controller for the entire delivery application.
 * Handles users, menu, order placement, driver assignment, and ratings.
 */
public class DeliverySystem {

    // Stores all users by username
    private final Map<String, User> users = new HashMap<>();

    // Queue of orders waiting for assignment
    private final Queue<Order> pendingOrders = new LinkedList<>();

    // Drivers ordered by highest average rating first
    private final PriorityQueue<Driver> availableDrivers =
            new PriorityQueue<>((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()));

    // Stores all orders by ID
    private final Map<Integer, Order> allOrders = new HashMap<>();

    // The system-wide menu
    private final List<MenuItem> menu = new ArrayList<>();

    private int orderIdCounter = 1;

    public DeliverySystem() {
        loadMenu();
        loadDefaultUsers();
    }

    /**
     * Attempts to authenticate a user.
     */
    public User login(String username, String password) {
        User u = users.get(username.toLowerCase());
        return (u != null && u.verifyPassword(password)) ? u : null;
    }

    /**
     * Registers a user and adds drivers to the priority queue when available.
     */
    public void registerUser(User u) {
        users.put(u.getUserName().toLowerCase(), u);
        if (u instanceof Driver d && d.isAvailable()) {
            availableDrivers.add(d);
        }
    }

    // ------------------------------
    // MENU MANAGEMENT
    // ------------------------------

    private void loadMenu() {
        menu.add(new MenuItem("Hamburger", 5.99));
        menu.add(new MenuItem("Fries", 3.00));
        menu.add(new MenuItem("Drink", 1.50));
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void addMenuItem(String name, double price) {
        menu.add(new MenuItem(name, price));
    }

    public void removeMenuItem(String name) {
        menu.removeIf(item -> item.getName().equalsIgnoreCase(name));
    }

    // ------------------------------
    // ORDER PROCESSING
    // ------------------------------

    /**
     * Places a new order and attempts to assign a driver immediately.
     */
    public Order placeOrder(Customer c, List<MenuItem> items) {
        Order order = new Order(orderIdCounter++, c, items);
        pendingOrders.offer(order);
        allOrders.put(order.getOrderId(), order);
        tryAssignDrivers();
        return order;
    }

    /**
     * Tries to assign waiting orders to the best-available drivers.
     */
    private void tryAssignDrivers() {
        Queue<Order> remaining = new LinkedList<>();

        while (!pendingOrders.isEmpty()) {
            Order o = pendingOrders.poll();
            Driver best = availableDrivers.poll();

            if (best != null) {
                o.assignDriver(best);
            } else {
                remaining.offer(o);
            }
        }

        pendingOrders.addAll(remaining);
    }

    /**
     * Updates an order's status (e.g., delivered, in-progress).
     */
    public void updateOrderStatus(Order order, OrderStatus status) {
        order.updateStatus(status);

        if (status == OrderStatus.DELIVERED) {
            Driver d = order.getDriver();
            if (d != null) {
                d.setAvailable(true);
                availableDrivers.add(d);
            }
        }
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(allOrders.values());
    }

    // ------------------------------
    // DRIVER RATING
    // ------------------------------

    /**
     * Adds a rating to a driver and updates their priority position.
     */
    public void rateDriver(Driver d, int rating) {
        d.addRating(rating);

        if (d.isAvailable()) {
            availableDrivers.remove(d);
            availableDrivers.add(d);
        }
    }

    // ------------------------------
    // DEFAULT USERS FOR TESTING
    // ------------------------------

    private void loadDefaultUsers() {
        registerUser(new Admin("admin", "admin123", "Admin User", "555-0000", "admin@test.com"));

        Driver d1 = new Driver("driver1", "pass1", "John Driver", "555-1111", "driver1@test.com");
        d1.addRating(5);
        d1.addRating(4);
        registerUser(d1);

        Driver d2 = new Driver("driver2", "pass2", "Lucy Driver", "555-2222", "driver2@test.com");
        d2.addRating(3);
        registerUser(d2);

        registerUser(new Customer("customer1", "cpass", "Mina Customer", "555-3333", "cust1@test.com"));
    }
}