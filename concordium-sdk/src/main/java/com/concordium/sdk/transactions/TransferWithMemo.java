package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class TransferWithMemo extends Payload {
    private final AccountAddress receiver;
    private final CCDAmount amount;
    private final Memo memo;

    @Builder
    public TransferWithMemo(@NonNull AccountAddress receiver,
                            @NonNull CCDAmount amount,
                            @NonNull Memo memo) {
        super(TransactionType.TRANSFER_WITH_MEMO);
        this.receiver = receiver;
        this.amount = amount;
        this.memo = memo;
    }

    @Override
    protected byte[] getPayloadBytes() {
        val buffer = ByteBuffer.allocate(AccountAddress.BYTES + UInt64.BYTES + memo.getLength());
        buffer.put(getReceiver().getBytes());
        buffer.put(memo.getBytes());
        buffer.put(getAmount().getValue().getBytes());

        return buffer.array();
    }

    public static TransferWithMemo fromBytes(ByteBuffer source) {
        val receiver = AccountAddress.fromBytes(source);
        val memo = Memo.fromBytes(source);
        val amount = CCDAmount.fromBytes(source);
        return new TransferWithMemo(receiver, amount, memo);
    }
}
