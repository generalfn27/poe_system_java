package Main;

import User_Types.UserType;
import Process.OrderProcessor;
import Process.CashierProcess;

public class Main {
    public static void main(String[] args) {
        OrderProcessor.initialize_queue_number();
        CashierProcess.initialize_receipt_number();

    //naka if no tong part na to at throwing boolean 
    //dahil sa first open ng program dapat may store name setup at manager setup
        UserType.user_type_menu();
    }
}