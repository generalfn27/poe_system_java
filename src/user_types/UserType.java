package user_types;

import shopper.*;

import java.util.Scanner;

public class UserType {

    public void user_type_menu() {
        Scanner scanf = new Scanner(System.in);

        while (true) {
            System.out.println("Please enter what user type you are:");
            System.out.println(" ");
            System.out.println("[1] Customer");
            System.out.println("[2] Cashier");
            System.out.println("[3] Manager");
            System.out.println("\n[0] Exit");
            System.out.println("Type here: ");

            int user_type_choice = scanf.nextInt();

            // Consume the leftover newline from nextInt
            scanf.nextLine();

            switch (user_type_choice) {
                case 1:
                    System.out.println("You selected Customer.");
                    UserCustomer user_customer = new UserCustomer();
                    user_customer.user_customer_menu();
                    break;
                case 2:
                    System.out.println("You selected Cashier.");
                    // Call out cashier class function
                    break;
                case 3:
                    System.out.println("You selected Manager.");
                    // Call out manager class function
                    break;
                case 0:
                    System.out.flush();
                    System.out.println("\n\n\n\n\tAre you sure you want to close the program?\n");
                    System.out.println("\t[Y] for Yes  [N] for No: ");

                    String exit_confirmation = scanf.nextLine().trim(); // Trim to remove any leading/trailing whitespace

                    if (exit_confirmation.equalsIgnoreCase("Y")) {
                        System.out.println("\t============================================\n");
                        System.out.println("\t|                                          |\n");
                        System.out.println("\t|     Thank You for Using our Program!     |\n");
                        System.out.println("\t|                                          |\n");
                        System.out.println("\t============================================\n");
                        scanf.close(); // Close scanner before exiting
                        System.exit(0); // Exits the program completely

                    } else if (exit_confirmation.equalsIgnoreCase("N")) {
                        // Do nothing, stay in the loop and return to the menu
                        System.out.flush();
                        break;

                    } else {
                        System.out.println("\tInvalid input. Going back to menu.\n");
                        // Do nothing, stay in the loop and return to the menu
                        break;
                    }
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }
}
