package Shopper;

import Employee.ManagerCredentials;
import User_Types.*;
import Process.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserCustomer {
    public final List<Customer> customers = new ArrayList<>();
    public CouponManager couponManager;

    public UserCustomer() {
        this.couponManager = new CouponManager();
        load_customers_from_CSV();
    }

    public void user_customer_menu() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }
        String choice;

        while (true) {
            String storeName = ManagerCredentials.getStoreName();
            System.out.println("\n\t--------------------------------------------------");
            System.out.printf ("\t|           Welcome to %-22s    |\n", storeName + " Store");
            System.out.println("\t|                                                |");
            System.out.println("\t|        [1] Login                               |");
            System.out.println("\t|        [2] Continue as Guest                   |");
            System.out.println("\t|        [3] Register                            |");
            System.out.println("\t|        [0] Go Back                             |");
            System.out.println("\t|                                                |");
            System.out.println("\t--------------------------------------------------");

            System.out.print("\n\tEnter Here: ");
            choice = console.readLine().trim();

            switch (choice) {
                case "1":
                    customer_login();
                    break;
                case "2":
                    guest_customer_item_category();
                    break;
                case "3":
                    customer_register();
                    break;
                case "0":
                    UserType.user_type_menu();
                    return;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine(); //used for press any key to continue
            }
        }
    }


    public void guest_customer_item_category() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }
        String item_category;

        // Create an instance of OrderProcessor to handle the guest's order
        //bale parang nag ready na ba ng kariton pag napunta sa puregold
        OrderProcessor order_processor = new OrderProcessor();

        while (true) {
            if (!OrderProcessor.cart.isEmpty()) {   OrderProcessor.display_cart(); }
            System.out.println("\n\t----------------------------------------------");
            System.out.println("\t|            Welcome Customer!               |");
            System.out.println("\t|         What do you want to browse?        |");
            System.out.println("\t|                                            |");
            System.out.println("\t|        [1] Beverages                       |");
            System.out.println("\t|        [2] Snacks                          |");
            System.out.println("\t|        [3] Canned Goods                    |");
            System.out.println("\t|        [4] Condiments                      |");
            System.out.println("\t|        [5] Dairy                           |");
            System.out.println("\t|        [6] Frozen Foods                    |");
            System.out.println("\t|        [7] Body Care & Beauty Care         |");
            System.out.println("\t|        [8] Detergents & Soaps              |");
            if (!OrderProcessor.cart.isEmpty()) {
            System.out.println("\t|        [9] Modify Items in cart            |"); }
            System.out.println("\t|                                            |");
            System.out.println("\t|        [0] Go Back                         |");
            System.out.println("\t|                                            |");
            System.out.println("\t----------------------------------------------");
            System.out.print("\t|        Enter here: ");
            item_category = console.readLine().trim();

            // Variable to hold the products in the chosen category
            // parang shelf to
            List<Product> selected_products = null;

            switch (item_category) {
                case "0":
                    if (!OrderProcessor.cart.isEmpty()) {
                        boolean cancel = true;
                        while (cancel) {
                            System.out.print("\n\tIf you go back, the cart will get empty. Do you still want to go back? (Y/N): ");
                            String go_back = console.readLine().trim().toUpperCase();

                            if (go_back.equals("Y")) {
                                OrderProcessor.cart.clear();
                                user_customer_menu();
                            } // Return to main menu
                            else if (go_back.equals("N")) { cancel = false; }
                        }
                    }
                    if (OrderProcessor.cart.isEmpty()) { user_customer_menu(); return; }
                    break;
                case "1":
                    selected_products = BrowseProduct.browse_beverages(); // Browse beverages
                    break;
                case "2":
                    selected_products = BrowseProduct.browse_snacks(); // Browse snacks
                    break;
                case "3":
                    selected_products = BrowseProduct.browse_canned_goods(); // Browse canned goods
                    break;
                case "4":
                    selected_products = BrowseProduct.browse_condiments(); // Browse condiments
                    break;
                case "5":
                    selected_products = BrowseProduct.browse_dairy(); // Browse dairy
                    break;
                case "6":
                    selected_products = BrowseProduct.browse_frozen_foods(); // Browse frozen foods
                    break;
                case "7":
                    selected_products = BrowseProduct.browse_self_care_items(); // Browse self-care items
                    break;
                case "8":
                    selected_products = BrowseProduct.browse_detergents(); // Browse detergents
                    break;
                case "9":
                    if (!OrderProcessor.cart.isEmpty()) {
                        order_processor.modify_menu_process();
                        break;
                    }
                    break;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\n\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
                    continue;
            }

            if (selected_products != null && !selected_products.isEmpty()) {
                // After displaying products, process the order by asking for product code
                if (order_processor.process_customer_order(selected_products)) {
                    order_processor.modify_menu_process();
                }
            } else {
                if (!item_category.equals("9")){ // para hindi to lumalabas dahil di naman list ang case 9
                    System.out.println("No products available in this category.");
                }
            }
        }
    }


    private static String get_valid_input(Console console, String prompt, String errorMsg, int minLength) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = console.readLine().trim();
            if (input.length() >= minLength) {
                return input;
            }
            System.out.println("\t" + errorMsg);
        }
    }


    private static String get_valid_password(Console console) {
        String password, confirmPassword;

        while (true) {
            // Prompt for password
            System.out.println("\n\tEnter password (min 8 characters): ");
            password = new String(console.readPassword()); // Using `readPassword` to mask input

            // Check password length
            if (password.length() < 8) {
                System.out.println("\tPassword must be at least 8 characters long.");
                continue;
            }

            // Prompt for confirmation
            System.out.print("\tConfirm password: ");
            confirmPassword = new String(console.readPassword());

            // Check if passwords match
            if (password.equals(confirmPassword)) {
                return password; // Password is valid
            }

            System.out.println("\tPasswords do not match. Please try again.");
        }
    }


    private void customer_register() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }
        final int MAX_CUSTOMERS = 100;
        if (customers.size() >= MAX_CUSTOMERS) {
            System.out.println("\n\tCustomer limit reached. Cannot register new customers.");
            return;
        }

        Customer newCustomer = new Customer();
        String confirmPin = "";
        float initialFunds;
        String pinCode;
        String phoneNumber;
        String paymentMethod;

        System.out.println("\n\t-----------------------------------------------");
        System.out.println("\t|               Terms of Agreement            |");
        System.out.println("\t|  By registering, you acknowledge that this  |");
        System.out.println("\t|  is a sample project and the information    |");
        System.out.println("\t|  you provide will only be used for the      |");
        System.out.println("\t|  purpose of demonstrating customer          |");
        System.out.println("\t|  registration functionality. We deeply      |");
        System.out.println("\t|  care for your privacy so your data will    |");
        System.out.println("\t|  not be shared or used for any other        |");
        System.out.println("\t|  purposes.                                  |");
        System.out.println("\t|                                             |");
        System.out.println("\t|  (Press Enter to continue, or type 'exit'   |");
        System.out.println("\t|  to cancel registration.)                   |");
        System.out.println("\t----------------------------------------------");

        String agreement = "";
        boolean agreement_consent = false;

        while (!agreement_consent) {
            System.out.print("\n\tDo you agree to the terms? (y/n): ");
            agreement = console.readLine().trim().toLowerCase();

            if (agreement.equals("y") || agreement.equals("n")) {
                agreement_consent = true;
            } else {
                System.out.println("\n\tInvalid input. Please enter 'y' or 'n'.");
            }
        }

        if (agreement.equals("n")) {
            System.out.println("\n\tRegistration cancelled.");
            return;
        }

        String username = get_valid_input(console, "\n\tEnter Username (min 8 characters): ",
                "\tUsername must be at least 8 characters long.", 8);

        boolean not_exist = false;
        while (!not_exist) {
            not_exist = true;
            for (Customer customer : customers) {
                if (customer.getUsername().equals(username)) {
                    System.out.println("\n\tUsername already exists. Please choose a different username.");
                    username = get_valid_input(console, "\n\tEnter Username: ",
                            "\tUsername must be at least 8 characters long.", 8);
                    not_exist = false;
                    break;
                }
            }
        }
        newCustomer.setUsername(username);

        String password = get_valid_password(console);
        newCustomer.setPassword(password);

        phoneNumber = get_valid_input(console, "\n\tEnter Phone Number (e.g. 09091234567): ",
                "\tInvalid phone number. Please enter an 11-digit number starting with '09'.", 11);
        while (!phoneNumber.matches("^09\\d{9}$")) {
            phoneNumber = get_valid_input(console, "\tInvalid phone number. Try again: ",
                    "\tInvalid phone number. Please enter an 11-digit number starting with '09'.", 11);
        }
        newCustomer.setPhoneNumber(phoneNumber);

        do {
            System.out.print("\tIs the phone number #" + newCustomer.getPhoneNumber() + " for GCash (G) or PayMaya (P)? ");
            String paymentChoice = console.readLine().trim().toUpperCase();
            if (paymentChoice.equals("G")) {
                paymentMethod = "GCash";
                break;
            } else if (paymentChoice.equals("P")) {
                paymentMethod = "PayMaya";
                break;
            } else {
                System.out.println("\n\tInvalid choice. Please try again.");
            }
        } while (true);
        newCustomer.setPaymentMethod(paymentMethod);

        System.out.println("\n\tThe payment method of user: " + newCustomer.getUsername() + " is using " + newCustomer.getPaymentMethod());

        pinCode = get_valid_input(console, "\n\tEnter a 4-digit PIN code: ",
                "\tInvalid PIN. Please enter a 4-digit number.", 4);
        while (!pinCode.matches("\\d{4}")) {
            pinCode = get_valid_input(console, "\tInvalid PIN. Try again: ",
                    "\tInvalid PIN. Please enter a 4-digit number.", 4);
        }
        newCustomer.setPinCode(pinCode);

        while (!newCustomer.getPinCode().equals(confirmPin)) {
            confirmPin = get_valid_input(console, "\tConfirm PIN: ",
                    "\tPINs do not match. Please try again.", 4);
        }

        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("\n\tEnter initial amount to add to your account: ");
                initialFunds = Float.parseFloat(console.readLine());
                newCustomer.setBalance(initialFunds);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("\tInvalid input. Please enter a valid number.");
            }
        }

        newCustomer.setTransaction(0);
        newCustomer.setRewardPoint(0);
        newCustomer.setTotal_spent(0);

        save_customer_to_file(newCustomer);
        customers.add(newCustomer);

        System.out.print("\n\tCongratulations! Your registration was successful. " + newCustomer.getUsername() +
                ".\n\t\t\tPress Enter key to start exploring!\n");
        console.readLine();
        registered_user_customer_item_category(newCustomer.getUsername(), newCustomer, OrderProcessor.cart);
    }

    // Helper method to input hidden password pero dapat magiging ****
    //shortcut sa pag fill ups thanks sa AI
    private String input_password(Console console, String prompt) {
        System.out.print(prompt);
        char[] passwordChars = console.readPassword(); // Read password securely
        return new String(passwordChars); // Convert char[] to String
    }


    // eto sinisave naman lahat nito after every changes, rewriting data kumbaga
    public void saveAllCustomersToCSV() {
        File file = new File("oopr-poe-data/accounts/customers.csv"); // Simple file in project root
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Username,Password,PhoneNumber,PaymentMethod,Balance,PIN,Transaction,RewardPoint,TotalSpent\n");
            for (Customer customer : customers) {
                writer.write(customer.getUsername() + "," +
                        customer.getPassword() + "," +
                        customer.getPhoneNumber() + "," +
                        customer.getPaymentMethod() + "," +
                        customer.getBalance() + "," +
                        customer.getPinCode() + "," +
                        customer.getTransaction() + "," +
                        customer.getRewardPoint() + "," +
                        customer.getTotal_spent());
                writer.newLine();
            }
            System.out.println("\n\tCustomer data saved to CSV.");
        } catch (IOException e) {
            System.out.println("\tError saving customer data: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //dagdag ka lang sa line sa dulo after register
    private void save_customer_to_file(Customer customer) {
        File file = new File("oopr-poe-data/accounts/customers.csv");

        // Check if the file exists before opening the writer
        boolean fileExists = file.exists();

        try (FileWriter writer = new FileWriter(file, true)) {
            // Write header only if the file doesn't exist (i.e., it's a new file)
            if (!fileExists) {
                writer.write("Username,Password,PhoneNumber,PaymentMethod,Balance,PIN,Transaction,RewardPoint\n");
            }
            // Write customer data
            writer.append(customer.getUsername()).append(",");
            writer.append(customer.getPassword()).append(",");
            writer.append(customer.getPhoneNumber()).append(",");
            writer.append(customer.getPaymentMethod()).append(",");
            writer.append(String.valueOf(customer.getBalance())).append(",");
            writer.append(customer.getPinCode()).append(",");
            writer.append(String.valueOf(customer.getTransaction())).append(",");
            writer.append(String.valueOf(customer.getRewardPoint())).append(",");
            writer.append(String.valueOf(customer.getTotal_spent())).append("\n");

        } catch (IOException e) {
            // Handle exceptions gracefully
            System.err.println("Error writing customer data to file: " + e.getMessage());
        }
    }


    // Load all customers from the CSV file
    private void load_customers_from_CSV() {
        customers.clear();

        String CUSTOMER_CSV_FILE = "oopr-poe-data/accounts/customers.csv";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CUSTOMER_CSV_FILE))) {
            String line;
            // Skip the field name / header line
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                // Expect 8 fields: username, password, phone number, payment method, balance, PIN, transaction, reward points, total_spent
                if (data.length == 9) {
                    Customer customer = new Customer();
                    customer.setUsername(data[0]);
                    customer.setPassword(data[1]);
                    customer.setPhoneNumber(data[2]);
                    customer.setPaymentMethod(data[3]);
                    customer.setBalance(Float.parseFloat(data[4]));
                    customer.setPinCode(data[5]);
                    customer.setTransaction(Float.parseFloat(data[6]));
                    customer.setRewardPoint(Double.parseDouble(data[7]));
                    customer.setTotal_spent(Double.parseDouble(data[8]));
                    customers.add(customer);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("\tCSV file not found. No customers loaded.");
        } catch (IOException e) {
            System.out.println("\tError loading customers from file: " + e.getMessage());
        }
    }


    private void customer_login() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }
        int attempt_count = 0;
        boolean login_successful = false;
        final int MAX_ATTEMPTS = 3;

        load_customers_from_CSV();
        // Load customers from the CSV if not already loaded
        if (customers.isEmpty()) {
            load_customers_from_CSV();
            System.out.println("\n\tNo registered customer so far. You are welcome to register, thank you...");
            System.out.print("\t\tPress Enter key to continue.");
            console.readLine();
            return;
        }

        // Loop until login is successful or maximum attempts reached
        // Maximum number of login attempts
        while (attempt_count < MAX_ATTEMPTS) {
            System.out.println("\n\t===================================");
            System.out.println("\t|                                 |");
            System.out.println("\t|          Customer Login         |");
            System.out.println("\t|                                 |");
            System.out.println("\t===================================\n");
            System.out.print("\tEnter Username: ");
            String username = console.readLine();

            // Get hidden password input
            String password = input_password(console, "\tEnter Password: ");

            // Check if the username and password match any customer record
            for (Customer customer : customers) {
                if (customer.getUsername().equalsIgnoreCase(username) && customer.getPassword().equals(password)) {
                    System.out.println("\n\tLogin successful.\n");
                    login_successful = true;
                    registered_user_customer_item_category(customer.getUsername(), customer, null);
                    break;
                }
            }

            if (login_successful) { break; }
            else {
                attempt_count++;
                System.out.println("\n\tInvalid username or password. Attempts left: " + (MAX_ATTEMPTS - attempt_count));
            }
        }
        if (!login_successful) {
            System.out.println("\n\tMaximum login attempts reached.");
            System.out.println("\t\tTo recover your account please contact the manager to reset password.");
            System.out.print("\t\t\tPress Enter key to continue...");
            console.readLine();
            console.flush();//used for press any key to continue
        }
    }


    public void registered_user_customer_item_category(String username, Customer customer, List<Product> existing_cart) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }
        String item_category;

        if (existing_cart == null) {
            existing_cart = new ArrayList<>();
        }
        // Create an instance of OrderProcessor to handle the registered user's order
        // Pass the existing cart to the OrderProcessor constructor
        OrderProcessor order_processor = new OrderProcessor(existing_cart);

        while (true) {
            if (!OrderProcessor.cart.isEmpty()) {   OrderProcessor.display_cart(); }
            System.out.println("\t----------------------------------------------");
            System.out.printf ("\t|          Welcome Customer! %-16s|\n", username);
            System.out.printf ("\t|        Your remaining balance: %-7.2f     |\n", customer.getBalance());
            System.out.println("\t|          What do you want to browse?       |");
            System.out.println("\t|                                            |");
            System.out.println("\t|        [1] Beverages                       |");
            System.out.println("\t|        [2] Snacks                          |");
            System.out.println("\t|        [3] Canned Goods                    |");
            System.out.println("\t|        [4] Condiments                      |");
            System.out.println("\t|        [5] Dairy                           |");
            System.out.println("\t|        [6] Frozen Foods                    |");
            System.out.println("\t|        [7] Body Care & Beauty Care         |");
            System.out.println("\t|        [8] Detergents & Soaps              |");
            System.out.println("\t|        [9] Add funds                       |");
            System.out.println("\t|        [10] View Purchase History          |");
            System.out.println("\t|        [11] Change Password                |");
            System.out.println("\t|        [12] Redeem Rewards                 |");
            if (!OrderProcessor.cart.isEmpty()) {
            System.out.println("\t|        [13] Modify Items in cart           |"); }
            System.out.println("\t|                                            |");
            System.out.println("\t|        [0] Log Out                         |");
            System.out.println("\t|                                            |");
            System.out.println("\t----------------------------------------------");
            System.out.print("\t|        Enter here: ");
            item_category = console.readLine();

            // Variable to hold the products in the chosen category
            //variable declaration lang to pero ang datatype nya ay array list
            List<Product> selected_products = null;

            switch (item_category) {
                case "1":
                    selected_products = BrowseProduct.browse_beverages(); // Browse beverages
                    break;
                case "2":
                    selected_products = BrowseProduct.browse_snacks(); // Browse snacks
                    break;
                case "3":
                    selected_products = BrowseProduct.browse_canned_goods(); // Browse canned goods
                    break;
                case "4":
                    selected_products = BrowseProduct.browse_condiments(); // Browse condiments
                    break;
                case "5":
                    selected_products = BrowseProduct.browse_dairy(); // Browse dairy
                    break;
                case "6":
                    selected_products = BrowseProduct.browse_frozen_foods(); // Browse frozen foods
                    break;
                case "7":
                    selected_products = BrowseProduct.browse_self_care_items(); // Browse self-care items
                    break;
                case "8":
                    selected_products = BrowseProduct.browse_detergents(); // Browse detergents
                    break;
                case "9":
                    add_funds(customer, existing_cart);
                    break;
                case "10":
                    display_purchase_history_menu(customer);
                    break;
                case "11":
                    registered_customer_change_password(username, customer, existing_cart);
                    break;
                case "12":
                    redeem_reward_points(customer, existing_cart);
                    break;
                case "13":
                    if (!OrderProcessor.cart.isEmpty()) {
                        order_processor.registered_user_modify_menu_process(customer, existing_cart);
                        break;
                    }
                    break;
                case "0":
                    while (true) {
                        System.out.println("\n\n\tAre you sure you want to Logout and go back to menu?\n");
                        System.out.print("\t[Y] for Yes  [N] for No: ");
                        String exit_confirmation = console.readLine().trim();

                        if (exit_confirmation.equalsIgnoreCase("Y")) {
                            user_customer_menu();
                            return;
                        } else if (exit_confirmation.equalsIgnoreCase("N")) {
                            registered_user_customer_item_category(username, customer, existing_cart);
                            break;
                        } else {
                            System.out.println("\n\tAn error has occurred");
                            System.out.print("\t\tPress Enter key to continue.");
                            console.readLine(); //used for press any key to continue
                        }
                    }
                    break;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine(); //used for press any key to continue
                    console.flush();
            }

            // After displaying products, process the order by asking for product code
            if (selected_products != null && !selected_products.isEmpty()) {
                if(order_processor.process_customer_order(selected_products)) {
                    order_processor.registered_user_modify_menu_process(customer, existing_cart);
                }
            }
        }
    }


    private void add_funds(Customer customer, List<Product> existing_cart) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

            while (true) {
                System.out.print("\n\tEnter your registered phone number (or type '0' to cancel): ");
                String inputPhoneNumber = console.readLine().trim();

                if (inputPhoneNumber.equalsIgnoreCase("0")) {
                    System.out.println("\tOperation canceled.");
                    return; // Exit the method
                }

                if (!inputPhoneNumber.equals(customer.getPhoneNumber())) {
                    System.out.println("\tIncorrect phone number. Please try again.");
                    continue; // Ask again
                }

                while (true) {
                    System.out.print("\tEnter your PIN (or type '0' to cancel): ");
                    String inputPin = console.readLine().trim();

                    if (inputPin.equalsIgnoreCase("0")) {
                        System.out.println("\tOperation canceled.");
                        return; // Exit the method
                    }

                    if (!inputPin.equals(customer.getPinCode())) {
                        System.out.println("\tIncorrect PIN. Please try again.");
                        continue; // Ask again
                    }

                    float amount = 0;
                    boolean validInput = false;

                    while (!validInput) {
                        try {
                            System.out.print("\n\tEnter amount to add (or type '0' to cancel): ");
                            String input = console.readLine().trim();

                            if (input.equalsIgnoreCase("0")) {
                                System.out.println("\tOperation canceled.");
                                return; // Exit the method
                            }

                            amount = Float.parseFloat(input);

                            if (amount <= 0) {
                                System.out.println("\tAmount must be greater than zero. Please try again.");
                                continue;
                            }

                            validInput = true; // Input is valid, exit the loop
                        } catch (NumberFormatException e) {
                            System.out.println("\tInvalid input. Please enter a valid number.");
                        }
                    }

                    customer.setBalance(customer.getBalance() + amount);
                    saveAllCustomersToCSV();

                    System.out.printf("\n\tFunds added successfully. New balance: %.2f\n", customer.getBalance());
                    System.out.print("\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
                    registered_user_customer_item_category(customer.getUsername(), customer, existing_cart);
                }
            }
    }



    private void display_purchase_history_menu(Customer customer) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        File directory = new File("oopr-poe-data/receipts");
        FilenameFilter filter = (dir, name) -> name.startsWith("receipt_number_") && name.endsWith("Customer_" + customer.getUsername() + ".csv");
        File[] files = directory.listFiles(filter);
        List<String> csvFiles = new ArrayList<>();

        System.out.println("\t=======================================");
        System.out.println("\t|                                     |");
        System.out.println("\t|           Purchase History          |");
        System.out.printf ("\t|      Your total spent %-8.2f      |\n", customer.getTotal_spent());
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
                            return;
                        }

                        String selectedFile = csvFiles.get(choice - 1);
                        System.out.println("\tYou selected: " + selectedFile);
                        read_history_from_csv(selectedFile);
                        System.out.print("\tPress Enter key to continue.");
                        console.readLine();
                        display_purchase_history_menu(customer);
                        break;
                    } else {
                        System.out.println("\tInvalid choice! Please enter a number between 0 and " + csvFiles.size());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\tInvalid input! Please enter a valid number.");
                }
            }
        } else {
            System.out.println("\tNo CSV files found.");
            System.out.println("\tReturning to previous menu.");
            System.out.print("\t\tPress Enter key to continue.");
            console.readLine();
        }
    }


    public void read_history_from_csv(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader("oopr-poe-data/receipts/" + filename))) {
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
                System.out.println("\t" + "-".repeat(80));  // Print a separator line
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


    public void registered_customer_change_password(String username, Customer customer, List<Product> existing_cart) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        String new_password;
        String confirm_password;
        int attemptCount = 0;
        final int MAX_ATTEMPTS = 3;

        while (attemptCount < MAX_ATTEMPTS) {
            System.out.print("\n\tAre you sure you want to change the password (Y/N): ");
            String change_password_confirmation = console.readLine().trim();

            if (change_password_confirmation.equalsIgnoreCase("Y")) {
                System.out.print("\tEnter your current password: ");
                char[] current_password_chars = console.readPassword();
                String current_password = new String(current_password_chars); // Convert to String

                if (current_password.equals(customer.getPassword())) {
                    System.out.println("\n\tPassword matched.");
                    while (true) {
                        System.out.print("\tSet your new password: ");
                        char[] new_password_chars = console.readPassword();
                        new_password = new String(new_password_chars); // Convert to String

                        System.out.print("\tConfirm the new password: ");
                        char[] confirm_password_chars = console.readPassword();
                        confirm_password = new String(confirm_password_chars); // Convert to String

                        if (new_password.equals(confirm_password)) {
                            customer.setPassword(new_password);
                            saveAllCustomersToCSV();
                            System.out.println("\n\tThe password has been successfully changed.");
                            System.out.print("\t\tPress Enter key to continue.");
                            console.readLine(); // Wait for Enter key
                            console.flush();
                            registered_user_customer_item_category(username, customer, existing_cart);
                            return;
                        } else {
                            System.out.println("\n\tPasswords do not match. Please try again.");
                        }
                    }
                } else {
                    attemptCount++;
                    System.out.println("\n\tIncorrect current password. Attempts left: " + (MAX_ATTEMPTS - attemptCount));
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
                }
            } else if (change_password_confirmation.equalsIgnoreCase("N")) {
                registered_user_customer_item_category(username, customer, existing_cart);
                return;
            } else {
                System.out.println("\tInvalid input. Please enter 'Y' or 'N'.");
                System.out.print("\t\tPress Enter key to continue.");
                console.readLine();
                console.flush();
            }
        }

        // Handle case where attempts are exhausted
        if (attemptCount == MAX_ATTEMPTS) {
            System.out.println("\n\tMaximum attempts reached. Password change failed.\n");
            System.out.print("\t\tPress Enter key to continue.");
            console.readLine();
            console.flush();
        }
    }



    public void redeem_reward_points(Customer customer, List<Product> existing_cart) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        String promo_choice;
        double cashback;
        double reward_points;

        if (customer.getRewardPoint() <= 0) {
            System.out.println("\n\tYou do not have enough points to redeem rewards.");
            System.out.print("\t\tPress Enter key to continue.");
            console.readLine();
            console.flush();
            return;
        }

        while (true) {
            // Display the menu
            System.out.println("\t-------------------------------------------------");
            System.out.printf ("\t|         Your remaining Points: %-5.0fPts      |\n", customer.getRewardPoint());
            System.out.println("\t|   Cashback Reward!!! Convert Your Points Now  |");
            System.out.println("\t|                                               |");
            System.out.println("\t|        [1] 1 PT To 5 peso                     |");
            System.out.println("\t|        [2] 5 PT To 30 pesos                   |");
            System.out.println("\t|        [3] 10 PT To 60 pesos                  |");
            System.out.println("\t|        [4] 25 PT To 300 pesos                 |");
            System.out.println("\t|        [5] 50 PT To 600 pesos                 |");
            System.out.println("\t|                                               |");
            System.out.println("\t|        [0] Go back                            |");
            System.out.println("\t-------------------------------------------------");
            System.out.print("\n\tEnter choice: ");
            promo_choice = console.readLine();

            switch (promo_choice) {
                case "0":
                    registered_user_customer_item_category(customer.getUsername(), customer, existing_cart);
                    return;
                case "1":
                    cashback = 5;
                    reward_points = 1;
                    break;
                case "2":
                    cashback = 30;
                    reward_points = 5;
                    break;
                case "3":
                    cashback = 60;
                    reward_points = 10;
                    break;
                case "4":
                    cashback = 300;
                    reward_points = 25;
                    break;
                case "5":
                    cashback = 600;
                    reward_points = 50;
                    break;
                default:
                    System.out.println("\n\tWrong input");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
                    continue; // Restart the loop
            }

            while (true) {
                System.out.print("\n\tAre you sure you want to proceed to redeem points? (Y/N): ");
                String confirm_redeem = console.readLine().trim().toUpperCase();

                switch (confirm_redeem) {
                    case "Y":
                        redeem_point_reward_process(cashback, reward_points, customer);
                        System.out.print("\t\tPress Enter key to continue.");
                        console.readLine(); //used for press any key to continue
                        console.flush();
                        return;
                    case "N":
                        System.out.println("\n\tRedeem cancelled.");
                        System.out.print("\t\tPress Enter key to continue.");
                        console.readLine(); //used for press any key to continue
                        console.flush();
                        registered_user_customer_item_category(customer.getUsername(), customer, existing_cart);
                        return;
                    default:
                        System.out.println("\n\tWrong input");
                        System.out.print("\t\tPress Enter key to continue.");
                        console.readLine(); //used for press any key to continue
                        console.flush();
                }
            }
        }
    }


    public void redeem_point_reward_process(double cashBack, double rewardPoints, Customer customer) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        customer.setRewardPoint(customer.getRewardPoint() - rewardPoints);
        customer.setBalance(customer.getBalance() + cashBack);

        saveAllCustomersToCSV();

        System.out.println("\n\tRedemption successful! Cashback: " + cashBack + " pesos.");
        System.out.printf("\n\tAmount added successfully. New balance: %.2f\n", customer.getBalance());
        System.out.print("\t\tPress Enter key to continue.");
        console.readLine(); //used for press any key to continue
        console.flush();
    }


    public void view_customer_list() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        load_customers_from_CSV();

        if (customers.isEmpty()) {
            System.out.println("\n\tNo customer found in the system.");
            System.out.println("\n\tPress Enter to continue...");
            console.readLine();
            return;
        }

        System.out.println("\n\t=========================================================");
        System.out.println("\t|                    Customer List                      |");
        System.out.println("\t=========================================================");
        System.out.printf("\t%-10s %-13s %-16s %-9s %-12s %-14s %-12s%n",
                "Username", "Phone", "Payment Method", "Balance", "Transactions", "Reward Points", "Total Spent");
        System.out.println("\t-------------------------------------------------");

        for (Customer customer : customers) {
            System.out.printf("\t%-10s %-13s %-16s %-12s %-12s %-14s %-12s%n",
                    customer.getUsername(),
                    customer.getPhoneNumber(),
                    customer.getPaymentMethod(),
                    customer.getBalance(),
                    customer.getTransaction(),
                    customer.getRewardPoint(),
                    customer.getTotal_spent());
        }

        System.out.print("\n\tPress Enter to continue...");
        console.readLine();
        console.flush();
    }


    public void delete_customer() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        if (customers.isEmpty()) {
            System.out.println("\n\tNo Customer found in the system.");
            System.out.print("\t\tPress Enter to continue...");
            console.readLine();
            console.flush();
            return;
        }

        while (true) {
            view_customer_list();
            System.out.print("\n\tEnter Username to delete (0 to cancel): ");
            try {
                String user_name = console.readLine();

                if (user_name.equalsIgnoreCase("0")) {
                    System.out.println("\n\tDeletion cancelled.");
                    System.out.print("\n\tPress Enter to continue...");
                    console.readLine();
                    return;
                }

                Customer customer_to_delete = null;
                for (Customer customer : customers) {
                    if (customer.getUsername().equals(user_name)) {
                        customer_to_delete = customer;
                        break;
                    }
                }

                if (customer_to_delete == null) {
                    System.out.println("\n\tEmployee ID not found. Please try again.");
                    System.out.print("\n\tPress Enter to continue...");
                    console.readLine();
                    continue;
                }

                while (true) {
                    System.out.println("\n\tAre you sure you want to delete employee:");
                    System.out.println("\tUser Name: " + customer_to_delete.getUsername());
                    System.out.println("\tPhone Number: " + customer_to_delete.getPhoneNumber());
                    System.out.println("\tPayment Method: " + customer_to_delete.getPaymentMethod());
                    System.out.println("\tBalance: " + customer_to_delete.getBalance());
                    System.out.println("\tTotal Transaction: " + customer_to_delete.getTransaction());
                    System.out.println("\tReward Points: " + customer_to_delete.getTransaction());
                    System.out.println("\tTotal Spent: " + customer_to_delete.getTotal_spent());

                    System.out.print("\n\t[Y] to confirm, [N] to cancel: ");
                    String confirmation = console.readLine();
                    console.flush();

                    if (confirmation.equalsIgnoreCase("Y")) {
                        customers.remove(customer_to_delete);
                        saveAllCustomersToCSV();
                        System.out.println("\n\tEmployee successfully deleted.");
                        System.out.print("\t\tPress Enter to continue...");
                        console.readLine();
                        console.flush();
                        return;
                    } else if (confirmation.equalsIgnoreCase("N")) {
                        System.out.println("\n\tDeletion cancelled.");
                        System.out.print("\t\tPress Enter to continue...");
                        console.readLine();
                        console.flush();
                        return;
                    } else {
                        System.out.println("\n\tInvalid Input");
                        System.out.print("\t\tPress Enter to continue...");
                        console.readLine();
                        console.flush();
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("\n\tInvalid input. Please enter a valid customer Username.");
                System.out.print("\t\tPress Enter to continue...");
                console.readLine();
                console.flush();
            }
        }
    }


    public void manager_reset_customer_password() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        if (customers.isEmpty()) {
            System.out.println("\n\tNo Customer found in the system.");
            System.out.print("\t\tPress Enter to continue...");
            console.readLine();
            console.flush();
            return;
        }

        while (true) {
            view_customer_list();
            System.out.print("\n\tEnter Username to delete (0 to cancel): ");
            try {
                String user_name = console.readLine();

                if (user_name.equalsIgnoreCase("0")) {
                    System.out.println("\n\tPassword reset cancelled.");
                    System.out.print("\n\tPress Enter to continue...");
                    console.readLine();
                    console.flush();
                    return;
                }

                Customer customer_to_reset = null;
                for (Customer customer : customers) {
                    if (customer.getUsername().equals(user_name)) {
                        customer_to_reset = customer;
                        break;
                    }
                }

                if (customer_to_reset == null) {
                    System.out.println("\n\tCustomer Username not found. Please try again.");
                    System.out.print("\n\tPress Enter to continue...");
                    console.readLine();
                    console.flush();
                    continue;
                }

                System.out.println("\n\tResetting password for customer:");
                System.out.println("\tUser Name: " + customer_to_reset.getUsername());
                System.out.println("\tPhone Number: " + customer_to_reset.getPhoneNumber());
                System.out.println("\tPayment Method: " + customer_to_reset.getPaymentMethod());
                System.out.println("\tBalance: " + customer_to_reset.getBalance());
                System.out.println("\tTotal Transaction: " + customer_to_reset.getTransaction());
                System.out.println("\tReward Points: " + customer_to_reset.getTransaction());
                System.out.println("\tTotal Spent: " + customer_to_reset.getTotal_spent());

                String newPassword;
                String confirmPassword;
                do {
                    System.out.print("\n\tEnter (0 to cancel): ");
                    newPassword = input_password(console, "\n\tEnter new password: ");
                    if (newPassword.equalsIgnoreCase("0")) {
                        System.out.println("\n\tPassword reset cancelled.");
                        System.out.print("\n\tPress Enter to continue...");
                        console.readLine();
                        console.flush();
                        return;
                    }
                    confirmPassword = input_password(console, "\tConfirm new password: ");

                    if (!newPassword.equals(confirmPassword)) {
                        System.out.println("\n\tPasswords do not match. Please try again.");
                    }
                } while (!newPassword.equals(confirmPassword));

                customer_to_reset.setPassword(newPassword);
                saveAllCustomersToCSV();

                System.out.println("\n\tPassword successfully reset.");
                System.out.print("\n\tPress Enter to continue...");
                console.readLine();
                console.flush();
                return;

            } catch (NumberFormatException e) {
                System.out.println("\n\tInvalid input. Please enter a valid customer Username.");
                System.out.print("\t\tPress Enter to continue...");
                console.readLine();
                console.flush();
            }
        }
    }


}
