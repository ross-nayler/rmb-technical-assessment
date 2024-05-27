package com.utils;

import com.bond.Bond;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateUtils {

    public static HashMap<String, LocalDate> getLastAndNextCpnDates(Bond bond, LocalDate settlementDate) {
        int settlementYear = settlementDate.getYear();
        MonthDay cpnDate1 = bond.getCouponDate1();
        MonthDay cpnDate2 = bond.getCouponDate2();

        List<LocalDate> dateChoices = new ArrayList<>();

        if (settlementDate.isBefore(LocalDate.of(settlementYear, 7, 1))) {
            int prevYear = settlementDate.getYear() - 1;
            for (MonthDay cpnDate : Arrays.asList(cpnDate1, cpnDate2)) {
                for (int year : Arrays.asList(prevYear, settlementYear)) {
                    dateChoices.add(cpnDate.atYear(year));
                }
            }
        }

        if (settlementDate.isAfter(LocalDate.of(settlementYear, 7, 1))) {
            int nextYear = settlementDate.getYear() + 1;
            for (MonthDay cpnDate : Arrays.asList(cpnDate1, cpnDate2)) {
                for (int year : Arrays.asList(settlementYear, nextYear)) {
                    dateChoices.add(cpnDate.atYear(year));
                }
            }
        }

        LocalDate lastCouponDate = LocalDate.MIN;
        LocalDate nextCouponDate = LocalDate.MAX;
        for(LocalDate cpnDate : dateChoices) {
            if (!cpnDate.isAfter(settlementDate)) {
                if (cpnDate.isAfter(lastCouponDate)) {
                    lastCouponDate = cpnDate;
                }
            }
            if (cpnDate.isAfter(settlementDate)) {
                if (cpnDate.isBefore(nextCouponDate)) {
                    nextCouponDate = cpnDate;
                }
            }
        }

        HashMap<String, LocalDate> couponDates = new HashMap<>();

        couponDates.put("LCD", lastCouponDate);
        couponDates.put("NCD", nextCouponDate);

        return couponDates;
    }

    public static LocalDate getBooksClosedDate(Bond bond, LocalDate lastCouponDate, LocalDate nextCouponDate) {
        List<LocalDate> dateOptions = new ArrayList<>();
        dateOptions.add(bond.getBooksClosedDate1().atYear(lastCouponDate.getYear()));
        dateOptions.add(bond.getBooksClosedDate1().atYear(nextCouponDate.getYear()));
        dateOptions.add(bond.getBooksClosedDate2().atYear(lastCouponDate.getYear()));
        dateOptions.add(bond.getBooksClosedDate2().atYear(nextCouponDate.getYear()));

        LocalDate booksClosedDate = LocalDate.MIN;
        for (LocalDate bcDate : dateOptions) {
            if (dateInRange(bcDate, lastCouponDate, nextCouponDate, false)) {
                if (bcDate.isAfter(booksClosedDate)) {
                    booksClosedDate = bcDate;
                }
            }
        }
        return booksClosedDate;
    }

    public static double getRemainingCouponDates(Bond bond, LocalDate nextCouponDate) {
        return Math.floor(ChronoUnit.DAYS.between(nextCouponDate, bond.getMaturityDate())/(365.25/2));
    }

    public static boolean isCumInterest(LocalDate settlementDate, LocalDate booksClosedDate) {
        return settlementDate.isBefore(booksClosedDate);
    }

    public static boolean dateInRange(LocalDate d, LocalDate a, LocalDate b, boolean isInclusive) {
        int diff = a.compareTo(d) * d.compareTo(b);
        if (isInclusive) {
            return diff >= 0;
        }
        else {
            return diff > 0;
        }
    }

    public static void main(String[] args) {
        String bondName = "R186";
        LocalDate maturityDate = LocalDate.parse("2026-12-21");
        Float couponRate = 10.5f;
        MonthDay couponDate1 = MonthDay.parse("--06-21");
        MonthDay couponDate2 = MonthDay.parse("--12-21");
        MonthDay booksClosedDate1 = MonthDay.parse("--06-11");
        MonthDay booksClosedDate2 = MonthDay.parse("--12-11");
        Bond bond = new Bond(bondName, maturityDate, couponRate, couponDate1, couponDate2, booksClosedDate1, booksClosedDate2);

    }

}