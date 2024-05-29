package com.helper;

import com.bond.Bond;

import java.time.LocalDate;
import java.time.MonthDay;

public class TestHelper {
    public static Bond R186Bond() {
        String bondName = "R186";
        LocalDate maturityDate = LocalDate.parse("2026-12-21");
        Float couponRate = 10.5f;
        MonthDay couponDate1 = MonthDay.parse("--06-21");
        MonthDay couponDate2 = MonthDay.parse("--12-21");
        MonthDay booksClosedDate1 = MonthDay.parse("--06-11");
        MonthDay booksClosedDate2 = MonthDay.parse("--12-11");

        return new Bond(bondName, maturityDate, couponRate, couponDate1, couponDate2, booksClosedDate1, booksClosedDate2);
    }

    public static Bond R2032Bond() {
        String bondName = "R2032";
        LocalDate maturityDate = LocalDate.parse("2032-03-31");
        Float couponRate = 8.25f;
        MonthDay couponDate1 = MonthDay.parse("--03-31");
        MonthDay couponDate2 = MonthDay.parse("--09-30");
        MonthDay booksClosedDate1 = MonthDay.parse("--03-21");
        MonthDay booksClosedDate2 = MonthDay.parse("--09-20");

        return new Bond(bondName, maturityDate, couponRate, couponDate1, couponDate2, booksClosedDate1, booksClosedDate2);
    }
}