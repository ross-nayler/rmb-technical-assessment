package com.bond_calculator;

import com.bond.Bond;
import com.helper.BondCalculatorTestHelper;
import com.bond_utils.MathUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class R2032Tests {

    Bond bond = BondCalculatorTestHelper.R2032Bond();
    LocalDate settlementDate = LocalDate.parse("2024-05-16");
    double yield = 9.5d;
    BondCalculator bondCalculator = new BondCalculator(bond, settlementDate, yield);

    @Test
    @DisplayName("LCD and NCD are calculated correctly")
    void setCouponDates() {
        assertEquals(LocalDate.of(2024, 3, 31), bondCalculator.getLastCouponDate());
        assertEquals(LocalDate.of(2024, 9, 30), bondCalculator.getNextCouponDate());
    }

    @Test
    @DisplayName("BCD is calculated correctly")
    void setBooksClosedDate() {
        assertEquals(LocalDate.of(2024, 9, 20), bondCalculator.getBooksClosedDate());
    }

    @Test
    @DisplayName("N is calculated correctly")
    void getRemainingCoupons() {
        assertEquals(15d, bondCalculator.getRemainingCoupons());
    }

    @Test
    @DisplayName("CUMEX is calculated correctly")
    void getCumEx() {
        assertTrue(bondCalculator.isCumInterest());
    }

    @Test
    @DisplayName("DAYSACC is calculated correctly")
    void getDaysAcc() {
        assertEquals(46, bondCalculator.getDaysAcc());
    }

    @Test
    @DisplayName("F is calculated correctly")
    void getDiscountFactor() {
        assertEquals(0.954653938, MathUtils.round(bondCalculator.getDiscountFactor(), 9));
    }

    @Test
    @DisplayName("BP is calculated correctly")
    void getBrokenPeriod() {
        assertEquals(0.748633880, MathUtils.round(bondCalculator.getBrokenPeriod(), 9));
    }

    @Test
    @DisplayName("BPF is calculated correctly")
    void getBrokenPeriodDiscountFactor() {
        assertEquals(0.965855171, MathUtils.round(bondCalculator.getBrokenPeriodDiscountFactor(), 9));
    }

    @Test
    @DisplayName("Accrued Interest (ACCRINT) is calculated correctly")
    void getAccruedInterest() {
        assertEquals(1.03973, bondCalculator.getAccruedInterest(true));
    }

    @Test
    @DisplayName("All-in Price (AIP) is calculated correctly")
    void getAllInPrice() {
        assertEquals(94.19666, bondCalculator.getAllInPrice(true));
    }

    @Test
    @DisplayName("Clean Price (CP) is calculated correctly")
    void getCleanPrice() {
        assertEquals(93.15693, bondCalculator.getCleanPrice(true));
    }
}