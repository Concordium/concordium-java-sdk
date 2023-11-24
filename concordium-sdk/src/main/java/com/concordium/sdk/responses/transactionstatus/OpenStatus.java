package com.concordium.sdk.responses.transactionstatus;

public enum OpenStatus {
    OPEN_FOR_ALL,
    CLOSED_FOR_NEW,
    CLOSED_FOR_ALL;

    public static OpenStatus from(com.concordium.grpc.v2.OpenStatus openStatus) {
        switch (openStatus) {
            case OPEN_STATUS_OPEN_FOR_ALL:
                return OPEN_FOR_ALL;
            case OPEN_STATUS_CLOSED_FOR_NEW:
                return CLOSED_FOR_NEW;
            case OPEN_STATUS_CLOSED_FOR_ALL:
                return CLOSED_FOR_ALL;
            default:
                throw new IllegalArgumentException("Unrecognized open status.");
        }
    }
}
