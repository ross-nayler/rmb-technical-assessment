package com.bond_calculator;

import com.bond.Bond;
import com.utils.DateUtils;
import com.utils.MathUtils;

import java.time.LocalDate;
import java.util.*;

public class BondCalculator {

    private final Bond bond;
    private LocalDate settlementDate;
    private double yield;
    private final HashMap<String, LocalDate> lastAndNextCouponDates;
    private final LocalDate booksClosedDate;


    public BondCalculator(Bond bond, LocalDate settlementDate, double yield) {
        this.bond = bond;
//        Ensure entered settlement date is before bond maturity date
        if (settlementDate.isAfter(bond.getMaturityDate())) {
            throw new IllegalArgumentException("Settlement date is after maturity date");
        }
        this.settlementDate = settlementDate;
        this.yield = yield;
        this.lastAndNextCouponDates = DateUtils.getLastAndNextCpnDates(bond.getCouponDate1(), bond.getCouponDate2(), settlementDate);
        this.booksClosedDate = DateUtils.getBooksClosedDate(bond.getBooksClosedDate1(), bond.getBooksClosedDate2(), this.getLastCouponDate(), this.getNextCouponDate());
    }

    public Bond getBond() {
        return this.bond;
    }

    public LocalDate getSettlementDate() {
        return this.settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public double getYield() {
        return this.yield;
    }

    public void setYield(float yield) {
        this.yield = yield;
    }

    public LocalDate getLastCouponDate() {
        return this.lastAndNextCouponDates.get("LCD");
    }

    public LocalDate getNextCouponDate() {
        return this.lastAndNextCouponDates.get("NCD");
    }

    public LocalDate getBooksClosedDate() {
        return this.booksClosedDate;
    }

    public double getRemainingCoupons() {
        return DateUtils.getRemainingCouponDates(this.bond.getMaturityDate(), this.getNextCouponDate());
    }

    public boolean isCumInterest() {
        return DateUtils.isCumInterest(this.settlementDate, this.booksClosedDate);
    }

    public double getDaysAcc() {
        return DateUtils.getDaysAcc(this.settlementDate, this.getLastCouponDate(), this.getNextCouponDate(), this.isCumInterest());
    }

    public float getBasicCoupon() {
        return MathUtils.getBasicCoupon(this.bond.getCouponRate());
    }

    public float getCouponAtNCD() {
        int cumEx = 0;
        if (this.isCumInterest()) {
            cumEx = 1;
        }
        return this.getBasicCoupon() * cumEx;
    }

    public double getDiscountFactor() {
        return MathUtils.getDiscountFactor(this.yield);
    }

    public double getBrokenPeriod() {
        return DateUtils.getBrokenPeriod(this.getNextCouponDate(), this.settlementDate, this.getLastCouponDate(), this.bond.getMaturityDate());
    }

    public double getBrokenPeriodDiscountFactor() {
        return MathUtils.getBrokenPeriodDiscountFactor(this.getDiscountFactor(), this.getBrokenPeriod(), this.getNextCouponDate(), this.bond.getMaturityDate());
    }

    public double getAccruedInterest(boolean rounded) {
        double accruedInterest = (this.getDaysAcc() * this.bond.getCouponRate()) / 365;
        if (rounded) {
            return MathUtils.round(accruedInterest, 5);
        }
        else {
            return accruedInterest;
        }
    }

    public double getAllInPrice(boolean rounded) {
        double bpf = this.getBrokenPeriodDiscountFactor();
        double f = this.getDiscountFactor();
        float cpnAtNCD = this.getCouponAtNCD();
        float cpn = this.getBasicCoupon();
        double n = this.getRemainingCoupons();
        float r = 100f;

        if (rounded) {
            return MathUtils.round(this.getCleanPrice(true) + this.getAccruedInterest(true), 5);
        }
        else {
            if (this.getDiscountFactor() != 1f) {
                return bpf * (cpnAtNCD + cpn*f*(1-Math.pow(f, n))/(1-f) + r*Math.pow(f,n));
            }
            else {
                return cpnAtNCD + cpn * n + r;
            }
        }
    }

    public double getCleanPrice(boolean rounded) {
        double cleanPrice = this.getAllInPrice(false) - this.getAccruedInterest(false);
        if (rounded) {
            return MathUtils.round(cleanPrice, 5);
        }
        else {
            return cleanPrice;
        }
    }
}