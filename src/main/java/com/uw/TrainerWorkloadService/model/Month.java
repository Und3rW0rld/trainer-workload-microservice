package com.uw.TrainerWorkloadService.model;

import lombok.Getter;

@Getter
public enum Month {
    JANUARY(1, "January"),
    FEBRUARY(2, "February"),
    MARCH(3, "March"),
    APRIL(4, "April"),
    MAY(5, "May"),
    JUNE(6, "June"),
    JULY(7, "July"),
    AUGUST(8, "August"),
    SEPTEMBER(9, "September"),
    OCTOBER(10, "October"),
    NOVEMBER(11, "November"),
    DECEMBER(12, "December");

    private final int monthNumber;
    private final String monthName;

    Month(int monthNumber, String monthName) {
        this.monthNumber = monthNumber;
        this.monthName = monthName;
    }

    public static Month fromNumber(int monthNumber) {
        for (Month month : Month.values()) {
            if (month.getMonthNumber() == monthNumber) {
                return month;
            }
        }
        throw new IllegalArgumentException("Invalid month number: " + monthNumber);
    }

    public static Month fromName(String monthName) {
        for (Month month : Month.values()) {
            if (month.getMonthName().equalsIgnoreCase(monthName)) {
                return month;
            }
        }
        throw new IllegalArgumentException("Invalid month name: " + monthName);
    }
}