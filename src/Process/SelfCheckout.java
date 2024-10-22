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
        System.out.println("\t---------------------------------");
        System.out.printf("\n\tTotal Items: %d\n", calculate_total_items());
        System.out.printf("\tTotal Price: %.2f\n", calculate_total_price());
    }


    int calculate_total_items() {
        int total = 0;
        for (Product product : cart) {
            total += product.getStock();
        }
        return total;
    }


    private double calculate_total_price() {
        double total = 0;
        for (Product product : cart) {
            total += product.getPrice() * product.getStock();
        }
        return total;
    }


    private void processEWalletPayment(Customer customer) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\tProcessing e-wallet payment for " + customer.getUsername());
        System.out.println("\tYou have chosen " + customer.getPaymentMethod() + " as your mode of payment.");
        double totalPrice = calculate_total_price();

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
            System.out.println("\t\tPress Enter key to continue.\n");
            scanf.nextLine();
            return;
        }

        // Deduct funds from customer's account
        userCustomer.minus_funds(customer.getUsername(), totalPrice);

        System.out.printf("\tPayment successful. New balance: %.2f\n", customer.getBalance());

        generateReceipt(customer, totalPrice);
        generate_personal_receipt(customer,totalPrice);

        double new_transaction = customer.getTransaction() + 1.0;
        customer.setTransaction(new_transaction);
        //System.out.printf("\tPayment successful. New transaction: %.0f\n", customer.getTransaction());
        // debugger
        //System.out.println("\tCalling saveAllCustomersToCSV to save updated balance.");  //debugger
        userCustomer.saveAllCustomersToCSV();

        OrderProcessor.reset_cart_no_display();
        userCustomer.registered_user_customer_item_category(customer.getUsername(), customer);
    }


    private void generateReceipt(Customer customer, double totalPrice) {
        System.out.println("\n\t----- E-WALLET RECEIPT -----");
        System.out.println("\tCustomer: " + customer.getUsername());
        System.out.println("\tPayment Method: " + customer.getPaymentMethod());
        System.out.println("\n\tItems purchased:");
        System.out.println("\tCODE\t  Product Name\t Quantity  Price");

        for (Product product : this.cart) {
            System.out.printf("\t%-6s\t%-20s x%d  %.2f\n",product.getCode(), product.getName(), product.getStock(), product.getPrice() * product.getStock());
        }

        System.out.println("\n\t-----------------------------");
        System.out.printf("\tTotal Items: %d\n", calculate_total_items());
        System.out.printf("\tTotal Amount: %.2f\n", totalPrice);
        System.out.printf("\tNew Balance: %.2f\n", customer.getBalance());
        System.out.println("\t-----------------------------");
        System.out.println("\tThank you for your purchase!");

        // Save receipt to CSV
        cashierProcess.save_receipt_to_csv(this.cart, totalPrice, totalPrice);
    }


    private void generate_personal_receipt(Customer customer, double totalPrice) {
        Scanner scanf = new Scanner(System.in);

        save_personal_receipt_to_csv(customer, this.cart, totalPrice, totalPrice);

        System.out.println("\tPress Enter key to continue.\n");
        scanf.nextLine(); //used for press any key to continue
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
            writer.printf("%nTotal Items: ,%d%n", calculate_total_items());
            writer.printf("Total Price: ,%.2f%n", totalPrice);
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

        return String.format("%s_transaction_#%.0f_%s.csv", customer.getUsername(), customer.getTransaction(), formattedDate);
    }

}
