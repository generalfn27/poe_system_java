package Employee;

import Shopper.Product;
import Shopper.UserCustomer;
import User_Types.UserType;
import Process.BrowseProduct;
import Process.CouponManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Manager {
    private static final int MAX_ATTEMPTS = 3; // Maximum login attempts
    private String currentCategoryFile;
    public CouponManager couponManager;

    public void user_manager() {
        this.couponManager = new CouponManager();
        manager_login();
    }


    private void manager_login() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }
        int attempt_count = 0;
        boolean valid = false;

        while (!valid) {
            System.out.println("\n\t===================================");
            System.out.println("\t|          Manager Login          |");
            System.out.println("\t===================================");
            System.out.println("\t[1] Login");
            System.out.println("\t[2] Forgot Password");
            System.out.println("\n\t[0] Exit");
            System.out.print("\n\tEnter choice: ");

            String choice = console.readLine();

            switch (choice) {
                case "1":
                    System.out.print("\n\tEnter username: ");
                    String inputUsername = console.readLine();
                    System.out.print("\tEnter password: ");
                    char[] passwordArray = console.readPassword();
                    String inputPassword = new String(passwordArray);

                    if (validate_manager_login(inputUsername, inputPassword)) {
                        valid = true;
                        System.out.println("\tLogin successful!");
                        manager_dashboard();
                    } else {
                        attempt_count++;
                        System.out.println("\tInvalid login attempt #" + attempt_count);

                        if (attempt_count >= MAX_ATTEMPTS) {
                            System.out.println("\tMaximum attempts reached.");
                            return;
                        }
                    }
                    break;
                case "2":
                    ManagerCredentials.recover_manager_credentials();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("\tInvalid choice!");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }
        }
    }


    private boolean validate_manager_login(String inputUsername, String inputPassword) {
        return ManagerCredentials.validate_manager_login(inputUsername, inputPassword);
    }


    private boolean handle_logout(Console console) {
        while (true) {
            System.out.println("\n\n\tAre you sure you want to Logout and go back to menu?\n");
            System.out.print("\t[Y] for Yes  [N] for No: ");

            String exit_confirmation = console.readLine().trim();
            console.readLine();
            console.flush();

            if (exit_confirmation.equalsIgnoreCase("Y")) {
                UserType.user_type_menu();
                return true;
            } else if (exit_confirmation.equalsIgnoreCase("N")) {
                return false;
            }
        }
    }


    private void manager_dashboard() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        String choice;

        while (true) {
            String storeName = ManagerCredentials.getStoreName();
            System.out.println("\n\t=======================================");
            System.out.println("\t|                                     |");
            System.out.printf ("\t|     %-11s  Manager Dashboard  |\n", storeName);
            System.out.println("\t|                                     |");
            System.out.println("\t=======================================\n");
            System.out.println  ("\t[1] Voucher code / Promotions");
            System.out.println("\t[2] Sales report"); //display total sales at recent total transactions
            System.out.println("\t[3] Purchase/Transaction History"); //same sa personal account
            System.out.println("\t[4] Inventory Report"); //refill stocks
            System.out.println("\t[5] HR Management"); //sino mga employees at handle ng account nila change pass/delete acc
            System.out.println("\t[6] Customer Account Management"); //account retrieval at delete account
            System.out.println("\t[7] Manager Account Management");
            System.out.println("\t[0] Exit");

            System.out.print("\n\n\tEnter Here: ");
            choice = console.readLine().trim();

            switch (choice) {
                case "1":
                    voucher_management_dashboard();
                    break;
                case "2":
                    display_sales_report();
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
                    break;
                case "3":
                    display_purchase_history_menu();
                    break;
                case "4":
                    inventory_management();
                    break;
                case "5":
                    hr_management_menu();
                    break;
                case "6":
                    customer_account_management();
                    break;
                case "7":
                    manager_account_management(console);
                    break;
                case "0":
                    boolean logout_confirmed = handle_logout(console);
                    if (logout_confirmed) {
                        UserType.user_type_menu();
                        return;
                    }
                    break;
                default:
                    System.out.println("\n\tInvalid input. Try again...");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }
        }
    }


    private void display_purchase_history_menu() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        File directory = new File("oopr-poe-data/receipts/");
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().startsWith("receipt_number"));
        List<String> csvFiles = new ArrayList<>();

        System.out.println("\n\n\t=======================================");
        System.out.println("\t|                                     |");
        System.out.println("\t|         Transaction History         |");
        System.out.println("\t|                                     |");
        System.out.println("\t=======================================");
        System.out.println("\n\tCSV Files to Open:");

        if (files != null) {
            for (File file : files) {
                csvFiles.add(file.getName());
                System.out.println("\t[" + (csvFiles.size()) + "] " + file.getName());
            }
        }

        if (!csvFiles.isEmpty()) {
            System.out.println("\t[0] Go back");

            while (true) {
                System.out.print("\n\tEnter the number of the file to open: ");
                try {
                    String input = console.readLine().trim();
                    int choice = Integer.parseInt(input);

                    if (choice >= 0 && choice <= csvFiles.size()) {
                        if (choice == 0) {
                            System.out.println("\tReturning to previous menu...");
                            manager_dashboard();
                        }

                        String selectedFile = csvFiles.get(choice - 1);
                        System.out.println("\n\n\tYou selected: " + selectedFile);
                        read_transaction_history_from_csv(selectedFile);
                        System.out.print("\tPress Enter key to continue.");
                        console.readLine();
                        console.flush();
                        break;
                    } else {
                        System.out.println("\tInvalid choice! Please enter a number between 0 and " + csvFiles.size());
                        System.out.print("\t\tPress Enter key to continue.");
                        console.readLine();
                        console.flush();
                    }
                    display_purchase_history_menu();
                } catch (NumberFormatException e) {
                    System.out.println("\tInvalid input! Please enter a valid number.");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
                }
            }
        } else {
            System.out.println("\tNo CSV files found.");
            System.out.println("\tReturning to previous menu...");
            System.out.print("\tPress Enter key to continue.");
            console.readLine();
            console.flush();
        }
    }


    public static void read_transaction_history_from_csv(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader("oopr-poe-data/" + filename))) {
            String line;

            System.out.println("\n\t=== Purchase History ===\n");
            // Read and print the first header line
            if ((line = reader.readLine()) != null) {
                String[] headers1 = line.split(",");
                System.out.print("\t");
                for (String header : headers1) {
                    System.out.print(header + "\t\t");
                }
                System.out.println();
            }

            // Read and print the second header line
            if ((line = reader.readLine()) != null) {
                String[] headers2 = line.split(",");
                for (String header : headers2) {
                    System.out.print("\t" + header + "\t\t");
                }
                System.out.println();
                System.out.println("\t" + "-".repeat(60));  // Print a separator line
            }

            // Read and print all data lines
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                for (String column : columns) {
                    System.out.print("\t" + column + "\t");
                }
                System.out.println();
            }

            System.out.println("\n\tEnd of purchase history\n");

        } catch (IOException e) {
            System.out.println("\tError reading CSV file: " + e.getMessage());
        }
    }


    public void inventory_management() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        String item_category;

        while (true) {
            System.out.println("\n\t----------------------------------------------");
            System.out.println("\t|             INVENTORY MANAGEMENT           |");
            System.out.println("\t|         Select item category to check?     |");
            System.out.println("\t|                                            |");
            System.out.println("\t|        [1] Beverages                       |");
            System.out.println("\t|        [2] Snacks                          |");
            System.out.println("\t|        [3] Canned Goods                    |");
            System.out.println("\t|        [4] Condiments                      |");
            System.out.println("\t|        [5] Dairy                           |");
            System.out.println("\t|        [6] Frozen Foods                    |");
            System.out.println("\t|        [7] Body Care & Beauty Care         |");
            System.out.println("\t|        [8] Detergents & Soaps              |");
            System.out.println("\t|        [0] Go Back                         |");
            System.out.println("\t|                                            |");
            System.out.println("\t----------------------------------------------");
            System.out.print  ("\t|        Enter here: ");

            item_category = console.readLine().trim();

            // Variable to hold the products in the chosen category
            List<Product> selected_products = null;

            switch (item_category) {
                case "0":
                    manager_dashboard();
                    return;
                case "1":
                    selected_products = BrowseProduct.browse_beverages();
                    currentCategoryFile = "beverages.csv"; // Set the category file
                    break;
                case "2":
                    selected_products = BrowseProduct.browse_snacks();
                    currentCategoryFile = "snacks.csv"; // Set the category file
                    break;
                case "3":
                    selected_products = BrowseProduct.browse_canned_goods();
                    currentCategoryFile = "canned_goods.csv"; // Set the category file
                    break;
                case "4":
                    selected_products = BrowseProduct.browse_condiments();
                    currentCategoryFile = "condiments.csv"; // Set the category file
                    break;
                case "5":
                    selected_products = BrowseProduct.browse_dairy();
                    currentCategoryFile = "dairy.csv"; // Set the category file
                    break;
                case "6":
                    selected_products = BrowseProduct.browse_frozen_foods();
                    currentCategoryFile = "frozen_foods.csv"; // Set the category file
                    break;
                case "7":
                    selected_products = BrowseProduct.browse_self_care_items();
                    currentCategoryFile = "self_care_items.csv"; // Set the category file
                    break;
                case "8":
                    selected_products = BrowseProduct.browse_detergents();
                    currentCategoryFile = "detergents.csv"; // Set the category file
                    break;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }

            if (selected_products != null && !selected_products.isEmpty()) {
                System.out.print("\n\t\tPress Enter key to continue.");
                console.readLine();
                console.flush();
                inventory_item_options(selected_products);

            } else {
                System.out.println("\n\tNo products available in this category.");
                console.readLine();
                console.flush();
            }

        }

    }


    public void inventory_item_options(List<Product> products) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        String inventory_option_choice;

        while (true) {
            System.out.println("\n\t----------------------------------------------");
            System.out.println("\t|             INVENTORY MANAGEMENT           |");
            System.out.printf ("\t|                %-19s         |\n", currentCategoryFile);
            System.out.println("\t|             Select Options to do           |");
            System.out.println("\t|                                            |");
            System.out.println("\t|        [1] Add new item                    |");
            System.out.println("\t|        [2] Remove Product                  |");
            System.out.println("\t|        [3] Restock                         |");
            System.out.println("\t|        [4] Decrease Stock                  |");
            System.out.println("\t|        [5] Update Price                    |");
            System.out.println("\t|        [6] Display Product                 |");
            System.out.println("\t|                                            |");
            System.out.println("\t|        [0] Go Back                         |");
            System.out.println("\t----------------------------------------------");
            System.out.print  ("\t|        Enter here: ");
            inventory_option_choice = console.readLine();

            switch (inventory_option_choice) {
                case "0":
                    inventory_management();
                    return;
                case "1":
                    add_new_item(products);
                    break;
                case "2":
                    remove_product(products);
                    break;
                case "3":
                    restock_product(products);
                    break;
                case "4":
                    decrease_stock_product(products);
                    break;
                case "5":
                    update_product_price(products);
                    break;
                case "6":
                    BrowseProduct.display_products(products);
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
                    break;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }
        }
    }

    private void add_new_item(List<Product> products) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }
        System.out.println("\n\t=== Add New Item ===");
        BrowseProduct.display_products(products);

        String code = get_valid_input(console);

        // Check if product code already exists
        if (products.stream().anyMatch(p -> p.getCode().equals(code))) {
            System.out.println("\n\tProduct code already exists!");
            System.out.print("\t\tPress Enter key to continue.");
            console.readLine();
            console.flush();
            return;
        }

        System.out.print("\tEnter product name: ");
        String name = console.readLine();

        double price;
        while (true) {
            try {
                System.out.print("\tEnter product price: ");
                price = Double.parseDouble(console.readLine());
                if (price <= 0) {
                    System.out.println("\tPrice must be greater than 0!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("\tInvalid price format. Please enter a valid number.");
            }
        }

        int stock;
        while (true) {
            try {
                System.out.print("\tEnter initial stock: ");
                stock = Integer.parseInt(console.readLine());
                if (stock < 0) {
                    System.out.println("\tStock cannot be negative!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("\tInvalid stock format. Please enter a valid number.");
            }
        }

        Product newProduct = new Product(code, name, price, stock);
        products.add(newProduct);
        update_product_csv_file(products);

        System.out.println("\tProduct added successfully!");
        System.out.print("\t\tPress Enter key to continue.");
        console.readLine();
        console.flush();
    }


    private void remove_product(List<Product> products) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }
        System.out.println("\n\t=== Remove Product ===");

        BrowseProduct.display_products(products);
        System.out.print("\n\tEnter product code to remove: ");
        String code = console.readLine().toUpperCase();

        Product productToRemove = null;
        for (Product product : products) {
            if (product.getCode().equals(code)) {
                productToRemove = product;
                break;
            }
        }

        if (productToRemove != null) {
            System.out.println("\tProduct found: " + productToRemove.getName());

            String confirm;
            while (true) {
                System.out.print("\tAre you sure you want to remove this product? (Y/N): ");
                confirm = console.readLine();
                if (confirm.equalsIgnoreCase("Y") || confirm.equalsIgnoreCase("N")) {
                    break;
                } else {
                    System.out.println("\tInvalid input. Please enter 'Y' or 'N'.");
                }
            }

            if (confirm.equalsIgnoreCase("Y")) {
                products.remove(productToRemove);
                update_product_csv_file(products);
                System.out.println("\tProduct removed successfully!");
            } else {
                System.out.println("\tProduct removal cancelled.");
            }
        } else {
            System.out.println("\tProduct not found!");
        }

        System.out.print("\t\tPress Enter key to continue.");
        console.readLine();
        console.flush();
    }


    private void restock_product(List<Product> products) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        System.out.println("\n\t=== Restock Product ===");

        BrowseProduct.display_products(products);
        System.out.print("\n\tEnter product code to restock: ");
        String code = console.readLine().toUpperCase();

        Product productToRestock = null;
        for (Product product : products) {
            if (product.getCode().equals(code)) {
                productToRestock = product;
                break;
            }
        }

        if (productToRestock != null) {
            System.out.println("\tCurrent stock for " + productToRestock.getName() + ": " + productToRestock.getStock());

            int additionalStock;
            while (true) {
                try {
                    System.out.print("\tEnter additional stock quantity: ");
                    additionalStock = Integer.parseInt(console.readLine());
                    if (additionalStock <= 0) {
                        System.out.println("\tPlease enter a positive number!");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("\tInvalid input. Please enter a valid number.");
                }
            }

            productToRestock.update_stock(additionalStock);
            update_product_csv_file(products);
            System.out.println("\n\tStock updated successfully!");
        } else {
            System.out.println("\n\tProduct not found!");
        }

        System.out.print("\t\tPress Enter key to continue...");
        console.readLine();
        console.flush();
    }


    private void decrease_stock_product(List<Product> products) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }
        System.out.println("\n\t=== Decrease Product ===");

        BrowseProduct.display_products(products);
        System.out.print("\n\tEnter product code to decrease: ");
        String code = console.readLine().toUpperCase();

        Product productToRestock = null;
        for (Product product : products) {
            if (product.getCode().equals(code)) {
                productToRestock = product;
                break;
            }
        }

        if (productToRestock != null) {
            System.out.println("\tCurrent stock for " + productToRestock.getName() + ": " + productToRestock.getStock());

            int additionalStock;
            while (true) {
                try {
                    System.out.print("\tEnter reduction stock quantity: ");
                    additionalStock = Integer.parseInt(console.readLine());
                    if (additionalStock <= 0) {
                        System.out.println("\tPlease enter a positive number!");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("\tInvalid input. Please enter a valid number.");
                }
            }

            productToRestock.update_stock(-additionalStock);
            update_product_csv_file(products);
            System.out.println("\n\tStock updated successfully!");
        } else {
            System.out.println("\n\tProduct not found!");
        }

        System.out.print("\t\tPress Enter key to continue...");
        console.readLine();
        console.flush();
    }


    private void update_product_price(List<Product> products) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        System.out.println("\n\t=== Update Product Price ===");

        BrowseProduct.display_products(products);
        System.out.print("\n\tEnter product code to update: ");
        String code = console.readLine().toUpperCase();

        Product productToUpdate = null;
        for (Product product : products) {
            if (product.getCode().equals(code)) {
                productToUpdate = product;
                break;
            }
        }

        if (productToUpdate != null) {
            System.out.println("\tCurrent price for " + productToUpdate.getName() + ": " + productToUpdate.getPrice());

            double newPrice;
            while (true) {
                try {
                    System.out.print("\tEnter new price: ");
                    newPrice = Double.parseDouble(console.readLine());
                    if (newPrice <= 0) {
                        System.out.println("\tPrice must be greater than 0!");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("\tInvalid price format. Please enter a valid number.");
                }
            }

            productToUpdate.setPrice(newPrice);
            update_product_csv_file(products);
            System.out.println("\tPrice updated successfully!");
        } else {
            System.out.println("\tProduct not found!");
        }

        System.out.print("\t\tPress Enter key to continue.");
        console.readLine();
        console.flush();
    }


    private void update_product_csv_file(List<Product> products) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(get_current_category_file()))) {
            // Write header
            writer.println("Code,Name,Price,Stock");

            // Write product data
            for (Product product : products) {
                writer.printf("%s,%s,%.2f,%d%n",
                        product.getCode(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock());
            }
        } catch (IOException e) {
            System.out.println("\tError updating CSV file: " + e.getMessage());
        }
    }


    private String get_current_category_file() {
        if (currentCategoryFile == null || currentCategoryFile.isEmpty()) {
            System.out.println("\tNo category selected. Defaulting to a generic file.");
            return "products.csv"; // A fallback generic file
        }
        return currentCategoryFile;
    }


    public void hr_management_menu() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        String choice;
        Cashier cashier = new Cashier();

        while (true) {
            System.out.println("\n\n\t=======================================");
            System.out.println("\t|                                     |");
            System.out.println("\t|          HR Management Menu         |");
            System.out.println("\t|                                     |");
            System.out.println("\t=======================================\n");
            System.out.println("\t[1] Register New Employee");
            System.out.println("\t[2] View Employee List");
            System.out.println("\t[3] Delete Employee");
            System.out.println("\t[4] Reset Employee Password");
            System.out.println("\n\t[0] Go back to Dashboard");

            System.out.print("\n\n\tEnter Here: ");
            choice = console.readLine();

            switch (choice) {
                case "0":
                    manager_dashboard();
                    return;
                case "1":
                    cashier.create_new_cashier_employee();
                    break;
                case "2":
                    cashier.view_employee_list();
                    break;
                case "3":
                    cashier.delete_employee();
                    break;
                case "4":
                    cashier.manager_reset_employee_password();
                    break;
                default:
                    System.out.println("\n\tInvalid input. Try again...");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }
        }
    }


    public void voucher_management_dashboard() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        String choice;

        while (true) {
            System.out.println("\n\n\t=======================================");
            System.out.println("\t|                                     |");
            System.out.println("\t|        Voucher Codes Manager        |");
            System.out.println("\t|                                     |");
            System.out.println("\t=======================================\n");
            System.out.println("\t[1] Register New Coupon");
            System.out.println("\t[2] View Coupon List");
            System.out.println("\t[3] Restock");
            System.out.println("\t[4] Decrease stock");
            System.out.println("\t[5] Delete Coupon Code");
            System.out.println("\n\t[0] Go back to Dashboard");

            System.out.print("\n\n\tEnter Here: ");
            choice = console.readLine();

            switch (choice) {
                case "0":
                    manager_dashboard();
                    return;
                case "1":
                    couponManager.register_coupon();
                    break;
                case "2":
                    couponManager.view_employee_list();
                    break;
                case "3":
                    couponManager.restock_coupon_quantity();
                    break;
                case "4":
                    couponManager.decrease_coupon_quantity();
                    break;
                case "5":
                    couponManager.delete_coupon();
                    break;
                default:
                    System.out.println("\n\tInvalid input. Try again...");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }

        }

    }


    public void customer_account_management() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        String choice;
        UserCustomer userCustomer = new UserCustomer();

        while (true) {
            System.out.println("\n\n\t=======================================");
            System.out.println("\t|                                     |");
            System.out.println("\t|      Customer Account Management    |");
            System.out.println("\t|                                     |");
            System.out.println("\t=======================================\n");
            System.out.println("\t[1] View Customer List");
            System.out.println("\t[2] Delete Customer Account");
            System.out.println("\t[3] Reset Customer Password");
            System.out.println("\n\t[0] Go back to Dashboard");

            System.out.print("\n\n\tEnter Here: ");
            choice = console.readLine();

            switch (choice) {
                case "0":
                    manager_dashboard();
                    return;
                case "1":
                    userCustomer.view_customer_list();
                    break;
                case "2":
                    userCustomer.delete_customer();
                    break;
                case "3":
                    userCustomer.manager_reset_customer_password();
                    break;
                default:
                    System.out.println("\n\tInvalid input. Try again...");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }
        }
    }


    public void display_sales_report() {
        String filename = "oopr-poe-data/products/sales_report.csv";

        // Read and display the sales report
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            System.out.println("\n===== Sales Report =====");
            System.out.println("Product Code | Product Name         | Quantity | Price/Unit | Total Sales | Date");
            System.out.println("-------------------------------------------------------------------------------");

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 6) {
                    if (!fields[0].equals("TOTAL_ITEMS")) {
                        // Display product entries
                        System.out.printf("%-12s | %-20s | %-8s | %-10s | %-11s | %s%n",
                                fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
                    } else {
                        // Display totals
                        System.out.println("-------------------------------------------------------------------------------");
                        System.out.printf("Total Items: %-6s | Total Sales: %-10s | Date: %s%n",
                                fields[1], fields[3], fields[4]);
                    }
                }
            }
            System.out.println("===============================================================================");
        } catch (IOException e) {
            System.out.println("\tError reading the sales report: " + e.getMessage());
        }
    }


    private String get_valid_input(Console console) {
        String input;
        while (true) {
            System.out.print("\n\tEnter new product code for new item: ");
            input = console.readLine().trim();
            if (input.length() >= 6) {
                return input;
            }
            System.out.println("\t" + "\n\tProduct code must be at least 6 characters long.");
        }
    }


    private void manager_account_management(Console console) {
        while (true) {
            System.out.println("\n\t=======================================");
            System.out.println("\t|    Manager Account Management      |");
            System.out.println("\t=======================================\n");
            System.out.println("\t[1] Change Password");
            System.out.println("\t[2] Change Store Name");
            System.out.println("\t[0] Return to Main Menu");

            System.out.print("\n\n\tEnter Here: ");
            String choice = console.readLine();

            switch (choice) {
                case "1":
                    ManagerCredentials.recover_manager_credentials();
                    break;
                case "2":
                    ManagerCredentials.change_store_name();
                    break;
                case "0":
                    manager_dashboard();
                    return;
                default:
                    System.out.println("\n\tInvalid input. Try again...");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }
        }
    }


}
