package Main;

import User_Types.UserType;

import static Process.OrderProcessor.initialize_queue_number;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");
        initialize_queue_number();
        UserType.user_type_menu();

        System.out.flush();
    }
}