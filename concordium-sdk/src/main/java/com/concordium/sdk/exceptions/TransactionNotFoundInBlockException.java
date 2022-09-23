package com.concordium.sdk.exceptions;

import com.concordium.sdk.transactions.Hash;
import lombok.Getter;

public final class TransactionNotFoundInBlockException extends Exception {
    @Getter
    private final Hash transactionHash;
    @Getter
    private final Hash blockHash;

    /**
     * Creates a new {@link TransactionNotFoundInBlockException} from a Transaction {@link Hash} and a Block {@link Hash}.
     * This happens when the Transaction could not be found for the given block.
     * <p>
     * Use {@link TransactionNotFoundInBlockException#from(Hash, Hash)} to instantiate.
     *
     * @param transactionHash Transaction Hash.
     * @param blockHash       The block hash
     */
    private TransactionNotFoundInBlockException(Hash transactionHash, Hash blockHash) {
        super("Transaction (" + transactionHash.toString() + ") not found for block " + blockHash.asHex());
        this.transactionHash = transactionHash;
        this.blockHash = blockHash;
    }

    public static TransactionNotFoundInBlockException from(Hash transactionHash, Hash blockHash) {
        return new TransactionNotFoundInBlockException(transactionHash, blockHash);
    }
}
