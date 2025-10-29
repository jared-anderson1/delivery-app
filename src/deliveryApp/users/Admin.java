package deliveryApp.users;

import deliveryApp.users.Driver;
import deliveryApp.orders.Order;
import java.util.*;
public class Admin extends User {
    public Admin(String name) {
        super(name);
    }

    public void viewDrivers(List<Driver> drivers) {
        System.out.println("--All Drivers--");
        for (Driver d : drivers) {
            System.out.println(d);
        }
    }

    public void viewOrders(List<Order> orders) {
        System.out.println("--All Orders--");
        for (Order o : orders) {
            System.out.println(o);
        }
    }

    public void assignDriver(List<Driver> drivers, Order order) {
        Driver bestDriver = null;
        double highestRating = -1;

        for(Driver d : drivers) {
            if (d.isAvailable() && d.getAverageRating() > highestRating) {
                bestDriver = d;
                highestRating = d.getAverageRating();
            }
        }

        if (bestDriver != null) {
            order.assignDriver(bestDriver);
            System.out.println(bestDriver.getName() + " assigned to order");
        } else {
            System.out.println("Searching for drivers");
        }

    }

}
