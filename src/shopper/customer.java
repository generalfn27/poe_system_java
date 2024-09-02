package shopper;

public class customer {
    private String name;
    private boolean isRegistered;
    private String accountNumber;
    private String accountId;

    // Constructor/function for guest customers (no account details)
    public customer(String name) {
        this.name = name;
        this.isRegistered = false; // Guests are not registered by default
        this.accountNumber = null;
        this.accountId = null;
    }

    // Constructor for registered customers (with account details)
    public customer(String name, String accountNumber, String accountId) {
        this.name = name;
        this.isRegistered = true; // Registered customer
        this.accountNumber = accountNumber;
        this.accountId = accountId;
    }

}
