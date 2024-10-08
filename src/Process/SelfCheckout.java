package Process;

import Shopper.Customer;
import Shopper.UserCustomer;
import Shopper.Product;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        String enteredPin = "";

        while (!enteredPin.equals(customer.getPinCode())) {
            System.out.print("\n\tEnter your PIN: ");
            enteredPin = scanf.nextLine().trim();
            if (!enteredPin.equals(customer.getPinCode())) {
                System.out.println("\n\tPin do not match. Please try again.");
            }
        }

        if (customer.getBalance() < totalPrice) {
            System.out.println("\tInsufficient balance. Payment cancelled.");
            return;
        }

        // Deduct funds from customer's account
        userCustomer.minus_funds(customer.getUsername(), totalPrice);

        //System.out.println("\tCalling saveAllCustomersToCSV to save updated balance.");  //debugger
        userCustomer.saveAllCustomersToCSV();

        System.out.printf("\tPayment successful. New balance: %.2f\n", customer.getBalance());

        generate_personal_receipt(customer,totalPrice);
        generateReceipt(customer, totalPrice);

        OrderProcessor.reset_cart_no_display();
        userCustomer.registered_user_customer_item_category(customer.getUsername(), customer);
    }


    private void generateReceipt(Customer customer, double totalPrice) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\t----- E-WALLET RECEIPT -----");
        System.out.println("\tCustomer: " + customer.getUsername());
        System.out.println("\tPayment Method: " + customer.getPaymentMethod());
        System.out.println("\n\tItems purchased:");
        System.out.println("\tCODE\t  Product Name\t Quantity  Price");

        for (Product product : this.cart) {
            System.out.printf("\t%-6s\t%-20s x%d  %.2f\n",product.getCode(), product.getName(), product.getStock(), product.getPrice() * product.getStock());
        }

        System.out.println("\n\t-----------------------------");
        System.out.printf("\tTotal Amount: %.2f\n", totalPrice);
        System.out.printf("\tNew Balance: %.2f\n", customer.getBalance());
        System.out.println("\t-----------------------------");
        System.out.println("\tThank you for your purchase!");

        // Save receipt to CSV
        cashierProcess.save_receipt_to_csv(this.cart, totalPrice, totalPrice);

        System.out.println("ano dapat ung choice kung sakali after order, order again & log out and exit");
        System.out.println("\n\nOr okay na topagka enter mag auto balik sa dashboard");

        System.out.println("\tPress Enter key to continue.\n");
        scanf.nextLine(); //used for press any key to continue
    }

    private void generate_personal_receipt(Customer customer, double totalPrice) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\t----- E-WALLET RECEIPT -----");
        System.out.println("\tCustomer: " + customer.getUsername());
        System.out.println("\tPayment Method: " + customer.getPaymentMethod());
        System.out.println("\n\tItems purchased:");
        System.out.println("\tCODE\t  Product Name\t Quantity  Price");

        for (Product product : this.cart) {
            System.out.printf("\t%-6s\t%-20s x%d  %.2f\n",product.getCode(), product.getName(), product.getStock(), product.getPrice() * product.getStock());
        }

        System.out.println("\n\t-----------------------------");
        System.out.printf("\tTotal Amount: %.2f\n", totalPrice);
        System.out.printf("\tNew Balance: %.2f\n", customer.getBalance());
        System.out.println("\t-----------------------------");
        System.out.println("\tThank you for your purchase!");

        save_personal_receipt_to_csv(customer, this.cart, totalPrice, totalPrice);

    }

    public void save_personal_receipt_to_csv(Customer customer, List<Product> counter, double totalPrice, double payment) {
        if (counter.isEmpty()) {
            System.out.println("No items in the counter. Nothing to save.");
            return;
        }

        String fileName = generate_receipt_file_name(customer);
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write header
            writer.println("Username,Payment method,Balance");
            writer.println(customer.getUsername() + "," + customer.getPaymentMethod() + "," +customer.getBalance());
            writer.println("Code,Name,Quantity,Price");

            // Write each product's details
            for (Product product : counter) {
                writer.printf("%s,%s,%d,%.2f%n",
                        product.getCode(),
                        product.getName(),
                        product.getStock(),
                        product.getPrice());
            }

            writer.printf("%nTotal Price: ,%.2f%n", totalPrice);
            writer.printf("Payment: ,%.2f%n", payment);

            // Calculate and write change if needed
            if (payment > totalPrice) {
                writer.printf("Change: ,%.2f%n", payment - totalPrice);
            }
            System.out.println("\n\tReceipt successfully saved to " + fileName + "\n\n");
        } catch (IOException e) {
            System.out.println("Error saving the receipt: " + e.getMessage());
        }
    }

    private static String generate_receipt_file_name(Customer customer) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String formattedDate = dateFormat.format(new Date());

        return String.format("%s_%s.csv", customer.getUsername(), formattedDate);
    }

}
