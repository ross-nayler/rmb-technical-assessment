package com.bond_calculator;

import com.bond.Bond;
import com.helper.BondCalculatorTestHelper;
import com.bond_utils.MathUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class R186ExInterestTests {

    Bond bond = BondCalculatorTestHelper.R186Bond();
    LocalDate settlementDate = LocalDate.parse("2026-12-12");
    double yield = 8.75d;
    BondCalculator bondCalculator = new BondCalculator(bond, settlementDate, yield);

    @Test
    @DisplayName("LCD and NCD are calculated correctly")
    void setCouponDates() {
        assertEquals(LocalDate.of(2026, 6, 21), bondCalculator.getLastCouponDate());
        assertEquals(LocalDate.of(2026, 12, 21), bondCalculator.getNextCouponDate());
    }

    @Test
    @DisplayName("BCD is calculated correctly")
    void setBooksClosedDate() {
        assertEquals(LocalDate.of(2026, 12, 11), bondCalculator.getBooksClosedDate());
    }

    @Test
    @DisplayName("N is calculated correctly")
    void getRemainingCoupons() {
        assertEquals(0d, bondCalculator.getRemainingCoupons());
    }

    @Test
    @DisplayName("CUMEX is calculated correctly")
    void getCumEx() {
        assertFalse(bondCalculator.isCumInterest());
    }

    @Test
    @DisplayName("DAYSACC is calculated correctly")
    void getDaysAcc() {
        assertEquals(-9, bondCalculator.getDaysAcc());
    }

    @Test
    @DisplayName("F is calculated correctly")
    void getDiscountFactor() {
        assertEquals(0.958083832, MathUtils.round(bondCalculator.getDiscountFactor(), 9));
    }

    @Test
    @DisplayName("BP is calculated correctly")
    void getBrokenPeriod() {
        assertEquals(0.049315068, MathUtils.round(bondCalculator.getBrokenPeriod(), 9));
    }

    @Test
    @DisplayName("BPF is calculated correctly")
    void getBrokenPeriodDiscountFactor() {
        assertEquals(0.997847111, MathUtils.round(bondCalculator.getBrokenPeriodDiscountFactor(), 9));
    }

    @Test
    @DisplayName("Accrued Interest (ACCRINT) is calculated correctly")
    void getAccruedInterest() {
        assertEquals(-0.25890, bondCalculator.getAccruedInterest(true));
    }

    @Test
    @DisplayName("All-in Price (AIP) is calculated correctly")
    void getAllInPrice() {
        assertEquals(99.78472, bondCalculator.getAllInPrice(true));
    }

    @Test
    @DisplayName("Clean Price (CP) is calculated correctly")
    void getCleanPrice() {
        assertEquals(100.04362, bondCalculator.getCleanPrice(true));
    }
}
