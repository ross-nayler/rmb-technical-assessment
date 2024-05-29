package com.utils;

import java.time.LocalDate;

public class MathUtils {

    public static float getBasicCoupon(float couponRate) {
        return couponRate/2;
    }

    public static double getDiscountFactor(double yield) {
        return 1/(1+yield/200);
    }

    public static double getBrokenPeriodDiscountFactor(double discountFactor, double brokenPeriod, LocalDate nextCouponDate, LocalDate maturityDate) {
        if (!nextCouponDate.equals(maturityDate)) {
            return Math.pow(discountFactor, brokenPeriod);
        }
        else {
            return discountFactor / (discountFactor + brokenPeriod * (1 - discountFactor));
        }
    }

    public static double round(double a, int n) {
        return (double)Math.round(a * Math.pow(10, n)) / Math.pow(10, n);
    }
}
