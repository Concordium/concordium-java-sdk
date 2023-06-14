package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Status {
    /**
     * Block item is received, but not yet in any blocks.
     */
    @JsonProperty("received")
    RECEIVED,

    /**
     * Block item is finalized in the given block, with the given summary.
     */
    @JsonProperty("finalized")
    FINALIZED,

    /**
     * Block item is committed to one or more blocks. The outcomes are listed for each block.
     * Note that in the vast majority of cases the outcome of a transaction should not be dependent on the block it is in,
     * but this can in principle happen.
     */
    @JsonProperty("committed")
    COMMITTED,
    @JsonProperty("absent")
    ABSENT
}
