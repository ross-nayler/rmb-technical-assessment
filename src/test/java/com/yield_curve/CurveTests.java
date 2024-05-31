package com.yield_curve;

import com.helper.YieldCurveTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CurveTests {
    Curve curve = YieldCurveTestHelper.exampleCurve();

    @Test
    @DisplayName("Bonds are correctly ordered in curve")
    void addRates() {
        LocalDate[] expectedOrderedDates = new LocalDate[]{LocalDate.parse("2024-05-17"), LocalDate.parse("2024-08-15"), LocalDate.parse("2024-11-13"), LocalDate.parse("2025-02-11"), LocalDate.parse("2025-05-12"), LocalDate.parse("2025-08-10"), LocalDate.parse("2025-11-08"), LocalDate.parse("2026-02-06"), LocalDate.parse("2026-05-07")};
        Object[] orderedDates = curve.keySet().toArray();

//        Check order matches
        for (int i = 0; i < expectedOrderedDates.length; i++) {
            assertEquals(expectedOrderedDates[i], orderedDates[i]);
        }
    }

    @Test
    @DisplayName("Correct day indices are added to bonds")
    void addNumDaysToRates() {
        assertEquals(0, curve.firstEntry().getValue().getNumDays());
        assertEquals(720, curve.lastEntry().getValue().getNumDays());
    }

    @Test
    @DisplayName("Exception thrown if query date is before first date")
    void getRateEarlyQueryDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> curve.getRate(LocalDate.parse("2024-01-01"), Rate.BID));
        assertEquals("Query date is before the first date on record.", exception.getMessage());
    }

    @Test
    @DisplayName("Flat extrapolated bid rates are returned if query date is after last date")
    void getBidRateLateQueryDate() {
        double rate = curve.getRate(LocalDate.parse("2029-08-18"), Rate.BID);
        assertEquals(11.30, rate);
    }

    @Test
    @DisplayName("Flat extrapolated mid rates are returned if query date is after last date")
    void getMidRateLateQueryDate() {
        double rate = curve.getRate(LocalDate.parse("2029-08-18"), Rate.MID);
        assertEquals(11.325, rate);
    }

    @Test
    @DisplayName("Flat extrapolated ask rates are returned if query date is after last date")
    void getAskRateLateQueryDate() {
        double rate = curve.getRate(LocalDate.parse("2029-08-18"), Rate.ASK);
        assertEquals(11.35, rate);
    }

    @Test
    @DisplayName("Exact bid rates are returned if query date is equal to a date in the dataset")
    void getBidRateMatchingQueryDate() {
        double rate = curve.getRate(LocalDate.parse("2025-05-12"), Rate.BID);
        assertEquals(7.60, rate);
    }

    @Test
    @DisplayName("Exact mid rates are returned if query date is equal to a date in the dataset")
    void getMidRateMatchingQueryDate() {
        double rate = curve.getRate(LocalDate.parse("2025-05-12"), Rate.MID);
        assertEquals(7.625, rate);
    }

    @Test
    @DisplayName("Exact ask rates are returned if query date is equal to a date in the dataset")
    void getAskRateMatchingQueryDate() {
        double rate = curve.getRate(LocalDate.parse("2025-05-12"), Rate.ASK);
        assertEquals(7.65, rate);
    }

    @Test
    @DisplayName("Interpolated bid rates are returned if query date is in date range")
    void getInterpolatedBidRate() {
        double rate = curve.getRate(LocalDate.parse("2025-04-20"), Rate.BID);
        assertEquals(7.50222, rate);
    }

    @Test
    @DisplayName("Interpolated mid rates are returned if query date is in date range")
    void getInterpolatedMidRate() {
        double rate = curve.getRate(LocalDate.parse("2025-04-20"), Rate.MID);
        assertEquals(7.52722, rate);
    }

    @Test
    @DisplayName("Interpolated mid rates are returned if query date is in date range")
    void getInterpolatedAskRate() {
        double rate = curve.getRate(LocalDate.parse("2025-04-20"), Rate.ASK);
        assertEquals(7.55222, rate);
    }
}
