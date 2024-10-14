package Process;

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
    private static int currentQueueNumber = 1;
    private static final String QUEUE_NUMBER_FILE = "current_queue_number.txt";

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
            System.out.print("\n\tEnter product code: ");
            product_code = scanf.nextLine().toUpperCase();

            //find product code kung nag eexist
            selected_product = find_product_code(product_code, products);

            if (selected_product == null) {
                System.out.println("\tInvalid product code. Try again.");
                System.out.println("\tPress Enter to continue...");
                scanf.nextLine(); // Wait for user input
            }

            //gagawan ng option para mag go back kung wala mapili
            //example enter 000 if you want to cancel/go back

        } while (selected_product == null);

        //display ung detalye ng product na pinili
        display_product_details(selected_product);

        //quantity check at error handling din kung kulang or sobra order ng kupal
        boolean valid_input = false;
        do {
            System.out.print("\tEnter quantity: ");

            // Check if input is an integer
            if (scanf.hasNextInt()) {
                quantity = scanf.nextInt();

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

        System.out.printf("\tPrice: %.2f\n", selected_product.getPrice() * quantity);

        //tatanong kung sigurado ba ung kupal pag may pangbili sya
        char choice;

        while (true) {
            System.out.print("\tAdd to cart (A) or cancel (C)? ");
            choice = scanf.next().charAt(0);

            switch (choice){
                case 'a':
                case 'A':
                    add_to_cart(selected_product, quantity);
                    System.out.println("\tItem added to cart.");
                    break; // Exit the switch statement, but still in the loop

                case 'c':
                case 'C':
                    System.out.println("\tItem not added to cart.");
                    break; // Exit the switch statement, but still in the loop

                default:
                    System.out.println("\tInvalid input. Please enter A or C only.");
                    continue; // Go back to the beginning of the loop if invalid input
            }

            // Exit the loop after valid input
            break;
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
        System.out.printf("\tProduct: %-22s\tPrice: %6.2f\tStock: %4d\n",
                product.getName(), product.getPrice(), product.getStock());}


    public void add_to_cart(Product product, int quantity) {
        //babawasan ung stock at dagdagan ung asa cart pero need pa ayusin kasi baka d mag update ung csv
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


    public static void modify_menu_process() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            display_cart();
            System.out.println("\n\tMODIFY MENU:");
            System.out.println("\tAdd more items (A)\tpwedeng add more quantity to the item nalang to");
            System.out.println("\tRemove Items (R)");
            System.out.println("\tDeduct Quantity (D)");
            System.out.println("\tClear Cart (C)");
            System.out.println("\tProceed to checkout (P)");
            System.out.println("\tDisplay cart(V)");
            System.out.println("\tGo Back (B)");
            System.out.print("\n\tEnter choice: ");

            choice = scanf.nextLine();

            switch (choice) {
                case "A":
                case "a":
                    //pwedeng add more quantity to the item nalang to
                    break;
                case "R":
                case "r":
                    System.out.print("Enter 0 to Cancel remove");
                    System.out.print("Enter product code to remove: ");
                    String codeToRemove = scanf.nextLine();
                    remove_item(codeToRemove);  // Remove the item sa cart all quantity
                    break;
                case "D":
                case "d":
                    System.out.print("Enter product code to deduct: ");
                    String codeToDeduct = scanf.nextLine();
                    System.out.print("Enter quantity to deduct: ");
                    int quantityToDeduct = scanf.nextInt();
                    deduct_item_quantity(codeToDeduct, quantityToDeduct);  // Deduct quantity
                    break;
                case "C":
                case "c":
                    reset_cart();  // Reset the cart
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
                    // Confirmation before checkout
                    System.out.print("\n\tAre you sure you want to proceed to checkout? (Y/N): ");
                    String confirmInput = scanf.nextLine().trim();

                    if (!confirmInput.isEmpty() && (confirmInput.charAt(0) == 'Y' || confirmInput.charAt(0) == 'y')) {
                        System.out.println("\n\tProcessing checkout...");

                        System.out.print("\n\tPress [E] to checkout or press any key to shop again: ");
                        char exit_choice = scanf.next().charAt(0);

                        if (exit_choice == 'E' || exit_choice == 'e') {
                            // Checkout logic here or queue card muna tapos ang algorithm ay queue syempre
                            save_cart_to_csv();
                            reset_cart();
                            UserCustomer user_customer = new UserCustomer();
                            user_customer.user_customer_menu();
                            return;
                        } else {
                            OrderProcessor.modify_menu_process();
                        }

                    } else {
                        System.out.println("\n\tCheckout cancelled.");
                    }
                    break;
            }
        }
    }


    public void registered_user_modify_menu_process(String username) {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            display_cart();
            System.out.println("\n\tAdd more items (A)"); //pwedeng add more quantity to the item nalang to
            System.out.println("\tRemove Items (R)");
            System.out.println("\tDeduct Quantity (D)");
            System.out.println("\tClear Cart (C)");
            System.out.println("\tProceed to checkout (P)");
            System.out.println("\tDisplay cart(V)");
            System.out.println("\tGo Back (B)");
            System.out.print("\n\tEnter choice: ");

            choice = scanf.nextLine();

            switch (choice) {
                case "A":
                case "a":
                    //pwedeng add more quantity to the item nalang to
                    break;
                case "R":
                case "r":
                    System.out.print("Enter product code to remove: ");
                    String codeToRemove = scanf.nextLine();
                    remove_item(codeToRemove);  // Remove the item sa cart all quantity
                    break;
                case "D":
                case "d":
                    System.out.print("Enter product code to deduct: ");
                    String codeToDeduct = scanf.nextLine();
                    System.out.print("Enter quantity to deduct: ");
                    int quantityToDeduct = scanf.nextInt();
                    deduct_item_quantity(codeToDeduct, quantityToDeduct);  // Deduct quantity
                    break;
                case "C":
                case "c":
                    reset_cart();  // Reset the cart
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
                    // Confirmation before checkout
                    System.out.print("\n\tAre you sure you want to proceed to checkout? (Y/N): ");
                    String confirmInput = scanf.nextLine().trim();

                    if (!confirmInput.isEmpty() && (confirmInput.charAt(0) == 'Y' || confirmInput.charAt(0) == 'y')) {
                        System.out.println("\n\tProcessing checkout...");
                        System.out.println("\n\tChoose payment method:");
                        System.out.println("\t1. Cash (Queue Number)");
                        System.out.println("\t2. E-Wallet (Self-Checkout)");
                        System.out.print("\tEnter choice: ");
                        String paymentChoice = scanf.nextLine().trim();

                        if (paymentChoice.equals("1")) {
                            // Existing cash payment process
                            save_cart_to_csv();
                            reset_cart();

                            /*
                            dapat hindi muna auto logout
                            so may notif sa dashboard na proceed sya sa cashier or may option na logout and cashier
                            at may confirmations din
                            */

                            UserCustomer user_customer = new UserCustomer();
                            user_customer.user_customer_menu();
                            return;
                        } else if (paymentChoice.equals("2")) {
                            // New e-wallet self-checkout process
                            UserCustomer userCustomer = new UserCustomer(); // Create a new UserCustomer instance
                            SelfCheckout selfCheckout = new SelfCheckout(userCustomer, cart);
                            selfCheckout.processSelfCheckout(username);
                            return;
                        } else {
                            System.out.println("\n\tAn error has occurred. Returning to menu.");
                            System.out.println("\t\tPress Enter key to continue.\n");
                            scanf.nextLine(); //used for press any key to continue
                        }
                    } else {
                        System.out.println("\n\tCheckout cancelled.");
                    }
                    break;
            }
        }
    }


    public static void deduct_item_quantity(String product_code, int quantity) {
        for (Product product : cart) {
            if (product.getCode().equals(product_code.toUpperCase())) {
                int new_quantity = product.getStock() - quantity;
                if (new_quantity > 0) {
                    product.update_stock(-quantity);
                    total_items -= quantity;
                    total_price -= product.getPrice() * quantity;
                    System.out.printf("Deducted %d of %s from the cart.\n", quantity, product.getName());
                } else {
                    remove_item(product_code);
                }
                return;
            }
        }
        System.out.println("Product not found in the cart.");
    }


    public static void remove_item(String product_code) {
        Product to_remove = null;
        for (Product product : cart) {
            if (product.getCode().equals(product_code.toUpperCase())) {
                total_price-= product.getStock();
                total_price -= product.getPrice() * product.getStock();
                to_remove = product;
                break;
            }
        }
        if (to_remove != null) {
            cart.remove(to_remove);
            System.out.printf("Removed %s from the cart.\n", to_remove.getName());
        } else {
            System.out.println("Product not found in the cart.");
        }
    }

    public static void reset_cart() {
        cart.clear();
        total_items = 0;
        total_price = 0;
        System.out.println("The cart has been reset.");
    }

    public static void reset_cart_no_display() {
        cart.clear();
        total_items = 0;
        total_price = 0;
    }


    public static void save_cart_to_csv() {
        Scanner scanf = new Scanner(System.in);
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Nothing to save.");
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
            System.out.println("\t�                                                                                      �\n");
            System.out.printf ("\t�                            Your queue number is: %d                                  �\n", currentQueueNumber - 1);
            System.out.println("\t�                           Cart saved to " + fileName);
            System.out.println("\t�                                                                                      �");
            System.out.println("\t�                                                                                      �");
            System.out.println("\t�Thank you for ordering! Please remember your queue number and proceed to the cashier. �");
            System.out.println("\t����������������������������������������������������������������������������������������\n\n");

            System.out.println("\nType and Enter any key to continue...");
            scanf.next(); // Read any input from the user

            // Save the updated queue number
            save_queue_number();

        } catch (IOException e) {
            System.out.println("An error occurred while saving the cart: " + e.getMessage());
        }
    }


    private static String generate_queue_file_name() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String formattedDate = dateFormat.format(new Date());
        String fileName = String.format("queue_number_%d_%s.csv", currentQueueNumber, formattedDate);

        currentQueueNumber++; // Increment the queue number for the next file
        save_queue_number(); // Save the updated queue number

        return fileName;
    }


    private static String generate_order_id() {
        // This is a simple implementation. You might want to use a more robust system
        return "ORD" + System.currentTimeMillis();
    }

    // Method to initialize the queue number by reading from a file
    public static void initialize_queue_number() {
        try (BufferedReader reader = new BufferedReader(new FileReader(QUEUE_NUMBER_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                currentQueueNumber = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            // If file doesn't exist or is invalid, start from 1
            currentQueueNumber = 1;
        }
    }

    // Method to save the current queue number to a file
    private static void save_queue_number() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(QUEUE_NUMBER_FILE))) {
            writer.println(currentQueueNumber);
        } catch (IOException e) {
            System.out.println("Error saving queue number: " + e.getMessage());
        }
    }



}
