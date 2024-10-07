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
            choice = scanf.nextLine();

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
                    System.out.println("\n\tAn error has occurred");
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
            }
        }
    }


    // Placeholder for the guest customer item category function
    public void guest_customer_item_category(Customer guest) {
        Scanner scanf = new Scanner(System.in);
        String item_category;

        // Create an instance of OrderProcessor to handle the guest's order
        OrderProcessor order_processor = new OrderProcessor();

        while (true) {
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
            List<Product> selected_products;

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
                System.out.println("No products available in this category.");
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
        float initialFunds;

        System.out.println("\n\t----------------------------------------------");
        System.out.println("\t|        suggestion another scanf para                  |");
        System.out.println("\t|            sa agreement consent sa data etc.            |");
        System.out.print("\n\tEnter Username: ");
        String username = scanf.nextLine();

        // Check if the username already exists
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                System.out.println("\n\tUsername already exists. Please choose a different username.");
                System.out.print("\n\tEnter Username: ");
                username = scanf.nextLine();
            }
        }
        newCustomer.setUsername(username);

        // Password input
        String password = inputPassword(scanf, "\n\tEnter Password: ");
        newCustomer.setPassword(password);

        while (!newCustomer.getPassword().equals(confirmPassword)) {
            confirmPassword = inputPassword(scanf, "\tConfirm Password: ");
            if (!newCustomer.getPassword().equals(confirmPassword)) {
                System.out.println("\n\tPasswords do not match. Please try again.");
            }
        }

        // Phone number
        //dapat 11digits at nag sstart sa 09 palagi
        System.out.print("\n\tEnter Phone Number: ");
        String phoneNumber = scanf.nextLine();
        newCustomer.setPhoneNumber(phoneNumber);

        String paymentMethod;
        System.out.print("\tIs the phone number #"+ newCustomer.getPhoneNumber()+" for GCash (G) or PayMaya (P)? ");
        char paymentChoice = scanf.nextLine().toUpperCase().charAt(0);

        // Use a do-while loop for more efficient validation
        do {
            if (paymentChoice == 'G') {
                paymentMethod = "GCash";
                break;
            } else if (paymentChoice == 'P') {
                paymentMethod = "PayMaya";
                break;
            } else {
                System.out.println("\n\tInvalid choice. Please try again.");
                paymentChoice = scanf.nextLine().toUpperCase().charAt(0);
            }
        } while (true);
        newCustomer.setPaymentMethod(paymentMethod);
        System.out.println("\tThe payment method of user: " + newCustomer.getUsername() + " is " + newCustomer.getPaymentMethod());

        String pinCode;
        while (true) {
            System.out.print("\n\tEnter a 4-digit PIN code: ");
            pinCode = scanf.nextLine();

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
        initialFunds = scanf.nextFloat();
        scanf.nextLine(); // consume the leftover newline
        newCustomer.setBalance(initialFunds);

        saveCustomerToFile(newCustomer);

        // Add the customer to the in-memory list
        customers.add(newCustomer);

        System.out.println("\n\tRegistration successful. You can now log in. Just press any key to continue.\n");
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


    // Save a single customer to the CSV file
    public void saveAllCustomersToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.csv"))) {
            writer.write("Username,Password,PhoneNumber,PaymentMethod,Balance,PIN\n");
            for (Customer customer : customers) {
                // Debug: Print customer information that will be saved
                //System.out.println("\tSaving customer: " + customer.getUsername() + " with balance: " + customer.getBalance());

                writer.write(customer.getUsername() + "," + customer.getPassword() + "," + customer.getPhoneNumber() + ","
                        + customer.getPaymentMethod() + "," + customer.getBalance() + "," + customer.getPinCode());
                writer.newLine();
            }
            System.out.println("\n\tCustomer data saved to CSV.");
        } catch (IOException e) {
            System.out.println("\tError saving customer data: " + e.getMessage());
        }
    }


    private void saveCustomerToFile(Customer customer) {
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
        // CSV file name
        String CUSTOMER_CSV_FILE = "customers.csv";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CUSTOMER_CSV_FILE))) {
            String line;
            // Skip the field name / header line
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
            System.out.println("\tCSV file not found. No customers loaded.");
        } catch (IOException e) {
            System.out.println("\tError loading customers from file: " + e.getMessage());
        }
    }


    private void customer_login() {
        Scanner scanf = new Scanner(System.in);
        int attemptCount = 0;
        boolean loginSuccessful = false;
        final int MAX_ATTEMPTS = 5;

        // Load customers from the CSV if not already loaded
        if (customers.isEmpty()) {
            loadCustomersFromCSV();
        }

        // Loop until login is successful or maximum attempts reached
        // Maximum number of login attempts
        while (attemptCount < MAX_ATTEMPTS) {
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
                    loginSuccessful = true;
                    registered_user_customer_item_category(customer.getUsername(), customer);
                    break;
                }
            }

            if (loginSuccessful) { break; }
            else {
                attemptCount++;
                System.out.println("\n\tInvalid username or password. Attempts left: " + (MAX_ATTEMPTS - attemptCount));
            }
        }

        if (!loginSuccessful) { System.out.println("\n\tMaximum login attempts reached. Please try again later."); }
    }

    // Placeholder method for registered customer actions after login
    public void registered_user_customer_item_category(String username, Customer customer) {
        Scanner scanf = new Scanner(System.in);
        String item_category;

        // Create an instance of OrderProcessor to handle the registered user's order
        OrderProcessor order_processor = new OrderProcessor();

        while (true) {
            System.out.println("\t----------------------------------------------");
            System.out.println("\t|           Welcome Customer! " + username);
            System.out.println("\t|     Your remaining balance: " + customer.getBalance() + "    |");
            System.out.println("\t|        What do you want to browse?       |");
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
            System.out.println("\t|                                            |");
            System.out.println("\t|        [0] Go Back                         |");
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
                    registered_user_customer_item_category(username, customer);
                    break;
                case "0":
                    while (true) {
                        System.out.println("\n\n\tAre you sure you want to Logout and go back to menu?\n");
                        System.out.println("\t[Y] for Yes  [N] for No: ");
                        String exit_confirmation = scanf.nextLine().trim();

                        if (exit_confirmation.equalsIgnoreCase("Y")) {
                            user_customer_menu();
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
                order_processor.registered_user_modify_menu_process(customer.getUsername());
                //registered_user_customer_item_category(username, customer);
            } else {
                System.out.println("No products available in this category.");
            }
        }
    }

    // Add funds to customer's account
    private void add_funds(String username) {
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




    public Customer getCustomerByUsername(String username) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

}
