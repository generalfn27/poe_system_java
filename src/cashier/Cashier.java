package cashier;

import java.util.Scanner;

public class Cashier {
    private static final int MAX_ATTEMPTS = 3; // Maximum login attempts
    private String username = "admin"; // Default username
    private String password = "password"; // Default password

    public void user_cashier() {
        cashierLogin();
    }

    private void cashierLogin() {
        Scanner scanner = new Scanner(System.in);
        int attemptCount = 0; // Count failed login attempts
        boolean valid = false; // Indicate if login is valid

        while (!valid && attemptCount < MAX_ATTEMPTS) {
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
                attemptCount++;
                cashier_increment_attempts(attemptCount, inputUsername);

                if (attemptCount >= MAX_ATTEMPTS) {
                    System.out.println("Maximum attempts reached. Exiting.");
                    break;
                }
            }
        }
    }

    private boolean validate_cashier_login(String inputUsername, String inputPassword) {
        // Add logic to validate the cashier's credentials here
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

                case 0:
                    boolean exit_confirmed = false;
                    while (!exit_confirmed) {
                        System.out.flush();
                        System.out.println("\n\n\n\n\tAre you sure you want to close the program?\n");
                        System.out.println("\t[Y] for Yes  [N] for No: ");

                        String exit_confirmation = scanf.nextLine().trim();

                        if (exit_confirmation.equalsIgnoreCase("Y")) {
                            System.out.println("\t============================================\n");
                            System.out.println("\t|                                          |\n");
                            System.out.println("\t|     Thank You for Using our Program!     |\n");
                            System.out.println("\t|                                          |\n");
                            System.out.println("\t============================================\n");
                            scanf.close();
                            System.exit(0);
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
