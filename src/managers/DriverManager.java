package managers;
import deliveryApp.users.Driver;
import java.util.*;

public class DriverManager {
    // PriorityQueue orders drivers by descending average rating
    private PriorityQueue<Driver> availableDrivers;

    public DriverManager() {
        // Comparator: higher averageRating comes first
        this.availableDrivers = new PriorityQueue<>((d1, d2) -> Double.compare(d2.getAverageRating(), d1.getAverageRating()));
    }

    // Add a new driver
    public void addDriver(Driver driver) {
        if (driver.isAvailable()) {
            availableDrivers.offer(driver);
        }
    }

    // Remove a driver (e.g., deactivated)
    public void removeDriver(Driver driver) {
        availableDrivers.remove(driver);
    }

    // Automatically assign the best available driver
    public Driver assignDriver() {
        while (!availableDrivers.isEmpty()) {
            Driver driver = availableDrivers.poll();
            if (driver.isAvailable()) {
                driver.setAvailable(false); // mark as assigned
                return driver;
            }
        }
        return null; // no available drivers
    }

    // Return a list of all available drivers (for display)
    public List<Driver> getAvailableDrivers() {
        return new ArrayList<>(availableDrivers);
    }

    // Mark driver as available again (after delivery)
    public void markAvailable(Driver driver) {
        driver.setAvailable(true);
        availableDrivers.offer(driver); // re-add to priority queue
    }

    // Optional: show all available drivers
    public void showDrivers() {
        System.out.println("---- Available Drivers ----");
        for (Driver driver : availableDrivers) {
            System.out.println(driver.getName() + " | Rating: " + driver.getAverageRating());
        }
    }
}