package Employee;

import java.io.*;
import java.util.Scanner;

public class ManagerCredentials {
    private static final String BASE_DIRECTORY = "oopr-poe-data/";
    private static final String CREDENTIALS_FILE = BASE_DIRECTORY + "accounts/manager_credentials.txt";
    private static final String STORE_NAME_FILE = BASE_DIRECTORY + "accounts/store_name.txt";
    private static final String[] REQUIRED_DIRECTORIES = {
            BASE_DIRECTORY + "queues",
            BASE_DIRECTORY + "products",
            BASE_DIRECTORY + "accounts",
            BASE_DIRECTORY + "receipts"
    };

    // Check if manager credentials exist
    public static boolean needs_initial_setup() {
        File credFile = new File(CREDENTIALS_FILE);
        return !credFile.exists() || credFile.length() <= 0;
    }

    public static void initialSetup() {
        create_required_directories();
        Scanner scanf = new Scanner(System.in);

        System.out.println("\n\n\n\t=======================================");
        System.out.println("\t|                                     |");
        System.out.println("\t|     First Time Program Setup        |");
        System.out.println("\t|                                     |");
        System.out.println("\t=======================================\n");

        String username = get_valid_input(scanf, "\tEnter manager username: ",
                "\tManager username must be at least 8 characters long.", 8);

        String password = get_valid_password(scanf);

        String phoneNumber = getValidPhoneNumber(scanf);

        String birthplace = get_valid_input(scanf, "\tEnter your birthplace: ",
                "\tBirthplace must be at least 3 characters long.", 3);

        String motherMaidenName = get_valid_input(scanf, "\tEnter your mother's maiden name: ",
                "\tMother's maiden name must be at least 3 characters long.", 3);

        System.out.println("\n\tPlease confirm your details:");
        System.out.println("\tUsername: " + username);
        System.out.println("\tPhone Number: " + phoneNumber);
        System.out.println("\tBirthplace: " + birthplace);
        System.out.println("\tMother's Maiden Name: " + motherMaidenName);

        while (true) {
            System.out.println("\n\tAre you sure you want to save these credentials? ");
            System.out.println("\t[1] Save.");
            System.out.println("\t[2] Refill the initial setup.");
            System.out.println("\t[3] Exit the program.");
            System.out.print("\n\tChoose an option: ");

            String choice = scanf.nextLine().trim();
            switch (choice) {
                case "1":
                    save_credentials(username, password, phoneNumber, birthplace, motherMaidenName);
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine(); // Wait for user to press enter
                    break;
                case "2":
                    System.out.println("\n\tRestarting initial setup...");
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine(); // Wait for user to press enter
                    initialSetup();
                    break;
                case "3":
                    System.out.println("\tSetup cancelled.");
                    System.out.println("\t============================================\n");
                    System.out.println("\t|                                          |\n");
                    System.out.println("\t|     Thank You for Using our Program!     |\n");
                    System.out.println("\t|                                          |\n");
                    System.out.println("\t============================================\n");
                    scanf.close();
                    System.exit(0);
                    return;
                default:
                    System.out.println("\tInvalid input. Please enter 1, 2, or 3.");
                    continue;
            }
            break;
        }

        save_credentials(username, password, phoneNumber, birthplace, motherMaidenName);

        set_store_name(scanf);

        System.out.print("\t\tPress Enter key to continue.");
        scanf.nextLine(); //used for press any key to continue
    }


    private static boolean confirm_action(Scanner scanf) {
        while (true) {
            System.out.print("\tAre you sure you want to change your password?" + " (y/n): ");
            String choice = scanf.nextLine().trim().toLowerCase();
            switch (choice) {
                case "y": return true;
                case "n": return false;
                default:
                    System.out.println("\tInvalid input. Please enter 'y' for yes or 'n' for no.");
            }
        }
    }


