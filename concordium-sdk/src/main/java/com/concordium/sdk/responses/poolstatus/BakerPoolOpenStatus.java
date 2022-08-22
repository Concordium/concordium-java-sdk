package com.concordium.sdk.responses.poolstatus;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BakerPoolOpenStatus {
    OPEN_FOR_ALL,
    CLOSED_FOR_NEW,
    CLOSED_FOR_ALL;

    @JsonCreator
    public static BakerPoolOpenStatus from(String value) {
        switch (value) {
            case "openForAll":
                return BakerPoolOpenStatus.OPEN_FOR_ALL;
            case "closedForNew":
                return BakerPoolOpenStatus.CLOSED_FOR_NEW;
            case "closedForAll":
                return BakerPoolOpenStatus.CLOSED_FOR_ALL;
            default:
                throw new IllegalArgumentException("Invalid Baker Pool Open Status " + value);
        }
    }
}
