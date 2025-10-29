package managers;


import deliveryApp.orders.Order;
import deliveryApp.orders.OrderStatus;
import deliveryApp.users.Driver;
import java.util.*;

public class OrderManager {
    private final Queue<Order> orderQueue; // FIFO queue
    private final DriverManager driverManager;

    public OrderManager(DriverManager driverManager) {
        this.orderQueue = new LinkedList<>();
        this.driverManager = driverManager;
    }

    // Place a new order
    public void placeOrder(Order order) {
        orderQueue.offer(order); // add to queue
        System.out.println("Order placed for " + order.getCustomer().getName() + ". Total: $" + order.totalPrice());
        processOrders(); // try to assign a driver
    }

    // Process orders by FIFO
    private void processOrders() {
        if (orderQueue.isEmpty()) return;

        Queue<Order> remainingOrders = new LinkedList<>();

        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.poll();
            if (order.getDriver() == null) {
                Driver driver = driverManager.assignDriver();
                if (driver != null) {
                    order.assignDriver(driver);
                    System.out.println("Driver " + driver.getName() + " assigned to order for " +
                            order.getCustomer().getName());
                } else {
                    // No available driver, put back in queue
                    remainingOrders.offer(order);
                }
            }
        }

        // Keep unassigned orders in the main queue
        orderQueue.addAll(remainingOrders);
    }

    // Update an order status
    public void updateOrderStatus(Order order, String status) {
        order.updateStatus(status);
        if (status.equalsIgnoreCase("Delivered") && order.getDriver() != null) {
            driverManager.markAvailable(order.getDriver()); // make driver available again
        }
    }

    // Show all current orders
    public void showOrders() {
        System.out.println("---- Current Orders ----");
        for (Order order : orderQueue) {
            System.out.println(order);
        }
    }

    // Return all orders in queue
    public List<Order> getOrders() {
        return new ArrayList<>(orderQueue);
    }
}