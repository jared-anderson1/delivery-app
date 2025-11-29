package deliveryApp.users;

/**
 * Base class for all user types.
 * Defines shared fields and login behavior.
 */
public abstract class User {

    protected String userName;
    protected String password;
    protected String name;
    protected String phoneNumber;
    protected String email;

    public User(String userName, String password, String name,
                String phoneNumber, String email) {

        this.userName = userName;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * Checks whether the given password matches the stored one.
     */
    public boolean verifyPassword(String pw) {
        return this.password.equals(pw);
    }

    public String getName() {
        return name;
    }

    /**
     * Each user type has its own custom dashboard.
     */
    public abstract void showMenu(deliveryApp.core.DeliverySystem system);
}