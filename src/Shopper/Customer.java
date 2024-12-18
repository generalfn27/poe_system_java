package Shopper;

public class Customer {
    private String username;
    private String password;
    private String phoneNumber;
    private String paymentMethod;
    private double balance;
    private String pinCode; // New field for the 4-digit PIN code
    private double transaction;
    private double reward_point;
    private double total_spent;
    private String account_status;
    private String first_security_question;
    private String second_security_question;

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

    public double getRewardPoint() {
        return reward_point;
    }

    public void setRewardPoint(double reward_point) {
        this.reward_point = reward_point;
    }

    public double getTotal_spent() {
        return total_spent;
    }

    public void setTotal_spent(double total_spent) {
        this.total_spent = total_spent;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public String getFirst_security_question() {
        return first_security_question;
    }

    public void setFirst_security_question(String first_security_question) {
        this.first_security_question = first_security_question;
    }

    public String getSecond_security_question() {
        return second_security_question;
    }

    public void setSecond_security_question(String second_security_question) {
        this.second_security_question = second_security_question;
    }
}
