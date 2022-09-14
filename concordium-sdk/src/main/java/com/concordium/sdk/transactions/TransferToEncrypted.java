package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class TransferToEncrypted extends Payload {

    private final static TransactionType TYPE = TransactionType.TRANSFER_TO_ENCRYPTED;

    private final TransferToEncryptedPayload payload;

    private final UInt64 maxEnergyCost;

    public TransferToEncrypted(TransferToEncryptedPayload payload, UInt64 maxEnergyCost) {
        this.payload = payload;
        this.maxEnergyCost = maxEnergyCost;
    }

    @Override
    public PayloadType getType() {
        return PayloadType.TRANSFER_TO_ENCRYPTED;
    }

    @Override
    byte[] getBytes() {
        val payload_bytes = payload.getBytes();
        val buffer = ByteBuffer.allocate(payload_bytes.length);
        buffer.put(payload_bytes);
        return buffer.array();
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return this.maxEnergyCost;
    }

    static TransferToEncrypted createNew(TransferToEncryptedPayload payload, UInt64 maxEnergyCost) {
        return new TransferToEncrypted(payload, maxEnergyCost);
    }
}