package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.responses.accountinfo.AccountEncryptedAmount;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * An aggregated encrypted amount with a decrypted plaintext, collecting
 * encrypted amounts with decryption.
 */
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
