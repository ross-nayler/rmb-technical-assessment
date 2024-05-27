package com.bond_calculator;

import com.bond.Bond;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BondCalculator {

    private final Bond bond;
    private LocalDate settlementDate;
    private Float yield;
    private LocalDate lastCouponDate;
    private LocalDate nextCouponDate;


    public BondCalculator(Bond bond, LocalDate settlementDate, Float yield) {
        this.bond = bond;
//        Ensure entered settlement date is before bond maturity date
        if (settlementDate.isAfter(bond.getMaturityDate())) {
            throw new IllegalArgumentException("Settlement date is after maturity date");
        }
        this.settlementDate = settlementDate;
        this.yield = yield;
        this.setCouponDates();
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

    private void setCouponDates() {
        int settlementYear = this.settlementDate.getYear();
        MonthDay cpnDate1 = this.bond.getCouponDate1();
        MonthDay cpnDate2 = this.bond.getCouponDate2();

        List<LocalDate> dateChoices = new ArrayList<>();

        if (this.settlementDate.isBefore(LocalDate.of(settlementYear, Calendar.JULY, 1))) {
            int prevYear = this.settlementDate.getYear() - 1;
            for (MonthDay cpnDate : Arrays.asList(cpnDate1, cpnDate2)) {
                for (int year : Arrays.asList(prevYear, settlementYear)) {
                    dateChoices.add(cpnDate.atYear(year));
                }
            }
        }

        if (this.settlementDate.isAfter(LocalDate.of(settlementYear, Calendar.JULY, 1))) {
            int nextYear = this.settlementDate.getYear() + 1;
            for (MonthDay cpnDate : Arrays.asList(cpnDate1, cpnDate2)) {
                for (int year : Arrays.asList(settlementYear, nextYear)) {
                    dateChoices.add(cpnDate.atYear(year));
                }
            }
        }

        System.out.println("Date choices: " + Arrays.toString(dateChoices.toArray()));
    
        LocalDate lastCouponDate = LocalDate.MIN;
        LocalDate nextCouponDate = LocalDate.MAX;
        for(LocalDate cpnDate : dateChoices) {
            if (!cpnDate.isAfter(this.settlementDate)) {
                System.out.println(cpnDate + " is before or the same as the settlement date");
                if (cpnDate.isAfter(lastCouponDate)) {
                    lastCouponDate = cpnDate;
                }
            }
            if (cpnDate.isAfter(this.settlementDate)) {
                if (cpnDate.isBefore(nextCouponDate)) {
                    nextCouponDate = cpnDate;
                }
            }
        }

        this.lastCouponDate = lastCouponDate;
        this.nextCouponDate = nextCouponDate;
    }

    public static void main(String[] args) {
//        Instantiate bond object
        String bondName = "R186";
        LocalDate maturityDate = LocalDate.parse("2026-12-21");
        Float couponRate = 10.5f;
        MonthDay couponDate1 = MonthDay.parse("--06-21");
        MonthDay couponDate2 = MonthDay.parse("--02-07");
        MonthDay booksClosedDate1 = MonthDay.parse("--06-11");
        MonthDay booksClosedDate2 = MonthDay.parse("--12-11");
        Bond bond = new Bond(bondName, maturityDate, couponRate, couponDate1, couponDate2, booksClosedDate1, booksClosedDate2);

//        Instantiate bond calculator object
        LocalDate settlementDate = LocalDate.parse("2017-02-07");
        Float yield = 8.75f;
        BondCalculator bondCalculator = new BondCalculator(bond, settlementDate, yield);

//        Method testing
        System.out.println("Settlement date: " + settlementDate);
        System.out.println("Last coupon date: " + bondCalculator.lastCouponDate);
        System.out.println("Next coupon date: " + bondCalculator.nextCouponDate);
    }
}