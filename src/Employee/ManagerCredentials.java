package Employee;

import java.io.*;
import java.util.Scanner;

public class ManagerCredentials {
    private static final String CREDENTIALS_FILE = "accounts/manager_credentials.txt";
    private static final String STORE_NAME_FILE = "accounts/store_name.txt";

    // Check if manager credentials exist
    public static boolean needs_initial_setup() {
        File credFile = new File(CREDENTIALS_FILE);
        return !credFile.exists() || credFile.length() <= 0;
    }


    public static void initialSetup() {
        Scanner scanf = new Scanner(System.in);

        System.out.println("\n\t=======================================");
        System.out.println("\t|                                     |");
        System.out.println("\t|     First Time Program Setup        |");
        System.out.println("\t|                                     |");
        System.out.println("\t=======================================\n");

        String username = getValidInput(scanf, "\tEnter manager username: ",
                "\tManager username must be at least 3 characters long.", 3);

        String password = getValidPassword(scanf);

        String phoneNumber = getValidPhoneNumber(scanf);

        save_credentials(username, password, phoneNumber);

        set_store_name(scanf);

        //no need na restart kasi read lang din naman after so nagana siya smut
        //System.out.println("\n\tSetup complete!!! Please restart the program...");
        System.out.print("\t\tPress Enter key to continue.");
        scanf.nextLine(); //used for press any key to continue
        //System.exit(0);
    }

    private static String getValidInput(Scanner scanf, String prompt, String errorMsg, int minLength) {
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

    private static String getValidPassword(Scanner scanf) {
        String password, confirmPassword;
        while (true) {
            password = getValidInput(scanf, "\tEnter password (min 6 characters): ",
                    "\tPassword must be at least 6 characters long.", 6);

            System.out.print("\tConfirm password: ");
            confirmPassword = scanf.nextLine().trim();

            if (password.equals(confirmPassword)) {
                return password;
            }
            System.out.println("\tPasswords do not match. Please try again.");
        }
    }

    private static String getValidPhoneNumber(Scanner scanf) {
        String phoneNumber;
        while (true) {
            phoneNumber = getValidInput(scanf, "\tEnter contact phone number: ",
                    "\tPhone number must be at least 10 digits.", 10);

            // Remove any non-digit characters
            phoneNumber = phoneNumber.replaceAll("[^0-9]", "");

            if (phoneNumber.length() >= 10) {
                return phoneNumber;
            }
            System.out.println("\tInvalid phone number. Please enter a valid number.");
        }
    }

    private static void save_credentials(String username, String password, String phoneNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            // Encrypt or hash the password in a real-world application
            writer.write(username + "," + password + "," + phoneNumber);
        } catch (IOException e) {
            System.out.println("\tError saving credentials: " + e.getMessage());
        }
    }

    private static void set_store_name(Scanner scanf) {
        String storeName = getValidInput(scanf, "\tEnter store name: ",
                "\tStore name must be at least 2 characters long.", 2);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STORE_NAME_FILE))) {
            writer.write(storeName);
        } catch (IOException e) {
            System.out.println("\tError saving store name: " + e.getMessage());
        }
    }

    // Validate login credentials
    public static boolean validate_manager_login(String inputUsername, String inputPassword) {
        if (needs_initial_setup()) {
            return false;
        }

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
            return "no setup name"; // Default store name
        }
    }


    public static void recover_manager_credentials(Scanner scanf) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 3) {
                    System.out.println("\n\tPassword Recovery");
                    System.out.print("\tEnter registered phone number: ");
                    String inputPhone = scanf.nextLine().replaceAll("[^0-9]", "");

                    if (inputPhone.equals(credentials[2])) {
                        System.out.println("\n\tVerification successful!");
                        System.out.println("\tUsername: " + credentials[0]);
                        System.out.println("\tContact Number: " + credentials[2]);
                        System.out.print("\t\tPress Enter key to continue.");
                        scanf.nextLine(); //used for press any key to continue

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

                        // Update the password in the credentials
                        credentials[1] = newPassword;

                        // Save updated credentials back to the file
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
                            writer.write(String.join(",", credentials));
                            System.out.println("\tPassword updated successfully!");
                        } catch (IOException e) {
                            System.out.println("\tError saving updated credentials: " + e.getMessage());
                        }
                        System.out.print("\t\tPress Enter key to continue.");
                        scanf.nextLine(); //used for press any key to continue
                        return;
                    }
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
                    String newStoreName = getValidInput(scanf, "\n\tEnter new store name: ",
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




}