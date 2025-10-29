package managers;

public class Manager {
    private final MenuManager menuManager;
    private final DriverManager driverManager;
    private final OrderManager orderManager;
    private final UserManager userManager;

    public Manager() {
        this.menuManager = new MenuManager();
        this.driverManager = new DriverManager();
        this.orderManager = new OrderManager(driverManager);
        this.userManager = new UserManager();
    }

    public MenuManager getMenuManager() { return menuManager; }
    public DriverManager getDriverManager() { return driverManager; }
    public OrderManager getOrderManager() { return orderManager; }
    public UserManager getUserManager() { return userManager; }
}