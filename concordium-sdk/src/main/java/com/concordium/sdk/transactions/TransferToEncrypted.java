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

    /**
     * The amount to transfer from public to encrypted balance of the sender account.
     */
    private final CCDAmount amount;

    public TransferToEncrypted(CCDAmount amount) {
        this.amount = amount;
    }

    /**
     * This is a method that returns the type of the payload
     */
    @Override
    public PayloadType getType() {
        return PayloadType.TRANSFER_TO_ENCRYPTED;
    }

    /**
     * The bytes of a transfer to encrypted transaction are the bytes of the transaction type followed by the bytes of the
     * amount.
     *
     * @return The byte array of the transaction type and the amount.
     */
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
        return TransactionTypeCost.TRANSFER_TO_ENCRYPTED.getValue();
    }

    static TransferToEncrypted createNew(CCDAmount amount) {
        return new TransferToEncrypted(amount);
    }
}
