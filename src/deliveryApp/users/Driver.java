package deliveryApp.users;

import deliveryApp.core.DeliverySystem;
import deliveryApp.orders.Order;
import deliveryApp.orders.OrderStatus;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Represents a delivery driver.
 * Includes a dashboard for managing assigned orders.
 * Uses safe input parsing to prevent input mismatch crashes,
 * and provides helpers for saving/loading ratings to/from files.
 */
public class Driver extends User {

    private boolean available = true;
    private final Queue<Integer> ratings = new LinkedList<>();
    private double averageRating = 0.0;

    public Driver(String username, String pw, String name,
                  String phone, String email) {
        super(username, pw, name, phone, email);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean a) {
        available = a;
    }

    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Adds a rating (keeps only the last 10 ratings).
     */
    public void addRating(int r) {
        if (ratings.size() == 10) ratings.poll();
        ratings.offer(r);
        updateAverage();
    }

    private void updateAverage() {
        double sum = 0;
        for (int r : ratings) sum += r;
        averageRating = ratings.isEmpty() ? 0 : sum / ratings.size();
    }

    /**
     * Returns ratings as a semicolon-separated string (e.g. "5;4;3")
     * so they can be written to drivers.txt.
     */
    public String getRatingsData() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int r : ratings) {
            if (!first) sb.append(";");
            sb.append(r);
            first = false;
        }
        return sb.toString();
    }

    /**
     * Loads ratings from a semicolon-separated string (e.g. "5;4;3")
     * when reading from drivers.txt.
     */
    public void loadRatingsFromString(String data) {
        if (data == null || data.isBlank()) return;
        String[] parts = data.split(";");
        for (String p : parts) {
            try {
                int r = Integer.parseInt(p.trim());
                addRating(r);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    @Override
    public void showMenu(DeliverySystem system) {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- DRIVER MENU ---");
            System.out.println("1. View My Assigned Orders");
            System.out.println("2. Mark Order as IN_PROGRESS");
            System.out.println("3. Mark Order as DELIVERED");
            System.out.println("4. Logout");
            System.out.print("Choice: ");

            int c = getIntInput(in);   // SAFE INPUT

            if (c == 1) {
                showAssignedOrders(system);
            } else if (c == 2) {
                updateOrder(system, OrderStatus.IN_PROGRESS);
            } else if (c == 3) {
                updateOrder(system, OrderStatus.DELIVERED);
            } else if (c == 4) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Shows all orders currently assigned to this driver.
     */
    private void showAssignedOrders(DeliverySystem system) {
        boolean found = false;
        for (Order o : system.getAllOrders()) {
            if (o.getDriver() == this) {
                System.out.println(o);
                found = true;
            }
        }
        if (!found) System.out.println("You currently have no assigned orders.");
    }

    /**
     * Updates one of the driver's orders to a new status.
     */
    private void updateOrder(DeliverySystem system, OrderStatus status) {
        Scanner in = new Scanner(System.in);

        System.out.println("\nYour Assigned Orders:");
        int count = 0;
        for (Order o : system.getAllOrders()) {
            if (o.getDriver() == this) {
                System.out.println("Order #" + o.getOrderId() + " | Status: " + o.getStatus());
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No orders to update.");
            return;
        }

        System.out.print("Enter Order ID: ");
        int id = getIntInput(in);  // SAFE INPUT

        Order target = null;
        for (Order o : system.getAllOrders()) {
            if (o.getOrderId() == id && o.getDriver() == this) {
                target = o;
                break;
            }
        }

        if (target == null) {
            System.out.println("Invalid Order ID.");
            return;
        }

        system.updateOrderStatus(target, status);
        System.out.println("Order #" + id + " updated to " + status + ".");

        if (status == OrderStatus.DELIVERED) {
            this.setAvailable(true);
        }
    }

    /**
     * Reads an integer input safely without crashing.
     */
    private int getIntInput(Scanner in) {
        while (true) {
            String input = in.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (Exception e) {
                System.out.print("Please enter a NUMBER: ");
            }
        }
    }
}