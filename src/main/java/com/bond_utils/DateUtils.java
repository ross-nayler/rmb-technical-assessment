package com.bond_utils;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateUtils {

    public static HashMap<String, LocalDate> getLastAndNextCpnDates(MonthDay cpnDate1, MonthDay cpnDate2, LocalDate settlementDate) {
        int settlementYear = settlementDate.getYear();

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

    public static LocalDate getBooksClosedDate(MonthDay booksClosedDate1, MonthDay booksClosedDate2, LocalDate lastCouponDate, LocalDate nextCouponDate) {
        List<LocalDate> dateOptions = new ArrayList<>();
        dateOptions.add(booksClosedDate1.atYear(lastCouponDate.getYear()));
        dateOptions.add(booksClosedDate1.atYear(nextCouponDate.getYear()));
        dateOptions.add(booksClosedDate2.atYear(lastCouponDate.getYear()));
        dateOptions.add(booksClosedDate2.atYear(nextCouponDate.getYear()));

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

    public static double getRemainingCouponDates(LocalDate maturityDate, LocalDate nextCouponDate) {
        return MathUtils.round(ChronoUnit.DAYS.between(nextCouponDate, maturityDate)/(365.25/2), 0);
    }

    public static boolean isCumInterest(LocalDate settlementDate, LocalDate booksClosedDate) {
        return settlementDate.isBefore(booksClosedDate);
    }

    public static int getDaysAcc(LocalDate settlementDate, LocalDate lastCouponDate, LocalDate nextCouponDate, boolean isCumInterest) {
        if (isCumInterest) {
           return (int)ChronoUnit.DAYS.between(lastCouponDate, settlementDate);
        }
        else {
            return (int)ChronoUnit.DAYS.between(nextCouponDate, settlementDate);
        }
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

    public static double getBrokenPeriod(LocalDate nextCouponDate, LocalDate settlementDate, LocalDate lastCouponDate, LocalDate maturityDate) {
        if (!nextCouponDate.equals(maturityDate)) {
            return (double)ChronoUnit.DAYS.between(settlementDate, nextCouponDate) / (double) ChronoUnit.DAYS.between(lastCouponDate, nextCouponDate);
        }
        else {
            return (double)ChronoUnit.DAYS.between(settlementDate, nextCouponDate) / (365d/2d);
        }
    }
}