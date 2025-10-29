package deliveryApp.users;

public class Customer extends User {

    public Customer(String name, String password) {
        super(name, password);
    }

    public void placeOrder(String item) {
        System.out.println(name + " placed an order for " + item + ".");
    }
}