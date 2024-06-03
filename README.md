# RMB Algorithmic Trading Quant Developer Technical Assessment

<p align="center">
  <img src="assets/new_logo.jpg" alt="RMB"/>
</p>

> **Applicant**: Ross Nayler

*This document serves to explain the solution approach, efficiency mechanisms and data structures used in the completion of this assignment.*

## Part I (South African Bond Calculator)

> **Submission date**: 2024/05/30

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

### Solution Approach and Efficiency Mechanisms:

> A typical object-oriented approach was taken to develop the bond calculator.

The bond calculator itself is represented by a class (`BondCalculator`) which takes three parameters when it is initialised; the parameters are: a bond object  containing all of the economics for a given bond, a settlement date, and a yield. The bond object is of `Bond` type (explained below), the settlement date is a `java.time.LocalDate` object and the yield is a `float`.

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

### Data Structure Overview:

This section will briefly outline the more important data structures used in the bond calculator.

`Bond`:
 
    bondName: string
    maturityDate: LocalDate
    couponRate: float 
    couponDate1: java.time.MonthDay
    couponDate2: java.time.MonthDay
    booksClosedDate1: java.time.MonthDay
    booksClosedDate2: java.time.MonthDay

`BondCalculator`:

    bond: Bond
    maturityDate: LocalDate
    settlementDate: java.time.LocalDate 
    yield: double
    lastAndNextCouponDates: HashMap<String, LocalDate>
    booksClosedDate: java.time.LocalDate


### Testing:

> Testing was used thoroughly throughout the development of the bond calculator as test-driven-development was the development strategy of choice.

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

## Part II (Yield Curve)

> **Submission date**: 2024/06/03

A custom data structure was developed to represent a yield curve in Part II of this assignment. The Java files corresponding to Part II of this assignment are highlighted in the file diagram shown below.

### Java File Structure:

```
src
└───main
│   └───java
|       └───com
|           └───curve_utils
|           |   Utils.java
|           └───yield_curve
|           |   Curve.java
|           |   Rate.java
|           |   Yield.java
|           └───...
└───test
    └───java
        └───com
            └───yield_curve
            |   YieldCurveTestHelper.java
            └───helper
            |   YieldCurveTestHelper.java
            └───...

```

### Solution Approach, Efficiency Mechanisms and Data Structure Overview:

The implementation of the yield curve is dependent on three custom Java classes: 1) `Yield`, a custom class that represents bond instrument yields, 2) `Rate`, an Enum used to instantiate Bid, Mid, or Ask rates and 3) `Curve`, a custom class that represents the yield curve itself.

#### `Yield`:

`Yield` is designed to represent the bid and ask rates of a bond object at a specific time, and is therefore parameterised with a maturity date (`java.time.LocalDate`), as well as bid and ask rates (`float`). The class contains various getter methods for all of the mentioned parameters, as well as a `getMidRate` method which returns the mid rate of an object defined by $\frac{\text{bid rate} + \text{ask rate}}{2}$. The `Yield` class also contians a setter method called `setNumDays` which allows for the creation of the day index for the yield object. `setNumDays` takes another date (in this case it is the most recent date in the yield curve) as a paramater and sets the day index as the number of calendar days between the instantiated maturity date and the inputted date.

#### `Curve`:

The yield curve itself is represented by the `Curve` class. `Curve` is a custom data structure that extends Java's native Tree Map implementation (`java.util.TreeMap`). `Curve` does not take any parameters, and it is indexed by bond maturity date (`java.time.LocalDate`). The custom methods that `Curve` implements are: 1) `putYield` which simply adds a `Yield` object to the curve without having to specify a key; 2) `addDayCount` which enriches all of the `Yield` objects within the curve with a day count by recursively calling the `setNumDays` method on each `Yield`; and 3) `getRate` which accepts a date and a rate type as parameters and returns the rate as specified in the assignment brief. The `getRate()` method will be further explained later in this document.

