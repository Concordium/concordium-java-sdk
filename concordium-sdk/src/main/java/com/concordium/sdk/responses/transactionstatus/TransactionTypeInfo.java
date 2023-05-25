package com.concordium.sdk.responses.transactionstatus;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@EqualsAndHashCode
public final class TransactionTypeInfo {
    private final TransactionType type;
    private final TransactionContents contents;
}
