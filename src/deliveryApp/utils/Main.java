package deliveryApp.utils;

import deliveryApp.core.DeliverySystem;
import deliveryApp.users.User;

import java.util.Scanner;

/**
 * Entry point for the delivery system application.
 * Updated with safe input handling to prevent crashes.
 */
public class Main {

    public static void main(String[] args) {

        DeliverySystem system = new DeliverySystem();
        Scanner in = new Scanner(System.in);

        System.out.println("=== DELIVERY APP ===");

        while (true) {
            try {
                System.out.print("\nUsername: ");
                String username = in.nextLine().trim();

                System.out.print("Password: ");
                String password = in.nextLine().trim();

                User user = system.login(username, password);

                if (user == null) {
                    System.out.println("Invalid login. Please try again.");
                    continue;
                }

                // Launch user-specific dashboard
                user.showMenu(system);

            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
            }
        }
    }
}