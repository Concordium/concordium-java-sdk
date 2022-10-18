package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * Transfer from public to encrypted balance of the sender account.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class TransferToEncrypted extends Payload {

    private final static TransactionType TYPE = TransactionType.TRANSFER_TO_ENCRYPTED;

    private final CCDAmount amount;

    private final UInt64 maxEnergyCost;

    public TransferToEncrypted(CCDAmount amount, UInt64 maxEnergyCost) {
        this.amount = amount;
        this.maxEnergyCost = maxEnergyCost;
    }

    @Override
    public PayloadType getType() {
        return PayloadType.TRANSFER_TO_ENCRYPTED;
    }

    @Override
    byte[] getBytes() {
        val amount_bytes = amount.getBytes();
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + amount_bytes.length);
        buffer.put(TransactionType.TRANSFER_TO_ENCRYPTED.getValue());
        buffer.put(amount_bytes);
        return buffer.array();
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return this.maxEnergyCost;
    }

    static TransferToEncrypted createNew(CCDAmount amount, UInt64 maxEnergyCost) {
        return new TransferToEncrypted(amount, maxEnergyCost);
    }
}