As mentioned above, `Curve` extends Java's native `TreeMap` implementation which itself is an implementation of a self-balancing binary search tree (BST). Put simply, a BST is a hierarchical data structure used for storing data in a sorted manner where every node in the left subtree is less than the parent node, and every node in the right subtree is greater than the parent node. A simple example of how a BST stores data is shown below.

<p align="center">
  <img src="assets/BST.png" alt="self-balancing binary search tree"/>
</p>

The insertion and retrieval complexity of a `TreeMap` is $O(\log{n})$, which is not as efficient as other map implementations such as `HashMap` which has $O(1)$ retrieval and insertion complexity. The `TreeMap` does, however, have two characteristics which are extremely useful in the creation of a yield curve; it is 1) navigable and 2) sorted. `TreeMap` implements the `NavigableMap` interface which offers extremely useful methods such as `lower(e)` and `higher(e)` that are essential to the `Curve.getRate()` method (`lower(e)` and `higher(e)` return the greatest element strictly lower than query parameter `e` and *visa-versa*, and are also $O(\log{n})$ ). `TreeMap` also implements the `SortedMap` interface which ensures that entries are always stored in the correct order **no matter** the order of insertion (the keys, or maturity dates in this case, are used for ordering). This characteristic allows for iteration through the `TreeMap` to follow the same order of the keys much faster than a `HashMap` (which will typically have a sorting complexity of $O(n \log{n})$ ). It is also very important to mention that both `TreeMap` and (through inheritance) `Curve` are not thread-safe, but have been selected given the aforementioned benefits that are optimal given the nature of this assignment. If this solution was to be implemented in a concurrent environment, it is recommended that a `ConcurrentSkipListMap` be used because it is more efficient in a concurrent environment.

#### `getRate()`:

Now that the `Curve` data structure has been discussed, the methodology of the `getRate()` method can be explained. As specified in the assignment brief, the `getRate()` method should return either the bid, ask, or mid rate when provided with a date; the `getRate()` method therefore takes a query date and a rate type as parameters. A basic outline of the methodology of the `getRate()` method is as follows:

- If the query date is earlier than the earliest maturity date that the `Curve` was instantiated with, then an `IllegalArgumentException` is thrown.
- If the query date is later than the last maturity date that the `Curve` was instantiated with, then the required rate is flat-extrapolated from the latest avaliable rate.
- If the query date matches one of the maturity dates that the `Curve` was instantiated with, then the required rate is simply retrieved and returned as a `double`.
- If the query date does not match, but is within the range of maturity dates that the `Curve` was instantiated with, then the required rate is linearly interpolated piece-wise and returned as a double. In this case, rates are interpolated with: $$Yield_{n, r}=Yield_{n-1,r} \cdot \frac{(Yield_{n+1,r}-Yield_{n-1,r})(Days_{n}-Days_{n-1})}{(Days_{n+1}-Days_{n-1})}$$ where $Yield_{n, r}$ is the bond yield at query date $n$ with rate type $r$, and $Days_{n}$ is the number of days that have passed from a common base date at query date $n$. It is important to note that in this case $(n-1)$ and $(n+1)$ refer to the last and next avaliable maturity dates within the `Curve` when compared to the query date.

This methodology, along with the selected data structure allowed for the most efficient `getRate()` method with interpolation. The `getRate()` method has a worst-case time-complexity of $O(\log{n})$ .

### Testing:

Once again, test driven development was used in the creation of the `Curve` data structure. The tags received upon the passing of the unit tests are shown below to give an overview of the testing process.

- Bonds are correctly ordered in curve after out-of-order insertion
- Correct day indices are added to bonds
- Exception thrown if query date is before first date
- Flat extrapolated bid rates are returned if query date is after last date
- Flat extrapolated mid rates are returned if query date is after last date
- Flat extrapolated ask rates are returned if query date is after last date
- Exact bid rates are returned if query date is equal to a date in the dataset
- Exact mid rates are returned if query date is equal to a date in the dataset
- Exact ask rates are returned if query date is equal to a date in the dataset
- Interpolated bid rates are returned if query date is in date range
- Interpolated mid rates are returned if query date is in date range
- Interpolated mid rates are returned if query date is in date range

All tests were passed with the curve data provided in the assignment brief.