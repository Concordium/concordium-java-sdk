package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.Getter;

/**
 * Enum class representing the cost of different types of transactions
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
    CONFIGURE_DELEGATION(500),

    /**
     * Constant representing the cost of encrypted transfer
     */
    ENCRYPTED_TRANSFER(27000),

    /**
     * Constant representing the cost of transfer to encrypted
     */
    TRANSFER_TO_ENCRYPTED(600),

    /**
     * Constant representing the cost of transfer to public wallet.
     */
    TRANSFER_TO_PUBLIC(14850);

    /**
     * The cost of the transaction
     */
    @Getter
    private final UInt64 value;

    /**
     * Constructor to set the cost of the transaction
     * @param energy cost of the transaction
     */
    TransactionTypeCost(int energy) {
        this.value = UInt64.from(energy);
    }

}
