package Process;

import Shopper.Product;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class CashierProcess extends OrderProcessor {
    private final List<Product> counter;
    private static int currentReceiptNumber = 1;
    private static final String RECEIPT_NUMBER_FILE = "current_receipt_number.txt";

    public CashierProcess() {
        super(); // hinihiram lahat ng mga asa initialization na meron si OrderProcessor dahil sya ang parent
        counter = new ArrayList<>();
        initialize_receipt_number();
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
        counter.addAll(cart);
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
                    System.out.println("\tEnter rendered amount: ");
                    double payment = scanf.nextDouble();

                    if (payment >= calculate_total_price()) {
                        System.out.println("\tPayment accepted.\n");
                        if (payment > calculate_total_price()) {
                            System.out.printf("\tChange: %.2f\n", payment - calculate_total_price());
                        }

                        // Print receipt after successful payment
                        print_receipt(counter, calculate_total_items(), calculate_total_price(), payment);

                        // Reset the counter and go back to queue selection
                        counter.clear();
                        return;
                    } else {
                        System.out.println("\tInsufficient payment. Try again.");
                    }
                    break;
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


    // Method to print the receipt
    private void print_receipt(List<Product> counter, int total_items, double total_price, double payment) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\n----- RECEIPT -----");
        System.out.println("CODE\t\tProduct Name\t Quantity  Price");

        for (Product product : counter) {
            System.out.printf("%-8s \t%-24s \t  x%d  \t%5.2f\n",
                    product.getCode(), product.getName(), product.getStock(), product.getPrice() * product.getStock());
        }

        System.out.println("----------------------------------------");
        System.out.printf("Total Price: %.2f\n", total_price);
        System.out.printf("Payment: %.2f\n", payment);

        if (payment > total_price) {
            System.out.printf("Change: %.2f\n", payment - total_price);
        }
        System.out.println("------------------------------------------");
        System.out.print("\tDo you want to go back to cashier menu (press any key) or Save & Exit (E): ");

        String choice = scanf.nextLine();
        if (choice.equalsIgnoreCase("E")) {
            save_receipt_to_csv(counter, total_items, total_price, payment);
            counter.clear();
            //cashier_process_choice(); // Return to cashier process menu
        } else {
            System.out.println("\tExiting...");
        }
    }


    // Dummy method for saving the receipt to a CSV file
    public static void save_receipt_to_csv(List<Product> counter, int totalItems, double totalPrice, double payment) {
        if (counter.isEmpty()) {
            System.out.println("No items in the counter. Nothing to save.");
            return;
        }

        String fileName = generate_receipt_file_name();

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write header
            writer.println("Code,Name,Quantity,Price");

            // Write each product's details
            for (Product product : counter) {
                writer.printf("%s,%s,%d,%.2f%n",
                        product.getCode(),
                        product.getName(),
                        product.getStock(),
                        product.getPrice());
            }

            // Write total price and payment details
            writer.printf("%nTotal Price: ,%.2f%n", totalPrice);
            writer.printf("Payment: ,%.2f%n", payment);

            // Calculate and write change if needed
            if (payment > totalPrice) {
                writer.printf("Change: ,%.2f%n", payment - totalPrice);
            }

            System.out.println("\nReceipt successfully saved to " + fileName);

        } catch (IOException e) {
            System.out.println("Error saving the receipt: " + e.getMessage());
        }
    }


    private static String generate_receipt_file_name() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String formattedDate = dateFormat.format(new Date());
        String fileName = String.format("queue_number_%d_%s.csv", currentReceiptNumber, formattedDate);

        // Increment the queue number for the next file
        currentReceiptNumber++;
        save_receipt_number(); // Save the updated queue number

        return fileName;
    }

    // Method to initialize the queue number by reading from a file
    public static void initialize_receipt_number() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RECEIPT_NUMBER_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                currentReceiptNumber = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            // If file doesn't exist or is invalid, start from 1
            currentReceiptNumber = 1;
        }
    }

    // Method to save the current queue number to a file
    private static void save_receipt_number() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RECEIPT_NUMBER_FILE))) {
            writer.println(currentReceiptNumber);
        } catch (IOException e) {
            System.out.println("Error saving queue number: " + e.getMessage());
        }
    }

}