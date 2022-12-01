package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import lombok.val;
import org.apache.commons.codec.DecoderException;

import java.nio.ByteBuffer;

@Jacksonized
@Builder
@Data
public class EncryptedAmountTransferData {

    /**
     * Encryption of the remaining amount.
     */
    private final EncryptedAmount remainingAmount;

    /**
     * Encryption of the Amount that will be sent.
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


    public byte[] getBytes() throws DecoderException {
        val remainingAmountBytes = this.remainingAmount.getBytes();
        val transferAmountBytes = this.transferAmount.getBytes();
        val proofBytes = this.proof.getBytes();
        val buffer = ByteBuffer.allocate(
                transferAmountBytes.length
                        + remainingAmountBytes.length
                        + UInt64.BYTES
                        + proofBytes.length);
        buffer.put(remainingAmountBytes);
        buffer.put(transferAmountBytes);
        buffer.put(index.getBytes());
        buffer.put(proofBytes);

        return buffer.array();
    }
}
