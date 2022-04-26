package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
@ToString
public class TransactionHeader {
    private final AccountAddress sender;
    private final UInt64 accountNonce;
    private final UInt64 expiry;

    @Setter
    private UInt64 maxEnergyCost;
    @Setter
    private UInt32 payloadSize;

    @Builder
    TransactionHeader(AccountAddress sender, UInt64 accountNonce, UInt64 expiry) {
        this.sender = sender;
        this.accountNonce = accountNonce;
        this.expiry = expiry;
        this.maxEnergyCost = UInt64.from(0); // dummy value used for calculating the energy cost.
    }

    byte[] getBytes() {
        val buffer = ByteBuffer.allocate(AccountAddress.BYTES + UInt64.BYTES + UInt64.BYTES + UInt32.BYTES + UInt64.BYTES);
        buffer.put(sender.getBytes());
        buffer.put(accountNonce.getBytes());
        buffer.put(maxEnergyCost.getBytes());
        buffer.put(payloadSize.getBytes());
        buffer.put(expiry.getBytes());
        return buffer.array();
    }

    public static TransactionHeader fromBytes(ByteBuffer source) {
        AccountAddress sender = AccountAddress.fromBytes(source);
        UInt64 accountNonce = UInt64.fromBytes(source);
        UInt64 maxEnergyCost = UInt64.fromBytes(source);
        UInt32 payloadSize = UInt32.fromBytes(source);
        UInt64 expiry = UInt64.fromBytes(source);
        TransactionHeader header = new TransactionHeader(sender, accountNonce, expiry);
        header.setMaxEnergyCost(maxEnergyCost);
        header.setPayloadSize(payloadSize);
        return header;
    }
}
