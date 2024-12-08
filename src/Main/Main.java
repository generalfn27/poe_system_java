package Main;

import User_Types.UserType;
import Employee.ManagerCredentials;

public class Main {
    public static void main(String[] args) {
        if (ManagerCredentials.needs_initial_setup()) {
            ManagerCredentials.initialSetup();
        }
        UserType.user_type_menu();
    }
//hindi ko pa ma upload dahil sakit ng ulo ko sa molds
}