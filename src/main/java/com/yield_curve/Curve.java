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
}
