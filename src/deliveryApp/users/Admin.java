package deliveryApp.users;

import deliveryApp.core.DeliverySystem;
import deliveryApp.orders.Order;

import java.util.Scanner;

/**
 * Admin user who can manage menu items and view all orders.
 * Updated with safe input handling to prevent input mismatch errors.
 */
public class Admin extends User {

    public Admin(String username, String pw, String name,
                 String phone, String email) {
        super(username, pw, name, phone, email);
    }

    @Override
    public void showMenu(DeliverySystem system) {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View All Orders");
            System.out.println("2. Add Menu Item");
            System.out.println("3. Remove Menu Item");
            System.out.println("4. Logout");
            System.out.print("Choice: ");

            int choice = getIntInput(in);

            if (choice == 1) {
                viewAllOrders(system);
            } else if (choice == 2) {
                addMenuItem(system, in);
            } else if (choice == 3) {
                removeMenuItem(system, in);
            } else if (choice == 4) {
                break;  // Logout
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Displays all orders in the system.
     */
    private void viewAllOrders(DeliverySystem system) {
        for (Order o : system.getAllOrders()) {
            System.out.println(o);
        }
    }

    /**
     * Add a new item to the menu.
     */
    private void addMenuItem(DeliverySystem system, Scanner in) {
        System.out.print("Item name: ");
        String name = in.nextLine();

        System.out.print("Price: ");
        double price = getDoubleInput(in);

        system.addMenuItem(name, price);
        System.out.println("Added!");
    }

    /**
     * Allows admin to remove a menu item.
     */
    private void removeMenuItem(DeliverySystem system, Scanner in) {
        System.out.print("Item name: ");
        String name = in.nextLine();

        system.removeMenuItem(name);
        System.out.println("Item removed.");
    }

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

    private double getDoubleInput(Scanner in) {
        while (true) {
            String input = in.nextLine();
            try {
                return Double.parseDouble(input.trim());
            } catch (Exception e) {
                System.out.print("Please enter a VALID PRICE (e.g. 5.99): ");
            }
        }
    }
}
