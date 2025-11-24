package deliveryApp.utils;

import deliveryApp.users.*;
import deliveryApp.orders.*;
import deliveryApp.menu.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Create a new User instance
        User user = new User("test", "test");

        System.out.println("Welcome, " + user.getName() + "!");

        // Start the ordering process
        user.placeOrder();

        System.out.println("\nThank you for your order!");
    }
}
