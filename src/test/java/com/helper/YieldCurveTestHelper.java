package com.helper;

import com.yield_curve.Curve;
import com.yield_curve.Yield;

import java.time.LocalDate;

public class YieldCurveTestHelper {
    public static Curve exampleCurve() {
//        Instantiate Yield objects
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

//        Add Yields to Curve in incorrect order
        curve.putYield(yield8);
        curve.putYield(yield3);
        curve.putYield(yield9);
        curve.putYield(yield4);
        curve.putYield(yield2);
        curve.putYield(yield5);
        curve.putYield(yield1);
        curve.putYield(yield7);
        curve.putYield(yield6);

//        Add day counts to Yield objects within Curve
        curve.addDayCount();

        return curve;
    }
}
