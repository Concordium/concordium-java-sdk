package com.concordium.sdk.responses.transactionstatus;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public final class TransactionTypeInfo {
    private final TransactionType type;
    private final TransactionContents contents;
}
