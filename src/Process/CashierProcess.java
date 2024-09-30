package Process;

import Shopper.Product;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class CashierProcess extends OrderProcessor {
    private final List<Product> counter;

    public CashierProcess() {
        super();
        counter = new ArrayList<>();
    }


    public void read_order_from_csv(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean first_line = true;
            while ((line = reader.readLine()) != null) {
                if (first_line) {
                    first_line = false;
                    continue; // skip header or field
                }
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    // part[0] ay ung order id
                    String product_code = parts[1];
                    String product_name = parts[2];
                    int quantity = Integer.parseInt(parts[3]);
                    double price = Double.parseDouble(parts[4]);
                    // part[5] ay ung subtotal
                    Product product = new Product(product_code, product_name, price, quantity);
                    add_to_cart(product, quantity);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }
    public void transfer_cart_to_counter() {
        counter.addAll(super.cart);
        reset_cart_no_display();
    }


    public void process_payment() {
        Scanner scanf = new Scanner(System.in);
        display_counter();
        // Add logic for accepting payment, calculating change, etc.

        while (true) {
            System.out.printf("\nTotal Amount need Pay: %.2f\n", calculate_total_price());
            System.out.println("\tDo you want to proceed with the payment?\n");
            System.out.println("\\t[1] Yes");
            System.out.println("[0] Cancel");
            System.out.print("Enter choice: ");
            String choice = scanf.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("\tEnter rendered amount");
                    Double payment = scanf.nextDouble();

                    if (payment >= calculate_total_price()) {
                        System.out.println("\tPayment accepted.\n");
                    if (payment > calculate_total_price()) {
                        System.out.printf("\tChange: %.2f\n", payment - calculate_total_price());
                    }
                    // Print receipt after successful payment
                    //print_receipt(counter, total_items, *total_price, payment);
                        //after generating ng resibo dapat uupdate sa audit sales
                        // at update ng inventory stock ibabawas ung mga items
                        // save resibo at pupunta na
                        // ulit sa menu kung saan mamimili anong next queue number aasikasuhin
                }
                case "0":
                    //di ko sure kung reset ba or go back nalang sa last function bukas ko na alamin
            }
        }

    }


    private void display_counter() {
        System.out.println("\nItems at the counter:");
        for (Product product : counter) {
            System.out.printf("Product Code: %-10s Name: %-20s Quantity: %d Price: %.2f\n",
                    product.getCode(), product.getName(), product.getStock(), product.getPrice());
        }
        System.out.printf("\nTotal Items: %d\n", calculate_total_items());
        System.out.printf("Total Price: %.2f\n", calculate_total_price());
    }


    private int calculate_total_items() {
        int total = 0;
        for (Product product : counter) {
            total += product.getStock();
        }
        return total;
    }

    private double calculate_total_price() {
        double total = 0;
        for (Product product : counter) {
            total += product.getPrice() * product.getStock();
        }
        return total;
    }

    public List<Product> getCounter() {
        return counter;
    }

}