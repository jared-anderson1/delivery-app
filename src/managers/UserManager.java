package managers;

import deliveryApp.users.*;
import java.util.*;

public class UserManager {
    private List<Customer> customers;
    private List<Driver> drivers;
    private List<Admin> admins;

    public UserManager() {
        customers = new ArrayList<>();
        drivers = new ArrayList<>();
        admins = new ArrayList<>();
    }

    public void addCustomer(Customer c) { customers.add(c); }
    public void addDriver(Driver d) { drivers.add(d); }
    public void addAdmin(Admin a) { admins.add(a); }

    public Customer loginCustomer(String name) {
        return customers.stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Driver loginDriver(String name) {
        return drivers.stream().filter(d -> d.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Admin loginAdmin(String name) {
        return admins.stream().filter(a -> a.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Customer> getCustomers() { return customers; }
    public List<Driver> getDrivers() { return drivers; }
    public List<Admin> getAdmins() { return admins; }
}
