package Process;

import Shopper.*;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BrowseProduct {
    // Method to read products from the CSV file
    public List<Product> load_products_from_CSV(String fileName) {
        List<Product> products = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String code = values[0];
                String name = values[1];
                double price = Double.parseDouble(values[2]);
                int stock = Integer.parseInt(values[3]);
                products.add(new Product(code, name, price, stock));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return products;
    }

    // product lang nakalagay dahil may toString na override na para no need na isa isahin
    // ang isang product ay naka automatic na translate to string kesa parse
    public static void display_products(List<Product> products) {
        System.out.println("\n\tAvailable Products:\n");
        for (Product product : products) {
            System.out.println("\t" + product);
        }
    }

    public static List<Product> browse_beverages() {
        BrowseProduct browse_beverages = new BrowseProduct();

        //dito gumawa sya ng array form para iproseso ung laman ng csv
        List<Product> beverages = browse_beverages.load_products_from_CSV("beverages.csv");

        display_products(beverages); // Display the loaded products
        return beverages;
    }

    public static List<Product> browse_snacks(){
        BrowseProduct browse_snacks = new BrowseProduct();

        //dito gumawa sya ng array form para iproseso ung laman ng csv
        List<Product> snacks = browse_snacks.load_products_from_CSV("snacks.csv");

        display_products(snacks);
        return snacks;
    }

    public static List<Product> browse_canned_goods() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return null;
        }

        String choice;

        while (true) {
            System.out.println("\n\n\t----------------------------------------------");
            System.out.println("\t|              Select kind of Canned Goods: |");
            System.out.println("\t|                                            |");
            System.out.println("\t|              [1] Canned Fish               |");
            System.out.println("\t|              [2] Canned Meat               |");
            System.out.println("\t|                                            |");
            System.out.println("\t|              [0] Go Back                   |");
            System.out.println("\t----------------------------------------------");

            System.out.print("\n\tEnter Here: ");
            choice = console.readLine().trim(); // Use console.readLine() for input

            switch (choice) {
                case "0":
                    return null;  // Return null if user chooses to go back
                case "1":
                    BrowseProduct browse_canned_fish = new BrowseProduct();
                    List<Product> canned_fish = browse_canned_fish.load_products_from_CSV("canned_fish.csv");
                    display_products(canned_fish);
                    return canned_fish;
                case "2":
                    BrowseProduct browse_canned_meat = new BrowseProduct();
                    List<Product> canned_meat = browse_canned_meat.load_products_from_CSV("canned_meat.csv");
                    display_products(canned_meat);
                    return canned_meat;
                default:
                    System.out.println("\nInvalid input. Try again...\n");
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine(); // Use console.readLine() to pause before continuing
            }
        }
    }



    public static List<Product> browse_condiments() {
        BrowseProduct browse_condiments = new BrowseProduct();
        List<Product> condiments = browse_condiments.load_products_from_CSV("condiments.csv");
        display_products(condiments);
        return condiments;
    }


    public static List<Product> browse_dairy() {
        BrowseProduct browse_dairy = new BrowseProduct();
        List<Product> dairy = browse_dairy.load_products_from_CSV("dairy.csv");
        display_products(dairy);
        return dairy;
    }


    public static List<Product> browse_frozen_foods() {
        BrowseProduct browse_frozen_foods = new BrowseProduct();
        List<Product> dairy = browse_frozen_foods.load_products_from_CSV("frozen_foods.csv");
        display_products(dairy);
        return dairy;
    }


    public static List<Product> browse_self_care_items() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return null;
        }

        String choice;

        while (true) {
            System.out.println("\n\n\t���������������������������������������������������������");
            System.out.println("\t�              Select kind of Self Care Product:        �");
            System.out.println("\t�                                                       �");
            System.out.println("\t�              [1] Body Care                            �");
            System.out.println("\t�              [2] Beauty Care                          �");
            System.out.println("\t�                                                       �");
            System.out.println("\t�              [0] Go Back                              �");
            System.out.println("\t�                                                       �");
            System.out.println("\t�                                                       �");
            System.out.println("\t���������������������������������������������������������");

            System.out.print("\n\tEnter Here: ");
            choice = console.readLine();

            switch (choice) {
                case "0":
                    return null;
                case "1":
                    BrowseProduct browse_body_care = new BrowseProduct();
                    List<Product> body_care = browse_body_care.load_products_from_CSV("body_care.csv");
                    display_products(body_care);
                    return body_care;
                case "2":
                    BrowseProduct browse_beauty_care = new BrowseProduct();
                    List<Product> beauty_care = browse_beauty_care.load_products_from_CSV("beauty_care.csv");
                    display_products(beauty_care);
                    return beauty_care;
                default:
                    System.out.println("\nInvalid input. Try again...\n");
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
            }
        }
    }


    public static List<Product> browse_detergents() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Run this program in a terminal.");
            return null;
        }

        String choice;

        while (true) {
            System.out.println("\n\n\t���������������������������������������������������������");
            System.out.println("\t�              Select kind of Detergents:               �");
            System.out.println("\t�                                                       �");
            System.out.println("\t�              [1] Powder Detergent                     �");
            System.out.println("\t�              [2] Bar Soap                             �");
            System.out.println("\t�              [3] Liquid Soap                          �");
            System.out.println("\t�                                                       �");
            System.out.println("\t�              [0] Go Back                              �");
            System.out.println("\t�                                                       �");
            System.out.println("\t�                                                       �");
            System.out.println("\t���������������������������������������������������������");

            System.out.print("\n\tEnter Here: ");
            choice = console.readLine();

            switch (choice) {
                case "0":
                    return null;
                case "1":
                    BrowseProduct browse_powder_detergent = new BrowseProduct();
                    List<Product> powder_detergent = browse_powder_detergent.load_products_from_CSV("powder_detergents.csv");
                    display_products(powder_detergent);
                    return powder_detergent;
                case "2":
                    BrowseProduct browse_bar_soap = new BrowseProduct();
                    List<Product> bar_soap = browse_bar_soap.load_products_from_CSV("bar_soaps.csv");
                    display_products(bar_soap);
                    return bar_soap;
                case "3":
                    BrowseProduct browse_liquid_soap = new BrowseProduct();
                    List<Product> liquid_soap = browse_liquid_soap.load_products_from_CSV("liquid_soaps.csv");
                    display_products(liquid_soap);
                    return liquid_soap;
                default:
                    System.out.println("\nInvalid input. Try again...\n");
                    System.out.println("\n\tAn error has occurred");
                    System.out.print("\t\tPress Enter key to continue.");
                    console.readLine();
            }
        }
    }

}
