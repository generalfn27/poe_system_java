package Main;

import User_Types.UserType;
import Process.OrderProcessor;
import Process.CashierProcess;

public class Main {
    public static void main(String[] args) {
        OrderProcessor.initialize_queue_number();
        CashierProcess.initialize_receipt_number();

        UserType.user_type_menu();
    }
}