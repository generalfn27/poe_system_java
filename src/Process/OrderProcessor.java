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
        Product selected_product = null;

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



}
