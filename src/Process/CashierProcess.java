package Process;

import Employee.Cashier;
import Shopper.Customer;
import Shopper.Product;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class CashierProcess extends OrderProcessor {
    private final List<Product> counter;
    private static int currentReceiptNumber = 1;
    private String currentQueueOrderFile; // New field to store the current queue order file name para nasa track kung ano d-delete

    public CashierProcess() {
        super(); // hinihiram lahat ng mga asa initialization na meron si OrderProcessor dahil sya ang parent
        counter = new ArrayList<>();
        initialize_receipt_number();
    }


    public void read_order_from_csv(String filename) {
        this.currentQueueOrderFile = "queues/" + filename;
        File file = new File(this.currentQueueOrderFile);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first_line = true;
            while ((line = reader.readLine()) != null) {
                if (first_line) {
                    first_line = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length > 4) {
                    String product_code = parts[0];
                    String product_name = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    // part 4 ung subtotal sa csv
                    Product product = new Product(product_code, product_name, price, quantity);
                    add_to_cart(product, quantity);
                }
            }
        } catch (IOException e) {
            System.out.println("\tError reading CSV file: " + e.getMessage());
        }
    }


    public void transfer_cart_to_counter() {
        counter.clear();
        counter.addAll(cart);
        reset_cart_no_display();
    }


    public boolean process_payment(Cashier cashier) {
        Scanner scanf = new Scanner(System.in);
        String retryChoice;

        while (true) {
            display_counter();
            double totalPrice = calculate_total_price();
            System.out.printf("\tTotal Amount to Pay: %.2f\n", totalPrice);
            System.out.println("\tDo you want to proceed with the payment?\n");
            System.out.println("\t[1] Yes");
            System.out.println("\t[2] Yes & apply a discount coupon");
            System.out.println("\n\t[0] Cancel");
            System.out.print("\tEnter choice: ");
            String choice = scanf.nextLine();

            switch (choice) {
                case "1":
                    if (proceed_to_payment(totalPrice, cashier)) {
                        return true;
                    }
                    break;
                case "2":
                    while (true) {
                        System.out.print("\n\tEnter coupon code: ");
                        String couponCode = scanf.nextLine();
                        CouponManager coupon_manager = new CouponManager();
                        double discountedPrice = coupon_manager.apply_coupon(couponCode, totalPrice);

                        if (discountedPrice == -1) { // Invalid code or coupon exhausted
                            while (true) {
                                System.out.println("\n\tWould you like to try another coupon?");
                                System.out.println("\t[1] Yes");
                                System.out.println("\t[0] No, proceed without coupon");
                                System.out.print("\tEnter choice: ");
                                retryChoice = scanf.nextLine();

                                if (retryChoice.equals("1")) {
                                    break; // Break out of the retry choice loop to allow another coupon input
                                } else if (retryChoice.equals("0")) {
                                    break; // Exit the coupon application loop
                                } else {
                                    System.out.println("\tInvalid choice. Please enter [1] or [0].");
                                    System.out.print("\t\tPress Enter key to continue.");
                                    scanf.nextLine();
                                }
                            }

                            if (retryChoice.equals("0")) {
                                break; // Exit the outer coupon loop
                            }
                        }

                        else {
                            totalPrice = discountedPrice; // Apply the valid discount
                            if (proceed_to_payment(totalPrice, cashier)) {
                                return true;
                            }
                            break;
                        }
                    }
                    break;
                case "0":
                    return false; // Cancel payment process
                default:
                    System.out.println("\tInvalid choice. Please try again.");
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine();
            }
        }
    }



    public boolean proceed_to_payment (double totalPrice, Cashier cashier) {
        Scanner scanf = new Scanner(System.in);
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

        if (payment >= totalPrice) {
            System.out.println("\tPayment accepted.");
            if (payment > calculate_total_price()) {
                System.out.printf("\tChange: %.2f\n", payment - totalPrice);
            }

            update_sales_report(counter);

            update_all_stocks(counter);
            print_receipt(counter, payment, cashier);

            counter.clear();
            return true; // Return true to indicate successful payment para balik dashboard
        } else {
            System.out.println("\tInsufficient payment. Try again.");
            System.out.println("\t\tPress Enter key to continue.\n");
            scanf.nextLine(); //used for press any key to continue
        }
        return false;
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
    private void print_receipt(List<Product> counter, double payment, Cashier cashier) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\n\t----- RECEIPT -----");
        System.out.println("\tCashier Name," + cashier.getEmployee_full_name());
        System.out.println("\tDate," + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        System.out.println("\n\tCODE\t\tProduct Name\t Quantity  Price");

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
        System.out.print("\tPress Enter key to Save & Exit: ");
        scanf.nextLine(); //used for press any key to continue

        cashier_save_receipt_to_csv(counter, calculate_total_price(), payment, cashier);
        System.out.println("\t\tPress Enter key to continue.\n");
        scanf.nextLine(); //used for press any key to continue
        counter.clear();
    }


    private void cashier_save_receipt_to_csv(List<Product> counter, double totalPrice, double payment, Cashier cashier) {
        if (counter.isEmpty()) {
            System.out.println("No items in the counter. Nothing to save.");
            return;
        }

        String fileName = "receipts/" + cashier_generate_receipt_file_name(cashier);
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write header with cashier information
            writer.println("Cashier Name," + cashier.getEmployee_full_name());
            writer.println("Date," + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            writer.println("Code,Name,Quantity,Price");

            for (Product product : counter) {
                writer.printf("%s,%s,%d,%.2f%n",
                        product.getCode(),
                        product.getName(),
                        product.getStock(),
                        product.getPrice());
            }

            writer.printf("%nTotal Items: ,%d%n", calculate_total_items());
            writer.printf("Total Price: ,%.2f%n", calculate_total_price());
            writer.printf("Payment: ,%.2f%n", payment);

            if (payment > totalPrice) {
                writer.printf("Change: ,%.2f%n", payment - totalPrice);
            }
            System.out.println("\n\tReceipt successfully saved to " + fileName);
            delete_queue_order_file();
        } catch (IOException e) {
            System.out.println("Error saving the receipt: " + e.getMessage());
        }
    }


    public void self_checkout_save_receipt_to_csv(List<Product> counter, double totalPrice, double payment, Customer customer) {
        if (counter.isEmpty()) {
            System.out.println("No items in the counter. Nothing to save.");
            return;
        }

        String fileName = "receipts/" + self_checkout_generate_receipt_file_name(customer);
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write header with customer information
            writer.println("Username,Payment method,Balance");
            writer.println(customer.getUsername() + "," + customer.getPaymentMethod() + "," +customer.getBalance());
            writer.println("Date," + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            writer.println("Code,Name,Quantity,Price");

            for (Product product : counter) {
                writer.printf("%s,%s,%d,%.2f%n",
                        product.getCode(),
                        product.getName(),
                        product.getStock(),
                        product.getPrice());
            }

            writer.printf("%nTotal Items: ,%d%n", calculate_total_items());
            writer.printf("Total Price: ,%.2f%n", calculate_total_price());
            writer.printf("Payment: ,%.2f%n", payment);

            if (payment > totalPrice) {
                writer.printf("Change: ,%.2f%n", payment - totalPrice);
            }
            System.out.println("\n\tReceipt successfully saved to " + fileName);
            delete_queue_order_file();
        } catch (IOException e) {
            System.out.println("Error saving the receipt: " + e.getMessage());
        }
    }


    private String cashier_generate_receipt_file_name(Cashier cashier) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String formattedDate = dateFormat.format(new Date());
        String fileName = String.format("receipt_number_%d_%s_Cashier_%s.csv", currentReceiptNumber, formattedDate, cashier.getEmployee_username());

        currentReceiptNumber++; // Increment the queue number for the next file
        save_receipt_number(); // Save the updated queue number

        return fileName;
    }


    private String self_checkout_generate_receipt_file_name(Customer customer) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String formattedDate = dateFormat.format(new Date());
        String fileName = String.format("receipt_number_%d_%s_Customer_%s.csv", currentReceiptNumber, formattedDate, customer.getUsername());

        currentReceiptNumber++; // Increment the queue number for the next file
        save_receipt_number(); // Save the updated queue number

        return fileName;
    }


    // Method to initialize the resibo number by reading from a file
    public static void initialize_receipt_number() {
        try (BufferedReader reader = new BufferedReader(new FileReader("receipts/current_receipt_number.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                currentReceiptNumber = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            // If file doesn't exist or is invalid, start from 1
            currentReceiptNumber = 1;
        }
    }


    // Method to save the current receipt number to a file
    private static void save_receipt_number() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("receipts/current_receipt_number.txt"))) {
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


    // Helper method to find and update product stock across multiple CSV files
    private boolean update_product_stock_in_files(String productCode, int quantityToDeduct) {
        String[] csvFiles = {
                "beverages.csv", "snacks.csv", "canned_fish.csv", "canned_meat.csv",
                "condiments.csv", "dairy.csv", "frozen_foods.csv", "body_care.csv",
                "beauty_care.csv", "powder_detergent.csv", "bar_soaps.csv", "liquid_soaps.csv"
        };

        // Iterate through all CSV files and update stock where the product is found
        for (String csvFile : csvFiles) {
            try {
                List<String> lines = new ArrayList<>();
                boolean productFound = false;

                try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                    String headerLine = reader.readLine();
                    lines.add(headerLine); // Preserve the header

                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] values = line.split(",");
                        if (values[0].equals(productCode)) {
                            // Found the product, update its stock
                            int currentStock = Integer.parseInt(values[3]);
                            int newStock = currentStock - quantityToDeduct;
                            newStock = Math.max(newStock, 0); // Prevent negative stock
                            values[3] = String.valueOf(newStock);
                            productFound = true;
                        }
                        lines.add(String.join(",", values));
                    }
                }

                if (productFound) {
                    // Write updated content back to the file
                    try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
                        for (String line : lines) {
                            writer.println(line);
                        }
                    }
                    return true; // Product found and updated, stop checking other files
                }
            } catch (IOException e) {
                System.out.println("Error reading/writing CSV file: " + csvFile + " - " + e.getMessage());
            }
        }

        // Product was not found in any file
        return false;
    }

    // Update stock for all products in the array of cart/counter
    public void update_all_stocks(List<Product> products_to_update) {
        for (Product product : products_to_update) {
            boolean updated = update_product_stock_in_files(product.getCode(), product.getStock());
            if (!updated) {
                System.out.println("Product not found: " + product.getCode());
            }
        }
    }


    public void update_sales_report(List<Product> soldProducts) {
        String filename = "products/sales_report.csv";
        int currentTotalItems = 0;
        double currentTotalSales = 0.0;
        List<String> existingEntries = new ArrayList<>();

        // Read existing sales report to collect only product entries
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Only keep non-total lines
                if (!line.startsWith("TOTAL_ITEMS")) {
                    existingEntries.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("\tError reading the sales report. Proceeding with new file...");
        }

        // Write all entries including new products and updated totals
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());

            // Write existing entries first
            for (String entry : existingEntries) {
                writer.println(entry);
                // Re-calculate total items and sales from existing entries
                if (!entry.startsWith("TOTAL_ITEMS") && entry.split(",").length > 2) {
                    try {
                        int itemCount = Integer.parseInt(entry.split(",")[2]);
                        double productSales = Double.parseDouble(entry.split(",")[4]);
                        currentTotalItems += itemCount;
                        currentTotalSales += productSales;
                    } catch (NumberFormatException e) {
                        // Skip if parsing fails
                    }
                }
            }

            // Write new product entries
            for (Product product : soldProducts) {
                double productTotalSales = product.getPrice() * product.getStock();
                writer.printf("%s,%s,%d,%.2f,%.2f,%s%n",
                        product.getCode(),
                        product.getName(),
                        product.getStock(),
                        product.getPrice(),
                        productTotalSales,
                        currentDateTime
                );
                currentTotalItems += product.getStock();
                currentTotalSales += productTotalSales;
            }

            // Append new totals at the end
            writer.printf("TOTAL_ITEMS,%d,TOTAL_SALES,%.2f,%s%n",
                    currentTotalItems,
                    currentTotalSales,
                    currentDateTime
            );

            System.out.printf("\tSales report updated: Total Items = %d, Total Sales = %.2f\n",
                    currentTotalItems,
                    currentTotalSales
            );
        } catch (IOException e) {
            System.out.println("\tError updating the sales report: " + e.getMessage());
        }
    }


    public void increase_item_quantity_counter(String product_code, int quantity) {
        for (Product product : counter) {
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


    public void deduct_item_quantity_counter(String product_code, int quantity) {
        for (Product product : counter) {
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


    public void remove_item_counter(String product_code) {
        Product to_remove = null;
        for (Product product : counter) {
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


}
