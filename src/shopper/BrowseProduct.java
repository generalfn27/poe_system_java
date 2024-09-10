package shopper;

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
        BrowseProduct browse_canned_goods = new BrowseProduct();
        char choice;
        Scanner scanf = new Scanner(System.in);

        System.out.println("\t���������������������������������������������������������\n");
        System.out.println("\t�              Select kind of Canned Goods:             �\n");
        System.out.println("\t�                                                       �\n");
        System.out.println("\t�              [1] Canned Fish                          �\n");
        System.out.println("\t�              [2] Canned Meat                          �\n");
        System.out.println("\t�                                                       �\n");
        System.out.println("\t�              [0] Go Back                              �\n");
        System.out.println("\t�                                                       �\n");
        System.out.println("\t�                                                       �\n");
        System.out.println("\t���������������������������������������������������������\n");

        System.out.print("\n\tEnter Here: ");
        scanf.nextLine();

    }

}
