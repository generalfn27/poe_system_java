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

    // if discount type == off edi minus
    // if discount type == percentage edi multiply sa double para 10% off or 5
    //gagamit lng ng off sa mga general store discount at max 15% off sa mga items

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
