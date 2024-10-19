package Employee;

import User_Types.UserType;

import java.util.Scanner;

public class Manager {

    private static final int MAX_ATTEMPTS = 3; // Maximum login attempts

    public void user_manager() {
        manager_login();
    }

    private void manager_login() {
        Scanner scanf = new Scanner(System.in);
        int attempt_count = 0; // Count failed login attempts
        boolean valid = false; // Indicate if login is valid

        while (!valid) {
            System.out.println("\t===================================");
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
            System.out.println("\t[Y] for Yes  [N] for No: ");

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
            System.out.println("\t=======================================");
            System.out.println("\t|                                     |");
            System.out.println("\t|          Manager Dashboard          |");
            System.out.println("\t|                                     |");
            System.out.println("\t=======================================\n");
            System.out.println("\t[1] Cashier Mode");
            System.out.println("\t[2] Sales report"); //display total sales at recent total transactions
            System.out.println("\t[2] Receipt History"); //same sa personal account
            System.out.println("\t[2] Inventory"); //refill stocks
            System.out.println("\t[2] Employees"); //sino mga employees at handle ng account nila change pass/delete acc
            System.out.println("\t[2] Customer Account"); //account retrieval at delete account
            System.out.println("\t[0] Exit");

            System.out.print("\n\n\tEnter Here: ");
            choice = scanf.nextLine();

            switch (choice) {
                case "1":

                    break;

                case "2":
                    // Call method to modify counter items (under development)
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
                    break;
            }
        }
    }




}
