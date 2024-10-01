package Process;

import Shopper.Customer;
import Shopper.UserCustomer;
import Shopper.Product;

import java.util.Scanner;
import java.util.List;

public class SelfCheckout {
    private final UserCustomer userCustomer;
    private final CashierProcess cashierProcess;
    private final List<Product> cart;

    public SelfCheckout(UserCustomer userCustomer, List<Product> cart) {
        this.userCustomer = userCustomer;
        this.cashierProcess = new CashierProcess();
        this.cart = cart;
    }

    public void processSelfCheckout(String username) {
        Customer customer = userCustomer.getCustomerByUsername(username);
        if (customer == null) {
            System.out.println("\tCustomer not found. Aborting self-checkout.");
            return;
        }

        if (this.cart.isEmpty()) {
            System.out.println("\tYour cart is empty. Please add items before checking out.");
            return;
        }

        displayCart();
        processEWalletPayment(customer);
    }

    private void displayCart() {
        System.out.println("\n\tYour Cart:");
        for (Product product : this.cart) {
            System.out.printf("\t%-20s x%d  %.2f\n", product.getName(), product.getStock(), product.getPrice() * product.getStock());
        }
        System.out.println("\t-----------------------------");
        System.out.printf("\tTotal: %.2f\n", calculateTotal());
    }

    private double calculateTotal() {
        return this.cart.stream().mapToDouble(p -> p.getPrice() * p.getStock()).sum();
    }

    private void processEWalletPayment(Customer customer) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\tProcessing e-wallet payment for " + customer.getUsername());
        System.out.println("\tYou have chosen " + customer.getPaymentMethod() + " as your mode of payment.");
        double totalPrice = calculateTotal();

        System.out.print("\tEnter your PIN: ");
        String enteredPin = scanf.nextLine().trim();

        if (!enteredPin.equals(customer.getPinCode())) {
            System.out.println("\tIncorrect PIN. Payment cancelled.");
            return;
        }

        if (customer.getBalance() < totalPrice) {
            System.out.println("\tInsufficient balance. Payment cancelled.");
            return;
        }

        //nababawasan na ung balance ng account
        customer.setBalance(customer.getBalance() - totalPrice);
        System.out.printf("\tPayment successful. New balance: %.2f\n", customer.getBalance());

        // Generate and print receipt
        generateReceipt(customer, totalPrice);

        OrderProcessor.reset_cart_no_display();
        userCustomer.saveAllCustomersToCSV();
        //next dapat ay mag uupdate mga product stock
    }

    private void generateReceipt(Customer customer, double totalPrice) {
        System.out.println("\n\t----- E-WALLET RECEIPT -----");
        System.out.println("\tCustomer: " + customer.getUsername());
        System.out.println("\tPayment Method: " + customer.getPaymentMethod());
        System.out.println("\n\tItems purchased:");
        System.out.println("CODE\t\tProduct Name\t Quantity  Price");

        for (Product product : this.cart) {
            System.out.printf("\t%-20s x%d  $%.2f\n", product.getName(), product.getStock(), product.getPrice() * product.getStock());
        }

        System.out.println("\t-----------------------------");
        System.out.printf("\tTotal Amount: %.2f\n", totalPrice);
        System.out.printf("\tNew Balance: %.2f\n", customer.getBalance());
        System.out.println("\t-----------------------------");
        System.out.println("\tThank you for your purchase!");

        // Save receipt to CSV
        cashierProcess.save_receipt_to_csv(this.cart, totalPrice, totalPrice);
    }
}