package User_Types;

import Shopper.*;
import Employee.*;

import java.io.Console;

public class UserType {
    public static void user_type_menu() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return;
        }

        while (true) {
            String storeName = ManagerCredentials.getStoreName();
            System.out.println("\n\n\n\n\t=======================================");
            System.out.println("\t|                                     |");
            System.out.printf ("\t|       Welcome to %-15s    |\n", storeName + " Store");
            System.out.println("\t|                                     |");
            System.out.println("\t=======================================\n");
            System.out.println("\tGood day user!");
            System.out.println("\tPlease enter what user type you are: \n");
            System.out.println("\t[1] Customer");
            System.out.println("\t[2] Cashier");
            System.out.println("\t[3] Manager");
            System.out.println("\n\t[0] Exit\n");
            System.out.print("\tType here: ");

            String user_type_choice = console.readLine().trim();

            switch (user_type_choice) {
                case "1":
                    System.out.println("\n\tYou selected Customer.");
                    UserCustomer user_customer = new UserCustomer();
                    user_customer.user_customer_menu();
                    break;
                case "2":
                    System.out.println("\n\tYou selected Cashier.");
                    Cashier cashier = new Cashier();
                    cashier.user_cashier();
                    break;
                case "3":
                    System.out.println("\n\tYou selected Manager.");
                    Manager manager = new Manager();
                    manager.user_manager();
                    break;
                case "0":
                    while (true) {
                        System.out.flush();
                        System.out.println("\n\n\tAre you sure you want to close the program?\n");
                        System.out.print("\t[Y] for Yes  [N] for No: ");

                        String exit_confirmation = console.readLine().trim();

                        if (exit_confirmation.equalsIgnoreCase("Y")) {
                            System.out.println("\n\n\n\t============================================\n");
                            System.out.println("\t|                                          |\n");
                            System.out.println("\t|     Thank You for Using our Program!     |\n");
                            System.out.println("\t|                                          |\n");
                            System.out.println("\t============================================\n");
                            System.exit(0);
                        } else if (exit_confirmation.equalsIgnoreCase("N")) {
                            user_type_menu();
                            break;
                        } else {
                            System.out.println("\n\tAn error has occurred");
                        }
                    }
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
                    console.flush();
            }
            System.out.println();
        }
    }
}
