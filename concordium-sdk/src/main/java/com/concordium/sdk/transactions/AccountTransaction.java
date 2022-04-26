package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@EqualsAndHashCode
@Getter
@ToString
public final class AccountTransaction {
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

    public static AccountTransaction fromBytes(ByteBuffer source) {
        val signature = TransactionSignature.fromBytes(source);
        val header = TransactionHeader.fromBytes(source);
        byte tag = source.get();
        Payload payload;
        switch (tag) {
            case 3:
                payload = Transfer.fromBytes(source);
                break;
            case 22:
                payload = TransferWithMemo.fromBytes(source);
                break;
            default:
                throw new UnsupportedOperationException("Only transfers and transfers with memo are currently supported.");
        }
        return new AccountTransaction(signature, header, payload);
    }
}
