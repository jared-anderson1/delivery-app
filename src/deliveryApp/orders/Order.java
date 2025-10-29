package deliveryApp.orders;

import deliveryApp.users.Customer;
import deliveryApp.users.Driver;
import deliveryApp.menu.MenuItem;
import java.util.List;

public class Order {
    private Customer customer;
    private Driver driver;
    private List<MenuItem> items;
    private String status;

    public Order(Customer customer, List<MenuItem> items, Driver driver) {
        this.customer = customer;
        this.items = items;
        this.driver = driver;
        this.status = "Placed";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Driver getDriver() {
        return driver;
    }

    public void assignDriver(Driver driver) {
        this.driver = driver;
        this.status = "in progress";
        driver.setAvailable(false);
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
        if(newStatus.equalsIgnoreCase("Delivered") && driver != null) {
            driver.setAvailable(true);
        }
    }

    public double totalPrice() {
        double total = 0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        String driverName = (driver != null) ? driver.getName() : "Assigning a driver";
        return "Customer: " + customer.getName() +"\nDriver: " + driverName +
                "\nStatus: " + status + "\nTotal: $" + String.format("$2f", totalPrice());
    }

}
