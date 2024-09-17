package Process;

import Shopper.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderProcessor {
    private static List<Product> cart;
    private static int total_items;
    private static double total_price;

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

    public static void display_cart() {
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

    public static void modify_menu_process() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            display_cart();
            System.out.println("\n\tAdd more items (A)");
            System.out.println("\tRemove Items (R)");
            System.out.println("\tDeduct Quantity (D)");
            System.out.println("\tClear Cart (C)");
            System.out.println("\tProceed to checkout (P)");
            System.out.println("\tDisplay cart(V)");
            System.out.println("\tGo Back (B)");
            System.out.println("\n\tEnter choice: ");

            choice = scanf.nextLine();

            switch (choice) {
                case "A":
                case "a":
                    //UserCustomer.guest_customer_item_category(guest);
                    break;
                case "R":
                case "r":
                    System.out.print("Enter product code to remove: ");
                    String codeToRemove = scanf.nextLine();
                    remove_item(codeToRemove);  // Remove the item
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
                    // Checkout logic here or queue card muna tapos ang algorithm ay queue syempre
                    break;
            }
        }
    }

    public static void deduct_item_quantity(String product_code, int quantity) {
        for (Product product : cart) {
            if (product.getCode().equals(product_code)) {
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
            if (product.getCode().equals(product_code)) {
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




}