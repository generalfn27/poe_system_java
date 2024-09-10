package shopper;

public class Customer {
    private String name;
    private boolean isRegistered;
    private String accountNumber;
    private String accountId;

    // Constructor/function for guest customers (no account details)
    public Customer(String name) {
        this.name = name;
        this.isRegistered = false; // Guests are not registered by default
        this.accountNumber = null;
        this.accountId = null;
    }

    // Constructor for registered customers (with account details)
    public Customer(String name, String accountNumber, String accountId) {
        this.name = name;
        this.isRegistered = true; // Registered customer
        this.accountNumber = accountNumber;
        this.accountId = accountId;
    }

    // Getters and Setters
    public String getName() {  return name; }

    public boolean isRegistered() {  return isRegistered; }

    public String getAccountNumber() {  return accountNumber; }

    public String getAccountId() {  return accountId; }

    public void setAccountNumber(String accountNumber) {  this.accountNumber = accountNumber; }

    public void setAccountId(String accountId) {  this.accountId = accountId; }

}