    private static String get_valid_input(Scanner scanf, String prompt, String errorMsg, int minLength) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanf.nextLine().trim();
            if (input.length() >= minLength) {
                return input;
            }
            System.out.println("\t" + errorMsg);
        }
    }


    private static String get_valid_password(Scanner scanf) {
        String password, confirmPassword;
        while (true) {
            password = get_valid_input(scanf, "\n\tEnter password (min 8 characters): ",
                    "\tPassword must be at least 8 characters long.", 8);

            System.out.print("\tConfirm password: ");
            confirmPassword = scanf.nextLine().trim();

            if (password.equals(confirmPassword)) {
                return password;
            }
            System.out.println("\tPasswords do not match. Please try again.");
            System.out.print("\t\tPress Enter key to continue.");
            scanf.nextLine(); //used for press any key to continue
        }
    }


    private static String getValidPhoneNumber(Scanner scanf) {
        String phoneNumber;
        while (true) {
            phoneNumber = get_valid_input(scanf, "\n\tEnter contact phone number (e.g. 09091234567): ",
                    "\tPhone number must be at least 11 digits.", 11);

            if (phoneNumber.matches("^09\\d{9}$")) {
                return phoneNumber;
            } else {
                System.out.println("\tInvalid phone number. Please enter an 11-digit number starting with '09'.");
            }
            System.out.print("\t\tPress Enter key to continue.");
            scanf.nextLine(); //used for press any key to continue
        }
    }


    private static void save_credentials(String username, String password,
                                         String phoneNumber, String birthplace,
                                         String motherMaidenName) {
        File credFile = new File(CREDENTIALS_FILE);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(credFile))) {
            writer.write(String.join(",",
                    username,
                    password,
                    phoneNumber,
                    birthplace,
                    motherMaidenName
            ));
        } catch (IOException e) {
            System.out.println("\tError saving credentials: " + e.getMessage());
        }
    }


    private static void set_store_name(Scanner scanf) {
        File storeNameFile = new File(STORE_NAME_FILE);

        String storeName = get_valid_input(scanf, "\n\tEnter store name: ",
                "\tStore name must be at least 4 characters long.", 4);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storeNameFile))) {
            writer.write(storeName);
        } catch (IOException e) {
            System.out.println("\tError saving store name: " + e.getMessage());
            System.out.print("\t\tPress Enter key to continue.");
            scanf.nextLine(); //used for press any key to continue
        }
    }


    public static boolean validate_manager_login(String inputUsername, String inputPassword) {
        if (needs_initial_setup()) { return false; }

        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 2) {
                    return inputUsername.equals(credentials[0]) &&
                            inputPassword.equals(credentials[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("\tError reading credentials: " + e.getMessage());
        }
        return false;
    }


    public static String getStoreName() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STORE_NAME_FILE))) {
            return reader.readLine();
        } catch (IOException e) {
            return "Default"; // Default store name
        }
    }


    public static void recover_manager_credentials(Scanner scanf) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 5) {
                    System.out.println("\n\tPassword Recovery");
                    System.out.print("\tEnter registered phone number: ");
                    String inputPhone = scanf.nextLine().replaceAll("[^0-9]", "");

                    if (!inputPhone.equals(credentials[2])) {
                        System.out.println("\tVerification failed.");
                        return;
                    }

                    System.out.print("\tEnter your birthplace: ");
                    String inputBirthplace = scanf.nextLine().trim();

                    System.out.print("\tEnter your mother's maiden name: ");
                    String inputMotherMaidenName = scanf.nextLine().trim();

                    if (!inputBirthplace.equals(credentials[3]) || !inputMotherMaidenName.equals(credentials[4])) {
                        System.out.println("\tVerification failed.");
                        System.out.print("\t\tPress Enter key to continue.");
                        scanf.nextLine(); //used for press any key to continue
                        return;
                    }

                    System.out.println("\n\tVerification successful!");
                    System.out.println("\tUsername: " + credentials[0]);

                    String newPassword;
                    String confirmPassword;
                    do {
                        System.out.print("\tEnter new password: ");
                        newPassword = scanf.nextLine();
                        System.out.print("\tConfirm new password: ");
                        confirmPassword = scanf.nextLine();

                        if (!newPassword.equals(confirmPassword)) {
                            System.out.println("\tError: Passwords do not match. Please try again.");
                        } else if (newPassword.isEmpty()) {
                            System.out.println("\tError: Password cannot be empty.");
                        }
                    } while (!newPassword.equals(confirmPassword) || newPassword.isEmpty());

                    if (confirm_action(scanf)) {
                        credentials[1] = newPassword;

                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
                            writer.write(String.join(",", credentials));
                            System.out.println("\tPassword updated successfully!");
                        } catch (IOException e) {
                            System.out.println("\tError saving updated credentials: " + e.getMessage());
                        }
                    } else { System.out.println("\tPassword change cancelled."); }

                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine(); //used for press any key to continue
                    return;
                }
            }
            System.out.println("\tVerification failed.");
        } catch (IOException e) {
            System.out.println("\tError in recovery process: " + e.getMessage());
        }
    }


    public static void change_store_name(Scanner scanf) {
        String currentStoreName = getStoreName();
        System.out.println("\n\tCurrent store name: " + currentStoreName);
        String choice;

        while (true) {
            System.out.print("\n\tDo you want to change the store name? (y/n): ");
            choice = scanf.nextLine().trim().toLowerCase();

            switch (choice) {
                case "y":
                    String newStoreName = get_valid_input(scanf, "\n\tEnter new store name: ",
                            "\tStore name must be at least 2 characters long.", 2);

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(STORE_NAME_FILE))) {
                        writer.write(newStoreName);
                        System.out.println("\tStore name updated successfully to: " + newStoreName);
                    } catch (IOException e) {
                        System.out.println("\tError saving new store name: " + e.getMessage());
                    }
                    return; // Exit the method after updating the store name
                case "n":
                    System.out.println("\tStore name was not changed.");
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine(); //used for press any key to continue
                    return; // Exit the method without making changes
                default:
                    System.out.println("\tInvalid input. Please enter 'y' for yes or 'n' for no.");
                    System.out.print("\t\tPress Enter key to continue.");
                    scanf.nextLine(); //used for press any key to continue
                    break;
            }
        }
    }


    private static void create_required_directories() {
        for (String directoryPath : REQUIRED_DIRECTORIES) {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("\tCreated directory: " + directoryPath);
                } else {
                    System.out.println("\tFailed to create directory: " + directoryPath);
                }
            }
        }
    }


}