package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.Getter;

/**
 * Enum class representing the cost of different types of transactions
 * This is in accordance with <a href="https://github.com/Concordium/concordium-base/blob/main/haskell-src/Concordium/Cost.hs">Cost.hs</a>
 */
public enum TransactionTypeCost {
    /**
     * Constant representing the cost of configuring a baker
     */
    CONFIGURE_BAKER(300),
    /**
     * Constant representing the cost of configuring a baker with proofs
     */
    CONFIGURE_BAKER_WITH_PROOFS(4050),
    /**
     * Constant representing the cost of configuring delegation
     */
    CONFIGURE_DELEGATION(300),

    /**
     * Constant representing the cost of transfer to public wallet.
     */
    TRANSFER_TO_PUBLIC(14850),

    /**
     * Constant representing the cost of Encrypted Transfer With Memo.
     */
    ENCRYPTED_TRANSFER_WITH_MEMO(27000),

    /**
     * Constant representing the cost of Transfer With Memo.
     */
    TRANSFER_WITH_MEMO(300),

    /**
     * Base cost for registering data on the chain.
     */
    REGISTER_DATA(300),

    /**
     * Base cost for a basic transfer.
     */
    TRANSFER_BASE_COST(300),

    /**
     * Base cost for a token update transaction.
     */
    TOKEN_UPDATE_BASE_COST(300),
    ;

    /**
     * The cost of the transaction
     */
    @Getter
    private final UInt64 value;

    /**
     * Constructor to set the cost of the transaction
     *
     * @param energy cost of the transaction
     */
    TransactionTypeCost(int energy) {
        this.value = UInt64.from(energy);
    }
}
