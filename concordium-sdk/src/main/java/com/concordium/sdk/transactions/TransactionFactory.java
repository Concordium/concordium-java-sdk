package com.concordium.sdk.transactions;

/**
 * TransactionFactory provides convenient functions for building a
 * {@link Transaction}
 */
public class TransactionFactory {

    /**
     * Creates a new {@link SimpleTransferTransaction.SimpleTransferTransactionBuilder} for the purpose of
     * creating a {@link SimpleTransferTransaction}
     * @return the builder for a {@link SimpleTransferTransaction}
     */
    public static SimpleTransferTransaction.SimpleTransferTransactionBuilder newSimpleTransfer() {
        return SimpleTransferTransaction.builder();
    }
}
