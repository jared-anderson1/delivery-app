package deliveryApp.users;

import deliveryApp.menu.MenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class User {
    protected String name;
    protected String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public boolean login(String name, String password) {
        return this.name.equals(name) && this.password.equals(password);
    }

    public void displayMenu() {
        MenuItem.displayMenu();
    }

    public void placeOrder() {
        Scanner scanner = new Scanner(System.in);
        List<MenuItem> order = new ArrayList<>();
        double total = 0.0;
        List<MenuItem> menuItems = MenuItem.getMenuItems();

        while (true) {
            displayMenu();
            System.out.println("Enter the item number to order, or 0 to finish:");
            int choice = scanner.nextInt();

            if (choice == 0) {
                break;
            }

            if (choice > 0 && choice <= menuItems.size()) {
                MenuItem selectedItem = menuItems.get(choice - 1);
                order.add(selectedItem);
                total += selectedItem.getPrice();
                System.out.println(selectedItem.getName() + " added to your order.");
            } else {
                System.out.println("Invalid item number.");
            }
        }

        System.out.println("\nYour order:");
        if (order.isEmpty()) {
            System.out.println("No items in your order.");
        } else {
            for (MenuItem item : order) {
                System.out.println("- " + item);
            }
            System.out.println("Total: $" + String.format("%.2f", total));
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
