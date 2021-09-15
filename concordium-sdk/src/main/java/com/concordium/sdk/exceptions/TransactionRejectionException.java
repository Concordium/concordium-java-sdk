package com.concordium.sdk.exceptions;

import com.concordium.sdk.transactions.Transaction;
import lombok.Getter;

public class TransactionRejectionException extends Exception {

    @Getter
    private final Transaction transaction;

    /**
     * Creates a new {@link TransactionRejectionException} from a transaction.
     * This happens when the transaction was correctly build locally,
     * but the transaction was rejected by the node.
     * Consult the logs of the node for more information.
     * <p>
     * Use {@link TransactionRejectionException#from(Transaction)} to instantiate.
     *
     * @param transaction The failed transaction
     */
    private TransactionRejectionException(Transaction transaction) {
        super("The Transaction was rejected by the node: " + transaction.getHash().asHex());
        this.transaction = transaction;
    }


    public static TransactionRejectionException from(Transaction transaction) {
        return new TransactionRejectionException(transaction);
    }

}
