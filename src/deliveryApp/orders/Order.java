package deliveryApp.orders;

import deliveryApp.menu.MenuItem;
import deliveryApp.users.Customer;
import deliveryApp.users.Driver;

import java.util.List;

/**
 * Represents a customer's order containing selected menu items,
 * an assigned driver, and a status.
 */
public class Order {

    private final int orderId;
    private final Customer customer;
    private Driver driver;
    private final List<MenuItem> items;
    private OrderStatus status;

    public Order(int id, Customer c, List<MenuItem> items) {
        this.orderId = id;
        this.customer = c;
        this.items = items;
        this.status = OrderStatus.PLACED;
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Driver getDriver() {
        return driver;
    }

    /**
     * Assigns a driver to the order and marks it accepted.
     */
    public void assignDriver(Driver d) {
        this.driver = d;
        this.status = OrderStatus.ACCEPTED;
        d.setAvailable(false);
    }

    /**
     * Updates the order status (e.g. delivered, in-progress).
     */
    public void updateStatus(OrderStatus st) {
        this.status = st;
    }

    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Returns total price of the order.
     */
    public double totalPrice() {
        return items.stream().mapToDouble(MenuItem::getPrice).sum();
    }

    @Override
    public String toString() {
        return "\nOrder #" + orderId +
                "\nCustomer: " + customer.getName() +
                "\nDriver: " + (driver == null ? "Unassigned" : driver.getName()) +
                "\nStatus: " + status +
                "\nTotal: $" + String.format("%.2f", totalPrice());
    }
}