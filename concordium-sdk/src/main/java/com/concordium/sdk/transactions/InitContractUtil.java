package com.concordium.sdk.transactions;

/**
 * InitContractUtil provides convenient functions for initialization a
 * {@link Transaction}
 */
public class InitContractUtil {
    public static InitContractTransaction.InitContractTransactionBuilder newInitContract() {
        return InitContractTransaction.builder();
    }

}
