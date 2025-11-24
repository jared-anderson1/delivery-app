package deliveryApp.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
    private String name;
    private double price;

    private static final List<MenuItem> menuItems = new ArrayList<>();

    static {
        menuItems.add(new MenuItem("Pizza", 12.99));
        menuItems.add(new MenuItem("Burger", 8.99));
        menuItems.add(new MenuItem("Salad", 6.99));
    }

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public static List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public static void displayMenu() {
        System.out.println("Menu:");
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i));
        }
    }

    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", price);
    }
}
