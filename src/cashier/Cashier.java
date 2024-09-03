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
            if (validateCashierLogin(inputUsername, inputPassword)) {
                System.out.println("\tLogin successful!");
                valid = true; // Set flag to exit loop
                cashierProcessChoice();
            } else {
                attemptCount++;
                cashierIncrementAttempts(attemptCount, inputUsername);

                if (attemptCount >= MAX_ATTEMPTS) {
                    System.out.println("Maximum attempts reached. Exiting.");
                    break;
                }
            }
        }
    }

    private boolean validateCashierLogin(String inputUsername, String inputPassword) {
        // Add logic to validate the cashier's credentials here
        return inputUsername.equals(username) && inputPassword.equals(password);
    }

    private void cashierIncrementAttempts(int attemptCount, String username) {
        System.out.println("Invalid login attempt #" + attemptCount + " for user: " + username);
    }

    private void cashierProcessChoice() {
        // Add logic for cashier's post-login options here
        System.out.println("Processing cashier choices...");
    }
}
