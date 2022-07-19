package com.concordium.sdk.transactions.account;

import com.concordium.sdk.transactions.TransactionHeader;
import com.concordium.sdk.transactions.TransactionSignature;
import lombok.val;

import java.nio.ByteBuffer;

public abstract class IAccountTransaction2<T extends IAccountTransactionPayload2> {
    abstract TransactionSignature getSignature();

    abstract TransactionHeader getHeader();

    public abstract T getPayload();

    public byte[] serialize() {
        val signatureBytes = this.getSignature().getBytes();
        val headerBytes = this.getHeader().getBytes();
        val payloadBytes = this.getPayload().serialize();
        val buffer = ByteBuffer.allocate(signatureBytes.length + headerBytes.length + payloadBytes.length);
        buffer.put(signatureBytes);
        buffer.put(headerBytes);
        buffer.put(payloadBytes);
        return buffer.array();
    }
}
