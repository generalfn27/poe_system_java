package Main;

import User_Types.UserType;
import Process.OrderProcessor;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");
        OrderProcessor.initialize_queue_number();
        UserType.user_type_menu();

        System.out.flush();
    }
}