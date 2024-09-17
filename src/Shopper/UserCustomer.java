package Shopper;

import User_Types.*;
import Process.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserCustomer {
    private UserType userType;

    private List<Customer> customers = new ArrayList<>();
    private final int MAX_CUSTOMERS = 100;
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
                    userType.user_type_menu();
                    return; // Go back to the previous menu
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
            System.out.println("\t|        Enter here: ");

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
            } else {
                System.out.println("No products available in this category.");
            }
            OrderProcessor.modify_menu_process();
        } while (true);
    }

    // Customer registration function
    public void customerRegister() {
        Scanner scanner = new Scanner(System.in);
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
        String phoneNumber;
        System.out.print("\tEnter Phone Number: ");
        phoneNumber = scanner.nextLine();
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

        System.out.println("\n\tRegistration successful. You can now log in.");
        //customerLogin();
    }


    // Add funds to customer's account
    public void addFunds(String username) {
        Scanner scanner = new Scanner(System.in);
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                System.out.print("\tEnter amount to add: ");
                float amount = scanner.nextFloat();
                scanner.nextLine(); // consume the leftover newline

                // Add the amount to the customer's balance
                customer.setBalance(customer.getBalance() + amount);

                // Save all customers back to the CSV
                saveAllCustomersToCSV();

                System.out.printf("\n\tFunds added successfully. New balance: %.2f\n", customer.getBalance());
                return;
            }
        }
        System.out.println("\n\tUsername not found. Please try again.");
    }

    // Helper method to input hidden password
    private String inputPassword(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();  // Simplified for Java, as hiding characters is more complex
    }

    // Save a single customer to the CSV file
    public void saveAllCustomersToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (Customer customer : customers) {
                writer.write(customer.getUsername() + "," +
                        customer.getPassword() + "," +
                        customer.getPhoneNumber() + "," +
                        customer.getPaymentMethod() + "," +
                        customer.getBalance() + "," +
                        customer.getPinCode() + "\n");  // Add PIN code
            }
        } catch (IOException e) {
            System.out.println("Error saving customers to CSV: " + e.getMessage());
        }
    }



    public void saveCustomerToFile(Customer customer) {
        File file = new File("customers.csv");

        try (FileWriter writer = new FileWriter(file, true)) {
            // Write header only if the file is empty or doesn't exist
            if (!file.exists() || file.length() == 0) {
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
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    Customer customer = new Customer();
                    customer.setUsername(data[0]);
                    customer.setPassword(data[1]);
                    customer.setPhoneNumber(data[2]);
                    customer.setPaymentMethod(data[3]);
                    customer.setBalance(Float.parseFloat(data[4]));

                    customers.add(customer);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("CSV file not found. No customers loaded.");
        } catch (IOException e) {
            System.out.println("Error loading customers from file: " + e.getMessage());
        }
    }


    public void customerLogin() {
        // Implement customer login functionality here
    }

}
