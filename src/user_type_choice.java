import java.util.Scanner;

//eto ung class
public class user_type_choice {

    //eto ung constructor (function)
    public user_type_choice() {
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

            switch (user_type_choice) {
                case 1:
                    System.out.println("You selected Customer.");
                    // Call out customer class function
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
                    System.out.println("Returning to welcome screen");
                    // Call out the welcome screen of the store
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();

            // Exit the loop if the user chooses 0
            if (user_type_choice == 0) {
                break;
            }
        }

        // Close the scanner after the loop to avoid resource leaks
        scanf.close();
    }
}
