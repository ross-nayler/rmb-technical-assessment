package com.bond;

import java.time.LocalDate;
import java.time.MonthDay;

public class Bond {
    private final String bondName;
    private final LocalDate maturityDate;
    private final Float couponRate;
    private final MonthDay couponDate1;
    private final MonthDay couponDate2;
    private final MonthDay booksClosedDate1;
    private final MonthDay booksClosedDate2;

    public Bond(String bondName,
                LocalDate maturityDate,
                Float couponRate,
                MonthDay couponDate1,
                MonthDay couponDate2,
                MonthDay booksClosedDate1,
                MonthDay booksClosedDate2) {
        this.bondName = bondName;
        this.maturityDate = maturityDate;
        this.couponRate = couponRate;
        this.couponDate1 = couponDate1;
        this.couponDate2 = couponDate2;
        this.booksClosedDate1 = booksClosedDate1;
        this.booksClosedDate2 = booksClosedDate2;
    }

    public String getBondName() {
        return this.bondName;
    }

    public LocalDate getMaturityDate() {
        return this.maturityDate;
    }

    public Float getCouponRate() {
        return this.couponRate;
    }

    public MonthDay getCouponDate1() {
        return this.couponDate1;
    }

    public MonthDay getCouponDate2() {
        return this.couponDate2;
    }

    public MonthDay getBooksClosedDate1() {
        return this.booksClosedDate1;
    }

    public MonthDay getBooksClosedDate2() {
        return this.booksClosedDate2;
    }

    @Override
    public String toString() {
        return this.bondName + ": {MaturityDate: " + this.maturityDate + ", CouponRate: " + this.couponRate + "%, CouponDate1: " + this.couponDate1 + ", CouponDate2: " + this.couponDate2 + ", BooksClosedDate1: " + this.booksClosedDate1 + ", BooksClosedDate2: " + this.booksClosedDate2 + "}";
    }
}