package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.responses.accountinfo.AccountEncryptedAmount;
import com.concordium.sdk.transactions.EncryptedAmount;
import com.concordium.sdk.transactions.EncryptedAmountIndex;
import lombok.Builder;
import lombok.Data;

/**
 * An encrypted amount that we know the index of.
 */
@Data
@Builder
class IndexedEncryptedAmount {
    /**
     * The actual encrypted amount.
     */
    private final EncryptedAmount encryptedChunks;
    /**
     * Index of the amount on the account.
     */
    private final EncryptedAmountIndex index;

    public static IndexedEncryptedAmount from(AccountEncryptedAmount encryptedAmount) {
        return new IndexedEncryptedAmount(
                encryptedAmount.getSelfAmount(),
                encryptedAmount.getStartIndex()
        );
    }
}
