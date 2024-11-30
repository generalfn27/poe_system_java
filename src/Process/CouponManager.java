package Process;

import Shopper.Coupon;

import java.io.*;
import java.util.*;

public class CouponManager {
    private static final String COUPON_FILE = "products/coupons.csv";
    private final List<Coupon> availableCoupons;

    public CouponManager() {
        availableCoupons = new ArrayList<>();
        loadCouponsFromFile();
    }


    public void register_coupon() {
        Scanner scanf = new Scanner(System.in);

        Coupon newCoupon = new Coupon();
        String discountCode = "";
        double discountValue;
        String discountType;
        int couponQuantity;

        System.out.println("\n\t----------------------------------------------");
        System.out.println("\t|            Coupon Registration                |");
        System.out.println("\t|                                               |");

        boolean coupon_exist = true;
        while (coupon_exist) {
            System.out.print("\n\tEnter /// to Cancel");
            System.out.print("\tEnter New Discount Code (4 letters followed by 4 digits): ");
            discountCode = scanf.nextLine();

            if (discountCode.equals("///")){ return; }

            // Validate discount code format
            if (discountCode.matches("^[a-zA-Z]{4}[0-9]{4}$")) {
                boolean duplicateFound = false;
                for (Coupon existingCoupon : availableCoupons) {
                    if (existingCoupon.getDiscountCode().equalsIgnoreCase(discountCode)) {
                        System.out.println("\tCoupon with code " + discountCode + " already exists.");
                        duplicateFound = true;
                        break;
                    }
                }
                if (!duplicateFound) {
                    coupon_exist = false; // Exit the loop if no duplicate found
                }
            } else {
                System.out.println("\tInvalid code format. Must be 4 letters followed by 4 digits.");
            }
        }
        newCoupon.setDiscountCode(discountCode.toUpperCase());


        do {
            System.out.print("\tEnter Discount Type (percentage/off): ");
            discountType = scanf.nextLine().trim().toLowerCase();
            if (!discountType.equals("percentage") && !discountType.equals("off")) {
                System.out.println("\tInvalid discount type. Must be 'percentage' or 'off'.");
            }
        } while (!discountType.equals("percentage") && !discountType.equals("off"));
        newCoupon.setDiscountType(discountType);


        do {
            System.out.print("\tEnter Discount Value: ");
            try {
                discountValue = Double.parseDouble(scanf.nextLine());
                if (discountValue <= 0) {
                    System.out.println("\tDiscount value must be positive.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\tInvalid input. Please enter a numeric value.");
                discountValue = -1; // Force loop to repeat
            }
        } while (discountValue <= 0);
        newCoupon.setDiscountValue(discountValue);

        do {
            System.out.print("\tEnter Coupon Quantity: ");
            try {
                couponQuantity = Integer.parseInt(scanf.nextLine());
                if (couponQuantity <= 0) {
                    System.out.println("\tCoupon quantity must be positive.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\tInvalid input. Please enter an integer value.");
                couponQuantity = -1; // Force loop to repeat
            }
        } while (couponQuantity <= 0);
        newCoupon.setCouponQuantity(couponQuantity);

        availableCoupons.add(newCoupon);
        saveCouponsToFile();

        System.out.println("\tCoupon " + discountCode + " registered successfully.");
        System.out.print("\t\tPress Enter key to continue.");
        scanf.nextLine(); //used for press any key to continue
    }


    public double apply_coupon(String discountCode, double totalPrice) {
        Coupon appliedCoupon = null;
        for (Coupon coupon : availableCoupons) {
            if (coupon.getDiscountCode().equalsIgnoreCase(discountCode)) {
                appliedCoupon = coupon;
                break;
            }
        }

        if (appliedCoupon == null) {
            System.out.println("\n\tInvalid coupon code. Please try again.");
            return -1; // Return a sentinel value to indicate invalid code
        }

        if (appliedCoupon.getCouponQuantity() <= 0) {
            System.out.println("\n\tCoupon has no remaining uses.");
            return -1; // Return a sentinel value to indicate invalid code
        }

        double discountedPrice = getDiscountedPrice(totalPrice, appliedCoupon);

        appliedCoupon.setCouponQuantity(appliedCoupon.getCouponQuantity() - 1);
        saveCouponsToFile();

        System.out.printf("\tCoupon applied. Original price: %.2f, Discounted price: %.2f\n",
                totalPrice, discountedPrice);
        return discountedPrice;
    }


    private double getDiscountedPrice(double totalPrice, Coupon appliedCoupon) {
        double discountedPrice = totalPrice;
        if (appliedCoupon.getDiscountType().equals("percentage")) {
            // Percentage discount (max 100%) para kay mam sheinn
            double MAX_PERCENTAGE_DISCOUNT = 100;
            double discountPercentage = Math.min(appliedCoupon.getDiscountValue(), MAX_PERCENTAGE_DISCOUNT);
            discountedPrice = totalPrice * (1 - (discountPercentage / 100));
        } else if (appliedCoupon.getDiscountType().equals("off")) {
            // Fixed amount off
            // 0 ung ibibigay dahil bawal maging negative so kung
            // binili kang 100 tapos 500 ung voucher mo edi 0 na babayaran mo pero d kita babayaran ano ka swerte
            discountedPrice = Math.max(totalPrice - appliedCoupon.getDiscountValue(), 0);
        }
        return discountedPrice;
    }


    public void loadCouponsFromFile() {
        availableCoupons.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(COUPON_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Coupon coupon = new Coupon();
                    coupon.setDiscountCode(parts[0]);
                    coupon.setDiscountType(parts[1]);
                    coupon.setDiscountValue(Double.parseDouble(parts[2]));
                    coupon.setCouponQuantity(Integer.parseInt(parts[3]));
                    availableCoupons.add(coupon);
                }
            }
        } catch (IOException e) {
            System.out.println("\tError loading coupons: " + e.getMessage());
            // Initialize file if it doesn't exist
            initializeCouponFile();
        }
    }


