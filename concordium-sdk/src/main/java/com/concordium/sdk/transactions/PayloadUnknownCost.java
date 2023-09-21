package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;

/**
 * A common class for payloads where the cost cannot
 * be deduced before the transaction is executed.
 * This is the case for transactions that operate on smart contracts, here the user must provide
 * the maximum energy allowed for the transaction.
 * {@link DeployModule}, {@link InitContract} and {@link UpdateContract}
 */
public abstract class PayloadUnknownCost extends Payload {

    @Override
    protected UInt64 getTransactionTypeCost() {
        return null;
    }
}
