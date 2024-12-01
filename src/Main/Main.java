package Main;

import User_Types.UserType;
import Process.OrderProcessor;
import Process.CashierProcess;
import Employee.ManagerCredentials;

public class Main {
    public static void main(String[] args) {
        OrderProcessor.initialize_queue_number();
        CashierProcess.initialize_receipt_number();

        if (ManagerCredentials.needs_initial_setup()) {
            ManagerCredentials.initialSetup();
        }
        UserType.user_type_menu();
    }

}