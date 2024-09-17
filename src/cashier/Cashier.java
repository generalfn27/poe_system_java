package cashier;

import java.util.Scanner;

public class Cashier {
    private static final int MAX_ATTEMPTS = 3; // Maximum login attempts

    public void user_cashier() {
        cashierLogin();
    }

    private void cashierLogin() {
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
                System.out.println("\tLogin successful!");
                valid = true; // Set flag to exit loop
                cashier_process_choice();
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
        // Default username
        String username = "admin";
        // Default password
        String password = "password";
        return inputUsername.equals(username) && inputPassword.equals(password);
    }

    private void cashier_increment_attempts(int attemptCount, String username) {
        System.out.println("Invalid login attempt #" + attemptCount + " for user: " + username);
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
            System.out.println("\t[0] Exit");

            System.out.print("\n\n\tEnter Here: ");
            choice = scanf.next().charAt(0); // Read choice

            switch (choice) {
                case '1':
                    // Call method to proceed to payment and handle discount coupon
                    break;

                case '2':
                    // Call method to modify counter items (under development)
                    break;

                case '3':
                    // Call method to view and select receipt from list
                    break;

                case '0':
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
                    // Handle invalid input
                    System.out.println("\n\tInvalid input. Try again...");
                    break;
            }
        }
    }
}
