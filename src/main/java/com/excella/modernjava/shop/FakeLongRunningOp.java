package com.excella.modernjava.shop;

public class FakeLongRunningOp {
    public static void delay() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
