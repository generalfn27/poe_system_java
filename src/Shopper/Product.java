package Shopper;


// Product class to represent each product
public class Product {
    public String code;
    public String name;
    public double price;
    public int stock;

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return String.format("Code: %-6s    Name: %-24s Price: %-5.2f    Stock: %2d", code, name, price, stock);
    }
}



