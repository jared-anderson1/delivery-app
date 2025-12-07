package deliveryApp.core;

import deliveryApp.users.*;
import deliveryApp.menu.MenuItem;
import deliveryApp.orders.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Central controller for the entire delivery application.
 * Handles menu, users, orders, driver assignment, ratings,
 * and full file-based persistence.
 */
public class DeliverySystem {

    // ---------- File names for persistence ----------
    private static final String MENU_FILE = "menu.txt";
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String DRIVERS_FILE = "drivers.txt";
    private static final String ADMINS_FILE = "admins.txt";
    private static final String ORDERS_FILE = "orders.txt";

    // ---------- In-memory data structures ----------
    private final Map<String, User> users = new HashMap<>();

    private final Queue<Order> pendingOrders = new LinkedList<>();

    private final PriorityQueue<Driver> availableDrivers =
            new PriorityQueue<>((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()));

    private final Map<Integer, Order> allOrders = new HashMap<>();

    private final List<MenuItem> menu = new ArrayList<>();

    private int orderIdCounter = 1;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public DeliverySystem() {
        loadMenuFromFile();
        loadUsersFromFiles();
    }

    // ============================================================
    // AUTHENTICATION
    // ============================================================

    public User login(String username, String password) {
        User u = users.get(username.toLowerCase());
        return (u != null && u.verifyPassword(password)) ? u : null;
    }

    /**
     * Check whether a username is already taken.
     */
    public boolean isUsernameTaken(String username) {
        return username != null && users.containsKey(username.toLowerCase());
    }

    /**
     * Registers a new user and persists to disk.
     */
    public void registerUser(User u) {
        users.put(u.getUserName().toLowerCase(), u);

        if (u instanceof Driver d && d.isAvailable()) {
            availableDrivers.add(d);
        }

        saveUsersToFiles();
    }

    // ============================================================
    // MENU MANAGEMENT + FILE PERSISTENCE
    // ============================================================

