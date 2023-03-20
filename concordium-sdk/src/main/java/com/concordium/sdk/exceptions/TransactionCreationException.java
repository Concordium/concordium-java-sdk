package com.concordium.sdk.exceptions;

import com.concordium.sdk.transactions.Transaction;
import lombok.Getter;

public class TransactionCreationException extends RuntimeException {

    @Getter
    private final Exception inner;

    /**
     * Creates a new {@link TransactionCreationException} from an inner {@link Exception}
     * This happens when the transaction failed to be generated locally.
     * The `inner` {@link Exception} contains information about
     * the failure.
     * <p>
     * Use {@link TransactionCreationException#from(Exception)} to instantiate.
     *
     * @param inner An underlying {@link Exception} with the reason for
     *              why the {@link Transaction} could not be build successfully.
     */
    private TransactionCreationException(Exception inner) {
        super("The creation of the Transaction failed. " + inner.getMessage());
        this.inner = inner;
    }

    public static TransactionCreationException from(Exception inner) {
        return new TransactionCreationException(inner);
    }
}
