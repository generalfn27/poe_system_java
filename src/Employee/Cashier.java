package Employee;

import Shopper.Customer;
import User_Types.UserType;
import Process.CashierProcess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Cashier {
    private static final int MAX_ATTEMPTS = 3; // Maximum login attempts
    private final CashierProcess cashier_process; // Class-level instance variable for CashierProcess
    public final List<Cashier> cashiers = new ArrayList<>();
    private String employee_id;
    private String employee_name;
    private String password;
    private String address;
    private int phone_number;
    private String hired_date;
    private int total_transaction_processed;

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public String getHired_date(){
        return hired_date;
    }

    public void setHired_date(String hired_date) {
        this.hired_date = hired_date;
    }

    public int getTotal_transaction_processed() {
        return total_transaction_processed;
    }

    public void setTotal_transaction_processed(int total_transaction_processed) {
        this.total_transaction_processed = total_transaction_processed;
    }



    public Cashier() {
        this.cashier_process = new CashierProcess(); // Assign to the class-level variable
    }


    public void user_cashier() {
        cashier_login();
    }

    // mag iiba na tong login kasi gagawin ng empleyado
    private void cashier_login() {
        Scanner scanf = new Scanner(System.in);
        int attempt_count = 0; // Count failed login attempts
        boolean valid = false; // Indicate if login is valid

        while (!valid) {
            System.out.println("\t===================================");
            System.out.println("\t|                                 |");
            System.out.println("\t|          Cashier Login          |");
            System.out.println("\t|                                 |");
            System.out.println("\t===================================");
            System.out.println("\tEnter Credentials\n");

            System.out.print("\tEnter username: ");
            String inputUsername = scanf.nextLine();

            System.out.print("\tEnter password: ");
            String inputPassword = scanf.nextLine(); // Capture password input directly without hiding

            // Validate login
            if (validate_cashier_login(inputUsername, inputPassword)) {
                valid = true; // Set flag to exit loop
                System.out.println("\tLogin successful!");
                cashier_dashboard();
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


    private boolean validate_cashier_login(String inputUsername, String inputPassword) {
        //madaming changes pa dito if gagawa ng multiple accounts for cashier employees

        // Default username
        String username = "admin";
        // Default password
        String password = "password";
        return inputUsername.equals(username) && inputPassword.equals(password);
    }

    private boolean handle_logout(Scanner scanf) {
        while (true) {
            System.out.println("\n\n\tAre you sure you want to Logout and go back to menu?\n");
            System.out.print("\t[Y] for Yes  [N] for No: ");

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


    public void create_new_cashier_employee() {
        Scanner scanf = new Scanner(System.in);
        final int MAX_EMPLOYEES = 10;
        if (cashiers.size() >= MAX_EMPLOYEES) {
            System.out.println("\n\tCashier employees limit reached. Cannot register and hire new cashier.");
            return;
        }

        Cashier new_cashier = new Cashier();

        //autogenerated id dapat

        String confirmPassword = "";
        String confirmPin = "";
        String name = "";
        String pinCode;
        String phoneNumber;
        String address;
        double transaction = 1;

        //set sa save nitong csv dapat ung hired date nila

        System.out.println("\n\t----------------------------------------------");
        System.out.println("\t|        suggestion another scanf para                  |");
        System.out.println("\t|            sa agreement consent sa data etc.            |");
        System.out.print("\n\tEnter cashier_name: ");
        String cashier_name = scanf.nextLine();

        boolean not_exist = false;
        while (!not_exist) {
            not_exist = true;  // assume username doesn't exist until proven otherwise
            // Check if the username already exists
            for (Cashier cashier : cashiers) {
                if (cashier.getEmployee_name().equals(name)) {
                    System.out.println("\n\tUsername already exists. Please choose a different username.");
                    System.out.print("\n\tEnter cashier_name: ");
                    cashier_name = scanf.nextLine();
                    not_exist = false;  // username exists, so set flag to false
                    break;  // break out of the loop to recheck the new username
                }
            }
        }
        new_cashier.setEmployee_name(name);




    }


    //next development dapat pinapasa na username sa parameter as welcome sa employee
    private void cashier_dashboard() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("\t=======================================");
            System.out.println("\t|                                     |");
            System.out.println("\t|          Cashier Dashboard          |");
            System.out.println("\t|                                     |");
            System.out.println("\t=======================================\n");
            System.out.println("\t[1] Process Queue Orders");
            System.out.println("\t[2] hindi pa alam ano dapat talaga dito");
            System.out.println("\t[0] Exit");

            System.out.print("\n\n\tEnter Here: ");
            choice = scanf.nextLine();

            switch (choice) {
                case "1":
                    selecting_queue_list_to_process();
                    break;

                case "2":
                    // Call method to modify counter items (under development)
                    break;

                case "0":
                    boolean logout_confirmed = handle_logout(scanf);
                    if (logout_confirmed) {
                        UserType.user_type_menu();
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


    public void selecting_queue_list_to_process() {
        Scanner scanf = new Scanner(System.in);
        File directory = new File(".");
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().startsWith("queue_number_"));
        List<String> csvFiles = new ArrayList<>();

        System.out.println("\t=======================================");
        System.out.println("\t|                                     |");
        System.out.println("\t|       Select Queue To Process       |");
        System.out.println("\t|                                     |");
        System.out.println("\t=======================================");
        System.out.println("\n\tCSV Files to Open:");

        if (files != null) {
            for (File file : files) {
                csvFiles.add(file.getName());
                System.out.println("\t[" + (csvFiles.size()) + "] " + file.getName());
            }
        }

        if (!csvFiles.isEmpty()) {
            System.out.println("\t[0] Go back");

            while (true) {
                System.out.print("\tEnter the number of the file to open: ");
                try {
                    String input = scanf.nextLine().trim();
                    int choice = Integer.parseInt(input);

                    if (choice >= 0 && choice <= csvFiles.size()) {
                        if (choice == 0) {
                            System.out.println("\tReturning to previous menu...");
                            cashier_dashboard();
                        }

                        String selectedFile = csvFiles.get(choice - 1);
                        System.out.println("\n\n\tYou selected: " + selectedFile);
                        cashier_process.read_order_from_csv(selectedFile);
                        cashier_process.transfer_cart_to_counter();
                        cashier_process_choice();
                        break;
                    } else {
                        System.out.println("\tInvalid choice! Please enter a number between 0 and " + csvFiles.size());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\tInvalid input! Please enter a valid number.");
                }
            }
        } else {
            System.out.println("\tNo CSV files found.");
            System.out.println("\tNo Queue Order to process.");
            System.out.println("\t\tPress Enter key to continue.\n");
            scanf.nextLine(); //used for press any key to continue

            System.out.println("\tReturning to Cashier Dashboard.");
            cashier_dashboard();
        }
    }


    public void cashier_process_choice() {
        System.out.flush();
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("\t=======================================");
            System.out.println("\t|                                     |");
            System.out.println("\t|          Cashier Dashboard          |");
            System.out.println("\t|                                     |");
            System.out.println("\t=======================================\n");

            System.out.println("\n\t[1] Proceed to pay");
            System.out.println("\t[2] Modify Counter Items (under development)");
            System.out.println("\t[3] Preview items");
            System.out.println("\t[0] Go back");

            System.out.print("\n\n\tEnter Here: ");
            choice = scanf.nextLine();

            switch (choice) {
                case "1":
                    if (cashier_process.process_payment()) {
                        cashier_dashboard();
                    }
                    break;
                case "2":
                    modify_counter_process();
                    break;
                case "3":
                    cashier_process.display_counter();
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
                    break;
                case "0":
                    selecting_queue_list_to_process();
                    return;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
            }
        }
    }

    // ung mga changes here hanggang sa arraylist lang so pag nag back ka hindi mag rreflect un sa csv
    public void modify_counter_process() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            cashier_process.display_counter();
            System.out.println("\n\tMODIFY MENU: not updating kasi cart ang nasa for each hindi counter");
            System.out.println("\tIncrease Quantity (I)");
            System.out.println("\tDeduct Quantity (D)");
            System.out.println("\tRemove Items (R)");
            System.out.println("\tProceed to Pay (P)");
            System.out.println("\tDisplay cart(V)");
            System.out.println("\tGo Back to Dashboard (B)");
            System.out.print("\n\tEnter choice: ");
            choice = scanf.nextLine();

            switch (choice) {
                case "I":
                case "i":
                    int quantityToIncrease;
                    boolean quantity_increase_valid_input = false;

                    System.out.println("\n\tEnter 0 to Cancel item increase");
                    System.out.print("\tEnter the product code to increase: ");
                    String codeToIncrease = scanf.nextLine();

                    if (codeToIncrease.equals("0")) { break; }

                    while (!quantity_increase_valid_input) {
                        try {
                            System.out.print("\tEnter quantity to increase: ");
                            quantityToIncrease = Integer.parseInt(scanf.nextLine());
                            cashier_process.increase_item_quantity_counter(codeToIncrease, quantityToIncrease);
                            quantity_increase_valid_input = true; // Input is valid, exit the loop
                        } catch (NumberFormatException e) {
                            System.out.println("\tInvalid input. Please enter a valid number.");
                        }
                    }
                    break;
                case "D":
                case "d":
                    int quantityToDeduct;
                    boolean deduction_valid_input = false;

                    System.out.println("\tEnter 0 to Cancel item deduction");
                    System.out.print("\tEnter product code to deduct: ");
                    String codeToDeduct = scanf.nextLine();

                    if (codeToDeduct.equals("0")) { break; }

                    while (!deduction_valid_input) {
                        try {
                            System.out.print("\tEnter quantity to deduct: ");
                            quantityToDeduct = Integer.parseInt(scanf.nextLine());
                            cashier_process.deduct_item_quantity_counter(codeToDeduct, quantityToDeduct);
                            deduction_valid_input = true; // Input is valid, exit the loop
                        } catch (NumberFormatException e) {
                            System.out.println("\tInvalid input. Please enter a valid number.");
                        }
                    }
                    break;
                case "R":
                case "r":
                    System.out.println("\tEnter 0 to Cancel item removal");
                    System.out.print("\tEnter product code to remove: ");
                    String codeToRemove = scanf.nextLine();
                    if (codeToRemove.equals("0")) { break; }
                    cashier_process.remove_item_counter(codeToRemove);  // Remove the item sa cart all quantity
                    break;
                case "V":
                case "v":
                    System.out.println("\t\tPress Enter key to continue.\n");
                    cashier_process.display_counter();
                    scanf.nextLine();
                    break;
                case "P":
                case "p":
                    if (cashier_process.process_payment()) {
                        cashier_dashboard(); // Return to dashboard after payment if true
                    }
                    break;
                case "B":
                case "b":
                    cashier_process_choice();
                    return;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.println("\t\tPress Enter key to continue.\n");
                    scanf.nextLine(); //used for press any key to continue
            }
        }
    }







}
