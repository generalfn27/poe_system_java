package User_Types;

import Shopper.*;
import Employee.*;

import java.util.Scanner;

public class UserType {
    public static void user_type_menu() {
        Scanner scanf = new Scanner(System.in);

        while (true) {
            System.out.println("Please enter what user type you are:");
            System.out.println();
            System.out.println("[1] Customer");
            System.out.println("[2] Cashier");
            System.out.println("[3] Manager");
            System.out.println("\n[0] Exit");
            System.out.print("Type here: ");

            String user_type_choice = scanf.next();
            scanf.nextLine();

            switch (user_type_choice) {
                case "1":
                    System.out.println("You selected Customer.");
                    UserCustomer user_customer = new UserCustomer();
                    user_customer.user_customer_menu();
                    break;
                case "2":
                    System.out.println("You selected Cashier.");
                    Cashier cashier = new Cashier();
                    cashier.user_cashier();
                    break;
                case "3":
                    System.out.println("You selected Manager.");
                    // Call out manager class function
                    break;
                case "0":
                    while (true) {
                        System.out.flush();
                        System.out.println("\n\n\n\n\tAre you sure you want to close the program?\n");
                        System.out.print("\t[Y] for Yes  [N] for No: ");

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
                            user_type_menu();
                            break;
                        } else {
                            System.out.println("\tInvalid input. Going back to menu.\n");
                            System.out.println("\tPress any key to continue.\n");
                            scanf.nextLine(); //used for press any key to continue
                            // Do nothing, stay in the loop and return to the menu
                        }
                    }
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }
}
