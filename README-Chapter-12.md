# Session 18

_[Recording (10/13/20)](https://excella.zoom.us/rec/share/R81OubxX9nPwwAxs-zIRMNMwgrHA9IfCh4Mhc6T2_mVAEKfHHK3FfKwF0_FasAs2.ghdjhWE2DMV-PKNT)_

## Agenda

- **Housekeeping**: notes & code, expensing food, Exchange next week, start recording
- **Recap**
    - Finished chapter 11 - optionals
- **Today:** 
    - Chapter 12 - Date and Time API, start chapter 13 - default methods
- **Next time:** Finish 13, start 14 - Java Module System

# Chapter 12. New Date and Time API

_Covers_

* Why we needed a new date and time library, introduced in Java 8
* Representing date and time for both humans and machines
* Defining an amount of time
* Manipulating, formatting, and parsing dates
* Dealing with different time zones and calendars

## Issue Summary

Date and time support in Java was far from ideal, so Java 8 introduced new `Date and Time API` to tackle the issue.

Issues: Not intuitive or usable. A history...

- used to only be the java.util.Date class. Represents a point in time with millisecond precision instead of a date
- choice of offsets were nebulous; years start from 1900, months start at index 0
- If you wanted to represent the release date of Java 9, which is 21 September 2017, you’d have to create an instance of Date as follows:

    `Date date = new Date(117, 8, 21);`

    Printing this:

    `Thu Sep 21 00:00:00 CET 2017`

- Problems were clear in Java 1.0 when it came out, but wasn't fixed for backwards compatibility
- replaced by the alternative java.util.Calendar class. Unfortunately, Calendar has similar problems and design flaws that lead to error-prone code
- the presence of both the Date and Calendar classes increases confusion
- DateFormat feature has problems like not thread-safe
- Both Date and Calendar are mutable classes
- These issues have encouraged 3rd party libraries such as Joda-Time and Oracle's enhancements in the java.time package
- So, Java 8 integrated many of the Joda-Time features into the java.time package

## Date and Time API Overview

 - basic use cases such as creating dates and times that are suitable to be used by both humans and machines. 
 - more advanced applications of the new Date and Time API, such as manipulating, parsing, and printing date-time objects, and working with different time zones and alternative calendars

## 12.1 LOCALDATE, LOCALTIME, LOCALDATETIME, INSTANT, DURATION, AND PERIOD

Create simple dates and intervals, the `java.time` package includes many new classes to help: `LocalDate, LocalTime, LocalDateTime, Instant, Duration, and Period`

### 12.1.1. Working with LocalDate and LocalTime

_`LocalDate`_

- Instance of class is immutable object representing a plain date without time of day; doesn't carry info about time zone
- Create using static factory method
- All the other date-time classes that we investigate in the remaining part of this chapter provide a similar factory method
- Includes many methods to read its most commonly used values

```
LocalDate date = LocalDate.of(2017, 9, 21);   1
int year = date.getYear();                    2
Month month = date.getMonth();                3
int day = date.getDayOfMonth();               4
DayOfWeek dow = date.getDayOfWeek();          5
int len = date.lengthOfMonth();               6
boolean leap = date.isLeapYear(); 
```

- 1 2017-09-21
- 2 2017
- 3 SEPTEMBER
- 4 21
- 5 THURSDAY
- 6 30 (days in September)
- 7 false (not a leap year)

It’s also possible to obtain the current date from the system clock by using the now factory method:

```
LocalDate today = LocalDate.now();
```

_`LocalTime`_

Similarly:

```
LocalTime time = LocalTime.of(13, 45, 20);     1
int hour = time.getHour();                     2
int minute = time.getMinute();                 3
int second = time.getSecond();                 4
```

- 1 13:45:20
- 2 13
- 3 45
- 4 20

You can create them also by parsing a string:

```
LocalDate date = LocalDate.parse("2017-09-21");
LocalTime time = LocalTime.parse("13:45:20");
```

### 12.1.2. Combining a date and a time

- The composite class called LocalDateTime pairs a LocalDate and a LocalTime. 
- It represents both a date and a time without a time zone and can be created directly or by combining a date and time, as shown in the following listing

```
// 2017-09-21T13:45:20
LocalDateTime dt1 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 45, 20);
LocalDateTime dt2 = LocalDateTime.of(date, time);
LocalDateTime dt3 = date.atTime(13, 45, 20);
LocalDateTime dt4 = date.atTime(time);
LocalDateTime dt5 = time.atDate(date);

LocalDate date1 = dt1.toLocalDate();       
LocalTime time1 = dt1.toLocalTime();       
```

### 12.1.3. Instant: a date and time for machines

- human representation of date/time isn't easy for machines
- natural model for time is a single large number on a point on a continuous timeline
- this is used in java.time.Instant class
- represents the number of seconds passed since the Unix epoch time, set by convention to midnight of January 1, 1970 UTC
- create an instance of this class by passing the number of seconds to its ofEpochSecond static factory method
- nanosecond precision
- use when working with Duration or Precision classes...

### 12.1.4. Defining a Duration or a Period

- All the classes you’ve seen so far implement the Temporal interface, which defines how to read and manipulate the values of an object modeling a generic point in time. 

You can create a duration between two LocalTimes, two LocalDateTimes, or two Instants as follows:

```
Duration d1 = Duration.between(time1, time2);
Duration d1 = Duration.between(dateTime1, dateTime2);
Duration d2 = Duration.between(instant1, instant2);
```

Find out the difference between two LocalDates with the between factory method of that class:

```
Period tenDays = Period.between(LocalDate.of(2017, 9, 11),
                                LocalDate.of(2017, 9, 21));
```

They have other convenient factory methods:

```
Duration threeMinutes = Duration.ofMinutes(3);
Duration threeMinutes = Duration.of(3, ChronoUnit.MINUTES);
Period tenDays = Period.ofDays(10);
Period threeWeeks = Period.ofWeeks(3);
Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
```

- See _Table 12.1. The common methods of date-time classes representing an interval_

## 12.2. MANIPULATING, PARSING, AND FORMATTING DATES

The withAttributes methods are the easiest way to change dates (which return new objects!)

### Manipulating the attributes of a LocalDate in a relative way

```
LocalDate date1 = LocalDate.of(2017, 9, 21);             
LocalDate date1b = date1.withYear(2011);
LocalDate date2 = date1.plusWeeks(1);                    
LocalDate date3 = date2.minusYears(6);                   
LocalDate date4 = date3.plus(6, ChronoUnit.MONTHS); 
```

The last uses the more generic method taking a TemporalField

### 12.2.1. Working with TemporalAdjusters

- Use for advanced operations, such as adjusting a date to the next Sunday, the next working day, or the last day of the month
- Date and Time API already provides many predefined TemporalAdjusters via static factory methods for the most common use cases:

```
import static java.time.temporal.TemporalAdjusters.*;
LocalDate date1 = LocalDate.of(2014, 3, 18);                  1
LocalDate date2 = date1.with(nextOrSame(DayOfWeek.SUNDAY));   2
LocalDate date3 = date2.with(lastDayOfMonth());      
```

- see _Table 12.3 lists the TemporalAdjusters that you can create with these factory methods._
- it’s relatively simple to create your own custom TemporalAdjuster implementation if you need to:

```
@FunctionalInterface
public interface TemporalAdjuster {
    Temporal adjustInto(Temporal temporal);
}
```

- Defines how to convert a Temporal object to another Temporal. You can think of a TemporalAdjuster as being like a `UnaryOperator<Temporal>`
- _Quiz 12.2: Implementing a custom TemporalAdjuster_

### 12.2.2. Printing and parsing date-time objects

- Formatting and parsing is now done in the new java.time.format package
- Easiest way is through static factory methods and constants, i.e.:

```
LocalDate date = LocalDate.of(2014, 3, 18);
String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE);      
String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE);      
```

You can also parse a String representing a date or a time in that format to re-create the date object itself, using the factory method provided by all the classes of the Date and Time API representing a point in time or an interval:

```
LocalDate date1 = LocalDate.parse("20140318",
                                 DateTimeFormatter.BASIC_ISO_DATE);
LocalDate date2 = LocalDate.parse("2014-03-18",
                                 DateTimeFormatter.ISO_LOCAL_DATE);
```

- all the DateTimeFormatter instances are thread-safe

in case you need even more control, the DateTimeFormatterBuilder class lets you define complex formatters step by step by using meaningful methods, for example:

```
DateTimeFormatter italianFormatter = new DateTimeFormatterBuilder()
        .appendText(ChronoField.DAY_OF_MONTH)
        .appendLiteral(". ")
        .appendText(ChronoField.MONTH_OF_YEAR)
        .appendLiteral(" ")
        .appendText(ChronoField.YEAR)
        .parseCaseInsensitive()
        .toFormatter(Locale.ITALIAN);
```

## 12.3. WORKING WITH DIFFERENT TIME ZONES AND CALENDARS

- Dealing with time zones is vastly simplified by the new Date and Time API
- The new java.time.ZoneId class is the replacement for the old java.util.TimeZone class

### 12.3.1. Using time zones

About 40 time zones are held in instances of the ZoneRules class. You can call getRules() on a ZoneId to obtain the rules for that time zone.

```
// supplied by the Internet Assigned Numbers Authority (IANA) Time Zone Database (see https://www.iana.org/time-zones)
ZoneId romeZone = ZoneId.of("Europe/Rome");

LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
// Transform into Zoned
ZonedDateTime zdt1 = date.atStartOfDay(romeZone);
LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
ZonedDateTime zdt2 = dateTime.atZone(romeZone);
Instant instant = Instant.now();
ZonedDateTime zdt3 = instant.atZone(romeZone);
```

_Try it out in [DateAndTimeAPIHarness.java](src/main/java/com/excella/reactor/chap12DateTimeApi/DateAndTimeAPIHarness.java)_

### 12.3.2. Fixed offset from UTC/Greenwich

Another common way to express a time zone is to use a fixed offset from UTC/Greenwich.

```
ZoneOffset newYorkOffset = ZoneOffset.of("-05:00"); // EST
```

A ZoneOffset defined this way doesn’t have any Daylight Saving Time management, and for this reason, it isn’t suggested in the majority of cases. 

### 12.3.3. Using alternative calendar systems

Advanced feature

# Session 19

_[Recording (10/21/20)](https://excella.zoom.us/rec/share/J8BM-Y1W0cKHRedv19pDSVA-sKRzcGpQprpORRwjqB-2SsmTXFTESs554VbTrgK0.hC6O0FNsKgdq29av)_

## Agenda

- **Housekeeping**: notes & code, expensing food, Exchange next week, start recording
- **Recap**
    - Did chapter 12 - New Date and Time API 
- **Today:** 
    - Summary of 12, Chapter 12 - Date and Time API, start chapter 13 - default methods
- **Next time:** Finish 13, start 14 - Java Module System

### SUMMARY

