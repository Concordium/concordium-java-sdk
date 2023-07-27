package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Status {

    /**
     * The transaction was received by the node, but not yet part of a block.
     */
    @JsonProperty("received")
    RECEIVED,

    /**
     * The transaction was committed into a block, i.e. a baker
     * has baked a block and included the transaction. Note that the
     * transaction is not yet part of the authoritative chain (i.e. finalized)
     */
    @JsonProperty("committed")
    COMMITTED,

    /**
     * The transaction is in a block which is finalized, i.e.
     * part of the authoritative chain.
     */
    @JsonProperty("finalized")
    FINALIZED,

    /**
     * The node does not know about the transaction.
     */
    @JsonProperty("absent")
    ABSENT
}
