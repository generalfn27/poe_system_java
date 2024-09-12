package Process;

import Shopper.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BrowseProduct {

    // Method to read products from the CSV file
    public List<Product> loadProductsFromCSV(String fileName) {
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

    // Method to display the products
    public void display_products(List<Product> products) {
        System.out.println("\nAvailable Products:");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    public static void browse_beverages() {
        BrowseProduct browse_beverages = new BrowseProduct();

        //dito gumawa sya ng array form para iproseso ung laman ng csv
        List<Product> beverages = browse_beverages.loadProductsFromCSV("beverages.csv");

        browse_beverages.display_products(beverages); // Display the loaded products
    }

    public static void browse_snacks(){
        BrowseProduct browse_snacks = new BrowseProduct();

        //dito gumawa sya ng array form para iproseso ung laman ng csv
        List<Product> snacks = browse_snacks.loadProductsFromCSV("snacks.csv");

        browse_snacks.display_products(snacks); // Display the loaded products
    }

    public static void browse_canned_goods() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        do {
            System.out.println("\t���������������������������������������������������������");
            System.out.println("\t�              Select kind of Canned Goods:             �");
            System.out.println("\t�                                                       �");
            System.out.println("\t�              [1] Canned Fish                          �");
            System.out.println("\t�              [2] Canned Meat                          �");
            System.out.println("\t�                                                       �");
            System.out.println("\t�              [0] Go Back                              �");
            System.out.println("\t�                                                       �");
            System.out.println("\t�                                                       �");
            System.out.println("\t���������������������������������������������������������");

            System.out.print("\n\tEnter Here: ");
            choice = scanf.nextLine();

            switch (choice) {
                case "0":
                    break;
                case "1":
                    BrowseProduct browse_canned_fish = new BrowseProduct();
                    List<Product> canned_fish = browse_canned_fish.loadProductsFromCSV("canned_fish.csv");
                    browse_canned_fish.display_products(canned_fish); // Display the loaded products
                    break;
                case "2":
                    BrowseProduct browse_canned_meat = new BrowseProduct();
                    List<Product> canned_meat = browse_canned_meat.loadProductsFromCSV("canned_fish.csv");
                    browse_canned_meat.display_products(canned_meat); // Display the loaded products
                    break;
                default:
                    System.out.println("\nInvalid input. Try again...\n");
            }
        } while (!choice.equals("0"));
    }

    public static void browse_condiments() {
        BrowseProduct browse_condiments = new BrowseProduct();
        List<Product> condiments = browse_condiments.loadProductsFromCSV("condiments.csv");
        browse_condiments.display_products(condiments); // Display the loaded products
    }

    public static void browse_dairy() {
        BrowseProduct browse_dairy = new BrowseProduct();
        List<Product> dairy = browse_dairy.loadProductsFromCSV("dairy.csv");
        browse_dairy.display_products(dairy); // Display the loaded products
    }

    public static void browse_frozen_foods() {
        BrowseProduct browse_frozen_foods = new BrowseProduct();
        List<Product> dairy = browse_frozen_foods.loadProductsFromCSV("frozen_foods.csv");
        browse_frozen_foods.display_products(dairy); // Display the loaded products
    }

    public static void browse_self_care_items() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        do {
            System.out.println("\t���������������������������������������������������������");
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
            choice = scanf.nextLine();

            switch (choice) {
                case "0":
                    break;
                case "1":
                    BrowseProduct browse_body_care = new BrowseProduct();
                    List<Product> body_care = browse_body_care.loadProductsFromCSV("body_care.csv");
                    browse_body_care.display_products(body_care); // Display the loaded products
                    break;
                case "2":
                    BrowseProduct browse_beauty_care = new BrowseProduct();
                    List<Product> beauty_care = browse_beauty_care.loadProductsFromCSV("beauty_care.csv");
                    browse_beauty_care.display_products(beauty_care); // Display the loaded products
                    break;
                default:
                    System.out.println("\nInvalid input. Try again...\n");
            }
        } while (!choice.equals("0"));
    }

    public static void browse_detergents() {
        Scanner scanf = new Scanner(System.in);
        String choice;

        do {
            System.out.println("\t���������������������������������������������������������");
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
            choice = scanf.nextLine();

            switch (choice) {
                case "0":
                    break;
                case "1":
                    BrowseProduct browse_powder_detergent = new BrowseProduct();
                    List<Product> powder_detergent = browse_powder_detergent.loadProductsFromCSV("powder_detergent.csv");
                    browse_powder_detergent.display_products(powder_detergent); // Display the loaded products
                    break;
                case "2":
                    BrowseProduct browse_bar_soap = new BrowseProduct();
                    List<Product> bar_soap = browse_bar_soap.loadProductsFromCSV("bar_soap.csv");
                    browse_bar_soap.display_products(bar_soap); // Display the loaded products
                    break;
                case "3":
                    BrowseProduct browse_liquid_soap = new BrowseProduct();
                    List<Product> liquid_soap = browse_liquid_soap.loadProductsFromCSV("liquid_soap.csv");
                    browse_liquid_soap.display_products(liquid_soap); // Display the loaded products
                    break;
                default:
                    System.out.println("\nInvalid input. Try again...\n");
            }
        } while (!choice.equals("0"));
    }

}
