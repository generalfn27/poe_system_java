package Main;

import User_Types.UserType;
import Employee.ManagerCredentials;

public class Main {
    public static void main(String[] args) {
        //System.out.print("\033[48;5;15m\033[30m");  // White background and black text
        if (ManagerCredentials.needs_initial_setup()) {
            ManagerCredentials.initialSetup();
        }
        UserType.user_type_menu();
        //isama revised flowchart sa files tapos mag
        // start na sa weekend ng may gui
    }
}