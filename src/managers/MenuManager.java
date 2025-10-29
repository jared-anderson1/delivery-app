package managers;

import deliveryApp.menu.MenuItem;
import java.util.*;

public class MenuManager {
    private Map<String, MenuItem> menuMap;

    public MenuManager() {
        this.menuMap = new HashMap<>();
        loadMenu();
    }

    // Add a menu item
    public void addItem(MenuItem item) {
        menuMap.put(item.getName().toLowerCase(), item);
    }

    // Remove a menu item
    public void removeItem(String itemName) {
        String key = itemName.toLowerCase();
        if (menuMap.remove(key) != null) {
            System.out.println(itemName + " removed from menu.");
        } else {
            System.out.println(itemName + " not found in menu.");
        }
    }

    // Find a menu item
    public MenuItem findItem(String name) {
        return menuMap.get(name.toLowerCase());
    }

    // Show all menu items
    public void showMenu() {
        System.out.println("---- Menu ----");
        if (menuMap.isEmpty()) {
            System.out.println("No items in the menu.");
        } else {
            for (MenuItem item : menuMap.values()) {
                System.out.println(item);
            }
        }
    }


    public List<MenuItem> getMenuList() {
        return new ArrayList<>(menuMap.values());
    }

    // Example loading menu
    private void loadMenu() {
        addItem(new MenuItem("Hamburger", 5.99));
        addItem(new MenuItem("Fries", 3.00));
        addItem(new MenuItem("Drink", 1.50));
    }
}