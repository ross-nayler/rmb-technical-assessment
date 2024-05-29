# RMB Algorithmic Trading Quant Developer Technical Assessment

**Applicant**: Ross Nayler

*This document serves to explain the solution approach, efficiency mechanisms and data structures used in the completion of this assignment.*

## Part I (South African Bond Calculator)

An operational and accurate price calculator for South African bonds was developed in Java for Part I of this assignment. The Java files corresponding to Part I are highlighted in the file diagram below.

### Java File Structure:

```
src
└───main
│   └───java
|       └───com
|           └───bond
|           |   Bond.java
|           └───bond_calculator
|           |   BondCalculator.java
|           └───utils
|           |   DateUtils.java
|           |   MathUtils.java
|           └───...
└───test
    └───java
        └───com
            └───bond_calculator
            |   R186Tests.java
            |   R186ExInterestTests.java
            |   R186MMTests.java
            |   R2032Tests.java
            └───helper
            |   TestHelper.java
            └───...

```

### Solution Approach, Data Structures and Efficiency Mechanisms:

A typical object-oriented approach was taken to develop the bond calculator. The bond calculator itself is represented by a class (`BondCalculator`) which takes three parameters when it is initialised; the parameters are: a bond object  containing all of the economics for a given bond, a settlement date, and a yield. The bond object is of `Bond` type (explained below), the settlement date is a `java.time.LocalDate` object and the yield is a `float`.

The `BondCalculator` performs price calculations via a variety of methods. For this assignment, the three most important methods are `getAccruedInterest`, `getAllInPrice` and `getCleanPrice`. There are also various other methods that involve the calculation of intermediate values used for validation and testing purposes.

As mentioned, the `Bond` object contains all of the economics for a given bond issuance, and is parametised as such. The parameters required to initialise a `Bond` object are the bond maturity date ($M_B$), coupon rate ($C_B$), coupon dates ($CpnDates$) and books closed dates ($BcDates$). All date parameters are required to be `java.time.LocalDate` or `java.time.MonthDay` objects and the coupon rate is required to be a `float`.

Most of the arithmetic calculations used within the `BondCalculator` are found within utility classes `DateUtils` and `MathUtils`. Both of these classes contain static methods used to perform a variety of operations and intermediate calculations used by the bond calculator.

An efficiency mechanism that is used that is worth noting is the way that the last coupon date ($LCD$) and next coupon date ($NCD$) are calculated. Due to the fact that coupon dates are specified in `MonthDay` format, there are $n=6$ possibilities for both the $LCD$ and $NCD$ values. These are:
- $CpnDate_1@\text{Year}_{prev}$
- $CpnDate_1@\text{Year}_{curr}$
- $CpnDate_1@\text{Year}_{next}$
- $CpnDate_2@\text{Year}_{prev}$
- $CpnDate_2@\text{Year}_{curr}$
- $CpnDate_2@\text{Year}_{next}$

Finding the $LCD$ and $NCD$ values is an iterative process with $O(n)$ therefore reducing the number of possibilities would increase the efficiency of the process. In order to do this, a check was put in place to see if the settlement date was before or after 1 July of the specified year. If the settlement date was before 1 July, only the previous year and current year would be appended to the coupon dates. If it was after 1 July, only the current year and the next year would be appended to the coupon dates. Both are calculated within a single pass and are returned in a `HashMap`.

### Testing:

*Testing was used thoroughly throughout the development of the bond calculator as test-driven-development was the development strategy of choice.*

As specified in the assignment requirements document, the R186 and R2032 bonds were used to test the accuracy of the calculator. The correct values for the accrued interest (`ACCRINT`), all-in price (`AIP`) and clean price (`CP`) were obtained from the [JSE Bond Calculator](https://bondcalculator.jse.co.za/BondSingle.aspx?calc=Spot) with the given bonds, settlement dates and yields.

A standard testing regime was developed to align with the selected test-driven-development strategy. The following are the tags for the testing processes that describe each test:

- LCD and NCD are calculated correctly
- BCD is calculated correctly
- N is calculated correctly
- CUMEX is calculated correctly
- DAYSACC is calculated correctly
- F is calculated correctly
- BP is calculated correctly
- BPF is calculated correctly
- Accrued Interest (ACCRINT) is calculated correctly
- All-in Price (AIP) is calculated correctly
- Clean Price (CP) is calculated correctly

This testing strategy allowed for more efficient development as all intermediate values were also validated in the process. This made the debugging process much faster.

Unit tests were executed for the R186 and R2032 bonds under the given conditions. All test were passed and results were as expected. Further tests were executed on the R186 bond in order to test features of the calculator that were not tested under the conditions given in the assignment. The first additional test was to ensure that when the bond has less than 6 months to maturity it is priced as a money market instrument, therefore a settlement date within 6 months of the maturity date was selected and the test was executed again. All tests were passed and results were as expected. The second additional test was executed to test the scenario when the settlement date is after the books-closed date *i.e., when a bond is ex interest*. Once again, all tests were passed and results were as expected.