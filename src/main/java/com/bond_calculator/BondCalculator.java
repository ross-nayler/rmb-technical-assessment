package com.bond_calculator;

import com.bond.Bond;
import com.utils.DateUtils;

import java.time.LocalDate;
import java.util.*;

public class BondCalculator {

    private final Bond bond;
    private LocalDate settlementDate;
    private Float yield;
    private final HashMap<String, LocalDate> lastAndNextCouponDates;
    private final LocalDate booksClosedDate;


    public BondCalculator(Bond bond, LocalDate settlementDate, Float yield) {
        this.bond = bond;
//        Ensure entered settlement date is before bond maturity date
        if (settlementDate.isAfter(bond.getMaturityDate())) {
            throw new IllegalArgumentException("Settlement date is after maturity date");
        }
        this.settlementDate = settlementDate;
        this.yield = yield;
        this.lastAndNextCouponDates = DateUtils.getLastAndNextCpnDates(bond, settlementDate);
        this.booksClosedDate = DateUtils.getBooksClosedDate(bond, this.getLastCouponDate(), this.getNextCouponDate());
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

    public Float getYield() {
        return this.yield;
    }

    public void setYield(Float yield) {
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
        return DateUtils.getRemainingCouponDates(this.bond, this.getNextCouponDate());
    }
}