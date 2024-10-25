package Process;

import Shopper.Customer;
import Shopper.Product;
import Shopper.UserCustomer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OrderProcessor {
    public static List<Product> cart;
    private static int total_items;
    public static double total_price;
    private static int currentQueueNumber;

    public OrderProcessor() {
        cart = new ArrayList<>();
        total_items = 0;
        total_price = 0;
        initialize_queue_number();
    }

    public void process_customer_order(List<Product> products) {
        Scanner scanf = new Scanner(System.in);
        String product_code;
        int quantity = 0;
        Product selected_product;

        do {
            System.out.println("\n\tEnter /// to Cancel product selection.");
            System.out.print("\tEnter product code: ");
            product_code = scanf.nextLine().toUpperCase();

            if (product_code.equals("///")){
                return;
            }

            //find product code kung nag eexist
            selected_product = find_product_code(product_code, products);

            if (selected_product == null) {
                System.out.println("\tInvalid product code. Try again.");
                System.out.println("\tPress Enter to continue...");
                scanf.nextLine();
            }

        } while (selected_product == null);

        display_product_details(selected_product);

        //quantity check at error handling din kung kulang or sobra order ng kupal
        boolean valid_input = false;
        do {
            System.out.print("\tEnter quantity: ");

            if (scanf.hasNextInt()) {
                quantity = scanf.nextInt();
                scanf.nextLine();

                // Validate quantity range (1 to stock)
                if (quantity > 0 && quantity <= selected_product.getStock()) {
                    valid_input = true;
                } else {
                    System.out.println("\n\tInvalid quantity. Please enter a value between 1 and " + selected_product.getStock() + ".");
                    System.out.println("\tType any key and enter to continue.");
                    scanf.next(); // Clear buffer (optional)
                }
            } else {
                System.out.println("\n\tInvalid input. Please enter a number.");
                System.out.println("\tType any key and enter to continue.");
                scanf.next(); // Clear buffer to avoid issues with next calls
            }
        } while (!valid_input);

        System.out.printf("\n\tPrice: %.2f\n", selected_product.getPrice() * quantity);

        //tatanong kung sigurado ba ung kupal pag may pangbili sya
        String add_item_confirmation;

        while (true) {
            System.out.print("\tAdd to cart (A) or cancel (C)? ");
            add_item_confirmation = scanf.nextLine().trim();

            switch (add_item_confirmation){
                case "a":
                case "A":
                    add_to_cart(selected_product, quantity);
                    System.out.println("\n\tItem added to cart.");
                    break; // Exit the switch statement, but still in the loop
                case "c":
                case "C":
                    System.out.println("\tItem not added to cart.");
                    break; // Exit the switch statement, but still in the loop
                default:
                    System.out.println("\tInvalid input. Please enter A or C only.");
                    continue; // Go back to the beginning of the loop if invalid input
            }
            break; // Exit the loop after valid input
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
        System.out.println("\n\tYour Cart:");
        if (cart.isEmpty()) {
            System.out.println("\tYour cart is empty.");
        } else {
            for (Product product : cart) {
                System.out.printf("\tProduct Code: %-10s Name: %-20s Quantity: %d Price: %.2f\n",
                        product.getCode(), product.getName(), product.getStock(), product.getPrice());
            }
            System.out.printf("\n\tTotal Items: %d\n", total_items);
            System.out.printf("\tTotal Price: %.2f\n", total_price);
        }
    }


    // both modify process menu ay pwede pang gawing method para mas malinis tignan
    // for now ganito muna dahil di pa naman sure kung need sobrang neat & clean code
    public void modify_menu_process() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            display_cart();
            System.out.println("\n\tMODIFY MENU:");
            System.out.println("\tIncrease Quantity (I)");
            System.out.println("\tDeduct Quantity (D)");
            System.out.println("\tRemove Items (R)");
            System.out.println("\tClear Cart (C)");
            System.out.println("\tProceed to checkout (P)");
            System.out.println("\tDisplay cart(V)");
            System.out.println("\tGo Back to Categories (B)");
            System.out.print("\n\tEnter choice: ");
            choice = scanf.nextLine();

            //pwedeng isang case lang and ang nasa scanf ay naka toupper, kung alin efficient
            switch (choice) {
                case "I":
                case "i":
                    int quantityToIncrease;
                    boolean quantity_increase_valid_input = false;

                    System.out.println("\n\tEnter 0 to Cancel item increase");
                    System.out.print("\tEnter the product code to increase: ");
                    String codeToIncrease = scanf.nextLine();

                    if (codeToIncrease.equals("0")) { break; }

                    while (!quantity_increase_valid_input) {
                        try {
                            System.out.print("\tEnter quantity to increase: ");
                            quantityToIncrease = Integer.parseInt(scanf.nextLine());
                            increase_item_quantity(codeToIncrease, quantityToIncrease);
                            quantity_increase_valid_input = true; // Input is valid, exit the loop
                        } catch (NumberFormatException e) {
                            System.out.println("\tInvalid input. Please enter a valid number.");
                        }
                    }
                    break;
                case "D":
                case "d":
                    int quantityToDeduct;
                    boolean deduction_valid_input = false;

                    System.out.println("\tEnter 0 to Cancel item deduction");
                    System.out.print("\tEnter product code to deduct: ");
                    String codeToDeduct = scanf.nextLine();

                    if (codeToDeduct.equals("0")) { break; }

                    while (!deduction_valid_input) {
                        try {
                            System.out.print("\tEnter quantity to deduct: ");
                            quantityToDeduct = Integer.parseInt(scanf.nextLine());
                            deduct_item_quantity(codeToDeduct, quantityToDeduct);
                            deduction_valid_input = true; // Input is valid, exit the loop
                        } catch (NumberFormatException e) {
                            System.out.println("\tInvalid input. Please enter a valid number.");
                        }
                    }
                    break;
                case "R":
                case "r":
                    System.out.println("\tEnter 0 to Cancel item removal");
                    System.out.print("\tEnter product code to remove: ");
                    String codeToRemove = scanf.nextLine();
                    if (codeToRemove.equals("0")) { break; }
                    remove_item(codeToRemove);  // Remove the item sa cart all quantity
                    break;
                case "C":
                case "c":
                    boolean valid_to_clear = false;

                    while (!valid_to_clear){
                        System.out.print("\tAre you sure you want to clear your cart? (Y/N): ");
                        String confirm_clear = scanf.nextLine().trim();

                        switch (confirm_clear) {
                            case "y":
                            case "Y":
                                reset_cart();  // Reset the cart
                                valid_to_clear = true;
                                break;
                            case "n":
                            case "N":
                                valid_to_clear = true;
                                break;
                            default:
                                System.out.println("\n\tWrong input");
                                System.out.println("\t\tPress Enter key to continue.\n");
                                scanf.nextLine(); //used for press any key to continue
                        }
                    }
                    break;
                case "V":
                case "v":
                    display_cart();
                    break;
                case "B":
                case "b":
                    return; //as far as i know kaya nagana ung return kasi return sa menu agad dahil dun sya last na callout
                case "P":
                case "p":
                    // Confirmation before checkout
                    String confirm_process_queue;
                    boolean valid = false;

                    while (!valid) {
                        System.out.print("\n\tAre you sure you want to proceed to checkout? (Y/N): ");
                        confirm_process_queue = scanf.nextLine().trim();

                        switch (confirm_process_queue) {
                            case "y":
                            case "Y":
                                System.out.println("\n\tProcessing checkout...");
                                System.out.print("\n\tPress and enter [E] to checkout");
                                System.out.print("\n\t\t\tPress and enter any key to return and shop again: ");
                                String exit_choice = scanf.nextLine().trim().toUpperCase();

                                if (exit_choice.equals("E")) {
                                    save_cart_to_queue_csv();
                                    reset_cart();
                                    UserCustomer user_customer = new UserCustomer();
                                    user_customer.user_customer_menu();
                                    return;
                                } else {
                                    valid = true;
                                }
                                break;
                            case "n":
                            case "N":
                                System.out.println("\n\tCheckout cancelled.");
                                valid = true;
                                break;
                            default:
                                System.out.println("\n\tWrong input");
                                System.out.println("\t\tPress Enter key to continue.\n");
                                scanf.nextLine(); //used for press any key to continue
                        }
                    }
                    break;
                default:
                    System.out.println("\tInvalid choice.");
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
            }
        }
    }


    public void registered_user_modify_menu_process(Customer customer) {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            display_cart();
            System.out.println("\n\tMODIFY MENU:");
            System.out.println("\tIncrease Quantity (I)");
            System.out.println("\tDeduct Quantity (D)");
            System.out.println("\tRemove Items (R)");
            System.out.println("\tClear Cart (C)");
            System.out.println("\tProceed to checkout (P)");
            System.out.println("\tDisplay cart(V)");
            System.out.println("\tGo Back to Categories (B)");
            System.out.print("\n\tEnter choice: ");
            choice = scanf.nextLine();

            //pwedeng isang case lang and ang nasa scanf ay naka toupper, kung alin efficient
            switch (choice) {
                case "I":
                case "i":
                    int quantityToIncrease;
                    boolean quantity_increase_valid_input = false;

                    System.out.println("\tEnter 0 to Cancel item increase");
                    System.out.print("\tEnter the product code to increase: ");
                    String codeToIncrease = scanf.nextLine();

                    if (codeToIncrease.equals("0")) { break; }

                    while (!quantity_increase_valid_input) {
                        try {
                            System.out.print("\tEnter quantity to increase: ");
                            quantityToIncrease = Integer.parseInt(scanf.nextLine());
                            increase_item_quantity(codeToIncrease, quantityToIncrease);
                            quantity_increase_valid_input = true; // Input is valid, exit the loop
                        } catch (NumberFormatException e) {
                            System.out.println("\tInvalid input. Please enter a valid number.");
                        }
                    }
                    break;
                case "D":
                case "d":
                    int quantityToDeduct;
                    boolean deduction_valid_input = false;

                    System.out.println("\tEnter 0 to Cancel item deduction");
                    System.out.print("\tEnter product code to deduct: ");
                    String codeToDeduct = scanf.nextLine();

                    if (codeToDeduct.equals("0")) { break; }

                    while (!deduction_valid_input) {
                        try {
                            System.out.print("\tEnter quantity to deduct: ");
                            quantityToDeduct = Integer.parseInt(scanf.nextLine());
                            deduct_item_quantity(codeToDeduct, quantityToDeduct);
                            deduction_valid_input = true; // Input is valid, exit the loop
                        } catch (NumberFormatException e) {
                            System.out.println("\tInvalid input. Please enter a valid number.");
                        }
                    }
                    break;
                case "R":
                case "r":
                    System.out.println("\tEnter 0 to Cancel item removal");
                    System.out.print("\tEnter product code to remove: ");
                    String codeToRemove = scanf.nextLine();
                    if (codeToRemove.equals("0")) { break; }
                    remove_item(codeToRemove);  // Remove the item sa cart all quantity
                    break;
                case "C":
                case "c":
                    boolean valid_to_clear = false;
                    while (!valid_to_clear){
                        System.out.print("\tAre you sure you want to clear your cart? (Y/N): ");
                        String confirm_clear = scanf.nextLine().trim();

                        switch (confirm_clear) {
                            case "y":
                            case "Y":
                                reset_cart();  // Reset the cart
                                valid_to_clear = true;
                                break;
                            case "n":
                            case "N":
                                valid_to_clear = true;
                                break;
                            default:
                                System.out.println("\n\tWrong input");
                                System.out.println("\t\tPress Enter key to continue.\n");
                                scanf.nextLine(); //used for press any key to continue
                        }
                    }
                    break;
                case "V":
                case "v":
                    display_cart();
                    break;
                case "B":
                case "b":
                    return;
                case "P":
                case "p":
                    while (true) {
                        // Confirmation before checkout
                        System.out.print("\n\tAre you sure you want to proceed to checkout? (Y/N): ");
                        String confirmInput = scanf.nextLine();

                        if (confirmInput.equalsIgnoreCase("y")) {
                            System.out.println("\n\tProcessing checkout...");
                            System.out.println("\n\tChoose payment method:");
                            System.out.println("\t1. Cash (Queue at Cashier)");
                            System.out.println("\t2. " + customer.getPaymentMethod() + " (Self-Checkout)");
                            System.out.println("\t0. Cancel");
                            System.out.print("\tEnter choice: ");
                            String paymentChoice = scanf.nextLine().trim();

                            if (!paymentChoice.equals("1")) {
                                if (paymentChoice.equals("2")) {
                                    // New e-wallet self-checkout process
                                    UserCustomer userCustomer = new UserCustomer(); // Create a new UserCustomer instance
                                    SelfCheckout selfCheckout = new SelfCheckout(userCustomer, cart);
                                    selfCheckout.processSelfCheckout(customer.getUsername());
                                } else if (paymentChoice.equals("0")){
                                    break; //para ang balik nya ay sa modify menu parin
                                } else {
                                    System.out.println("\n\tAn error has occurred.");
                                    System.out.println("\t\tPress Enter key to continue.\n");
                                    scanf.nextLine(); //used for press any key to continue
                                }
                            } else {
                                //eto ung choice 1
                                save_cart_to_queue_csv();
                                reset_cart();

                            /*
                            dapat hindi muna auto logout
                            so may notif sa dashboard na proceed sya sa cashier or may option na logout and cashier
                            at confirmations din
                            */

                                UserCustomer user_customer = new UserCustomer();
                                user_customer.user_customer_menu();
                            }
                        } else {
                            System.out.println("\n\tCheckout cancelled.");
                            System.out.println("\t\tPress Enter key to continue.\n");
                            scanf.nextLine(); //used for press any key to continue
                            break;
                        }
                    }
                    break;
            }
        }
    }


    public void increase_item_quantity(String product_code, int quantity) {
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
        System.out.println("\tProduct not found in the cart.");
    }

    public void deduct_item_quantity(String product_code, int quantity) {
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
        System.out.println("\tProduct not found in the cart.");
    }


    public void remove_item(String product_code) {
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
            System.out.println("\tProduct not found in the cart.");
        }
    }

    public static void reset_cart() {
        cart.clear();
        total_items = 0;
        total_price = 0;
        System.out.println("\tThe cart has been reset.");
    }

    public static void reset_cart_no_display() {
        cart.clear();
        total_items = 0;
        total_price = 0;
    }


    public static void save_cart_to_queue_csv() {
        Scanner scanf = new Scanner(System.in);
        if (cart.isEmpty()) {
            System.out.println("\tCart is empty. Nothing to save.");
            return;
        }

        String fileName = generate_queue_file_name();

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write header
            writer.println("OrderID,ProductCode,ProductName,Quantity,Price,Subtotal");

            // Generate a unique order ID (you might want to implement a more robust system)
            String order_id = generate_order_id();

            // Write cart items
            for (Product product : cart) {
                double subtotal = product.getPrice() * product.getStock();
                writer.printf("%s,%s,%s,%d,%.2f,%.2f%n",
                        order_id,
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
            scanf.nextLine();

            // Save the updated queue number
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

    //ai suggestion
    private static String generate_order_id() {
        // This is a simple implementation. You might want to use a more robust system
        return "ORD" + System.currentTimeMillis();
    }

    // Method to initialize the queue number by reading from a file
    public static void initialize_queue_number() {
        try (BufferedReader reader = new BufferedReader(new FileReader("current_queue_number.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                currentQueueNumber = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            // If file doesn't exist or is invalid, start from 0
            currentQueueNumber = 0;
        }
    }

    // Method to save the current queue number to a file
    private static void save_queue_number() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("current_queue_number.txt"))) {
            writer.println(currentQueueNumber);
        } catch (IOException e) {
            System.out.println("\tError saving queue number: " + e.getMessage());
        }
    }



}
