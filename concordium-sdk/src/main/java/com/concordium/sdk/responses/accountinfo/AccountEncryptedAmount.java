package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.EncryptedAmount;
import com.concordium.sdk.transactions.EncryptedAmountIndex;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * The state of the encrypted balance of an account.
 */
@Data
@Jacksonized
@Builder
public final class AccountEncryptedAmount {

    private final List<EncryptedAmount> incomingAmounts;
    /**
     * The actual encrypted amount.
     */
    private final EncryptedAmount selfAmount;
    /**
     * Index of the amount on the account.
     */
    private final EncryptedAmountIndex startIndex;
}
