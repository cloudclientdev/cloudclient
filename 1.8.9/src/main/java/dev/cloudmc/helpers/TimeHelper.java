/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeHelper {

    /**
     * Returns the current Time in minutes
     */

    public static String getFormattedTimeMinute() {
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.now();
        return dtfTime.format(localTime);
    }

    /**
     * Returns the current Time in minutes and seconds
     */

    public static String getFormattedTimeSecond() {
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.now();
        return dtfTime.format(localTime);
    }

    /**
     * Returns the current Date in years, months and days
     */

    public static String getFormattedDate() {
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();
        return dtfDate.format(localDate);
    }
}
