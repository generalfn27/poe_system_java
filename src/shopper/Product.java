package shopper;


// Product class to represent each product
class Product {
    private String code;
    private String name;
    private double price;
    private int stock;

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;

        System.out.println("\t|        [1] Beverages                               |");
        System.out.println("\t|        [2] Snacks                  |");
        System.out.println("\t|        [3] Canned Goods                            |");
        System.out.println("\t|        [4] Condiments                            |");
        System.out.println("\t|        [5] Dairy                            |");
        System.out.println("\t|        [6] Frozen Foods                            |");
        System.out.println("\t|        [7] Body Care & Beauty Care                            |");
        System.out.println("\t|        [9] Detergents & Soaps                            |\n");
        System.out.println("\t|        [0] Go Back                             |");
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



