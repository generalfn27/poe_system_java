package Process;

import Shopper.Customer;
import Shopper.UserCustomer;
import Shopper.Product;

import java.util.Scanner;
import java.util.List;

public class SelfCheckout {
    private final OrderProcessor orderProcessor;
    private final UserCustomer userCustomer;
    private final Scanner scanner;
    private final CashierProcess cashierProcess;

    public SelfCheckout(OrderProcessor orderProcessor, UserCustomer userCustomer) {
        this.orderProcessor = orderProcessor;
        this.userCustomer = userCustomer;
        this.scanner = new Scanner(System.in);
        this.cashierProcess = new CashierProcess();
    }

    public void processSelfCheckout(String username) {
        Customer customer = userCustomer.getCustomerByUsername(username);
        if (customer == null) {
            System.out.println("\tCustomer not found. Aborting self-checkout.");
            return;
        }

        if (OrderProcessor.cart.isEmpty()) {
            System.out.println("\tYour cart is empty. Please add items before checking out.");
            return;
        }

        OrderProcessor.display_cart();
        processEWalletPayment(customer);
    }

    private void processEWalletPayment(Customer customer) {
        System.out.println("\tProcessing e-wallet payment for " + customer.getUsername());
        System.out.println("\tYou have chosen " + customer.getPaymentMethod() + " as your mode of payment.");
        double totalPrice = OrderProcessor.total_price;

        System.out.print("\tEnter your PIN: ");
        String enteredPin = scanner.nextLine().trim();

        if (!enteredPin.equals(customer.getPinCode())) {
            System.out.println("\tIncorrect PIN. Payment cancelled.");
            return;
        }

        if (customer.getBalance() < totalPrice) {
            System.out.println("\tInsufficient balance. Payment cancelled.");
            return;
        }

        customer.setBalance(customer.getBalance() - totalPrice);
        System.out.printf("\tPayment successful. New balance: %.2f\n", customer.getBalance());

        // Generate and print receipt
        generateReceipt(customer, totalPrice);

        OrderProcessor.reset_cart();
        userCustomer.saveAllCustomersToCSV();
    }

    private void generateReceipt(Customer customer, double totalPrice) {
        System.out.println("\n\t----- E-WALLET RECEIPT -----");
        System.out.println("\tCustomer: " + customer.getUsername());
        System.out.println("\tPayment Method: " + customer.getPaymentMethod());
        System.out.println("\tItems purchased:");

        for (Product product : OrderProcessor.cart) {
            System.out.printf("\t%-20s x%d  $%.2f\n", product.getName(), product.getStock(), product.getPrice() * product.getStock());
        }

        System.out.println("\t-----------------------------");
        System.out.printf("\tTotal Amount: $%.2f\n", totalPrice);
        System.out.printf("\tNew Balance: $%.2f\n", customer.getBalance());
        System.out.println("\t-----------------------------");
        System.out.println("\tThank you for your purchase!");

        // Save receipt to CSV
        cashierProcess.save_receipt_to_csv(OrderProcessor.cart, totalPrice, totalPrice);
    }
}