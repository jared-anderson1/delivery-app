package deliveryApp.utils;

import deliveryApp.core.DeliverySystem;
import deliveryApp.users.*;

import java.util.Scanner;

/**
 * Entry point for the delivery system application.
 * Now supports login, registration, and quitting.
 */
public class Main {

    public static void main(String[] args) {

        DeliverySystem system = new DeliverySystem();
        Scanner in = new Scanner(System.in);

        System.out.println("=== DELIVERY APP ===");

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Quit");
            System.out.print("Choice: ");

            int choice = getIntInput(in);

            if (choice == 1) {
                handleLogin(system, in);
            } else if (choice == 2) {
                handleRegistration(system, in);
            } else if (choice == 3) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Handles user login and launches the appropriate dashboard.
     */
    private static void handleLogin(DeliverySystem system, Scanner in) {
        System.out.print("Username: ");
        String username = in.nextLine().trim();

        System.out.print("Password: ");
        String password = in.nextLine().trim();

        User user = system.login(username, password);

        if (user == null) {
            System.out.println("Invalid login. Please try again.");
            return;
        }

        // Launch user-specific dashboard
        try {
            user.showMenu(system);
        } catch (Exception e) {
            System.out.println("An error occurred while running the dashboard.");
        }
    }

    /**
     * Handles new user registration (Customer or Driver).
     */
    private static void handleRegistration(DeliverySystem system, Scanner in) {
        System.out.println("\n--- REGISTER NEW USER ---");

        // Choose user type
        int type;
        while (true) {
            System.out.println("Register as:");
            System.out.println("1. Customer");
            System.out.println("2. Driver");
            System.out.print("Choice: ");
            type = getIntInput(in);
            if (type == 1 || type == 2) break;
            System.out.println("Please choose 1 or 2.");
        }

        // Username (must be unique)
        String username;
        while (true) {
            System.out.print("Choose a username: ");
            username = in.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty.");
                continue;
            }
            if (system.isUsernameTaken(username)) {
                System.out.println("That username is already taken. Try another.");
            } else {
                break;
            }
        }

        // Password
        String password;
        while (true) {
            System.out.print("Choose a password: ");
            password = in.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty.");
            } else {
                break;
            }
        }

        // Basic profile info
        System.out.print("Your full name: ");
        String name = in.nextLine().trim();

        System.out.print("Phone number: ");
        String phone = in.nextLine().trim();

        System.out.print("Email: ");
        String email = in.nextLine().trim();

        // Create the appropriate user type
        User newUser;
        if (type == 1) {
            newUser = new Customer(username, password, name, phone, email);
        } else {
            newUser = new Driver(username, password, name, phone, email);
        }

        system.registerUser(newUser);
        System.out.println("Registration successful! You can now log in.");
    }

    /**
     * Reads an integer safely without throwing an exception on bad input.
     */
    private static int getIntInput(Scanner in) {
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