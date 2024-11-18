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

    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    public int getStock() {
        return stock;
    }

    // Method to update the stock quantity
    public void update_stock(int quantityChange) {
        if (this.stock + quantityChange >= 0) {  // Ensure stock doesn't go negative
            this.stock += quantityChange;
            //need ng update stock sa manager part para update product stock sa csv
        } else {
            System.out.println("Not enough stock available!");
        }
    }

    @Override
    public String toString() {
        return String.format("Code: %-6s\tName: %-24s\tPrice: %-5.2f\tStock: %2d", code, name, price, stock);
    }


}

