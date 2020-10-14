package com.excella.reactor.chap12DateTimeApi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateAndTimeAPIHarness {
    public static void run() {
        System.out.println("\n-- DATE AND TIME API --");

        // Quiz 12.2: Implementing a custom TemporalAdjuster
        LocalDate date = LocalDate.of(2020, 10, 31);
        date = date.with(new NextWorkingDay());
        System.out.println("Date used with TemporalAdjuster, NextWorkingDay: " + date);

        // Time zone operations
        runTimeZoneOperations();
    }

    public static void runTimeZoneOperations() { 
        // Zone IDs supplied by the Internet Assigned Numbers Authority (IANA) Time Zone Database (see https://www.iana.org/time-zones)
        ZoneId romeZone = ZoneId.of("Europe/Rome");
        ZoneId estZone = ZoneId.of("US/Eastern");
        
        // Add time and time zone info to date
        LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
        ZonedDateTime zdt1 = date.atStartOfDay(romeZone); 
        
        // Add time zone info to date
        LocalDateTime dateTimeManual = LocalDateTime.of(2014, Month.MARCH, 18, 23, 45);
        ZonedDateTime zdt2 = dateTimeManual.atZone(romeZone); // Time is in this time zone 
        
        // Changes actual time
        Instant instant = Instant.now();
        ZonedDateTime zdt3 = zdt2.withZoneSameInstant(estZone); 

        System.out.println("\nDate without zone info added: " + dateTimeManual);
        System.out.println("Zoned date 1, start of day: " + zdt1);
        System.out.println("Zoned date 2: " + zdt2 + " Date: " + zdt2.toOffsetDateTime());
        System.out.println("Zoned date 3: " + zdt3 + ", EST: " + instant.atZone(estZone));

        // Using Instant
        LocalDateTime nowDt = LocalDateTime.now();
        Instant instantNow = Instant.now();
        LocalDateTime timeFromInstant = LocalDateTime.ofInstant(instantNow, romeZone);
        LocalDateTime timeFromInstantEst = LocalDateTime.ofInstant(instantNow, estZone);
        
        System.out.println("-- Instant -- \nNow: " + nowDt + ", Instant now: " + instantNow + " with zone (Europe/Rome) added: " + timeFromInstant);
        System.out.println("In EST: " + timeFromInstantEst.getHour() + ":" + timeFromInstant.getMinute() + ", in Europe/Rome: " + timeFromInstant.getHour() + ":" + timeFromInstant.getMinute());

        ////  12.3.2. Fixed offset from UTC/Greenwich
        ZoneOffset newYorkOffset = ZoneOffset.of("-05:00");
        LocalDateTime dateTimeForOffset = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
        OffsetDateTime dateTimeInNewYork = OffsetDateTime.of(dateTimeForOffset, newYorkOffset);
        System.out.println("\n Date: " + dateTimeForOffset + " in NY offset: " + dateTimeInNewYork);
    }

}
