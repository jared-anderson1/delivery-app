package deliveryApp.users;

public abstract class User {
    protected String name;
    protected String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public boolean login(String name, String password) {
        return this.name.equals(name) && this.password.equals(password);
    }

    @Override
    public String toString() {
        return name;
    }
}