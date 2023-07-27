package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
public class TransferPayload {
    private final AccountAddress receiver;
    private final CCDAmount amount;

    private TransferPayload(final AccountAddress receiver, final CCDAmount amount) {
        this.receiver = receiver;
        this.amount = amount;
    }

    public static TransferPayload from(final AccountAddress receiver, final CCDAmount amount) {
        return new TransferPayload(receiver, amount);
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(AccountAddress.BYTES + UInt64.BYTES);
        buffer.put(receiver.getBytes());
        buffer.put(amount.getBytes());

        return buffer.array();
    }
}
