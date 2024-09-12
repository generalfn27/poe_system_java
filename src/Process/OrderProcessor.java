package Process;

import Shopper.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderProcessor {
    private List<Product> cart;
    private int total_items;
    private double total_price;

    public OrderProcessor() {
        cart = new ArrayList<>();
        total_items = 0;
        total_price = 0;
    }

    public void process_customer_order(List<Product> products) {
        Scanner scanf = new Scanner(System.in);
        String product_code;
        int quantity = 0;
        Product selected_product;

        do {
            System.out.println("\nEnter product code: ");
            product_code = scanf.nextLine();

            //find product code kung nag eexist
            selected_product = find_product_code(product_code, products);

            if (selected_product == null) {
                System.out.println("Invalid product code. Try again.");
                System.out.println("Press Enter to continue...");
                scanf.nextLine(); // Wait for user input
            }
        } while (selected_product == null);

        //display ung detalye ng product na pinili
        display_product_details(selected_product);

        //quantity check at error handling din kung kulang or sobra order ng kupal
        boolean valid_input;
        do {
            System.out.println("Enter quantity: ");
            if (scanf.hasNextInt()) {
                quantity = scanf.nextInt();
                valid_input = true;
            } else {
                System.out.println("Invalid input. Please enter a valid quantity.");
                scanf.next();
                valid_input = false;
            }
        } while (!valid_input);
        
        System.out.printf("Price: %.2f\n", selected_product.getPrice() * quantity);

        //tatanong kung sigurado ba ung kupal pag may pangbili sya
        System.out.print("Add to cart (A) or cancel (C)? ");
        char choice = scanf.next().charAt(0);

        if (choice == 'A' || choice == 'a') {
            add_to_cart(selected_product, quantity);
            System.out.println("Item added to cart.");
            display_cart();
        } else if (choice == 'C' || choice == 'c') {
            System.out.println("Item not added to cart.");
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
        System.out.printf("Product: %-22s\tPrice: %6.2f\tStock: %4d\n",
                product.getName(), product.getPrice(), product.getStock());}

    private void add_to_cart(Product product, int quantity) {
        //babawasan ung stock at dagdagan ung asa cart pero need pa ayusin kasi baka d mag update ung csv
        product.update_stock(-quantity);  // Adjust stock in the Product class
        total_items += quantity;
        total_price += product.getPrice() * quantity;

        cart.add(new Product(product.getCode(), product.getName(), product.getPrice(), quantity));
    }

    public void display_cart() {
        System.out.println("\nYour Cart:");
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            for (Product product : cart) {
                System.out.printf("Product Code: %-10s Name: %-20s Quantity: %d Price: %.2f\n",
                        product.getCode(), product.getName(), product.getStock(), product.getPrice());
            }
            System.out.printf("Total Items: %d\n", total_items);
            System.out.printf("Total Price: %.2f\n", total_price);
        }
    }

    public void modify_menu_process() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("\tAdd more items (A)\n");
            System.out.println("\tRemove Items (R)\n");
            System.out.println("\tDeduct Quantity(D)\n");
            System.out.println("\tClear Cart(C)\n");
            System.out.println("\tProceed to checkout (P)\n");
            System.out.println("\tGo Back (B)\n");
            System.out.println("\n\tEnter choice: ");

            choice = scanf.nextLine();

            switch (choice) {
                case "A":
                case "a":
                    //guest_customer_item_category();
                    break;
                case "R":
                case "r":
                    //view_cart(cart, *total_items, *total_price);
                    //ask_remove(cart, total_items, total_price);
                    break;
                case "D":
                case "d":
                    //view_cart(cart, *total_items, *total_price);
                    //ask_deduct(cart, total_items, total_price);
                    break;
                case "C":
                case "c":
                    //reset_cart(cart, total_items, total_price);
                    //view_cart(cart, *total_items, *total_price);
                    break;
                case "B":
                case "b":
                    //guest_customer_item_category();
                    break;
                case "P":
                case "p":
                    // Confirmation before checkout
                    String confirm_choice;
                    System.out.println("\n\tAre you sure you want to proceed to checkout? (Y/N): ");
                    confirm_choice = scanf.nextLine();

                    if (confirm_choice == "Y" || confirm_choice == "y") {
                        // Proceed to checkout logic
                        System.out.println("\n\tProcessing checkout...\n");
                        //callout sa process queue function
                    } else {
                        System.out.println("\n\tCheckout cancelled.\n");
                    }
                    break;
            }
        }

    }

}
