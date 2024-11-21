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
        CashierProcess.initialize_receipt_number();
    }

    public void processSelfCheckout(String username) {
        Customer customer = userCustomer.get_customer_by_username(username);
        if (customer == null) {
            System.out.println("\tCustomer not found. Aborting self-checkout.");
            return;
        }

        if (this.cart.isEmpty()) {
            System.out.println("\tYour cart is empty. Please add items before checking out.");
            return;
        }

        display_cart();
        process_e_wallet_payment(customer);
    }

    private void display_cart() {
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


    private void process_e_wallet_payment(Customer customer) {
        Scanner scanf = new Scanner(System.in);
        double totalPrice = calculate_total_price();
        String enteredPin = "";
        double new_transaction;

        System.out.println("\n\tProcessing e-wallet payment for " + customer.getUsername());
        System.out.println("\tYou have chosen " + customer.getPaymentMethod() + " as your mode of payment.");

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

        // Update report sales with current transaction details
        cashierProcess.update_sales_report(calculate_total_items(), calculate_total_price());

        // Update stock levels after successful payment
        cashierProcess.update_all_stocks();

        print_receipt(customer, totalPrice);

        new_transaction = customer.getTransaction() + 1.0;

        customer.setTransaction(new_transaction);
        //System.out.printf("\tPayment successful. New transaction: %.0f\n", customer.getTransaction());
        // debugger na naman kasi puro bug
        //System.out.println("\tCalling saveAllCustomersToCSV to save updated balance.");  //debugger ulit
        userCustomer.saveAllCustomersToCSV();
        OrderProcessor.reset_cart_no_display();

        System.out.println("\t\tPress Enter key to continue.\n");
        scanf.nextLine();

        userCustomer.registered_user_customer_item_category(customer.getUsername(), customer);
    }


    private void print_receipt(Customer customer, double totalPrice) {
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
        cashierProcess.self_checkout_save_receipt_to_csv(this.cart, totalPrice, totalPrice, customer);
    }

}
