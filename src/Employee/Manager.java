package Employee;

import Shopper.Product;
import User_Types.UserType;
import Process.BrowseProduct;
import Process.CouponManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Manager {
    private static final int MAX_ATTEMPTS = 3; // Maximum login attempts
    private String currentCategoryFile;
    public CouponManager couponManager;

    public void user_manager() {
        this.couponManager = new CouponManager();
        manager_login();
    }

    //GAWAN NG TXT OR CSV FILE PARA HINDI HARD CODED ANG CREDENTIALS AT KUNG WALA PANG CREDENTIALS,SA UNANG OPEN NG PROGRAM DUN DAPAT MAG ASK ANO NAME AT PASS, ALSO NAME NARIN NG STORE
    private void manager_login() {
        Scanner scanf = new Scanner(System.in);
        int attempt_count = 0; // Count failed login attempts
        boolean valid = false; // Indicate if login is valid

        while (!valid) {
            System.out.println("\n\n\t===================================");
            System.out.println("\t|                                 |");
            System.out.println("\t|          Manager Login          |");
            System.out.println("\t|                                 |");
            System.out.println("\t===================================");
            System.out.println("\tEnter Credentials\n");

            System.out.print("\tEnter username: ");
            String inputUsername = scanf.nextLine();

            System.out.print("\tEnter password: ");
            String inputPassword = scanf.nextLine(); // Capture password input directly without hiding

            // Validate login
            if (validate_manager_login(inputUsername, inputPassword)) {
                valid = true; // Set flag to exit loop
                System.out.println("\tLogin successful!");
                manager_dashboard();
            } else {
                attempt_count++;
                System.out.println("\tInvalid login attempt #" + attempt_count + " for user: " + inputUsername);

                if (attempt_count >= MAX_ATTEMPTS) {
                    System.out.println("\tMaximum attempts reached.");
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
                    break;
                }
            }
        }
    }

    // ma iiba to dahil sa unang open ng program dapat mag set up
    private boolean validate_manager_login(String inputUsername, String inputPassword) {
        // Default username
        String username = "manager";
        // Default password
        String password = "manager";
        return inputUsername.equals(username) && inputPassword.equals(password);
    }


    private boolean handle_logout(Scanner scanf) {
        while (true) {
            System.out.println("\n\n\tAre you sure you want to Logout and go back to menu?\n");
            System.out.print("\t[Y] for Yes  [N] for No: ");

            String exit_confirmation = scanf.next().trim();
            scanf.nextLine();

            if (exit_confirmation.equalsIgnoreCase("Y")) {
                UserType.user_type_menu();
                return true;
            } else if (exit_confirmation.equalsIgnoreCase("N")) {
                return false;
            }
        }
    }


    private void manager_dashboard() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("\n\t=======================================");
            System.out.println  ("\t|                                     |");
            System.out.println  ("\t|          Manager Dashboard          |");
            System.out.println  ("\t|                                     |");
            System.out.println  ("\t=======================================\n");
            System.out.println  ("\t[1] Voucher code / Promotions");
            System.out.println("\t[2] Sales report"); //display total sales at recent total transactions
            System.out.println("\t[3] Purchase/Transaction History"); //same sa personal account
            System.out.println("\t[4] Inventory Report"); //refill stocks
            System.out.println("\t[5] HR Management"); //sino mga employees at handle ng account nila change pass/delete acc
            System.out.println("\t[6] Customer Account Management"); //account retrieval at delete account
            System.out.println("\t[0] Exit");

            System.out.print("\n\n\tEnter Here: ");
            choice = scanf.nextLine();

            switch (choice) {
                case "1":
                    voucher_management_dashboard();
                    break;
                case "2":
                    //revise ung save files dapat may date ang resibo at sa total sales dun din mapupunta mga items na benta, listahan
                    break;
                case "3":
                    //refactor ng resibo dagdag date and time pati sa mismong csv
                    display_purchase_history_menu();
                    break;
                case "4":
                    //nababawasan na pero need madagdagan na at pwede mag dagdag mismo ng new items
                    inventory_management();
                    break;
                case "5":
                    hr_management_menu();
                    break;
                case "0":
                    boolean logout_confirmed = handle_logout(scanf);
                    if (logout_confirmed) {
                        UserType.user_type_menu(); //redundant
                        return;
                    }
                    break;
                default:
                    System.out.println("\n\tInvalid input. Try again...");
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
            }
        }
    }


    private void display_purchase_history_menu() {
        Scanner scanf = new Scanner(System.in);
        File directory = new File("receipts/");
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
                    String input = scanf.nextLine().trim();
                    int choice = Integer.parseInt(input);

                    if (choice >= 0 && choice <= csvFiles.size()) {
                        if (choice == 0) {
                            System.out.println("\tReturning to previous menu...");
                            manager_dashboard();
                        }

                        String selectedFile = csvFiles.get(choice - 1);
                        System.out.println("\n\n\tYou selected: " + selectedFile);
                        read_transaction_history_from_csv(selectedFile);
                        System.out.print("\tPress Enter key to continue.\n");
                        scanf.nextLine();
                        break;
                    } else {
                        System.out.println("\tInvalid choice! Please enter a number between 0 and " + csvFiles.size());
                        System.out.println("\t\tPress Enter key to continue.\n");
                        scanf.nextLine(); //used for press any key to continue
                    }
                    display_purchase_history_menu();
                } catch (NumberFormatException e) {
                    System.out.println("\tInvalid input! Please enter a valid number.");
                }
            }
        } else {
            System.out.println("\tNo CSV files found.");
            System.out.print("\tPress Enter key to continue.\n");
            scanf.nextLine();
            System.out.println("\tReturning to previous menu.");
        }
    }


    public static void read_transaction_history_from_csv(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader("receipts/" + filename))) {
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
        Scanner scanf = new Scanner(System.in);
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

            item_category = scanf.nextLine();

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
                    scanf.nextLine(); //used for press any key to continue
            }

            if (selected_products != null && !selected_products.isEmpty()) {
                System.out.print("\n\t\tPress Enter key to continue.");
                scanf.nextLine(); //used for press any key to continue
                inventory_item_options(selected_products);

            } else { System.out.println("\n\tNo products available in this category.");  }

        }

    }


    public void inventory_item_options(List<Product> products) {
        Scanner scanf = new Scanner(System.in);
        String inventory_option_choice;

        while (true) {
            System.out.println("\n\t----------------------------------------------");
            System.out.println("\t|             INVENTORY MANAGEMENT           |");
            System.out.println("\t|         Select options to do               |");
            System.out.println("\t|                                            |");
            System.out.println("\t|        [1] Add new item                    |");
            System.out.println("\t|        [2] Remove Product                  |");
            System.out.println("\t|        [3] Restock                         |");
            System.out.println("\t|        [4] Decrease Stock                   |");
            System.out.println("\t|        [5] Update Price                    |");
            System.out.println("\t|                                            |");
            System.out.println("\t|        [0] Go Back                         |");
            System.out.println("\t----------------------------------------------");
            System.out.print("\t|        Enter here: ");
            inventory_option_choice = scanf.nextLine();

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
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine();
            }
        }
    }

    private void add_new_item(List<Product> products) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\t=== Add New Item ===");
        BrowseProduct.display_products(products);

        System.out.print("\n\tEnter new product code for new item: ");
        String code = scanf.nextLine().toUpperCase();

        // Check if product code already exists
        if (products.stream().anyMatch(p -> p.getCode().equals(code))) {
            System.out.println("\n\tProduct code already exists!");
            System.out.print("\t\tPress Enter key to continue.");
            scanf.nextLine();
            return;
        }

        System.out.print("\tEnter product name: ");
        String name = scanf.nextLine();

        double price;
        while (true) {
            try {
                System.out.print("\tEnter product price: ");
                price = Double.parseDouble(scanf.nextLine());
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
                stock = Integer.parseInt(scanf.nextLine());
                if (stock < 0) {
                    System.out.println("\tStock cannot be negative!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("\tInvalid stock format. Please enter a valid number.");
            }
        }

        // Add to list and update CSV
        Product newProduct = new Product(code, name, price, stock);
        products.add(newProduct);
        update_product_csv_file(products);

        System.out.println("\tProduct added successfully!");
        System.out.print("\t\tPress Enter key to continue.");
        scanf.nextLine();
    }


    private void remove_product(List<Product> products) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\t=== Remove Product ===");

        BrowseProduct.display_products(products);
        System.out.print("\n\tEnter product code to remove: ");
        String code = scanf.nextLine().toUpperCase();

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
                confirm = scanf.nextLine();
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
        scanf.nextLine();
    }


    private void restock_product(List<Product> products) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\t=== Restock Product ===");

        BrowseProduct.display_products(products);
        System.out.print("\n\tEnter product code to restock: ");
        String code = scanf.nextLine().toUpperCase();

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
                    additionalStock = Integer.parseInt(scanf.nextLine());
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
        scanf.nextLine();
    }


    private void decrease_stock_product(List<Product> products) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\t=== Decrease Product ===");

        BrowseProduct.display_products(products);
        System.out.print("\n\tEnter product code to decrease: ");
        String code = scanf.nextLine().toUpperCase();

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
                    additionalStock = Integer.parseInt(scanf.nextLine());
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
        scanf.nextLine();
    }


    private void update_product_price(List<Product> products) {
        Scanner scanf = new Scanner(System.in);
        System.out.println("\n\t=== Update Product Price ===");

        BrowseProduct.display_products(products);
        System.out.print("\n\tEnter product code to update: ");
        String code = scanf.nextLine().toUpperCase();

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
                    newPrice = Double.parseDouble(scanf.nextLine());
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
        scanf.nextLine();
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
        Scanner scanf = new Scanner(System.in);
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
            choice = scanf.nextLine();

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
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
            }
        }
    }


    public void voucher_management_dashboard() {
        Scanner scanf = new Scanner(System.in);
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
            choice = scanf.nextLine();

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
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
            }

        }

    }





//more code review para sa efficiency 



}
