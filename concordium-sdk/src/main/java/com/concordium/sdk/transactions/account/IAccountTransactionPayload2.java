package com.concordium.sdk.transactions.account;

import com.concordium.sdk.transactions.TransactionType;

public interface IAccountTransactionPayload2 {
    byte[] serialize();

    TransactionType getType();
}
