package shopper;

import user_types.*;

import java.util.Scanner;

public class UserCustomer {
    private UserType userType;

    public UserCustomer() {
        this.userType = new UserType();
    }
    public void user_customer_menu() {
        Scanner scanf = new Scanner(System.in);
        int choice;

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
            choice = scanf.nextInt();

            // Consume the leftover newline from nextInt
            scanf.nextLine();

            switch (choice) {
                case 1:
                    // Call the login function (assuming it exists)
                    customerLogin();
                    break;
                case 2:
                    // Handle the guest case
                    customer guest = new customer("Guest"); // Guest with default name
                    guestCustomerItemCategory(guest); // Pass the guest customer to the item category function
                    break;
                case 3:
                    // Call the registration function (assuming it exists)
                    customerRegister();
                    break;
                case 0:
                    userType.user_type_menu();
                    return; // Go back to the previous menu
                default:
                    System.out.println("\n\tInvalid input. Try again...");
                    // Implement a method for pressAnyKey() if needed
            }
        }
    }

    // Placeholder for the customer login function
    public void customerLogin() {
        // Implement customer login functionality here
    }

    // Placeholder for the guest customer item category function
    public void guestCustomerItemCategory(customer guest) {
        // Implement guest item browsing functionality here
        System.out.println("Browsing as guest: " + guest.getName());
        // Continue with guest-specific browsing and processing
    }

    // Placeholder for the customer registration function
    public void customerRegister() {
        // Implement customer registration functionality here
    }
}
