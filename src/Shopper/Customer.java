package Shopper;

public class Customer {

    private String username;
    private String password;
    private String phoneNumber;
    private String paymentMethod;
    private double balance;
    private String pinCode; // New field for the 4-digit PIN code
    private double transaction;
    private int reward_point;


    public Customer() {
        // Default constructor, you can initialize default values if needed
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

    public double getTransaction() {
        return transaction;
    }

    public void setTransaction(double transaction) {
        this.transaction = transaction;
    }

    //every 50pesos, customer earn 1pt
    public int getRewardPoint() {
        return reward_point;
    }

    public void setRewardPoint(int reward_point) {
        this.reward_point = reward_point;
    }

}
