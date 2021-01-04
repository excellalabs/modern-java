package com.excella.modernjava.shop;

import static com.excella.modernjava.shop.FakeLongRunningOp.delay;
import static com.excella.modernjava.shop.Util.format;

public class Discount {
    /// ## 16.4 to simulate discount codes & applying

    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " price is " + Discount.apply(quote.getPrice(), quote.getDiscountCode());
    }

    private static double apply(double price, Code code) {
        delay();
        return format(price * (100 - code.percentage) / 100);
    }
}
