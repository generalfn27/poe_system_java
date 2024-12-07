package Employee;

import User_Types.UserType;
import Process.CashierProcess;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Cashier {
    private static final int MAX_ATTEMPTS = 3; // Maximum login attempts
    private final CashierProcess cashier_process; // Class-level instance variable for CashierProcess
    public final List<Cashier> cashiers = new ArrayList<>();
    private static int currentIDNumber;

    private int employee_id;
    private String employee_username;
    private String employee_first_name;
    private String employee_surname;
    private String password;
    private String phone_number;
    private String hired_date;
    private int total_transaction_processed;

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_username() {
        return employee_username;
    }

    public void setEmployee_username(String employee_user_name) {
        this.employee_username = employee_user_name;
    }

    public String getEmployee_first_name() {
        return employee_first_name;
    }

    public void setEmployee_first_name(String employee_first_name) {
        this.employee_first_name = employee_first_name;
    }

    public String getEmployee_surname() {
        return employee_surname;
    }

    public void setEmployee_surname(String employee_surname) {
        this.employee_surname = employee_surname;
    }

    public String getEmployee_full_name() {
        return employee_first_name + " " + employee_surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
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


    // Add a private constructor for creating Cashier objects from CSV
    private Cashier(int id, String username, String firstName, String surname,
                    String password, String phone, String hireDate, int transactions) {
        this.employee_id = id;
        this.employee_username = username;
        this.employee_first_name = firstName;
        this.employee_surname = surname;
        this.password = password;
        this.phone_number = phone;
        this.hired_date = hireDate;
        this.total_transaction_processed = transactions;
        this.cashier_process = null; // Not needed for data objects
    }

    // Main constructor for new Cashier instances
    public Cashier() {
        this.cashier_process = new CashierProcess();
        initialize_id_number();
        load_cashiers_from_CSV();
    }

    public void user_cashier() {
        cashier_login();
    }


    private void cashier_login() {
        Scanner scanf = new Scanner(System.in);
        int attempt_count = 0;
        boolean login_successful = false;

        // Load cashiers from the CSV if not already loaded
        if (cashiers.isEmpty()) {
            load_cashiers_from_CSV();

            //kung empty uli after file reading edi mag return sya sa menu
            if (cashiers.isEmpty()) {
                UserType.user_type_menu();
            }
        }

        while (attempt_count <= MAX_ATTEMPTS) {
            System.out.println("\n\t===================================");
            System.out.println("\t|                                 |");
            System.out.println("\t|          Cashier Login          |");
            System.out.println("\t|                                 |");
            System.out.println("\t===================================");
            System.out.print("\n\tEnter Username: ");
            String username = scanf.nextLine();

            System.out.print("\tEnter Password: ");
            String password = scanf.nextLine();

            for (Cashier cashier : cashiers) {
                if (cashier.getEmployee_username().equals(username) && cashier.getPassword().equals(password)) {
                    System.out.println("\n\tLogin successful.");
                    login_successful = true;
                    cashier_dashboard(cashier);
                }
            }

            if (login_successful) { break; }
            else {
                attempt_count++;
                System.out.println("\n\tInvalid username or password. Attempts left: " + (MAX_ATTEMPTS - attempt_count));
            }
        }

        if (!login_successful) {
            System.out.println("\n\tMaximum login attempts reached. Please try again later.");
            UserType.user_type_menu();
        }

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

        initialize_id_number();

        Cashier new_cashier = new Cashier();

        String cashier_username;
        String cashier_first_name;
        String cashier_surname;
        String confirmPassword = "";
        String phoneNumber;
        int transaction = 0;

        System.out.println("\n\t----------------------------------------------");
        System.out.println("\t|        New Cashier Employee Registration                |");
        System.out.println("\t|            sa agreement consent sa data etc.            |");
        System.out.print("\n\tEnter cashier user name (0 to cancel): ");
        cashier_username = scanf.nextLine();

        if (cashier_username.equals("0")) {
            System.out.println("\n\tRegistration Canceled.");
            System.out.print("\n\t\tPress Enter to continue...");
            scanf.nextLine();
            return;
        }

        boolean cashier_username_not_exist = false;
        while (!cashier_username_not_exist) {
            cashier_username_not_exist = true;  // assume username doesn't exist until proven otherwise
            // Check if the username already exists
            for (Cashier cashier : cashiers) {
                if (cashier.getEmployee_username().equals(cashier_username)) {
                    System.out.println("\n\tCashier already exists. Please choose a different username.");
                    System.out.print("\n\tEnter cashier_name: ");
                    cashier_username = scanf.nextLine();
                    cashier_username_not_exist = false;  // username exists, so set flag to false
                    break;  // break out of the loop to recheck the new username
                }
            }
        }
        new_cashier.setEmployee_username(cashier_username);


        while (true) {
            System.out.print("\n\tEnter cashier first name: ");
            cashier_first_name = scanf.nextLine();

            if (cashier_first_name.matches("^[a-zA-Z\\\\s]*$")){ break; }
            else {
                System.out.println("\n\tInvalid first name. Please enter your name without using numbers or symbols/signs.");
                System.out.println("\n\t\tPress enter to Continue");
                scanf.nextLine();
            }
        }
        new_cashier.setEmployee_first_name(cashier_first_name);

        while (true) {
            System.out.print("\n\tEnter cashier surname: ");
            cashier_surname = scanf.nextLine();

            if (cashier_surname.matches("^[a-zA-Z\\\\s]*$")){ break; }
            else {
                System.out.println("\n\tInvalid first name. Please enter your name without using numbers or symbols/signs.");
                System.out.print("\n\t\tPress enter to Continue");
                scanf.nextLine();
            }
        }
        new_cashier.setEmployee_surname(cashier_surname);

        System.out.println("\n\tWelcome " + new_cashier.getEmployee_first_name() + " "
        + new_cashier.getEmployee_surname());

        // Password input naka ibang method para sa future changes para mas madali mag asterisk
        String password = input_password(scanf, "\n\tEnter Password: ");
        new_cashier.setPassword(password);

        while (!new_cashier.getPassword().equals(confirmPassword)) {
            confirmPassword = input_password(scanf, "\tConfirm Password: ");
            if (!new_cashier.getPassword().equals(confirmPassword)) {
                System.out.println("\n\tPasswords do not match. Please try again.");
            }
        }

        while (true) {
            System.out.print("\n\tEnter Phone Number: ");
            phoneNumber = scanf.nextLine();

            //  Check if the entered Phone Number starts with "09" and is exactly 11 digits
            //  ^: This is a beginning-of-line anchor, which means that the pattern must match from the very start of the string.
            //  09: This simply matches the literal characters "09".
            //  \d: This matches any single digit character (0-9).
            //  {9}: This is a quantifier that specifies that the preceding element (\d) must occur exactly 9 times.
            //  $: This is an end-of-line anchor, which means that the pattern must match up to the very end of the string.

            if (phoneNumber.matches("^09\\d{9}$")) { break; }
            else { System.out.println("\tInvalid phone number. Please enter an 11-digit number starting with '09'.");   }
        }
        new_cashier.setPhone_number(phoneNumber);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String formattedDate = dateFormat.format(new Date());

        new_cashier.setHired_date(formattedDate);

        new_cashier.setTotal_transaction_processed(transaction);

        currentIDNumber++;
        new_cashier.setEmployee_id(currentIDNumber);

        String detail_confirmation;
        while (true) {
            //change tong display na to
            System.out.println("\tID: " + new_cashier.getEmployee_id());
            System.out.println("\tName: " + new_cashier.getEmployee_full_name());
            System.out.println("\tPhone Number: " + new_cashier.getPhone_number());
            System.out.println("\tHired Date: " + new_cashier.getHired_date());

            System.out.println("\n\tCheck your information.");
            System.out.println("\n\tEnter Y if you are sure.");
            System.out.println("\n\tEnter N if you are not sure and return to HR Manager Dashboard.");
            System.out.print("\n\tEnter choice: ");
            detail_confirmation = scanf.nextLine();

            // update dapat to may confirmation kung sure ba sya sa sagot nya
            if (detail_confirmation.equalsIgnoreCase("y")){
                save_new_cashier_employee_to_csv(new_cashier);
                cashiers.add(new_cashier);
                save_id_number();
                break;
            } else if (detail_confirmation.equalsIgnoreCase("n")) {
                System.out.println("\n\tReturning to HR Management Menu");
                System.out.println("\n\t\tPress enter to Continue");
                scanf.nextLine();
                //dapat ang balik nito ay sa hr management dashboard
                return;
            } else {
                System.out.println("\n\tInvalid Input");
                System.out.println("\n\t\tPress enter to Continue");
                scanf.nextLine();
            }


        }

        System.out.print("\n\tCongratulations! Your registration was successful.  " + new_cashier.getEmployee_username() +".\n\t\t\tPress Enter key to start exploring!\n");
        scanf.nextLine(); //used for press any key to continue
        // para pause muna sa bawat pagkakamali para isipin muna sa susunod tama
    }

    // Helper method to input hidden password pero dapat magiging ****
    //shortcut sa pag fill ups thanks sa AI
    private String input_password(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();  // Simplified for Java, as hiding characters is more complex
    }


    // Method to initialize the id number by reading from a file
    public static void initialize_id_number() {
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts/current_id_number.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                currentIDNumber = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            // If file doesn't exist or is invalid, start from 0
            currentIDNumber = 0;
        }
    }

    // Method to save the current id number to a file
    private static void save_id_number() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("accounts/current_id_number.txt"))) {
            writer.println(currentIDNumber);
        } catch (IOException e) {
            System.out.println("\tError saving queue number: " + e.getMessage());
        }
    }


    private void save_new_cashier_employee_to_csv(Cashier cashier) {
        File file = new File("accounts/cashier_employees.csv");

        boolean fileExists = file.exists();

        try (FileWriter writer = new FileWriter(file, true)) {
            // Write header only if the file doesn't exist (i.e., it's a new file)
            if (!fileExists) {
                writer.write("Employee_id,Employee_username,Employee_first_name,Employee_surname," +
                        "password,phone_number,hired_date,total_transaction_processed\n");
            }
            //Write cashier employee data
            writer.append(String.valueOf(cashier.getEmployee_id())).append(",");
            writer.append(cashier.getEmployee_username()).append(",");
            writer.append(cashier.getEmployee_first_name()).append(",");
            writer.append(cashier.getEmployee_surname()).append(",");
            writer.append(cashier.getPassword()).append(",");
            writer.append(cashier.getPhone_number()).append(",");
            writer.append(cashier.getHired_date()).append(",");
            writer.append(String.valueOf(cashier.getTotal_transaction_processed())).append("\n");

        } catch (IOException e) {
            System.err.println("Error writing new cashier employee data to file: " + e.getMessage());
        }

    }

    public void load_cashiers_from_CSV() {
        // Clear existing list before loading
        cashiers.clear();

        String CASHIER_CSV_FILE = "accounts/cashier_employees.csv";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CASHIER_CSV_FILE))) {
            String line;
            bufferedReader.readLine(); // Skip header

            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) {
                    // Use the private constructor to create Cashier objects
                    Cashier cashier = new Cashier(
                            Integer.parseInt(data[0]),  // id
                            data[1],                    // username
                            data[2],                    // firstName
                            data[3],                    // surname
                            data[4],                    // password
                            data[5],                    // phone
                            data[6],                    // hireDate
                            Integer.parseInt(data[7])   // transactions
                    );
                    cashiers.add(cashier);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("\n\tCSV file not found. No cashier loaded.");
        } catch (IOException e) {
            System.out.println("\n\tError loading cashier from file: " + e.getMessage());
        }
    }



    //next development dapat pinapasa na username sa parameter as welcome sa employee
    private void cashier_dashboard(Cashier cashier) {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("\t================================================");
            System.out.println("\t|                                               |");
            System.out.println("\t|                  Cashier Dashboard            |");
            System.out.printf ("\t|          Welcome ! %-27s|\n", cashier.getEmployee_full_name());
            System.out.println("\t|                                               |");
            System.out.println("\t|      [1] Process Queue Orders                 |");
            System.out.println("\t|      [2] Employee Account Option              |");
            System.out.println("\t|      [0] Exit                                 |");
            System.out.println("\t|                                               |");
            System.out.println("\t================================================\n");
            System.out.print("\n\tEnter Here: ");
            choice = scanf.nextLine();

            switch (choice) {
                case "1":
                    selecting_queue_list_to_process(cashier);
                    break;
                case "2":
                    cashier_account_menu_profile(cashier);
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
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine(); //used for press any key to continue
                    break;
            }
        }
    }


    public void selecting_queue_list_to_process(Cashier cashier) {
        Scanner scanf = new Scanner(System.in);
        File directory = new File("queues");
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
                            cashier_dashboard(cashier);
                        }

                        String selectedFile = csvFiles.get(choice - 1);
                        System.out.println("\n\n\tYou selected: " + selectedFile);
                        cashier_process.read_order_from_csv(selectedFile);
                        cashier_process.transfer_cart_to_counter();
                        cashier_process_choice(cashier);
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
            System.out.print("\t\tPress Enter key to continue.");
            scanf.nextLine(); //used for press any key to continue

            System.out.println("\tReturning to Cashier Dashboard.");
            cashier_dashboard(cashier);
        }
    }


    public void cashier_process_choice(Cashier cashier) {
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
                    if (cashier_process.process_payment(cashier)) {
                        cashier_dashboard(cashier);
                    }
                    break;
                case "2":
                    modify_counter_process(cashier);
                    break;
                case "3":
                    cashier_process.display_counter();
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine(); //used for press any key to continue
                    break;
                case "0":
                    selecting_queue_list_to_process(cashier);
                    return;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine(); //used for press any key to continue
            }
        }
    }

    // ung mga changes here hanggang sa arraylist lang so pag nag back ka hindi mag reflect un sa csv
    public void modify_counter_process(Cashier cashier) {
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
                    System.out.print("\t\tPress Enter key to continue.");
                    cashier_process.display_counter();
                    scanf.nextLine();
                    break;
                case "P":
                case "p":
                    if (cashier_process.process_payment(cashier)) {
                        cashier_dashboard(cashier); // Return to dashboard after payment if true
                    }
                    break;
                case "B":
                case "b":
                    cashier_process_choice(cashier);
                    return;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine(); //used for press any key to continue
            }
        }
    }


    public void cashier_account_menu_profile(Cashier cashier) {
        Scanner scanf = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("\n\n\t=================================================");
            System.out.println("\t|                                               |");
            System.out.println("\t|               Cashier Account Options         |");
            System.out.printf ("\t|          Welcome ! %-27s|\n", cashier.getEmployee_full_name());
            System.out.println("\t|                                               |");
            System.out.println("\t|      [1] View Processed Receipt               |");
            System.out.println("\t|      [2] Display Account Details              |");
            System.out.println("\t|      [3] Reset Password                       |");
            System.out.println("\t|                                               |");
            System.out.println("\t|      [0] Exit                                 |");
            System.out.println("\t|                                               |");
            System.out.println("\t=================================================\n");
            System.out.print("\n\tEnter Here: ");
            choice = scanf.nextLine();

            switch (choice) {
                case "0":
                    cashier_dashboard(cashier);
                    return;
                case "1":
                    view_cashier_receipts(cashier);
                    break;
                case "2":
                    display_cashier_account_details(cashier);
                    break;
                case "3":
                    cashier_employee_change_password(cashier);
                    break;
                default:
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine(); //used for press any key to continue
            }
        }
    }


    public void view_cashier_receipts(Cashier cashier) {
        Scanner scanf = new Scanner(System.in);
        File receiptDirectory = new File("receipts");
        FilenameFilter filter = (dir, name) -> name.startsWith("receipt_number_") && name.endsWith("Cashier_" + cashier.getEmployee_username() + ".csv");
        File[] files = receiptDirectory.listFiles(filter);
        List<String> csvFiles = new ArrayList<>();

        System.out.println("\n\n\t=======================================");
        System.out.println("\t|                                     |");
        System.out.println("\t|         Transaction History         |");
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
                System.out.print("\n\tEnter the number of the file to open: ");
                try {
                    String input = scanf.nextLine().trim();
                    int choice = Integer.parseInt(input);

                    if (choice >= 0 && choice <= csvFiles.size()) {
                        if (choice == 0) {
                            System.out.println("\tReturning to previous menu...");
                            cashier_account_menu_profile(cashier);
                        }

                        String selectedFile = csvFiles.get(choice - 1);
                        System.out.println("\n\n\tYou selected: " + selectedFile);
                        Manager.read_transaction_history_from_csv(selectedFile);
                        System.out.print("\tPress Enter key to continue.");
                        scanf.nextLine();
                        break;
                    } else {
                        System.out.println("\tInvalid choice! Please enter a number between 0 and " + csvFiles.size());
                        System.out.print("\t\tPress Enter key to continue.");
                        scanf.nextLine(); //used for press any key to continue
                    }
                    view_cashier_receipts(cashier);
                } catch (NumberFormatException e) {
                    System.out.println("\tInvalid input! Please enter a valid number.");
                }
            }
        } else {
            System.out.println("\tNo CSV files found.");
            System.out.print("\tPress Enter key to continue.");
            scanf.nextLine();
        }
    }

    public void display_cashier_account_details(Cashier cashier) {
        Scanner scanf = new Scanner(System.in);

        load_cashiers_from_CSV();

        System.out.println("\n\t=================================================");
        System.out.println("\t|              Employee Details                 |");
        System.out.println("\t=================================================");
        System.out.printf("\t%-5s %-15s %-20s %-15s %-12s %-24s%n",
                "ID", "Username", "Full Name", "Phone", "Hire Date", "Total Transaction Processed");
        System.out.println("\t-------------------------------------------------");

            System.out.printf("\t%-5d %-15s %-20s %-15s %-12s %16d%n",
                    cashier.getEmployee_id(),
                    cashier.getEmployee_username(),
                    cashier.getEmployee_full_name(),
                    cashier.getPhone_number(),
                    cashier.getHired_date(),
                    cashier.getTotal_transaction_processed());

        System.out.print("\n\tPress Enter to continue...");
        scanf.nextLine();

    }


    public void view_employee_list() {
        Scanner scanf = new Scanner(System.in);
        load_cashiers_from_CSV();

        if (cashiers.isEmpty()) {
            System.out.println("\n\tNo employees found in the system.");
            System.out.println("\n\tPress Enter to continue...");
            scanf.nextLine();
            return;
        }

        System.out.println("\n\t=================================================");
        System.out.println("\t|              Employee List                      |");
        System.out.println("\t=================================================");
        System.out.printf("\t%-5s %-15s %-20s %-15s %-12s %-24s%n",
                "ID", "Username", "Full Name", "Phone", "Hire Date", "Total Transaction Processed");
        System.out.println("\t-------------------------------------------------");

        for (Cashier cashier : cashiers) {
            System.out.printf("\t%-5d %-15s %-20s %-15s %-12s %16d%n",
                    cashier.getEmployee_id(),
                    cashier.getEmployee_username(),
                    cashier.getEmployee_full_name(),
                    cashier.getPhone_number(),
                    cashier.getHired_date(),
                    cashier.getTotal_transaction_processed());
        }

        System.out.print("\n\tPress Enter to continue...");
        scanf.nextLine();
    }

    public void delete_employee() {
        Scanner scanf = new Scanner(System.in);

        if (cashiers.isEmpty()) {
            return;
        }

        while (true) {
            view_employee_list();
            System.out.print("\n\tEnter employee ID to delete (0 to cancel): ");
            try {
                int employeeId = Integer.parseInt(scanf.nextLine());

                if (employeeId == 0) {
                    System.out.println("\n\tDeletion cancelled.");
                    System.out.print("\n\tPress Enter to continue...");
                    scanf.nextLine();
                    return;
                }

                Cashier employeeToDelete = null;
                for (Cashier cashier : cashiers) {
                    if (cashier.getEmployee_id() == employeeId) {
                        employeeToDelete = cashier;
                        break;
                    }
                }

                if (employeeToDelete == null) {
                    System.out.println("\n\tEmployee ID not found. Please try again.");
                    System.out.print("\n\tPress Enter to continue...");
                    scanf.nextLine();
                    continue;
                }

                while (true) {
                    System.out.println("\n\tAre you sure you want to delete employee:");
                    System.out.println("\tID: " + employeeToDelete.getEmployee_id());
                    System.out.println("\tName: " + employeeToDelete.getEmployee_full_name());
                    System.out.println("\tPhone Number: " + employeeToDelete.getPhone_number());
                    System.out.println("\tHired Date: " + employeeToDelete.getHired_date());
                    System.out.println("\tTotal Transaction Processed: " + employeeToDelete.getTotal_transaction_processed());
                    System.out.print("\n\t[Y] to confirm, [N] to cancel: ");

                    String confirmation = scanf.nextLine();
                    if (confirmation.equalsIgnoreCase("Y")) {
                        cashiers.remove(employeeToDelete);
                        save_all_cashiers_to_csv(); // Save updated list to CSV
                        System.out.println("\n\tEmployee successfully deleted.");
                        System.out.print("\t\tPress Enter to continue...");
                        scanf.nextLine();
                        return;
                    } else if (confirmation.equalsIgnoreCase("N")) {
                        System.out.println("\n\tDeletion cancelled.");
                        System.out.print("\t\tPress Enter to continue...");
                        scanf.nextLine();
                        return;
                    } else {
                        System.out.println("\n\tInvalid Input");
                        System.out.print("\t\tPress Enter to continue...");
                        scanf.nextLine();
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("\n\tInvalid input. Please enter a valid employee ID.");
                System.out.print("\t\tPress Enter to continue...");
                scanf.nextLine();
            }
        }
    }


    // dito ay id lang need ng manager para palitan password mo
    public void manager_reset_employee_password() {
        Scanner scanf = new Scanner(System.in);

        if (cashiers.isEmpty()) {
            return;
        }

        while (true) {
            view_employee_list();
            System.out.print("\n\tEnter employee ID to reset password (0 to cancel): ");
            try {
                int employeeId = Integer.parseInt(scanf.nextLine());

                if (employeeId == 0) {
                    System.out.println("\n\tPassword reset cancelled.");
                    System.out.print("\n\tPress Enter to continue...");
                    scanf.nextLine();
                    return;
                }

                // Find the employee
                Cashier employeeToReset = null;
                for (Cashier cashier : cashiers) {
                    if (cashier.getEmployee_id() == employeeId) {
                        employeeToReset = cashier;
                        break;
                    }
                }

                if (employeeToReset == null) {
                    System.out.println("\n\tEmployee ID not found. Please try again.");
                    System.out.print("\n\tPress Enter to continue...");
                    scanf.nextLine();
                    continue;
                }

                System.out.println("\n\tResetting password for employee:");
                System.out.println("\tID: " + employeeToReset.getEmployee_id());
                System.out.println("\tName: " + employeeToReset.getEmployee_full_name());

                String newPassword;
                String confirmPassword;
                do {
                    newPassword = input_password(scanf, "\n\tEnter new password: ");
                    confirmPassword = input_password(scanf, "\tConfirm new password: ");

                    if (!newPassword.equals(confirmPassword)) {
                        System.out.println("\n\tPasswords do not match. Please try again.");
                    }
                } while (!newPassword.equals(confirmPassword));

                employeeToReset.setPassword(newPassword);
                save_all_cashiers_to_csv(); // Save updated list to CSV

                System.out.println("\n\tPassword successfully reset.");
                System.out.print("\n\tPress Enter to continue...");
                scanf.nextLine();
                return;

            } catch (NumberFormatException e) {
                System.out.println("\n\tInvalid input. Please enter a valid employee ID.");
                System.out.print("\t\tPress Enter to continue...");
                scanf.nextLine();
            }
        }
    }


    // Helper method to save all cashiers to CSV after modifications
    private void save_all_cashiers_to_csv() {
        try (FileWriter writer = new FileWriter("accounts/cashier_employees.csv")) {

            writer.write("Employee_id,Employee_username,Employee_first_name,Employee_surname," +
                    "password,phone_number,hired_date,total_transaction_processed\n");

            for (Cashier cashier : cashiers) {
                writer.append(String.valueOf(cashier.getEmployee_id())).append(",");
                writer.append(cashier.getEmployee_username()).append(",");
                writer.append(cashier.getEmployee_first_name()).append(",");
                writer.append(cashier.getEmployee_surname()).append(",");
                writer.append(cashier.getPassword()).append(",");
                writer.append(cashier.getPhone_number()).append(",");
                writer.append(cashier.getHired_date()).append(",");
                writer.append(String.valueOf(cashier.getTotal_transaction_processed())).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving cashier employees data to file: " + e.getMessage());
        }
    }


    // pag personal mismo na cashier ung magpapalit ng password
    private void cashier_employee_change_password(Cashier cashier) {
        Scanner scanf = new Scanner(System.in);
        String new_password;
        String confirm_password = "";
        int attemptCount = 0;
        final int MAX_ATTEMPTS = 3;

        while (attemptCount < MAX_ATTEMPTS) {

            System.out.print("\n\tAre you sure changing password (Y/N): ");
            String change_password_confirmation = scanf.nextLine().trim();

            if (change_password_confirmation.equalsIgnoreCase("Y")) {
                System.out.print("\tEnter your current password: ");
                String current_password = scanf.nextLine();

                if (current_password.equals(cashier.getPassword())) {
                    System.out.print("\tSet your new password: ");
                    new_password = scanf.nextLine();

                    while (!new_password.equals(confirm_password)) {
                        System.out.print("\tConfirm the new password: ");
                        confirm_password = scanf.nextLine();
                        if (!new_password.equals(confirm_password)) {
                            System.out.println("\n\tPasswords do not match. Please try again.");
                        }
                    }
                    cashier.setPassword(new_password);
                    save_all_cashiers_to_csv();
                    System.out.println("\n\tThe password is successfully changed.");
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine();
                    cashier_account_menu_profile(cashier);
                    break;
                } else {
                    attemptCount++;
                    System.out.println("\n\tIncorrect current password. Attempts left: " + (MAX_ATTEMPTS - attemptCount));
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine();
                }

            } else if (change_password_confirmation.equalsIgnoreCase("N")) {
                cashier_account_menu_profile(cashier);
                return;
            } else {
                System.out.print("\t\tPress Enter key to continue.");
                scanf.nextLine();
            }

        }

        // kapag sumosobra kana tigil ka na
        if (attemptCount == MAX_ATTEMPTS) {
            System.out.println("\n\tMaximum attempts reached. Password change failed.\n");
            System.out.print("\t\tPress Enter key to continue.");
            scanf.nextLine();
        }
    }


}
