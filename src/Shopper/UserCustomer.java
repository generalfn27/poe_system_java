package Shopper;

import User_Types.*;
import Process.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserCustomer {
    private final List<Customer> customers = new ArrayList<>();
    private final String CSV_FILE = "customers.csv"; // CSV file name


    public UserCustomer() {
        // Load customers from CSV file when the program starts
        loadCustomersFromCSV();
    }

    public void user_customer_menu() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("\n\t----------------------------------------------");
            System.out.println("\t|              Welcome Customer!             |");
            System.out.println("\t|             Log in  |  Register            |");
            System.out.println("\t|                                                |");
            System.out.println("\t|        [1] Login                               |");
            System.out.println("\t|        [2] Continue as Guest                   |");
            System.out.println("\t|        [3] Register                            |");
            System.out.println("\t|        [0] Go Back                             |");
            System.out.println("\t|                                                |");
            System.out.println("\t----------------------------------------------");
            System.out.print("\n\tEnter Here: ");
            choice = scanf.next();

            // Consume the leftover newline from nextInt
            scanf.nextLine();

            switch (choice) {
                case "1":
                    // Call the login function (assuming it exists)
                    customer_login();
                    break;
                case "2":
                    // Handle the guest case
                    Customer guest = new Customer("Guest"); // Guest with default name
                    guest_customer_item_category(guest); // Pass the guest customer to the item category function
                    break;
                case "3":
                    // Call the registration function (assuming it exists)
                    customerRegister();
                    break;
                case "0":
                    UserType.user_type_menu();
                default:
                    System.out.println("\n\tInvalid input. Try again...");
                    // Implement a method for pressAnyKey() if needed
            }
        }
    }


    // Placeholder for the guest customer item category function
    public void guest_customer_item_category(Customer guest) {
        Scanner scanf = new Scanner(System.in);
        String item_category;

        // Create an instance of OrderProcessor to handle the guest's order
        OrderProcessor order_processor = new OrderProcessor();

        do {
            System.out.println("\n\nBrowsing as guest: " + guest.getUsername());
            System.out.println("\n\t----------------------------------------------");
            System.out.println("\t|              Welcome Customer!             |");
            System.out.println("\t|             What do you want to browse?    |");
            System.out.println("\t|                                            |");
            System.out.println("\t|        [1] Beverages                       |");
            System.out.println("\t|        [2] Snacks                          |");
            System.out.println("\t|        [3] Canned Goods                    |");
            System.out.println("\t|        [4] Condiments                      |");
            System.out.println("\t|        [5] Dairy                           |");
            System.out.println("\t|        [6] Frozen Foods                    |");
            System.out.println("\t|        [7] Body Care & Beauty Care         |");
            System.out.println("\t|        [8] Detergents & Soaps              |\n");
            System.out.println("\t|        [0] Go Back                         |");
            System.out.println("\t|                                            |");
            System.out.println("\t----------------------------------------------");
            System.out.print("\t|        Enter here: ");

            item_category = scanf.nextLine();

            // Variable to hold the products in the chosen category
            List<Product> selected_products = null;

            switch (item_category) {
                case "0":
                    user_customer_menu();
                    return;  // Return to main menu
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
                default:
                    System.out.println("\nInvalid input. Try again...");
                    continue;
            }

            if (selected_products != null && !selected_products.isEmpty()) {
                // After displaying products, process the order by asking for product code
                order_processor.process_customer_order(selected_products);
                OrderProcessor.modify_menu_process();
            } else {
                System.out.println("No products available in this category.");
            }
        } while (true);
    }


    // Customer registration function
    public void customerRegister() {
        Scanner scanner = new Scanner(System.in);
        final int MAX_CUSTOMERS = 100;
        if (customers.size() >= MAX_CUSTOMERS) {
            System.out.println("\n\tCustomer limit reached. Cannot register new customers.");
            return;
        }

        Customer newCustomer = new Customer();
        String confirmPassword;
        float initialFunds;

        System.out.print("\n\tEnter Username: ");
        String username = scanner.nextLine();

        // Check if the username already exists
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                System.out.println("\n\tUsername already exists. Please choose a different username.");
                return;
            }
        }
        newCustomer.setUsername(username);

        // Password input
        String password = inputPassword(scanner, "Enter Password: ");
        newCustomer.setPassword(password);

        // Confirm password
        confirmPassword = inputPassword(scanner, "Confirm Password: ");
        if (!newCustomer.getPassword().equals(confirmPassword)) {
            System.out.println("\n\tPasswords do not match. Please try again.");
            return;
        }

        // Phone number
        System.out.print("\tEnter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        newCustomer.setPhoneNumber(phoneNumber);

        // Payment method choice
        String paymentMethod;
        System.out.print("\tIs the phone number for GCash (G) or PayMaya (P)? ");
        char paymentChoice = scanner.nextLine().toUpperCase().charAt(0);
        if (paymentChoice == 'G') {
            paymentMethod = "GCash";
        } else if (paymentChoice == 'P') {
            paymentMethod = "PayMaya";
        } else {
            System.out.println("\n\tInvalid choice. Please try again.");
            return;
        }
        newCustomer.setPaymentMethod(paymentMethod);

        // Prompt for 4-digit PIN code
        String pinCode;
        while (true) {
            System.out.print("\tEnter a 4-digit PIN code: ");
            pinCode = scanner.nextLine();

            // Check if the entered PIN is exactly 4 digits
            if (pinCode.matches("\\d{4}")) {
                break;
            } else {
                System.out.println("\tInvalid PIN. Please enter a 4-digit number.");
            }
        }
        newCustomer.setPinCode(pinCode);

        // Initial funds
        System.out.print("\tEnter initial amount to add to your account: ");
        initialFunds = scanner.nextFloat();
        scanner.nextLine(); // consume the leftover newline
        newCustomer.setBalance(initialFunds);

        // Save the customer to the CSV file
        saveCustomerToFile(newCustomer);

        // Add the customer to the in-memory list
        customers.add(newCustomer);

        System.out.println("\n\tRegistration successful. You can now log in.");
    }


    // Helper method to input hidden password
    private String inputPassword(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();  // Simplified for Java, as hiding characters is more complex
    }


    // Save a single customer to the CSV file
    public void saveAllCustomersToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            // Write the header first
            writer.write("Username,Password,PhoneNumber,PaymentMethod,Balance,PIN\n");

            // Loop through the customer list and save each customer
            for (Customer customer : customers) {
                writer.write(customer.getUsername() + "," +
                        customer.getPassword() + "," +
                        customer.getPhoneNumber() + "," +
                        customer.getPaymentMethod() + "," +
                        customer.getBalance() + "," +      // Ensure balance is saved as a string
                        customer.getPinCode() + "\n");     // Make sure to include the PIN
            }
        } catch (IOException e) {
            System.out.println("Error saving customers to CSV: " + e.getMessage());
        }
    }


    public void saveCustomerToFile(Customer customer) {
        File file = new File("customers.csv");

        // Check if the file exists before opening the writer
        boolean fileExists = file.exists();

        try (FileWriter writer = new FileWriter(file, true)) {
            // Write header only if the file doesn't exist (i.e., it's a new file)
            if (!fileExists) {
                writer.write("Username,Password,PhoneNumber,PaymentMethod,Balance,PIN\n");
            }
            // Write customer data
            writer.append(customer.getUsername()).append(",");
            writer.append(customer.getPassword()).append(",");
            writer.append(customer.getPhoneNumber()).append(",");
            writer.append(customer.getPaymentMethod()).append(",");
            writer.append(String.valueOf(customer.getBalance())).append(",");
            writer.append(customer.getPinCode()).append("\n");

        } catch (IOException e) {
            // Handle exceptions gracefully
            System.err.println("Error writing customer data to file: " + e.getMessage());
        }
    }


    // Load all customers from the CSV file
    private void loadCustomersFromCSV() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            // Skip the header line
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) { // Expect 6 fields: username, password, phone number, payment method, balance, PIN
                    Customer customer = new Customer();
                    customer.setUsername(data[0]);
                    customer.setPassword(data[1]);
                    customer.setPhoneNumber(data[2]);
                    customer.setPaymentMethod(data[3]);
                    customer.setBalance(Float.parseFloat(data[4]));
                    customer.setPinCode(data[5]);
                    customers.add(customer);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("CSV file not found. No customers loaded.");
        } catch (IOException e) {
            System.out.println("Error loading customers from file: " + e.getMessage());
        }
    }


    public void customer_login() {
        Scanner scanner = new Scanner(System.in);
        int attemptCount = 0;
        boolean loginSuccessful = false;

        // Load customers from the CSV if not already loaded
        if (customers.isEmpty()) {
            loadCustomersFromCSV();
        }

        // Loop until login is successful or maximum attempts reached
        // Maximum number of login attempts
        final int MAX_ATTEMPTS = 5;
        while (attemptCount < MAX_ATTEMPTS) {
            System.out.println("\n===================================");
            System.out.println("|                                 |");
            System.out.println("|          Shopper Login          |");
            System.out.println("|                                 |");
            System.out.println("===================================\n");

            System.out.print("\tEnter Username: ");
            String username = scanner.nextLine();

            // Get hidden password input
            String password = inputPassword(scanner, "\tEnter Password: ");

            // Check if the username and password match any customer record
            for (Customer customer : customers) {
                if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                    System.out.println("\n\tLogin successful.\n");
                    registered_user_customer_item_category(customer.getUsername(), customer.getBalance());
                    loginSuccessful = true;
                    break;
                }
            }

            if (loginSuccessful) {
                break;
            } else {
                attemptCount++;
                System.out.println("\n\tInvalid username or password. Attempts left: " + (MAX_ATTEMPTS - attemptCount));
            }
        }

        if (!loginSuccessful) {
            System.out.println("\n\tMaximum login attempts reached. Please try again later.");
        }
    }

    // Placeholder method for registered customer actions after login
    private void registered_user_customer_item_category(String username, double balance) {
        Scanner scanf = new Scanner(System.in);
        String item_category;

        // Create an instance of OrderProcessor to handle the guest's order
        OrderProcessor order_processor = new OrderProcessor();

        do {
            System.out.println("\n\t----------------------------------------------");
            System.out.println("\t|              Welcome Customer! " + username);
            System.out.println("\t|         Your remaining balance: " + balance + "...");
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
            System.out.println("\t|        [9] Add funds                       |\n");
            System.out.println("\t|        [0] Go Back                         |");
            System.out.println("\t|                                            |");
            System.out.println("\t----------------------------------------------");
            System.out.print("\t|        Enter here: ");

            item_category = scanf.nextLine();

            // Variable to hold the products in the chosen category
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
                    registered_user_customer_item_category(username, balance);
                    break;
                case "0":
                    boolean logout_confirmed = false;
                    while (!logout_confirmed) {
                        System.out.println("\n\n\n\n\tAre you sure you want to Logout and go back to menu?\n");
                        System.out.println("\t[Y] for Yes  [N] for No: ");

                        String exit_confirmation = scanf.nextLine().trim();

                        if (exit_confirmation.equalsIgnoreCase("Y")) {
                            return;
                        } else if (exit_confirmation.equalsIgnoreCase("N")) {
                            // Do nothing, stay in the loop and return to the menu
                            break;
                        } else {
                            System.out.println("\tInvalid input. Going back to menu.\n");
                            // Do nothing, stay in the loop and return to the menu
                        }
                    }
                    break;
                default:
                    System.out.println("\nInvalid input. Try again...");
                    continue;
            }

            if (selected_products != null && !selected_products.isEmpty()) {
                // After displaying products, process the order by asking for product code
                order_processor.process_customer_order(selected_products);
                OrderProcessor.modify_menu_process();
            }else {
                System.out.println("No products available in this category.");
            }
        } while (true);
    }

    // Add funds to customer's account
    public void add_funds(String username) {
        Scanner scanner = new Scanner(System.in);
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                System.out.print("\tEnter amount to add: ");
                float amount = scanner.nextFloat();
                scanner.nextLine(); // consume the leftover newline

                // Add the amount to the customer's balance
                customer.setBalance(customer.getBalance() + amount);

                // Save all customers back to the CSV after updating the balance
                saveAllCustomersToCSV();

                System.out.printf("\n\tFunds added successfully. New balance: %.2f\n", customer.getBalance());
                return;
            }
        }
        System.out.println("\n\tUsername not found. Please try again.");
    }

}
