package Shopper;

public class Coupon {
    private String discountCode;
    private String discountType;
    private double discountValue;
    private int couponQuantity;


    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public int getCouponQuantity() {
        return couponQuantity;
    }

    public void setCouponQuantity(int couponQuantity) {
        this.couponQuantity = couponQuantity;
    }
}
