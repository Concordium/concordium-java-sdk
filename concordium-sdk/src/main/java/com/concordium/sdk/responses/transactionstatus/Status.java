package com.concordium.sdk.responses.transactionstatus;

public enum Status {

    /**
     * The transaction was received by the node, but not yet part of a block.
     */
    RECEIVED,

    /**
     * The transaction was committed into a block, i.e. a baker
     * has baked a block and included the transaction. Note that the
     * transaction is not yet part of the authoritative chain (i.e. finalized)
     */
    COMMITTED,

    /**
     * The transaction is in a block which is finalized, i.e.
     * part of the authoritative chain.
     */
    FINALIZED,

    /**
     * The node does not know about the transaction.
     */
    ABSENT
}
