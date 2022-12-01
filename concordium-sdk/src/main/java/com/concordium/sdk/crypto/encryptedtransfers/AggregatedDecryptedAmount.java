package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.responses.accountinfo.AccountEncryptedAmount;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class AggregatedDecryptedAmount {
    private final String encryptedChunks;
    private final long index;

    public static IndexedEncryptedAmount from(AccountEncryptedAmount encryptedAmount) {
        return new IndexedEncryptedAmount(
                encryptedAmount.getSelfAmount(),
                encryptedAmount.getStartIndex()
        );
    }

}
