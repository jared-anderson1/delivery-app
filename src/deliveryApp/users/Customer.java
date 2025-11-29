package deliveryApp.users;

import deliveryApp.menu.MenuItem;
import deliveryApp.orders.Order;
import deliveryApp.core.DeliverySystem;

import java.util.*;

/**
 * Represents a customer who can place orders and view the menu.
 * Now uses safe input handling to prevent input mismatch crashes.
 */
public class Customer extends User {

    public Customer(String username, String pw, String name,
                    String phone, String email) {
        super(username, pw, name, phone, email);
    }

    @Override
    public void showMenu(DeliverySystem system) {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. Place Order");
            System.out.println("2. View Menu");
            System.out.println("3. Logout");
            System.out.print("Choice: ");

            int choice = getIntInput(in);   // <-- SAFE INPUT

            if (choice == 1) {
                placeOrder(system);
            } else if (choice == 2) {
                for (MenuItem mi : system.getMenu()) {
                    System.out.println(mi);
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Allows the customer to select menu items and submit an order.
     * Now uses safe input parsing.
     */
    private void placeOrder(DeliverySystem system) {
        Scanner in = new Scanner(System.in);
        List<MenuItem> items = new ArrayList<>();

        while (true) {
            System.out.println("\nSelect item # (0 to finish):");
            List<MenuItem> menu = system.getMenu();

            for (int i = 0; i < menu.size(); i++) {
                System.out.println((i + 1) + ". " + menu.get(i));
            }

            int c = getIntInput(in);  // <-- SAFE INPUT

            if (c == 0) break;
            if (c > 0 && c <= menu.size()) {
                items.add(menu.get(c - 1));
                System.out.println("Added.");
            } else {
                System.out.println("Invalid selection.");
            }
        }

        Order o = system.placeOrder(this, items);
        System.out.println("Order placed! ID: " + o.getOrderId());
    }

    /**
     * Reads an integer input safely without throwing exceptions.
     * If the user enters text, they are asked again.
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