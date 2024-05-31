package com.yield_curve;

import com.bond_utils.MathUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Yield {

    private final LocalDate date;
    private int numDays;
    private final float bidRate;
    private final float askRate;

    public Yield(LocalDate date, float bidRate, float askRate) {
        this.date = date;
        this.bidRate = bidRate;
        this.askRate = askRate;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public int getNumDays() {
        return this.numDays;
    }

    public double getBidRate() {
        return this.bidRate;
    }

    public double getAskRate() {
        return this.askRate;
    }

    public double getMidRate() {
        return 0.5 * (this.bidRate + this.askRate);
    }

    public void setNumDays(LocalDate prevDate) {
        this.numDays = (int)ChronoUnit.DAYS.between(prevDate, this.date);
    }

    @Override
    public String toString() {
        return this.date + ": {Num Days: " + this.numDays + "; Bid Yield: " + this.bidRate + "; Ask Yield: " + this.askRate + "}";
    }

    public static void main(String[] args) {
        Yield yield = new Yield(LocalDate.now(), 4.50f, 4.55f);
        System.out.println(yield);
    }
}
