package shopper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BrowseBeverages {

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
                products.add(new Product(code, name, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Method to display the products
    public void displayProducts(List<Product> products) {
        System.out.println("\nAvailable Products:");
        for (Product product : products) {
            System.out.println(product);
        }
    }




}
