package Shopper;

import User_Types.*;
import Process.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserCustomer {
    private final List<Customer> customers = new ArrayList<>();

    public UserCustomer() {
        // Load customers from CSV file when the program starts
        load_customers_from_CSV();
    }

    public void user_customer_menu() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("\n\t----------------------------------------------");
            System.out.println("\t|              Welcome Customer!                 |");
            System.out.println("\t|             Log in  |  Register                |");
            System.out.println("\t|                                                |");
            System.out.println("\t|        [1] Login                               |");
            System.out.println("\t|        [2] Continue as Guest                   |");
            System.out.println("\t|        [3] Register                            |");
            System.out.println("\t|        [0] Go Back                             |");
            System.out.println("\t|                                                |");
            System.out.println("\t----------------------------------------------");
            System.out.print("\n\tEnter Here: ");
            choice = scanf.nextLine();

            switch (choice) {
                case "1":
                    customer_login();
                    break;
                case "2":
                    guest_customer_item_category(); // Pass the guest customer to the item category function
                    break;
                case "3":
                    customerRegister();
                    break;
                case "0":
                    UserType.user_type_menu();
                    return;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
            }
        }
    }


    public void guest_customer_item_category() {
        Scanner scanf = new Scanner(System.in);
        String item_category;

        // Create an instance of OrderProcessor to handle the guest's order
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
            item_category = scanf.nextLine();

            // Variable to hold the products in the chosen category
            List<Product> selected_products = null;

            switch (item_category) {
                case "0":
                    if (!OrderProcessor.cart.isEmpty()) {
                        boolean cancel = true;
                        while (cancel) {
                            System.out.print("\n\tIf you go back, the cart will get empty. Do you still want to go back? (Y/N): ");
                            String go_back = scanf.nextLine().trim().toUpperCase();

                            if (go_back.equals("Y")) { user_customer_menu(); } // Return to main menu
                            else if (go_back.equals("N")) { cancel = false; }
                        }
                    }
                    if (OrderProcessor.cart.isEmpty()) { return; }
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
                        OrderProcessor.modify_menu_process();
                        break;
                    }
                    break;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
                    continue;
            }

            if (selected_products != null && !selected_products.isEmpty()) {
                // After displaying products, process the order by asking for product code
                order_processor.process_customer_order(selected_products);
                OrderProcessor.modify_menu_process();
            } else {
                if (!item_category.equals("9")){ // para hindi to lumalabas dahil di naman list ang case 9
                    System.out.println("No products available in this category.");
                }
            }
        }
    }


    // Customer registration function
    private void customerRegister() {
        Scanner scanf = new Scanner(System.in);
        final int MAX_CUSTOMERS = 100;
        if (customers.size() >= MAX_CUSTOMERS) {
            System.out.println("\n\tCustomer limit reached. Cannot register new customers.");
            return;
        }

        Customer newCustomer = new Customer();
        String confirmPassword = "";
        String confirmPin = "";
        float initialFunds;
        String pinCode;
        String phoneNumber;
        String paymentMethod;
        double transaction = 1;

        System.out.println("\n\t----------------------------------------------");
        System.out.println("\t|        suggestion another scanf para                  |");
        System.out.println("\t|            sa agreement consent sa data etc.            |");
        System.out.print("\n\tEnter Username: ");
        String username = scanf.nextLine();

        boolean not_exist = false;
        while (!not_exist) {
            not_exist = true;  // assume username doesn't exist until proven otherwise
            // Check if the username already exists
            for (Customer customer : customers) {
                if (customer.getUsername().equals(username)) {
                    System.out.println("\n\tUsername already exists. Please choose a different username.");
                    System.out.print("\n\tEnter Username: ");
                    username = scanf.nextLine();
                    not_exist = false;  // username exists, so set flag to false
                    break;  // break out of the loop to recheck the new username
                }
            }
        }
        newCustomer.setUsername(username);

        // Password input naka ibang method para sa future changes para mas madali mag asterisk
        String password = inputPassword(scanf, "\n\tEnter Password: ");
        newCustomer.setPassword(password);

        while (!newCustomer.getPassword().equals(confirmPassword)) {
            confirmPassword = inputPassword(scanf, "\tConfirm Password: ");
            if (!newCustomer.getPassword().equals(confirmPassword)) {
                System.out.println("\n\tPasswords do not match. Please try again.");
            }
        }

        while (true) {
            System.out.print("\n\tEnter Phone Number: ");
            phoneNumber = scanf.nextLine();

            //  Check if the entered Phone Number starts with "09" and is exactly 11 digits
            //  ^: This is a beginning-of-line anchor, which means that the pattern must match from the very start of the string.
            //  09: This simply matches the literal characters "09".
            //  \d: This matches any single digit character (0-9).
            //  {9}: This is a quantifier that specifies that the preceding element (\d) must occur exactly 9 times.
            //  $: This is an end-of-line anchor, which means that the pattern must match up to the very end of the string.

            if (phoneNumber.matches("^09\\d{9}$")) { break; }
            else { System.out.println("\tInvalid phone number. Please enter an 11-digit number starting with '09'.");   }
        }
        newCustomer.setPhoneNumber(phoneNumber);

        // Use a do-while loop for more efficient validation para mag ulit ng display if mali
        do {
            System.out.print("\tIs the phone number #"+ newCustomer.getPhoneNumber()+" for GCash (G) or PayMaya (P)? ");
            String paymentChoice = scanf.nextLine().trim().toUpperCase();
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

        while (true) {
            System.out.print("\n\tEnter a 4-digit PIN code: ");
            pinCode = scanf.nextLine();

            // Check if the entered PIN is exactly 4 digits
            // \\d dahil integer
            // 4 ay ung bilang so dapat mag mmatch ung input na int
            if (pinCode.matches("\\d{4}")) { break; }
            else { System.out.println("\tInvalid PIN. Please enter a 4-digit number."); }
        }
        newCustomer.setPinCode(pinCode);

        while (!newCustomer.getPinCode().equals(confirmPin)) {
            confirmPin = inputPin(scanf);
            if (!newCustomer.getPinCode().equals(confirmPin)) {
                System.out.println("\n\tPin do not match. Please try again.");
            }
        }

        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("\n\tEnter initial amount to add to your account: ");
                initialFunds = Float.parseFloat(scanf.nextLine());
                newCustomer.setBalance(initialFunds);
                validInput = true; // Input is valid, exit the loop
            } catch (NumberFormatException e) {
                System.out.println("\tInvalid input. Please enter a valid number.");
            }
        }
        newCustomer.setTransaction(transaction);

        save_customer_to_file(newCustomer);

        // Add the customer to the in-memory list
        customers.add(newCustomer);

        System.out.println("\n\tCongratulations! Your registration was successful.  " + newCustomer.getUsername() +".\n\t\t\tPress Enter key to start exploring!\n");
        scanf.nextLine(); //used for press any key to continue
        // para pause muna sa bawat pagkakamli para isipin muna sa susunod tama

        registered_user_customer_item_category(newCustomer.getUsername(), newCustomer);
    }


    // Helper method to input hidden password pero dapat magiging ****
    //shortcut sa pag fill ups thanks sa AI
    private String inputPassword(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();  // Simplified for Java, as hiding characters is more complex
    }

    private String inputPin(Scanner scanner) {
        System.out.print("\tConfirm Pin: ");
        return scanner.nextLine();  // Simplified for Java, as hiding characters is more complex
    }


    // Save a single customer to the CSV file
    public void saveAllCustomersToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.csv"))) {
            writer.write("Username,Password,PhoneNumber,PaymentMethod,Balance,PIN,Transaction\n");
            for (Customer customer : customers) {
                // Debug: Print customer information that will be saved
                //System.out.println("\tSaving customer: " + customer.getUsername() + " with balance: " + customer.getBalance());

                writer.write(customer.getUsername() + "," +
                                customer.getPassword() + "," +
                                customer.getPhoneNumber() + "," +
                                customer.getPaymentMethod() + "," +
                                customer.getBalance() + "," +
                                customer.getPinCode()+ "," +
                                customer.getTransaction());
                                writer.newLine();
            }
            System.out.println("\n\tCustomer data saved to CSV.");
        } catch (IOException e) {
            System.out.println("\tError saving customer data: " + e.getMessage());
        }
    }


    private void save_customer_to_file(Customer customer) {
        File file = new File("customers.csv");

        // Check if the file exists before opening the writer
        boolean fileExists = file.exists();

        try (FileWriter writer = new FileWriter(file, true)) {
            // Write header only if the file doesn't exist (i.e., it's a new file)
            if (!fileExists) {
                writer.write("Username,Password,PhoneNumber,PaymentMethod,Balance,PIN,Transaction\n");
            }
            // Write customer data
            writer.append(customer.getUsername()).append(",");
            writer.append(customer.getPassword()).append(",");
            writer.append(customer.getPhoneNumber()).append(",");
            writer.append(customer.getPaymentMethod()).append(",");
            writer.append(String.valueOf(customer.getBalance())).append(",");
            writer.append(customer.getPinCode()).append(",");
            writer.append(String.valueOf(customer.getTransaction())).append("\n");

        } catch (IOException e) {
            // Handle exceptions gracefully
            System.err.println("Error writing customer data to file: " + e.getMessage());
        }
    }


    // Load all customers from the CSV file
    private void load_customers_from_CSV() {
        // CSV file name
        String CUSTOMER_CSV_FILE = "customers.csv";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CUSTOMER_CSV_FILE))) {
            String line;
            // Skip the field name / header line
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) { // Expect 7 fields: username, password, phone number, payment method, balance, PIN, transaction
                    Customer customer = new Customer();
                    customer.setUsername(data[0]);
                    customer.setPassword(data[1]);
                    customer.setPhoneNumber(data[2]);
                    customer.setPaymentMethod(data[3]);
                    customer.setBalance(Float.parseFloat(data[4]));
                    customer.setPinCode(data[5]);
                    customer.setTransaction(Float.parseFloat(data[6]));
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
        Scanner scanf = new Scanner(System.in);
        int attempt_count = 0;
        boolean login_successful = false;
        final int MAX_ATTEMPTS = 3;

        // Load customers from the CSV if not already loaded
        if (customers.isEmpty()) {
            load_customers_from_CSV();
        }

        // Loop until login is successful or maximum attempts reached
        // Maximum number of login attempts
        while (attempt_count < MAX_ATTEMPTS) {
            System.out.println("\n\t===================================");
            System.out.println("\t|                                 |");
            System.out.println("\t|          Shopper Login          |");
            System.out.println("\t|                                 |");
            System.out.println("\t===================================\n");
            System.out.print("\tEnter Username: ");
            String username = scanf.nextLine();

            // Get hidden password input
            String password = inputPassword(scanf, "\tEnter Password: ");

            // Check if the username and password match any customer record
            for (Customer customer : customers) {
                if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                    System.out.println("\n\tLogin successful.\n");
                    login_successful = true;
                    registered_user_customer_item_category(customer.getUsername(), customer);
                    break;
                }
            }

            if (login_successful) { break; }
            else {
                attempt_count++;
                System.out.println("\n\tInvalid username or password. Attempts left: " + (MAX_ATTEMPTS - attempt_count));
            }
        }
        if (!login_successful) { System.out.println("\n\tMaximum login attempts reached. Please try again later."); }
    }

    // Placeholder method for registered customer actions after login
    public void registered_user_customer_item_category(String username, Customer customer) {
        Scanner scanf = new Scanner(System.in);
        String item_category;

        // Create an instance of OrderProcessor to handle the registered user's order
        OrderProcessor order_processor = new OrderProcessor();

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
            if (!OrderProcessor.cart.isEmpty()) {
            System.out.println("\t|        [12] Modify Items in cart            |"); }
            System.out.println("\t|                                            |");
            System.out.println("\t|        [0] Log Out                         |");
            System.out.println("\t|                                            |");
            System.out.println("\t----------------------------------------------");
            System.out.print("\t|        Enter here: ");
            item_category = scanf.nextLine();

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
                    add_funds(username);
                    break;
                case "10":
                    display_purchase_history_menu(customer);
                    //registered_user_customer_item_category(username, customer);
                    break;
                case "11":
                    registered_customer_change_password(username, customer);
                    break;
                case "12":
                    if (!OrderProcessor.cart.isEmpty()) {
                        OrderProcessor.registered_user_modify_menu_process(customer);
                        break;
                    }
                    break;
                case "0":
                    while (true) {
                        System.out.println("\n\n\tAre you sure you want to Logout and go back to menu?\n");
                        System.out.print("\t[Y] for Yes  [N] for No: ");
                        String exit_confirmation = scanf.nextLine().trim();

                        if (exit_confirmation.equalsIgnoreCase("Y")) {
                            user_customer_menu();
                            return;
                        } else if (exit_confirmation.equalsIgnoreCase("N")) {
                            registered_user_customer_item_category(username, customer);
                            break;
                        } else {
                            System.out.println("\n\tAn error has occurred");
                            System.out.println("\t\tPress Enter key to continue.\n");
                            scanf.nextLine(); //used for press any key to continue
                        }
                    }
                    break;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
                    continue;
            }

            // After displaying products, process the order by asking for product code
            if (selected_products != null && !selected_products.isEmpty()) {
                order_processor.process_customer_order(selected_products);
                OrderProcessor.registered_user_modify_menu_process(customer);
            } else {
                if (!item_category.equals("12")){ // para hindi to lumalabas dahil di naman list ang case 12
                    //dapat pag 12 11 10 9
                    System.out.println("No products available in this category.");
                }
            }
        }
    }

    // Add funds to customer's account
    private void add_funds(String username) {
        Scanner scanf = new Scanner(System.in);
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                float amount = 0;
                boolean validInput = false;

                // Keep asking until a valid number is entered
                while (!validInput) {
                    try {
                        System.out.print("\n\tEnter amount to add: ");
                        amount = Float.parseFloat(scanf.nextLine());
                        validInput = true; // Input is valid, exit the loop
                    } catch (NumberFormatException e) {
                        System.out.println("\tInvalid input. Please enter a valid number.");
                    }
                }

                customer.setBalance(customer.getBalance() + amount);
                saveAllCustomersToCSV();

                System.out.printf("\n\tFunds added successfully. New balance: %.2f\n", customer.getBalance());
                System.out.println("\t\tPress Enter key to continue.\n");
                scanf.nextLine();
                //registered_user_customer_item_category(username, customer);
            }
        }
        System.out.println("\n\tUsername not found. Please try again.");
    }


    // Deduct funds from customer's account
    public void minus_funds(String username, Double amount) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                // Deduct the amount from the customer's balance
                customer.setBalance(customer.getBalance() - amount);

                // Debug: Print balance after deduction
                System.out.printf("\n\tFunds deducted successfully. New balance: %.2f\n", customer.getBalance());

                // Save all customers back to the CSV after updating the balance
                saveAllCustomersToCSV(); // Ensure this works as expected
                return;
            }
        }
        System.out.println("\n\tUsername not found. Please try again.");
    }


    public Customer get_customer_by_username(String username) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }


    private void display_purchase_history_menu(Customer customer) {
        Scanner scanf = new Scanner(System.in);
        File directory = new File(".");
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().startsWith(customer.getUsername()));
        List<String> csvFiles = new ArrayList<>();

        System.out.println("\t=======================================");
        System.out.println("\t|                                     |");
        System.out.println("\t|           Purchase History          |");
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
                            return;
                        }

                        String selectedFile = csvFiles.get(choice - 1);
                        System.out.println("\tYou selected: " + selectedFile);
                        read_history_from_csv(selectedFile);
                        System.out.println("\tPress Enter key to continue.\n");
                        scanf.nextLine();
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
            System.out.println("\tPress Enter key to continue.\n");
            scanf.nextLine();
            System.out.println("\tReturning to previous menu.");
        }
    }

    public void read_history_from_csv(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
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

    private void registered_customer_change_password(String username, Customer customer) {
        Scanner scanf = new Scanner(System.in);
        String new_password;
        String confirm_password = "";
        int attemptCount = 0;
        final int MAX_ATTEMPTS = 3;

        while (attemptCount < MAX_ATTEMPTS) {

            System.out.print("\n\tAre you sure changing password (Y/N): ");
            String change_password_confirmation = scanf.nextLine().trim();

            if (change_password_confirmation.equalsIgnoreCase("Y")) {
                System.out.print("\tEnter your current password: ");
                String current_password = scanf.nextLine();

                if (current_password.equals(customer.getPassword())) {
                    System.out.print("\tSet your new password: ");
                    new_password = scanf.nextLine();

                    while (!new_password.equals(confirm_password)) {
                        System.out.print("\tConfirm the new password: ");
                        confirm_password = scanf.nextLine();
                        if (!new_password.equals(confirm_password)) {
                            System.out.println("\n\tPasswords do not match. Please try again.");
                        }
                    }
                    customer.setPassword(new_password);
                    saveAllCustomersToCSV();
                    System.out.print("\n\tThe password is successfully changed.\n");
                    scanf.nextLine();
                    registered_user_customer_item_category(username, customer);
                    break;
                } else {
                    attemptCount++;
                    System.out.println("\n\tIncorrect current password. Attempts left: " + (MAX_ATTEMPTS - attemptCount));
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine();
                }

            } else if (change_password_confirmation.equalsIgnoreCase("N")) {
                registered_user_customer_item_category(username, customer);
                return;
            } else {
                System.out.println("\t\tPress Enter key to continue.\n");
                scanf.nextLine();
            }

        }

        // Handle case where attempts are exhausted
        if (attemptCount == MAX_ATTEMPTS) {
            System.out.println("\n\tMaximum attempts reached. Password change failed.\n");
            System.out.println("\t\tPress Enter key to continue.\n");
            scanf.nextLine();
        }
    }



}
