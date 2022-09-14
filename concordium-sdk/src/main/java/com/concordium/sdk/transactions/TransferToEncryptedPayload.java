package com.concordium.sdk.transactions;

import lombok.val;

import java.nio.ByteBuffer;

public class TransferToEncryptedPayload {
    private final CCDAmount amount;

    public TransferToEncryptedPayload(CCDAmount amount) {
        this.amount = amount;
    }

    public static TransferToEncryptedPayload from(int amount) {
        return new TransferToEncryptedPayload(CCDAmount.fromMicro(amount));
    }

    public byte[] getBytes() {
        val amount_bytes = amount.getBytes();
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + amount_bytes.length);
        buffer.put(TransactionType.TRANSFER_TO_ENCRYPTED.getValue());
        buffer.put(amount_bytes);
        return buffer.array();
    }

}
