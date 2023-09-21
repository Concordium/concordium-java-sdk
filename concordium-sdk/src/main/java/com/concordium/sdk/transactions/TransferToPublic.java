package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;

/**
 * Transfer CCDAmount from encrypted wallet to public wallet.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@ToString
public final class TransferToPublic extends Payload {

    /**
     * Encryption of the remaining amount.
     */
    private final EncryptedAmount remainingAmount;

    /**
     * Amount that will be sent.
     */
    private final CCDAmount transferAmount;

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


    @Override
    protected UInt64 getTransactionTypeCost() {
        return TransactionTypeCost.TRANSFER_TO_PUBLIC.getValue();
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TRANSFER_TO_PUBLIC;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        val proofBytes = this.proof.getBytes();
        val remainingAmountBytes = this.remainingAmount.getBytes();
        val buffer = ByteBuffer.allocate(remainingAmountBytes.length
                + CCDAmount.BYTES
                + UInt64.BYTES
                + proofBytes.length);
        buffer.put(remainingAmountBytes);
        buffer.put(this.transferAmount.getBytes());
        buffer.put(this.index.getBytes());
        buffer.put(proofBytes);

        return buffer.array();
    }

    static TransferToPublic createNew(
            EncryptedAmount remainingAmount,
            CCDAmount transferAmount,
            UInt64 index,
            SecToPubAmountTransferProof proof) {
        return new TransferToPublic(remainingAmount, transferAmount, index, proof);
    }
}
