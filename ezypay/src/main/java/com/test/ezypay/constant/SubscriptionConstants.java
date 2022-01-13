package com.test.ezypay.constant;

public class SubscriptionConstants {

    public static int DATE_MAX_RANGE = 90;

    public enum SubscriptionType {
        DAILY,
        MONTHLY,
        WEEKLY
    }

    public static boolean isSubscriptionTypeValid(String subscriptionType) {
        boolean isFound = false;

        for (SubscriptionType type : SubscriptionType.values()) {
            if(type.name().equals(subscriptionType)) {
                isFound = true;
                break;
            }
        }

        return isFound;
    }
}
