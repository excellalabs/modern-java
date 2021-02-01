package com.excella.modernjava.tempinfo;

import java.util.Random;

public class TempInfo {
    public static final Random random = new Random();

    private final String town;
    private final int temp;

    public TempInfo(String town, int temp) {
        this.town = town;
        this.temp = temp;
    }

    public static TempInfo fetch(String town) {
        // Comment if you want to see the stackoverflow referenced in Quiz 17.1
        if (random.nextInt(10) == 0)
            throw new RuntimeException("Error!");
        return new TempInfo(town, random.nextInt(100));
    }

    @Override
    public String toString() {
        return town + " : " + temp;
    }

    public int getTemp() {
        return temp;
    }

    public String getTown() {
        return town;
    }
}

/*
1 TempInfo instance for a given town is created via a static factory method.
2 Fetching the current temperature may randomly fail one time out of ten.
3 Returns a random temperature in the range 0 to 99 degrees Fahrenheit
*/
