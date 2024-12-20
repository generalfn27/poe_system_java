package Process;

import Shopper.Customer;
import Shopper.Product;
import Shopper.UserCustomer;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OrderProcessor {
    public static List<Product> cart;
    public static int total_items;
    public static double total_price;
    private static int currentQueueNumber;

    public OrderProcessor() {
        cart = new ArrayList<>();
        total_items = 0;
        total_price = 0;
        initialize_queue_number();
    }


    public OrderProcessor(List<Product> existingCart) {
        cart = existingCart;
        total_items = existingCart.size();
        total_price = calculate_total_price(existingCart);
        initialize_queue_number();
    }

    public boolean process_customer_order(List<Product> products) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return false;
        }

        String product_code;
        Product selected_product;

        while (true) {
            System.out.println("\n\tEnter /// to Cancel product selection.");
            product_code = get_valid_input(console, "\tEnter product code: ", "\tInvalid input. Please try again.", 3).toUpperCase();

            if (product_code.equals("///")) {
                System.out.println("\n\tProduct selection cancelled.");
                return false;
            }

            // Find product code in the list
            selected_product = find_product_code(product_code, products);

            if (selected_product != null) {
                break; // Exit loop when valid product is selected
            }

            System.out.println("\tInvalid product code. Try again.");
            System.out.print("\tPress Enter to continue...");
            console.readLine();
        }

        int quantity = 0;
        boolean valid_input = false;

        do {
            display_product_details(selected_product);
            System.out.print("\tEnter quantity: ");
            String quantityInput = console.readLine().trim();

            try {
                quantity = Integer.parseInt(quantityInput);

                // Validate quantity range (1 to stock)
                if (quantity > 0 && quantity <= selected_product.getStock()) {
                    valid_input = true;
                } else {
                    System.out.println("\n\tInvalid quantity. Please enter a value between 1 and " + selected_product.getStock() + ".");
                    System.out.print("\tPress Enter to continue...");
                    console.readLine();
                }
            } catch (NumberFormatException e) {
                System.out.println("\n\tInvalid input. Please enter a number.");
                System.out.print("\tPress Enter to continue...");
                console.readLine();
            }
        } while (!valid_input);

        while (true) {
            display_product_details(selected_product);
            System.out.printf("\n\tPrice: %.2f\tQuantity: %d\n", selected_product.getPrice() * quantity, quantity);

            String add_item_confirmation = get_valid_input(console, "\tAdd to cart (A) or cancel (C)? ", "\tInvalid input. Please enter A or C only.", 1).toUpperCase();

            switch (add_item_confirmation) {
                case "A":
                    add_to_cart(selected_product, quantity);
                    System.out.println("\n\tItem added to cart.");
                    return true;
                case "C":
                    System.out.println("\tItem not added to cart.");
                    System.out.print("\tPress Enter to continue...");
                    console.readLine();
                    return false;
                default:
                    System.out.println("\tInvalid input. Please enter A or C only.");
                    System.out.print("\tPress Enter to continue...");
                    console.readLine();
                    console.flush();
            }
        }
    }



    private static String get_valid_input(Console console, String prompt, String errorMsg, int minLength) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = console.readLine().trim();
            if (input.length() >= minLength) {
                return input;
            }
            System.out.println("\t" + errorMsg);
        }
    }


    private Product find_product_code(String product_code, List<Product> products) {
        for (Product product : products) {
            if (product.getCode().equals(product_code)) {
                return product;
            }
        } return null;
    }


    private void display_product_details(Product product) {
        System.out.printf("\n\tProduct: %-22s\tPrice: %6.2f\tStock: %4d\n",
                product.getName(), product.getPrice(), product.getStock());
    }

    public void add_to_cart(Product product, int quantity) {
        //babawasan ung stock at dagdagan ung asa cart
        product.update_stock(-quantity);  // Adjust stock in the Product class
        total_items += quantity;
        total_price += product.getPrice() * quantity;

        cart.add(new Product(product.getCode(), product.getName(), product.getPrice(), quantity));
    }


    public static void display_cart() {
        System.out.println("\n\n\n\tYour Cart:");
        if (cart.isEmpty()) {
            System.out.println("\tYour cart is empty.");
        } else {
            for (Product product : cart) {
                System.out.printf("\tProduct Code: %-10s Name: %-20s Quantity: %d Price: %.2f\n",
                        product.getCode(), product.getName(), product.getStock(), product.getPrice());
            }
            System.out.printf("\n\tTotal Items: %d\n", calculate_total_items());
            System.out.printf("\tTotal Price: %.2f\n", calculate_total_price());
        }
    }

    private static int calculate_total_items() {
        int total = 0;
        for (Product product : cart) {
            total += product.getStock();
        }
        return total;
    }


    private static double calculate_total_price() {
        double total = 0;
        for (Product product : cart) {
            total += product.getPrice() * product.getStock();
        }
        return total;
    }

    private static double calculate_total_price(List<Product> cartItems) {
        double total = 0;
        for (Product product : cart) {
            total += product.getPrice() * product.getStock();
        }
        return total;
    }


    // both modify process menu ay pwede pang gawing method para mas malinis tignan
    // for now ganito muna dahil di pa naman sure kung need sobrang neat & clean code
    public void modify_menu_process() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        String choice;

        while (true) {
            display_cart();
            System.out.println("\n\tMODIFY MENU:");
            System.out.println("\tIncrease Quantity (I)");
            System.out.println("\tDeduct Quantity (D)");
            System.out.println("\tRemove Items (R)");
            System.out.println("\tClear Cart (C)");
            System.out.println("\tProceed to checkout (P)");
            System.out.println("\tGo Back to Categories (B)");
            System.out.print("\n\tEnter choice: ");
            choice = console.readLine().trim().toUpperCase();

            switch (choice) {
                case "I":
                    handle_quantity_change(console, "increase");
                    break;
                case "D":
                    handle_quantity_change(console, "deduct");
                    break;
                case "R":
                    boolean valid_to_remove = false;
                    while (!valid_to_remove){
                        System.out.print("\tAre you sure you want to remove an item? (Y/N): ");
                        String confirm_clear = console.readLine().trim();

                        switch (confirm_clear) {
                            case "y":
                            case "Y":
                                System.out.println("\tEnter 0 to Cancel item removal");
                                System.out.print("\tEnter product code to remove: ");
                                String codeToRemove = console.readLine();
                                if (codeToRemove.equals("0")) { break; }
                                remove_item(codeToRemove);  // Remove the item sa cart all quantity
                                valid_to_remove = true;
                                break;
                            case "n":
                            case "N":
                                valid_to_remove = true;
                                break;
                            default:
                                System.out.println("\n\tWrong input");
                                System.out.print("\t\tPress Enter key to continue.");
                                console.readLine();
                                console.flush();
                        }
                    }
                    break;
                case "C":
                    handle_cart_clear(console);
                    break;
                case "B":
                    return; // Go back to the previous menu
                case "P":
                    handle_checkout(console);
                    break;
                default:
                    System.out.println("\tInvalid choice.");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
            }
        }
    }

    private void handle_quantity_change(Console console, String action) {
        int quantity = 0;
        boolean validInput = false;

        System.out.println("\n\tEnter 0 to Cancel item " + action);
        System.out.print("\tEnter the product code to " + action + ": ");
        String productCode = console.readLine();
        if (productCode.equals("0")) {
            return;
        }

        while (!validInput) {
            try {
                System.out.print("\tEnter quantity to " + action + ": ");
                quantity = Integer.parseInt(console.readLine());
                if (action.equals("increase")) {
                    increase_item_quantity(productCode, quantity);
                } else if (action.equals("deduct")) {
                    deduct_item_quantity(productCode, quantity);
                }
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("\tInvalid input. Please enter a valid number.");
            }
        }
    }

    private void handle_cart_clear(Console console) {
        boolean valid = false;

        while (!valid) {
            System.out.print("\tAre you sure you want to clear your cart? (Y/N): ");
            String confirmClear = console.readLine().trim().toUpperCase();

            switch (confirmClear) {
                case "Y":
                    reset_cart();
                    System.out.println("\n\tCart cleared.");
                    valid = true;
                    break;
                case "N":
                    System.out.println("\n\tCart clearing cancelled.");
                    valid = true;
                    break;
                default:
                    System.out.println("\n\tInvalid input. Please enter Y or N.");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
            }
        }
    }


    private void handle_checkout(Console console) {
        boolean valid = false;

        while (!valid) {
            System.out.print("\n\tAre you sure you want to proceed to checkout? (Y/N): ");
            String confirmCheckout = console.readLine().trim().toUpperCase();

            switch (confirmCheckout) {
                case "Y":
                    System.out.print("\n\tPress [E] to confirm checkout or [Any other key] to cancel: ");
                    String exitChoice = console.readLine().trim().toUpperCase();

                    if (exitChoice.equals("E")) {
                        System.out.println("\n\tProcessing checkout...");
                        save_cart_to_queue_csv();
                        reset_cart();
                        UserCustomer userCustomer = new UserCustomer();
                        userCustomer.guest_customer_item_category();
                        return;
                    } else {
                        System.out.println("\n\tCheckout cancelled.");
                        valid = true;
                    }
                    break;
                case "N":
                    System.out.println("\n\tCheckout cancelled.");
                    valid = true;
                    break;
                default:
                    System.out.println("\n\tInvalid input. Please enter Y or N.");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
            }
        }
    }



    public void registered_user_modify_menu_process(Customer customer, List<Product> cart) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        String choice;

        if (cart == null) {
            cart = new ArrayList<>();
        }

        while (true) {
            display_cart();
            System.out.println("\n\tMODIFY MENU:");
            System.out.println("\tIncrease Quantity (I)");
            System.out.println("\tDeduct Quantity (D)");
            System.out.println("\tRemove Items (R)");
            System.out.println("\tClear Cart (C)");
            System.out.println("\tProceed to checkout (P)");
            System.out.println("\tGo Back to Categories (B)");
            System.out.print("\n\tEnter choice: ");
            choice = console.readLine().trim().toUpperCase();

            //pwedeng isang case lang and ang nasa scanf ay naka toupper, kung alin efficient
            switch (choice) {
                case "I":
                    int quantityToIncrease;
                    boolean quantity_increase_valid_input = false;

                    System.out.println("\tEnter 0 to Cancel item increase");
                    System.out.print("\tEnter the product code to increase: ");
                    String codeToIncrease = console.readLine();

                    if (codeToIncrease.equals("0")) { break; }

                    while (!quantity_increase_valid_input) {
                        try {
                            System.out.print("\tEnter quantity to increase: ");
                            quantityToIncrease = Integer.parseInt(console.readLine());
                            increase_item_quantity(codeToIncrease, quantityToIncrease);
                            quantity_increase_valid_input = true; // Input is valid, exit the loop
                        } catch (NumberFormatException e) {
                            System.out.println("\tInvalid input. Please enter a valid number.");
                        }
                    }
                    break;
                case "D":
                    int quantityToDeduct;
                    boolean deduction_valid_input = false;

                    System.out.println("\tEnter 0 to Cancel item deduction");
                    System.out.print("\tEnter product code to deduct: ");
                    String codeToDeduct = console.readLine();

                    if (codeToDeduct.equals("0")) { break; }

                    while (!deduction_valid_input) {
                        try {
                            System.out.print("\tEnter quantity to deduct: ");
                            quantityToDeduct = Integer.parseInt(console.readLine());
                            deduct_item_quantity(codeToDeduct, quantityToDeduct);
                            deduction_valid_input = true; // Input is valid, exit the loop
                        } catch (NumberFormatException e) {
                            System.out.println("\tInvalid input. Please enter a valid number.");
                        }
                    }
                    break;
                case "R":
                    boolean valid_to_remove = false;
                    while (!valid_to_remove){
                        System.out.print("\tAre you sure you want to remove an item? (Y/N): ");
                        String confirm_clear = console.readLine().trim().toUpperCase();

                        switch (confirm_clear) {
                            case "Y":
                                System.out.println("\tEnter 0 to Cancel item removal");
                                System.out.print("\tEnter product code to remove: ");
                                String codeToRemove = console.readLine();
                                if (codeToRemove.equals("0")) { break; }
                                remove_item(codeToRemove);  // Remove the item sa cart all quantity
                                valid_to_remove = true;
                                break;
                            case "N":
                                valid_to_remove = true;
                                break;
                            default:
                                System.out.println("\n\tWrong input");
                                System.out.print("\t\tPress Enter key to continue.");
                                console.readLine();
                                console.flush();
                        }
                    }
                    break;
                case "C":
                    boolean valid_to_clear = false;
                    while (!valid_to_clear){
                        System.out.print("\tAre you sure you want to clear your cart? (Y/N): ");
                        String confirm_clear = console.readLine().trim().toUpperCase();

                        switch (confirm_clear) {
                            case "Y":
                                reset_cart();  // Reset the cart
                                valid_to_clear = true;
                                break;
                            case "N":
                                valid_to_clear = true;
                                break;
                            default:
                                System.out.println("\n\tWrong input");
                                System.out.print("\t\tPress Enter key to continue.");
                                console.readLine();
                                console.flush();
                        }
                    }
                    break;
                case "B":
                    //hindi maipasa ung cart dahil pag return lng ginamit ay balik balik lng sa methods
                    UserCustomer user_customer = new UserCustomer();
                    user_customer.registered_user_customer_item_category(customer.getUsername(), customer, cart);
                    return;
                case "P":
                    while (true) {
                        System.out.print("\n\tAre you sure you want to proceed to checkout? (Y/N): ");
                        String confirmInput = console.readLine();

                        if (confirmInput.equalsIgnoreCase("y")) {
                            self_checkout_or_queue_process(customer, cart);
                            break;
                        } else if (confirmInput.equalsIgnoreCase("n")) {
                            registered_user_modify_menu_process(customer, cart);
                        } else {
                            System.out.println("\n\tAn error has occurred.");
                            System.out.print("\t\tPress Enter key to continue.");
                            console.readLine();
                            console.flush();
                        }
                    }
                    break;
                default:
                    System.out.println("\n\tAn error has occurred.");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }
        }
    }


    public void self_checkout_or_queue_process(Customer customer, List<Product> cart) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        while (true) {
                OrderProcessor.cart = cart;
                System.out.println("\t===========================================");
                System.out.println("\t|          Processing Checkout...         |");
                System.out.println("\t|                                         |");
                System.out.println("\t|      Choose payment method:             |");
                System.out.println("\t|      [1] Cash (Queue at Cashier)        |");
                System.out.printf ("\t|      [2] %-8s(Self-Checkout)        |\n", customer.getPaymentMethod());
                System.out.println("\t|                                         |");
                System.out.println("\t|      [0] Cancel                         |");
                System.out.println("\t|                                         |");
                System.out.println("\t===========================================");
                System.out.print  ("\tEnter choice: ");
                String payment_choice = console.readLine().trim();

                switch (payment_choice) {
                    case "0":
                        while (true) {
                            System.out.print("\n\tAre you sure you want to cancel checkout? (Y/N): ");
                            String cancellation_confirmation = console.readLine();

                            if (cancellation_confirmation.equalsIgnoreCase("Y")) {
                                System.out.println("\n\tCheckout cancelled.");
                                System.out.print("\t\tPress Enter key to continue.");
                                console.readLine();
                                console.flush();
                                registered_user_modify_menu_process(customer, cart);
                                return;
                            } else if (cancellation_confirmation.equalsIgnoreCase("N")) {
                                break;
                            } else {
                                System.out.println("\n\tAn error has occurred.");
                                System.out.print("\t\tPress Enter key to continue.");
                                console.readLine();
                                console.flush();
                            }
                        }
                        break;
                    case "1":
                        while (true) {
                            System.out.print("\n\n\tAre you sure you want to queue? (Y/N): ");
                            String queue_confirmation = console.readLine();

                            if (queue_confirmation.equalsIgnoreCase("Y")) {
                                save_cart_to_queue_csv();
                                reset_cart();
                                UserCustomer user_customer = new UserCustomer();
                                user_customer.registered_user_customer_item_category(customer.getUsername(), customer, cart);
                            } else if (queue_confirmation.equalsIgnoreCase("N")) {
                                break;
                            } else {
                                System.out.println("\n\tAn error has occurred.");
                                System.out.print("\t\tPress Enter key to continue.");
                                console.readLine();
                                console.flush();
                            }
                        }
                        break;
                    case "2":
                        while (true) {
                            System.out.print("\n\n\tAre you sure you want to self checkout? (Y/N): ");
                            String self_checkout_confirmation = console.readLine();

                            if (self_checkout_confirmation.equalsIgnoreCase("Y")) {
                                SelfCheckout selfCheckout = new SelfCheckout(cart);
                                selfCheckout.processSelfCheckout(customer);
                            } else if (self_checkout_confirmation.equalsIgnoreCase("N")) {
                                break;
                            } else {
                                System.out.println("\n\tAn error has occurred.");
                                System.out.print("\t\tPress Enter key to continue.");
                                console.readLine();
                                console.flush();
                            }
                        }
                        break;
                    default:
                        System.out.println("\n\tAn error has occurred.");
                        System.out.print("\t\tPress Enter key to continue.");
                        console.readLine();
                        console.flush();
                }

        }
    }


    public void increase_item_quantity(String product_code, int quantity) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        for (Product product : cart) {
            if (product.getCode().equals(product_code.toUpperCase())) {
                int new_quantity = product.getStock() + quantity;
                if (new_quantity > 0) {
                    product.update_stock(+quantity);
                    total_items += quantity;
                    total_price += product.getPrice() * quantity;
                    System.out.printf("\tIncreased %d of %s from the cart.\n", quantity, product.getName());
                } else {
                    remove_item(product_code);
                }
                return;
            }
        }
        System.out.println("\n\tProduct not found in the cart. Please add the item first.");
        System.out.print("\t\tPress Enter key to continue.");
        console.readLine();
        console.flush();
    }

    public void deduct_item_quantity(String product_code, int quantity) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        for (Product product : cart) {
            if (product.getCode().equals(product_code.toUpperCase())) {
                int new_quantity = product.getStock() - quantity;
                if (new_quantity > 0) {
                    product.update_stock(-quantity);
                    total_items -= quantity;
                    total_price -= product.getPrice() * quantity;
                    System.out.printf("\n\tDeducted %d of %s from the cart.\n", quantity, product.getName());
                } else if (new_quantity < 0) {
                    //remove_item(product_code);
                    System.out.println("\n\tThe quantity you want to remove exceeds the item count you have.");
                }
                return;
            }
        }
        System.out.println("\n\tProduct not found in the cart. Please add the item first.");
        System.out.print("\t\tPress Enter key to continue.");
        console.readLine();
        console.flush();
    }


    public void remove_item(String product_code) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        Product to_remove = null;
        for (Product product : cart) {
            if (product.getCode().equals(product_code.toUpperCase())) {
                total_price-= product.getStock();
                total_price -= product.getPrice() * product.getStock();
                total_items -= product.getStock();
                to_remove = product;
                break;
            }
        }
        if (to_remove != null) {
            cart.remove(to_remove);
            System.out.printf("\tRemoved %s from the cart.\n", to_remove.getName());
        } else {
            System.out.println("\n\tProduct not found in the cart. Please add the item first.");
            System.out.print("\t\tPress Enter key to continue.");
            console.readLine();
            console.flush();
        }
    }

    public void reset_cart() {
        cart.clear();
        total_items = 0;
        total_price = 0;
        System.out.println("\tThe cart has been reset.");
    }

    public void reset_cart_no_display() {
        cart.clear();
        total_items = 0;
        total_price = 0;
    }


    public void save_cart_to_queue_csv() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        if (cart.isEmpty()) {
            System.out.println("\tCart is empty. Nothing to save.");
            return;
        }

        String fileName = generate_queue_file_name();

        // Replace with the absolute path to your "directory/queues" folder
        String filePath = "oopr-poe-data/queues/" + fileName;

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write header
            writer.println("ProductCode,ProductName,Quantity,Price,Subtotal");

            // Write cart items
            for (Product product : cart) {
                double subtotal = product.getPrice() * product.getStock();
                writer.printf("%s,%s,%d,%.2f,%.2f%n",
                        product.getCode(),
                        product.getName(),
                        product.getStock(),
                        product.getPrice(),
                        subtotal);
            }

            System.out.println("\t����������������������������������������������������������������������������������������");
            System.out.println("\t�                                                                                      �");
            System.out.printf ("\t�                                 Your queue number is: %02d                             �\n", currentQueueNumber);
            System.out.println("\t�                       Cart saved to " + fileName + "                   �");
            System.out.println("\t�                                                                                      �");
            System.out.println("\t�                                                                                      �");
            System.out.println("\t�Thank you for ordering! Please remember your queue number and proceed to the cashier. �");
            System.out.println("\t����������������������������������������������������������������������������������������\n\n");

            System.out.print("\n\tPress Enter key to continue...");
            console.readLine();
            console.flush();

            save_queue_number();

        } catch (IOException e) {
            System.out.println("\tAn error occurred while saving the cart: " + e.getMessage());
        }
    }


    private static String generate_queue_file_name() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String formattedDate = dateFormat.format(new Date());

        currentQueueNumber++; // Increment the queue number for the next file
        save_queue_number(); // Save the updated queue number

        return String.format("queue_number_%02d_%s.csv", currentQueueNumber, formattedDate);
    }


    public static void initialize_queue_number() {
        try (BufferedReader reader = new BufferedReader(new FileReader("oopr-poe-data/queues/current_queue_number.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                currentQueueNumber = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            // If file doesn't exist or is invalid, start from 0
            currentQueueNumber = 0;
        }
    }


    private static void save_queue_number() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("oopr-poe-data/queues/current_queue_number.txt"))) {
            writer.println(currentQueueNumber);
        } catch (IOException e) {
            System.out.println("\tError saving queue number: " + e.getMessage());
        }
    }



}