    // Save coupons to CSV file
    private void saveCouponsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COUPON_FILE))) {
            writer.println("DiscountCode,DiscountType,DiscountValue,CouponQuantity");
            for (Coupon coupon : availableCoupons) {
                writer.printf("%s,%s,%.2f,%d%n",
                        coupon.getDiscountCode(),
                        coupon.getDiscountType(),
                        coupon.getDiscountValue(),
                        coupon.getCouponQuantity());
            }
        } catch (IOException e) {
            System.out.println("\tError saving coupons: " + e.getMessage());
        }
    }


    // Initialize coupon file if it doesn't exist
    private void initializeCouponFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COUPON_FILE))) {
            writer.println("DiscountCode,DiscountType,DiscountValue,CouponQuantity");
        } catch (IOException e) {
            System.out.println("\tError creating coupon file: " + e.getMessage());
        }
    }


    public void view_employee_list() {
        Scanner scanf = new Scanner(System.in);
        loadCouponsFromFile();

        if (availableCoupons.isEmpty()) {
            System.out.println("\n\tNo Coupons found in the system.");
            System.out.print("\n\tPress Enter to continue...");
            scanf.nextLine();
            return;
        }

        System.out.println("\n\t=================================================");
        System.out.println("\t|              Coupon List                      |");
        System.out.println("\t=================================================");
        System.out.printf("\t%-14s %-13s %-8s %-8s%n",
                "Discount Code", "Discount Type", "Value", "Quantity");
        System.out.println("\t-------------------------------------------------");

        for (Coupon coupon : availableCoupons) {
            System.out.printf("\t%-14s %-13s %-8.2f %-8d%n",
                    coupon.getDiscountCode(),
                    coupon.getDiscountType(),
                    coupon.getDiscountValue(),
                    coupon.getCouponQuantity());
        }

        System.out.print("\n\tPress Enter to continue...");
        scanf.nextLine();
    }


    public void delete_coupon() {
        Scanner scanf = new Scanner(System.in);
        view_employee_list();
        System.out.println("\n\t=== Remove Coupon Code ===");
        System.out.print("\n\tEnter coupon code to remove: ");
        String code = scanf.nextLine().toUpperCase();

        Coupon couponToRemove = null;
        for (Coupon coupon : availableCoupons) {
            if (coupon.getDiscountCode().equals(code)){
                couponToRemove = coupon;
                break;
            }
        }

        if (couponToRemove != null) {
            System.out.println("\tCoupon found: " + couponToRemove.getDiscountCode());

            String confirm;
            while (true) {
                System.out.print("\tAre you sure you want to remove this product? (Y/N): ");
                confirm = scanf.nextLine();
                if (confirm.equalsIgnoreCase("Y") || confirm.equalsIgnoreCase("N")) {
                    break;
                } else {
                    System.out.println("\tInvalid input. Please enter 'Y' or 'N'.");
                }
            }

            if (confirm.equalsIgnoreCase("Y")) {
                availableCoupons.remove(couponToRemove);
                saveCouponsToFile();
                System.out.println("\tCoupon removed successfully!");
            } else {
                System.out.println("\tCoupon removal cancelled.");
            }
        }

        System.out.print("\n\tPress Enter to continue...");
        scanf.nextLine();
    }


    public void restock_coupon_quantity() {
        Scanner scanf = new Scanner(System.in);
        view_employee_list();
        System.out.println("\n\t=== Restock Coupon Code ===");
        System.out.print("\n\tEnter coupon code to restock: ");
        String code = scanf.nextLine().toUpperCase();

        Coupon couponToRestock = null;
        for (Coupon coupon : availableCoupons) {
            if (coupon.getDiscountCode().equals(code)){
                couponToRestock = coupon;
                break;
            }
        }

        if (couponToRestock != null) {
            System.out.println("\tCurrent stock for " + couponToRestock.getDiscountCode() + ": " + couponToRestock.getCouponQuantity());

            int additionalStock;
            while (true) {
                try {
                    System.out.print("\tEnter additional stock quantity: ");
                    additionalStock = Integer.parseInt(scanf.nextLine());
                    if (additionalStock <= 0) {
                        System.out.println("\tPlease enter a positive number!");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("\tInvalid input. Please enter a valid number.");
                }
            }

            couponToRestock.update_quantity(additionalStock);
            saveCouponsToFile();
            System.out.println("\n\tStock updated successfully!");
        } else {
            System.out.println("\n\tProduct not found!");
        }

        System.out.print("\t\tPress Enter key to continue...");
        scanf.nextLine();
    }


    public void decrease_coupon_quantity() {
        Scanner scanf = new Scanner(System.in);
        view_employee_list();
        System.out.println("\n\t=== Decrease Coupon Code ===");
        System.out.print("\n\tEnter coupon code to decrease: ");
        String code = scanf.nextLine().toUpperCase();

        Coupon couponToRestock = null;
        for (Coupon coupon : availableCoupons) {
            if (coupon.getDiscountCode().equals(code)){
                couponToRestock = coupon;
                break;
            }
        }

        if (couponToRestock != null) {
            System.out.println("\tCurrent stock for " + couponToRestock.getDiscountCode() + ": " + couponToRestock.getCouponQuantity());

            int additionalStock;
            while (true) {
                try {
                    System.out.print("\tEnter reduction stock quantity: ");
                    additionalStock = Integer.parseInt(scanf.nextLine());
                    if (additionalStock <= 0) {
                        System.out.println("\tPlease enter a positive number!");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("\tInvalid input. Please enter a valid number.");
                }
            }
            couponToRestock.update_quantity(-additionalStock);
            saveCouponsToFile();
            System.out.println("\n\tStock updated successfully!");

        } else { System.out.println("\n\tProduct not found!"); }

        System.out.print("\t\tPress Enter key to continue...");
        scanf.nextLine();
    }


    public void show_random_Coupon() {
        if (availableCoupons.isEmpty()) {
            System.out.println("\n\tNo coupons available to display.");
            return;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(availableCoupons.size());

        Coupon randomCoupon = availableCoupons.get(randomIndex);

        System.out.println("\n\t=== Special Coupon Advertisement ===");
        System.out.printf("\tDiscount Code: %s%n", randomCoupon.getDiscountCode());
        System.out.printf("\tDiscount Value: %.2f%n", randomCoupon.getDiscountValue());
        System.out.printf("\tAvailable Quantity: %d%n", randomCoupon.getCouponQuantity());
        System.out.println("\t====================================");
    }




}