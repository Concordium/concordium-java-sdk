package com.concordium.sdk.responses.blockitemsummary;


/**
 * Type of the block item.
 */
public enum Type {
    /**
     * The transaction was sent by an account on the chain.
     */
    ACCOUNT_TRANSACTION,
    /**
     * An account was created on the chain.
     */
    ACCOUNT_CREATION,
    /**
     * A chain update
     */
    CHAIN_UPDATE,
    /**
     * A new protocol-level token (PLT) was created.
     */
    TOKEN_CREATION,
}
