package shopper;

import user_types.*;

import java.util.Scanner;

public class UserCustomer {
    private final UserType userType;

    public UserCustomer() {
        this.userType = new UserType();
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
                    customerLogin();
                    break;
                case "2":
                    // Handle the guest case
                    customer guest = new customer("Guest"); // Guest with default name
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

    // Placeholder for the customer login function
    public void customerLogin() {
        // Implement customer login functionality here
    }

    // Placeholder for the guest customer item category function
    public void guest_customer_item_category(customer guest) {
        Scanner scanf = new Scanner(System.in);
        String item_category;

        do {
            // Implement guest item browsing functionality here
            System.out.println("Browsing as guest: " + guest.getName());
            // Continue with guest-specific browsing and processing


            System.out.println("\n\t----------------------------------------------");
            System.out.println("\t|              Welcome Customer!             |");
            System.out.println("\t|             What do you want to browse?    |");
            System.out.println("\t|                                                |");
            System.out.println("\t|        [1] Beverages                               |");
            System.out.println("\t|        [2] Snacks                  |");
            System.out.println("\t|        [3] Canned Goods                            |");
            System.out.println("\t|        [4] Condiments                            |");
            System.out.println("\t|        [5] Dairy                            |");
            System.out.println("\t|        [6] Frozen Foods                            |");
            System.out.println("\t|        [7] Body Care & Beauty Care                            |");
            System.out.println("\t|        [9] Detergents & Soaps                            |\n");
            System.out.println("\t|        [0] Go Back                             |");
            System.out.println("\t|                                                |");
            System.out.println("\t----------------------------------------------");
            System.out.println("\t|        Enter here: ");

            item_category = scanf.nextLine();

            switch (item_category) {
                case "0":
                    //reset_cart(cart, &total_items, &total_price);
                    user_customer_menu();
                    break;
                case "1":
                    BrowseProduct.browse_beverages();
                    guest_customer_item_category(guest);
                    break;
                case "2":
                    BrowseProduct.browse_snacks();
                    guest_customer_item_category(guest);
                    break;
                case "3":
                    BrowseProduct.browse_canned_goods();
                    //canned goods may dalawang choice
                    guest_customer_item_category(guest);
                    break;
                case "4":
                    BrowseProduct.browse_condiments();
                    guest_customer_item_category(guest);
                    break;
                case "5":
                    BrowseProduct.browse_dairy();
                    guest_customer_item_category(guest);
                    break;
                case "6":
                    BrowseProduct.browse_frozen_foods();
                    guest_customer_item_category(guest);
                    break;
                case "7":
                    BrowseProduct.browse_self_care_items();
                    guest_customer_item_category(guest);
                    break;
                case "8":
                    BrowseProduct.browse_detergents();
                    guest_customer_item_category(guest);
                    break;

                default:
                    System.out.println("\\nInvalid input. Try again...");
            }
        } while (!item_category.equals("0"));

    }

    // Placeholder for the customer registration function
    public void customerRegister() {
        // Implement customer registration functionality here
    }
}
