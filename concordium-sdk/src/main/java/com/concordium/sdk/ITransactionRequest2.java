package com.concordium.sdk;

import com.concordium.sdk.transactions.BlockItem2;
import com.concordium.sdk.transactions.account.IAccountTransaction2;
import com.concordium.sdk.transactions.account.IAccountTransactionPayload2;

public interface ITransactionRequest2<P extends IAccountTransactionPayload2> {
    int getNetworkId();
    BlockItem2<P> getPayload();
}
