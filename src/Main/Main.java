package Main;

import User_Types.UserType;

import static Process.OrderProcessor.initialize_queue_number;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");

        UserType user_type = new UserType();
        user_type.user_type_menu();
        initialize_queue_number();

        //System.out.flush();
    }
}