    private void loadMenuFromFile() {
        menu.clear();
        Path path = Paths.get(MENU_FILE);

        if (!Files.exists(path)) {
            // Create default menu if file missing
            menu.add(new MenuItem("Hamburger", 5.99));
            menu.add(new MenuItem("Fries", 3.00));
            menu.add(new MenuItem("Drink", 1.50));
            saveMenuToFile();
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) continue;

                String name = parts[0].trim();
                double price = Double.parseDouble(parts[1].trim());

                menu.add(new MenuItem(name, price));
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading menu file. Using default menu.");
            menu.clear();
            menu.add(new MenuItem("Hamburger", 5.99));
            menu.add(new MenuItem("Fries", 3.00));
            menu.add(new MenuItem("Drink", 1.50));
            saveMenuToFile();
        }
    }

    private void saveMenuToFile() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(MENU_FILE))) {
            for (MenuItem item : menu) {
                bw.write(item.getName() + "," + item.getPrice());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing menu file: " + e.getMessage());
        }
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void addMenuItem(String name, double price) {
        menu.add(new MenuItem(name, price));
        saveMenuToFile();
    }

    public void removeMenuItem(String name) {
        menu.removeIf(item -> item.getName().equalsIgnoreCase(name));
        saveMenuToFile();
    }

    // ============================================================
    // USER FILE LOADING + SAVING
    // ============================================================

    private void loadUsersFromFiles() {
        users.clear();

        loadAdmins();
        loadCustomers();
        loadDrivers();

        // Ensure at least one admin exists
        boolean hasAdmin = users.values().stream().anyMatch(u -> u instanceof Admin);
        if (!hasAdmin) {
            Admin admin = new Admin("admin", "admin123",
                    "Admin User", "555-0000", "admin@test.com");
            users.put("admin", admin);
            saveUsersToFiles();
        }
    }

    private void loadAdmins() {
        Path path = Paths.get(ADMINS_FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 5) continue;

                Admin a = new Admin(
                        p[0].trim(), p[1].trim(), p[2].trim(),
                        p[3].trim(), p[4].trim()
                );
                users.put(a.getUserName().toLowerCase(), a);
            }
        } catch (IOException e) {
            System.out.println("Error reading admins file.");
        }
    }

    private void loadCustomers() {
        Path path = Paths.get(CUSTOMERS_FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 5) continue;

                Customer c = new Customer(
                        p[0].trim(), p[1].trim(), p[2].trim(),
                        p[3].trim(), p[4].trim()
                );
                users.put(c.getUserName().toLowerCase(), c);
            }
        } catch (IOException e) {
            System.out.println("Error reading customers file.");
        }
    }

    private void loadDrivers() {
        Path path = Paths.get(DRIVERS_FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 5) continue;

                Driver d = new Driver(
                        p[0].trim(), p[1].trim(), p[2].trim(),
                        p[3].trim(), p[4].trim()
                );

                // optional ratings column
                if (p.length >= 6) {
                    d.loadRatingsFromString(p[5].trim());
                }

                users.put(d.getUserName().toLowerCase(), d);

                if (d.isAvailable()) {
                    availableDrivers.add(d);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading drivers file.");
        }
    }

    private void saveUsersToFiles() {
        try (BufferedWriter adminOut = Files.newBufferedWriter(Paths.get(ADMINS_FILE));
             BufferedWriter custOut = Files.newBufferedWriter(Paths.get(CUSTOMERS_FILE));
             BufferedWriter driverOut = Files.newBufferedWriter(Paths.get(DRIVERS_FILE))) {

            for (User u : users.values()) {

                if (u instanceof Admin a) {
                    adminOut.write(String.join(",",
                            a.getUserName(),
                            a.getPassword(),
                            a.getName(),
                            a.getPhoneNumber(),
                            a.getEmail()));
                    adminOut.newLine();
                }

                else if (u instanceof Customer c) {
                    custOut.write(String.join(",",
                            c.getUserName(),
                            c.getPassword(),
                            c.getName(),
                            c.getPhoneNumber(),
                            c.getEmail()));
                    custOut.newLine();
                }

                else if (u instanceof Driver d) {
                    driverOut.write(String.join(",",
                            d.getUserName(),
                            d.getPassword(),
                            d.getName(),
                            d.getPhoneNumber(),
                            d.getEmail(),
                            d.getRatingsData()));   // ratings: "5;4;3"
                    driverOut.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    // ============================================================
    // ORDER PROCESSING + PERSISTENCE
    // ============================================================

    public Order placeOrder(Customer c, List<MenuItem> items) {
        Order o = new Order(orderIdCounter++, c, items);
        pendingOrders.offer(o);
        allOrders.put(o.getOrderId(), o);

        appendOrderToLog(o);
        tryAssignDrivers();
        return o;
    }

    private void appendOrderToLog(Order o) {
        String driver = (o.getDriver() == null)
                ? ""
                : o.getDriver().getUserName();

        String line = o.getOrderId() + "," +
                o.getCustomer().getUserName() + "," +
                driver + "," +
                o.getStatus() + "," +
                o.totalPrice();

        try (BufferedWriter bw = Files.newBufferedWriter(
                Paths.get(ORDERS_FILE),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            bw.write(line);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error writing order log.");
        }
    }

    private void tryAssignDrivers() {
        Queue<Order> remaining = new LinkedList<>();

        while (!pendingOrders.isEmpty()) {
            Order o = pendingOrders.poll();
            Driver best = availableDrivers.poll();

            if (best != null) {
                o.assignDriver(best);
                appendOrderToLog(o);
            } else {
                remaining.offer(o);
            }
        }

        pendingOrders.addAll(remaining);
    }

    public void updateOrderStatus(Order o, OrderStatus status) {
        o.updateStatus(status);
        appendOrderToLog(o);

        // When delivered, driver becomes available again
        if (status == OrderStatus.DELIVERED) {
            Driver d = o.getDriver();
            if (d != null) {
                d.setAvailable(true);
                availableDrivers.add(d);
            }
        }
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(allOrders.values());
    }

    // ============================================================
    // DRIVER RATING
    // ============================================================

    public void rateDriver(Driver d, int rating) {
        d.addRating(rating);

        if (d.isAvailable()) {
            availableDrivers.remove(d);
            availableDrivers.add(d);
        }

        saveUsersToFiles();
    }
}