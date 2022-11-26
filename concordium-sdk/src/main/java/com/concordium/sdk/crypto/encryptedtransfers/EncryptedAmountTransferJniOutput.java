package com.concordium.sdk.crypto.encryptedtransfers;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.EncryptedAmount;
import com.concordium.sdk.transactions.SecToPubAmountTransferProof;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class EncryptedAmountTransferJniOutput {

    /**
     * Encryption of the remaining amount.
     */
    private final EncryptedAmount remainingAmount;

    /**
     * Amount that will be sent.
     */
    private final EncryptedAmount transferAmount;

    /**
     * The index such that the encrypted amount used in the transfer represents
     * the aggregate of all encrypted amounts with indices < `index` existing
     * on the account at the time. New encrypted amounts can only add new indices.
     */
    private final UInt64 index;

    /**
     * A collection of all the proofs.
     */
    private final SecToPubAmountTransferProof proof;

    public static EncryptedAmountTransferJniOutput fromJson(String encryptedTransferOutputJniString) {
        try {
            return JsonMapper.INSTANCE.readValue(encryptedTransferOutputJniString, EncryptedAmountTransferJniOutput.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse BlockInfo JSON", e);
        }
    }
}
