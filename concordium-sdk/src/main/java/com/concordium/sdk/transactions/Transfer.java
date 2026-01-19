package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Transfer extends Payload {
    private final AccountAddress receiver;
    private final CCDAmount amount;

    @Builder
    public Transfer(@NonNull AccountAddress receiver,
                    @NonNull CCDAmount amount) {
        super(TransactionType.SIMPLE_TRANSFER);
        this.receiver = receiver;
        this.amount = amount;
    }

    @Override
    protected byte[] getPayloadBytes() {
        val buffer = ByteBuffer.allocate(AccountAddress.BYTES + UInt64.BYTES);
        buffer.put(receiver.getBytes());
        buffer.put(amount.getBytes());
        return buffer.array();
    }

    public static Transfer fromBytes(ByteBuffer source) {
        return new Transfer(
                AccountAddress.fromBytes(source),
                CCDAmount.fromBytes(source)
        );
    }
}
