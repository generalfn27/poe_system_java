package Shopper;

import User_Types.*;
import Process.*;

import java.util.Scanner;
import java.util.List;

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
                    Customer guest = new Customer("Guest"); // Guest with default name
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
    public void guest_customer_item_category(Customer guest) {
        Scanner scanf = new Scanner(System.in);
        String item_category;

        // Create an instance of OrderProcessor to handle the guest's order
        OrderProcessor order_processor = new OrderProcessor();

        do {
            System.out.println("\n\nBrowsing as guest: " + guest.getName());

            System.out.println("\n\t----------------------------------------------");
            System.out.println("\t|              Welcome Customer!             |");
            System.out.println("\t|             What do you want to browse?    |");
            System.out.println("\t|                                            |");
            System.out.println("\t|        [1] Beverages                       |");
            System.out.println("\t|        [2] Snacks                          |");
            System.out.println("\t|        [3] Canned Goods                    |");
            System.out.println("\t|        [4] Condiments                      |");
            System.out.println("\t|        [5] Dairy                           |");
            System.out.println("\t|        [6] Frozen Foods                    |");
            System.out.println("\t|        [7] Body Care & Beauty Care         |");
            System.out.println("\t|        [9] Detergents & Soaps              |\n");
            System.out.println("\t|        [0] Go Back                         |");
            System.out.println("\t|                                            |");
            System.out.println("\t----------------------------------------------");
            System.out.println("\t|        Enter here: ");

            item_category = scanf.nextLine();

            // Variable to hold the products in the chosen category
            List<Product> selected_products = null;

            switch (item_category) {
                case "0":
                    user_customer_menu();
                    return;  // Return to main menu
                case "1":
                    selected_products = BrowseProduct.browse_beverages(); // Browse beverages
                    break;
                case "2":
                    selected_products = BrowseProduct.browse_snacks(); // Browse snacks
                    break;
                case "3":
                    selected_products = BrowseProduct.browse_canned_goods(); // Browse canned goods
                    break;
                case "4":
                    selected_products = BrowseProduct.browse_condiments(); // Browse condiments
                    break;
                case "5":
                    selected_products = BrowseProduct.browse_dairy(); // Browse dairy
                    break;
                case "6":
                    selected_products = BrowseProduct.browse_frozen_foods(); // Browse frozen foods
                    break;
                case "7":
                    selected_products = BrowseProduct.browse_self_care_items(); // Browse self-care items
                    break;
                case "8":
                    selected_products = BrowseProduct.browse_detergents(); // Browse detergents
                    break;
                default:
                    System.out.println("\nInvalid input. Try again...");
                    continue;
            }

            if (selected_products != null && !selected_products.isEmpty()) {
                // After displaying products, process the order by asking for product code
                order_processor.process_customer_order(selected_products);
            } else {
                System.out.println("No products available in this category.");
            }

        } while (!item_category.equals("0"));
    }


    // Placeholder for the customer registration function
    public void customerRegister() {
        // Implement customer registration functionality here
    }
}
