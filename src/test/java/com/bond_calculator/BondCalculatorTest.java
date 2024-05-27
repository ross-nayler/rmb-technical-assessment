package com.bond_calculator;

import com.bond.Bond;
import com.helper.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BondCalculatorTest {

    @Test
    @DisplayName("LCD and NCD are calculated correctly")
    void setCouponDates() {
//        Instantiate bond object
        Bond bond = TestHelper.R186Bond();

//        Instantiate bond calculator object
        LocalDate settlementDate = LocalDate.parse("2017-02-07");
        Float yield = 8.75f;
        BondCalculator bondCalculator = new BondCalculator(bond, settlementDate, yield);

        assertEquals(bondCalculator.getLastCouponDate(), LocalDate.of(2016, 12, 21));
        assertEquals(bondCalculator.getNextCouponDate(), LocalDate.of(2017, 6, 21));
    }

    @Test
    @DisplayName("BCD is calculated correctly")
    void setBooksClosedDate() {
//        Instantiate bond object
        Bond bond = TestHelper.R186Bond();
//        Instantiate bond calculator object
        LocalDate settlementDate = LocalDate.parse("2017-02-07");
        Float yield = 8.75f;
        BondCalculator bondCalculator = new BondCalculator(bond, settlementDate, yield);

        assertEquals(LocalDate.of(2017, 6, 11), bondCalculator.getBooksClosedDate());
    }

    @Test
    @DisplayName("N remaining coupons are calculated correctly")
    void getRemainingCoupons() {
//        Instantiate bond object
        Bond bond = TestHelper.R186Bond();
//        Instantiate bond calculator object
        LocalDate settlementDate = LocalDate.parse("2017-02-07");
        Float yield = 8.75f;
        BondCalculator bondCalculator = new BondCalculator(bond, settlementDate, yield);

        assertEquals(19d, bondCalculator.getRemainingCoupons());
    }
}