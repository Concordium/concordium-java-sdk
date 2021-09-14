package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
class TransactionHeader {
    private final Account sender;
    private final UInt64 accountNonce;
    private final UInt64 expiry;

    @Setter
    private UInt64 maxEnergyCost;
    @Setter
    private UInt32 payloadSize;

    @Builder
    TransactionHeader(Account sender, UInt64 accountNonce, UInt64 expiry) {
        this.sender = sender;
        this.accountNonce = accountNonce;
        this.expiry = expiry;
        this.maxEnergyCost = UInt64.from(0); // dummy value for calculating cost. Is overwritten before signing the transaction.
    }

    byte[] getBytes() {
        val buffer = ByteBuffer.allocate(AccountAddress.BYTES + UInt64.BYTES + UInt64.BYTES + UInt32.BYTES + UInt64.BYTES);
        buffer.put(sender.getAddress().getBytes());
        buffer.put(accountNonce.getBytes());
        buffer.put(maxEnergyCost.getBytes());
        buffer.put(payloadSize.getBytes());
        buffer.put(expiry.getBytes());
        return buffer.array();
    }
}
