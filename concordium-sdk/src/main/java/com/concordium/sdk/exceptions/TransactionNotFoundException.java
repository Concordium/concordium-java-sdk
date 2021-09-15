package com.concordium.sdk.exceptions;

import com.concordium.sdk.transactions.Hash;
import lombok.Getter;

public class TransactionNotFoundException extends Exception {
    @Getter
    private final Hash hash;

    /**
     * Creates a new {@link TransactionNotFoundException} from a {@link Hash}
     * This happens when a transaction could not be found.
     *
     * Use {@link TransactionNotFoundException#from(Hash)} to instantiate.
     *
     * @param hash the transaction hash
     */
    private TransactionNotFoundException(Hash hash) {
        super("Transaction not found: " + hash.asHex());
        this.hash = hash;
    }

    public static TransactionNotFoundException from(Hash hash) {
        return new TransactionNotFoundException(hash);
    }
}
