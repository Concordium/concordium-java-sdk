package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
public class TransferWithMemoPayload {
    private final AccountAddress receiver;
    private final CCDAmount amount;
    private final Memo memo;

    private TransferWithMemoPayload(final AccountAddress receiver, final CCDAmount amount, final Memo memo) {
        this.receiver = receiver;
        this.amount = amount;
        this.memo = memo;
    }

    public static TransferWithMemoPayload from(final AccountAddress receiver,
                                               final CCDAmount amount,
                                               final Memo memo) {
        return new TransferWithMemoPayload(receiver, amount, memo);
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(AccountAddress.BYTES + UInt64.BYTES + memo.getLength());
        buffer.put(getReceiver().getBytes());
        buffer.put(memo.getBytes());
        buffer.put(getAmount().getValue().getBytes());

        return buffer.array();
    }
}
