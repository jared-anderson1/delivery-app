package deliveryApp.menu;

/**
 * Represents a single menu item with a name and price.
 */
public class MenuItem {

    private final String name;
    private final double price;

    public MenuItem(String n, double p) {
        name = n;
        price = p;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", price);
    }
}