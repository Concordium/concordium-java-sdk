package com.concordium.sdk.responses.nodeinfov2;

public enum BakerConsensusStatus {

    NOT_IN_COMMITTEE,

    ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE,

    ADDED_BUT_WRONG_KEYS,
    ACTIVE_BAKER,
    ACTIVE_FINALIZER
}
