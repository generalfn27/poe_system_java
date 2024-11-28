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

        display_cart_self_checkout();
        process_e_wallet_payment(customer);
    }

    private void display_cart_self_checkout() {
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
            System.out.print("\n\tEnter /// to Cancel");
            System.out.print("\n\tEnter your PIN: ");
            enteredPin = scanf.nextLine().trim();

            if (enteredPin.equals("///")){
                return;
            }

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

        customer.setBalance(customer.getBalance() - totalPrice);

        // Debugger: Print balance after deduction
        System.out.printf("\n\tFunds deducted successfully. New balance: %.2f\n", customer.getBalance());
        userCustomer.saveAllCustomersToCSV(); // Ensure this works as expected

        //kada 5pesos ay 0.1 sa reward same sa globe
        //cashback reward calculation
        double point_reward = (totalPrice / 50.0) + customer.getRewardPoint();
        customer.setRewardPoint(point_reward);
        System.out.printf("\tYour Total cashback reward point is %.0f\n", customer.getRewardPoint());

        double total_spent = customer.getTotal_spent() + totalPrice;
        customer.setTotal_spent(total_spent);
        System.out.printf("\tYour Total spent is %-8.2f\n", customer.getTotal_spent());

        // need dagdag sa sales report kung ano mga items na ibenta
        // Update report sales with current transaction details
        cashierProcess.update_sales_report(calculate_total_items(), calculate_total_price());

        cashierProcess.update_all_stocks(cart);

        print_receipt(customer, totalPrice);

        new_transaction = customer.getTransaction() + 1.0;
        customer.setTransaction(new_transaction);
        //System.out.printf("\tPayment successful. New transaction: %.0f\n", customer.getTransaction()); // debugger na naman kasi puro bug

        userCustomer.saveAllCustomersToCSV();
        OrderProcessor.reset_cart_no_display();

        System.out.print("\t\tPress Enter key to continue.");
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
        System.out.printf("\tCurrent Reward Points: %.0f\n", customer.getRewardPoint());
        System.out.println("\t-----------------------------");
        System.out.println("\tThank you for your purchase!");

        // Save receipt to CSV
        cashierProcess.self_checkout_save_receipt_to_csv(this.cart, totalPrice, totalPrice, customer);
    }

}
