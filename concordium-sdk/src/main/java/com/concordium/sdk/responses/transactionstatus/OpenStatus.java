package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OpenStatus {
    @JsonProperty("openForAll")
    OPEN_FOR_ALL,
    @JsonProperty("closedForNew")
    CLOSED_FOR_NEW,
    @JsonProperty("closedForAll")
    CLOSED_FOR_ALL;

    /**
     * Parses {@link com.concordium.grpc.v2.OpenStatus} to {@link OpenStatus}.
     * @param openStatus {@link com.concordium.grpc.v2.OpenStatus} returned by the GRPC V2 API.
     * @return parsed {@link OpenStatus}.
     */
    public static OpenStatus parse(com.concordium.grpc.v2.OpenStatus openStatus) {
        switch (openStatus) {
            case OPEN_STATUS_OPEN_FOR_ALL: return OPEN_FOR_ALL;
            case OPEN_STATUS_CLOSED_FOR_NEW: return CLOSED_FOR_NEW;
            case OPEN_STATUS_CLOSED_FOR_ALL: return CLOSED_FOR_ALL;
            default: throw new IllegalArgumentException("Does not match any OpenStatus");
        }
    }
}
