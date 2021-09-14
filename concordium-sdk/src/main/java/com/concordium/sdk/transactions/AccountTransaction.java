package com.concordium.sdk.transactions;


import lombok.val;

import java.nio.ByteBuffer;

final class AccountTransaction {
    private final TransactionSignature signature;
    private final TransactionHeader header;
    private final Payload payload;

    AccountTransaction(TransactionSignature signature, TransactionHeader header, Payload payload) {
        this.signature = signature;
        this.header = header;
        this.payload = payload;
    }

    byte[] getBytes() {
        val signatureBytes = signature.getBytes();
        val headerBytes = header.getBytes();
        val payloadBytes = payload.getBytes();
        val buffer = ByteBuffer.allocate(signatureBytes.length + headerBytes.length + payloadBytes.length);
        buffer.put(signatureBytes);
        buffer.put(headerBytes);
        buffer.put(payloadBytes);
        return buffer.array();
    }

    BlockItem toBlockItem() {
        return BlockItem.from(this);
    }
}
