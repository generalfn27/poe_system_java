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
    private String currentQueueOrderFile; // New field to store the current queue order file name para nasa track kung ano d-delete

    public CashierProcess() {
        super(); // hinihiram lahat ng mga asa initialization na meron si OrderProcessor dahil sya ang parent
        counter = new ArrayList<>();
        initialize_receipt_number();
    }


    public void read_order_from_csv(String filename) {
        this.currentQueueOrderFile = filename; // Store the filename
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
        counter.clear();
        counter.addAll(cart);
        reset_cart_no_display();
    }


    public void process_payment() {
        Scanner scanf = new Scanner(System.in);
        display_counter();
        // Add logic for accepting payment, calculating change, etc.

        while (true) {
            System.out.printf("\n\tTotal Amount need Pay: %.2f\n", calculate_total_price());
            System.out.println("\tDo you want to proceed with the payment?\n");
            System.out.println("\t[1] Yes");
            System.out.println("\t[0] Cancel");
            System.out.print("\tEnter choice: ");
            String choice = scanf.nextLine();

            switch (choice) {
                case "1":
                    double payment = 0;
                    boolean validInput = false;

                    // Keep asking until a valid number is entered
                    while (!validInput) {
                        try {
                            System.out.print("\n\tEnter rendered amount: ");
                            payment = Float.parseFloat(scanf.nextLine());
                            validInput = true; // Input is valid, exit the loop
                        } catch (NumberFormatException e) {
                            System.out.println("\tInvalid input. Please enter a valid number.");
                        }
                    }

                    if (payment >= calculate_total_price()) {
                        System.out.println("\tPayment accepted.");
                        if (payment > calculate_total_price()) {
                            System.out.printf("\tChange: %.2f\n", payment - calculate_total_price());
                        }
                        // Print receipt after successful payment
                        print_receipt(counter, payment);

                        // Reset the counter and go back to queue selection
                        counter.clear();
                        return;
                    } else {
                        System.out.println("\tInsufficient payment. Try again.");
                        System.out.println("\t\tPress Enter key to continue.\n");
                        scanf.nextLine(); //used for press any key to continue
                    }
                    break;
                case "0":
                    //di ko sure kung reset ba or go back nalang sa last function bukas ko na alamin
                    return;
            }
        }

    }


    public void display_counter() {
        System.out.println("\n\tItems at the counter:");
        for (Product product : counter) {
            System.out.printf("\tProduct Code: %-6s\tName: %-20s\tQuantity: %d\tPrice: %.2f\n",
                    product.getCode(), product.getName(), product.getStock(), product.getPrice());
        }
        System.out.printf("\n\tTotal Items: %d\n", calculate_total_items());
        System.out.printf("\tTotal Price: %.2f\n\n", calculate_total_price());
    }


    int calculate_total_items() {
        int total = 0;
        for (Product product : counter) {
            total += product.getStock();
        }
        return total;
    }

    double calculate_total_price() {
        double total = 0;
        for (Product product : counter) {
            total += product.getPrice() * product.getStock();
        }
        return total;
    }


    // Method to print the receipt
    private void print_receipt(List<Product> counter, double payment) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\n\t----- RECEIPT -----");
        System.out.println("\tCODE\t\tProduct Name\t Quantity  Price");

        for (Product product : counter) {
            System.out.printf("\t%-8s \t%-24s \t\t\t  x%d  \t%5.2f\n",
                    product.getCode(), product.getName(), product.getStock(), product.getPrice() * product.getStock());
        }

        System.out.println("\t----------------------------------------");
        System.out.printf("\n\tTotal Items: %d\n", calculate_total_items());
        System.out.printf("\tTotal Price: %.2f\n", calculate_total_price());

        if (payment > calculate_total_price()) {
            System.out.printf("\tChange: %.2f\n", payment - calculate_total_price());
        }
        System.out.println("\t------------------------------------------");
        System.out.print("\tPress any key to Save & Exit: ");
        scanf.nextLine(); //used for press any key to continue

        save_receipt_to_csv(counter, calculate_total_price(), payment);
        System.out.println("\t\tPress Enter key to continue.\n");
        scanf.nextLine(); //used for press any key to continue
        counter.clear();
    }


    // Dummy method for saving the receipt to a CSV file
    public void save_receipt_to_csv(List<Product> counter, double totalPrice, double payment) {
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

            writer.printf("%nTotal Price: ,%.2f%n", totalPrice);
            writer.printf("Payment: ,%.2f%n", payment);

            // Calculate and write change if needed
            if (payment > totalPrice) {
                writer.printf("Change: ,%.2f%n", payment - totalPrice);
            }
            System.out.println("\n\tReceipt successfully saved to " + fileName + "\n\n");
            delete_queue_order_file();
        } catch (IOException e) {
            System.out.println("Error saving the receipt: " + e.getMessage());
        }
    }


    private static String generate_receipt_file_name() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String formattedDate = dateFormat.format(new Date());
        String fileName = String.format("receipt_number_%d_%s.csv", currentReceiptNumber, formattedDate);

        // Increment the queue number for the next file
        currentReceiptNumber++;
        save_receipt_number(); // Save the updated queue number

        return fileName;
    }


    // Method to initialize the resibo number by reading from a file
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


    // Method to save the current resibo number to a file
    private static void save_receipt_number() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RECEIPT_NUMBER_FILE))) {
            writer.println(currentReceiptNumber);
        } catch (IOException e) {
            System.out.println("Error saving queue number: " + e.getMessage());
        }
    }


    private void delete_queue_order_file() {
        if (currentQueueOrderFile != null) {
            File file = new File(currentQueueOrderFile);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("\tQueue order file " + currentQueueOrderFile + " has been deleted successfully.");
                } else {
                    System.out.println("\tFailed to delete the queue order file " + currentQueueOrderFile);
                }
            } else {
                System.out.println("\tQueue order file " + currentQueueOrderFile + " does not exist.");
            }
            currentQueueOrderFile = null; // Reset the file name after deletion attempt
        }
    }

}
