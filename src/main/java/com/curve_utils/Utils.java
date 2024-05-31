package com.curve_utils;

import com.yield_curve.Rate;
import com.yield_curve.Yield;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Utils {

    public static double interpolateRate(LocalDate queryDate, LocalDate date1, double rate1, LocalDate date2, double rate2) {
        return rate1 + (rate2 - rate1) * (ChronoUnit.DAYS.between(date1, queryDate)) / (ChronoUnit.DAYS.between(date1, date2));
    }

    public static double getRequiredRate(Yield item, Rate rateType) {
        switch (rateType) {
            case BID:
                return item.getBidRate();
            case MID:
                return item.getMidRate();
            case ASK:
                return item.getAskRate();
        }
        return 0;
    }
}
