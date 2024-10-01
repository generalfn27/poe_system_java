package Shopper;

public class Customer {

    private String username;
    private String password;
    private String phoneNumber;
    private String paymentMethod;
    private double balance;
    private String pinCode; // New field for the 4-digit PIN code

    public Customer() {
        // Default constructor, you can initialize default values if needed
    }


    // Constructor/function for guest customers (no account details)
    public Customer(String username) {
        this.username = username;
    }

    //eto ay hindi nagamit dahil may ibang style na ginamit sa pag save ng customer details
    // Constructor for registered user
    public Customer(String username, String password, String phoneNumber, String paymentMethod, float balance, String pinCode) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.paymentMethod = paymentMethod;
        this.balance = balance;
        this.pinCode = pinCode; // Initialize PIN code
    }


    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

}
