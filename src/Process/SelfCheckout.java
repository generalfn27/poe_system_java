package Process;

import Shopper.Customer;
import Shopper.UserCustomer;
import Shopper.Product;

import java.io.Console;
import java.util.List;

public class SelfCheckout {
    private final UserCustomer userCustomer;
    private final CashierProcess cashierProcess;
    private final List<Product> cart;

    public SelfCheckout(List<Product> cart) {
        this.userCustomer = new UserCustomer();
        this.cashierProcess = new CashierProcess();
        this.cart = cart;
        CashierProcess.initialize_receipt_number();
    }

    public void processSelfCheckout(Customer customer) {
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
        for (Product product : this.cart) {
            total += product.getStock();
        }
        return total;
    }


    private double calculate_total_price() {
        double total = 0;
        for (Product product : this.cart) {
            total += product.getPrice() * product.getStock();
        }
        return total;
    }


    private void process_e_wallet_payment (Customer customer) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        while (true) {
            double totalPrice = calculate_total_price();
            display_cart_self_checkout();
            System.out.printf("\n\tTotal Amount to Pay: %.2f\n", totalPrice);
            System.out.println("\tDo you want to proceed with the payment?\n");
            System.out.println("\t[1] Yes");
            System.out.println("\t[2] Apply Discount Coupon");
            System.out.println("\n\t[0] Cancel");
            System.out.print("\tEnter choice: ");
            String choice = console.readLine();

            switch (choice) {
                case "1":
                    processFinalPayment(customer, totalPrice);
                    break;
                case "2":
                    double discountedPrice = handleCouponApplication(totalPrice, console);

                    // If coupon application is cancelled or fails
                    if (discountedPrice < 0) {
                        System.out.println("\tCoupon application cancelled or failed.");
                        System.out.print("\t\tPress Enter key to continue.");
                        console.readLine();
                        console.flush();
                        continue; // Go back to main payment menu
                    }
                    // Proceed with discounted payment
                    processFinalPayment(customer, discountedPrice);
                    break;
                case "0":
                    System.out.println("\tPayment cancelled.");
                    cashierProcess.self_checkout_or_queue_process(customer, cart);
                    return;
                default:
                    System.out.println("\n\tInvalid choice. Please try again.");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }
        }
    }


    private double handleCouponApplication(double totalPrice, Console console) {
        CouponManager coupon_manager = new CouponManager();

        while (true) {
            System.out.print("\n\tEnter coupon code: ");
            String couponCode = console.readLine();

            double discountedPrice = coupon_manager.apply_coupon(couponCode, totalPrice);

            if (discountedPrice == -1) { // Invalid code or coupon exhausted
                System.out.println("\n\tInvalid coupon or coupon has been exhausted.");

                while (true) {
                    System.out.println("\tWould you like to try another coupon?");
                    System.out.println("\t[1] Yes");
                    System.out.println("\t[0] No, proceed without coupon");
                    System.out.print("\tEnter choice: ");
                    String retryChoice = console.readLine();

                    if (retryChoice.equals("1")) {
                        break; // Try another coupon
                    } else if (retryChoice.equals("0")) {
                        return -1; // Cancel coupon application
                    } else {
                        System.out.println("\tInvalid choice. Please enter [1] or [0].");
                    }
                }
            } else {
                // Successful coupon application
                System.out.printf("\tCoupon applied. New total: %.2f\n", discountedPrice);
                return discountedPrice;
            }
        }
    }


    private void processFinalPayment(Customer customer, double totalPrice) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        int maxAttempts = 3;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            System.out.print("\n\tEnter PIN (or '///' to Cancel): ");
            String enteredPin = console.readLine().trim();

            if (enteredPin.equals("///")) {
                System.out.println("\tPayment cancelled.");
                return;
            }

            if (enteredPin.equals(customer.getPinCode())) {
                // Check balance
                if (customer.getBalance() < totalPrice) {
                    System.out.println("\tInsufficient balance. Payment cancelled.");
                    return;
                }

                customer.setBalance(customer.getBalance() - totalPrice);
                userCustomer.saveAllCustomersToCSV();
                updateCustomerDetails(customer, totalPrice);
                return;
            } else {
                System.out.printf("\tIncorrect PIN. %d attempts remaining.\n",
                        maxAttempts - attempt - 1);
            }
        }

        System.out.println("\tToo many incorrect PIN attempts. Payment cancelled.");
    }


    private void updateCustomerDetails(Customer customer, double totalPrice) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        int point_reward = (int) ((totalPrice / 50) + customer.getRewardPoint());
        customer.setRewardPoint(point_reward);

        double total_spent = customer.getTotal_spent() + totalPrice;
        customer.setTotal_spent(total_spent);

        customer.setTransaction(customer.getTransaction() + 1);



        cashierProcess.update_sales_report(cart);
        cashierProcess.update_all_stocks(cart);

        print_receipt(customer, totalPrice);

        System.out.print("\t\tPress Enter key to continue.");
        console.readLine(); // Wait for the user to press Enter
        console.flush();

        userCustomer.saveAllCustomersToCSV();

        cart.clear();

        /* System.out.printf("\tFunds deducted successfully. New balance: %.2f\n", customer.getBalance());
        System.out.printf("\tTotal cashback reward point: %.0f\n", customer.getRewardPoint());
        System.out.printf("\tTotal spent: %.2f\n", customer.getTotal_spent()); */
        System.out.print("\t\tPress Enter key to continue.");
        console.readLine();
        console.flush();

        CouponManager couponManager = new CouponManager();
        couponManager.show_random_Coupon();
        System.out.print("\t\tPress Enter key to continue.");
        console.readLine();
        console.flush();

        userCustomer.registered_user_customer_item_category(customer.getUsername(), customer, OrderProcessor.cart);
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
        System.out.printf("\tVAT: %.2f\n", calculate_total_price() * 0.12);
        System.out.printf("\tTotal Amount: %.2f\n", totalPrice);
        System.out.printf("\tNew Balance: %.2f\n", customer.getBalance());
        System.out.printf("\tCurrent Reward Points: %.0f\n", customer.getRewardPoint());
        System.out.println("\t-----------------------------");
        System.out.println("\tThank you for your purchase!");

        cashierProcess.self_checkout_save_receipt_to_csv(this.cart, calculate_total_items(), calculate_total_price(), totalPrice, customer);
    }

}
