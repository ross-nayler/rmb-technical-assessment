package com.yield_curve;

import com.bond_utils.MathUtils;
import com.curve_utils.Utils;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class Curve extends TreeMap<LocalDate, Yield> {

    public void putDateYield(Yield yield) {
        super.put(yield.getDate(), yield);
    }

    public void addDayCount() {
        LocalDate parentKey = super.firstEntry().getKey();
        for (Map.Entry<LocalDate, Yield> entry : super.entrySet()) {
            entry.getValue().setNumDays(parentKey);
        }
    }

    public double getRate(LocalDate queryDate, Rate rateType) {
//        Check if query date is before first date
        if (queryDate.isBefore(super.firstKey())) {
            throw new IllegalArgumentException("Query date is before the first date on record.");
        }
//        Flat extrapolate if query date is after last date
        if (queryDate.isAfter(super.lastKey())) {
            Yield lastItem = super.lastEntry().getValue();
            return MathUtils.round(Utils.getRequiredRate(lastItem, rateType), 5);
        }

        Yield queryItem = super.get(queryDate);
//        Check if date exists in dataset
        if (queryItem != null) {
            return MathUtils.round(Utils.getRequiredRate(queryItem, rateType), 5);
        }
//        Interpolate if date doesn't exist
        else {
            Yield prevItem = super.lowerEntry(queryDate).getValue();
            Yield nextItem = super.higherEntry(queryDate).getValue();
            double prevRate = Utils.getRequiredRate(prevItem, rateType);
            double nextRate = Utils.getRequiredRate(nextItem, rateType);
            return MathUtils.round(Utils.interpolateRate(queryDate, prevItem.getDate(), prevRate, nextItem.getDate(), nextRate), 5);
        }
    }


    public static void main(String[] args) {
        Yield yield1 = new Yield(LocalDate.parse("2024-05-17"), 4.50f, 4.55f);
        Yield yield2 = new Yield(LocalDate.parse("2024-08-15"), 5.00f, 5.05f);
        Yield yield3 = new Yield(LocalDate.parse("2024-11-13"), 6.00f, 6.05f);
        Yield yield4 = new Yield(LocalDate.parse("2025-02-11"), 7.20f, 7.25f);
        Yield yield5 = new Yield(LocalDate.parse("2025-05-12"), 7.60f, 7.65f);
        Yield yield6 = new Yield(LocalDate.parse("2025-08-10"), 8.10f, 8.15f);
        Yield yield7 = new Yield(LocalDate.parse("2025-11-08"), 9.00f, 9.05f);
        Yield yield8 = new Yield(LocalDate.parse("2026-02-06"), 10.00f, 10.05f);
        Yield yield9 = new Yield(LocalDate.parse("2026-05-07"), 11.30f, 11.35f);

        Curve curve = new Curve();

        curve.putDateYield(yield1);
        curve.putDateYield(yield3);
        curve.putDateYield(yield2);
        curve.putDateYield(yield4);
        curve.putDateYield(yield5);
        curve.putDateYield(yield6);
        curve.putDateYield(yield7);
        curve.putDateYield(yield8);
        curve.putDateYield(yield9);

        curve.addDayCount();


        LocalDate queryDate = LocalDate.parse("2035-08-11");

        System.out.println(curve.getRate(queryDate, Rate.MID));
    }
}
