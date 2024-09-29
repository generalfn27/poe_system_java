package cashier;

import User_Types.UserType;
import Process.CashierProcess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Cashier {
    private static final int MAX_ATTEMPTS = 3; // Maximum login attempts
    private final CashierProcess cashier_process; // Class-level instance variable for CashierProcess

    public Cashier() {
        this.cashier_process = new CashierProcess(); // Assign to the class-level variable
    }


    public void user_cashier() {
        cashier_login();
    }

    private void cashier_login() {
        Scanner scanner = new Scanner(System.in);
        int attempt_count = 0; // Count failed login attempts
        boolean valid = false; // Indicate if login is valid

        while (!valid && attempt_count < MAX_ATTEMPTS) {
            System.out.println("===================================");
            System.out.println("|                                 |");
            System.out.println("|          Cashier Login          |");
            System.out.println("|                                 |");
            System.out.println("===================================");
            System.out.println("\tEnter Credentials\n");

            System.out.print("\tEnter username: ");
            String inputUsername = scanner.nextLine();

            System.out.print("\tEnter password: ");
            String inputPassword = scanner.nextLine(); // Capture password input directly without hiding

            // Validate login
            if (validate_cashier_login(inputUsername, inputPassword)) {
                valid = true; // Set flag to exit loop
                System.out.println("\tLogin successful!");
                cashier_dashboard();
            } else {
                attempt_count++;
                cashier_increment_attempts(attempt_count, inputUsername);

                if (attempt_count >= MAX_ATTEMPTS) {
                    System.out.println("Maximum attempts reached. Exiting.");
                    break;
                }
            }
        }
    }


    private boolean validate_cashier_login(String inputUsername, String inputPassword) {
        // Add logic to validate the cashier's credentials here

        //madaming changes pa dito if gagawa ng multiple accounts for cashier employees

        // Default username
        String username = "admin";
        // Default password
        String password = "password";
        return inputUsername.equals(username) && inputPassword.equals(password);
    }


    private void cashier_increment_attempts(int attemptCount, String username) {
        System.out.println("Invalid login attempt #" + attemptCount + " for user: " + username);
    }


    //next development dapat pinapasa na username sa parameter as welcome sa employee
    private void cashier_dashboard() {
        Scanner scanf = new Scanner(System.in);
        String choice;


        while (true) {
            System.out.println("\t[1] Process Queue Orders");
            System.out.println("\t[2] hindi pa alam ano dapat talaga dito");
            System.out.println("\t[0] Exit");

            System.out.print("\n\n\tEnter Here: ");
            choice = scanf.nextLine();

            switch (choice) {
                case "1":
                    selecting_queue_list_to_process();
                    break;

                case "2":
                    // Call method to modify counter items (under development)
                    break;

                case "0":
                    boolean logout_confirmed = handle_logout(scanf);
                    if (logout_confirmed) {
                        return; // Exit the method if logout is confirmed
                    }
                default:
                    // Handle invalid input
                    System.out.println("\n\tInvalid input. Try again...");
                    break;
            }
        }
    }


    public void selecting_queue_list_to_process() {
        Scanner scanner = new Scanner(System.in);
        File directory = new File(".");
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().startsWith("queue_number_"));
        List<String> csvFiles = new ArrayList<>();

        System.out.println("=======================================");
        System.out.println("|                                     |");
        System.out.println("|          Cashier Dashboard          |");
        System.out.println("|                                     |");
        System.out.println("=======================================\n");
        System.out.println("\n\nCSV Files to Open:");

        if (files != null) {
            for (File file : files) {
                csvFiles.add(file.getName());
                System.out.println("[" + (csvFiles.size()) + "] " + file.getName());
            }
        }

        if (!csvFiles.isEmpty()) {
            System.out.println("\t[0] 0 para return dun sa logout or re select ewan na");
            System.out.print("\tEnter the number of the file to open: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice > 0 && choice <= csvFiles.size()) {
                String selectedFile = csvFiles.get(choice - 1);
                System.out.println("\tYou selected: " + selectedFile);
                cashier_process.read_order_from_csv(selectedFile); // Now accessible
                cashier_process.transfer_cart_to_counter();
                cashier_process_choice();
            }
        } else {
            System.out.println("\tNo CSV files found.");
            System.out.println("\tNo Queue Order to process.");
        }
    }



    private void cashier_process_choice() {
        System.out.flush();
        Scanner scanf = new Scanner(System.in);
        char choice;
        char confirm;

        while (true) { // Loop until the cashier chooses to exit
            // Reset the counter, total items, and total price
            // Select the queue list to process

            System.out.println("\t[1] Proceed to pay");
            System.out.println("\t[2] Modify Counter Items (under development)");
            System.out.println("\t[0] Go back");

            System.out.print("\n\n\tEnter Here: ");
            choice = scanf.next().charAt(0); // Read choice

            switch (choice) {
                case '1':

                    cashier_process.process_payment();

                    break;

                case '2':
                    // Call method to modify counter items (under development)
                    break;

                case '3':
                    // Call method to view and select receipt from list
                    break;

                case '0':
                    return;
                default:
                    // Handle invalid input
                    System.out.println("\n\tInvalid input. Try again...");
                    break;
            }
        }
    }


    private boolean handle_logout(Scanner scanf) {
        while (true) {
            System.out.println("\n\n\n\n\tAre you sure you want to Logout and go back to menu?\n");
            System.out.println("\t[Y] for Yes  [N] for No: ");

            String exit_confirmation = scanf.next().trim();

            if (exit_confirmation.equalsIgnoreCase("Y")) {
                UserType.user_type_menu();
                return true;
            } else if (exit_confirmation.equalsIgnoreCase("N")) {
                return false;
            } else {
                System.out.println("\tInvalid input. Please enter Y or N.\n");
            }
        }
    }



}
