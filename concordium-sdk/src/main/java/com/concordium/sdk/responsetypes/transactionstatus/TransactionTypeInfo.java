package com.concordium.sdk.responsetypes.transactionstatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionTypeInfo {
    private TransactionType type;
    private TransactionContents contents;
}
