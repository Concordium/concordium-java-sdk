package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.EncryptedAmount;
import com.concordium.sdk.transactions.EncryptedAmountIndex;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@Builder
public final class AccountEncryptedAmount {
    private final List<EncryptedAmount> incomingAmounts;
    private final EncryptedAmount selfAmount;
    private final EncryptedAmountIndex startIndex;
}
