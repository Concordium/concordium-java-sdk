package com.concordium.sdk.responses.transactionstatus;


import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Hash;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public final class TransactionSummary {
    private final int index;
    private final Hash hash;
    private final AccountAddress sender;
    private final CCDAmount cost;
    private final int energyCost;
    private final TransactionResult result;
    private final TransactionTypeInfo type;
}
