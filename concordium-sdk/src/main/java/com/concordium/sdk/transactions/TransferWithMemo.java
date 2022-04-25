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
class TransferWithMemo extends Payload {

    private final static TransactionType TYPE = TransactionType.TRANSFER_WITH_MEMO;

    private final AccountAddress receiver;
    private final GTUAmount amount;
    private final Memo memo;

    private TransferWithMemo(AccountAddress receiver, GTUAmount amount, Memo memo) {
        this.receiver = receiver;
        this.amount = amount;
        this.memo = memo;
    }

    static TransferWithMemo createNew(AccountAddress receiver, GTUAmount amount, Memo memo) {
        return new TransferWithMemo(receiver, amount, memo);
    }

    @Override
    byte[] getBytes() {
        val buffer = ByteBuffer.allocate(
                TransactionType.BYTES +
                        AccountAddress.BYTES +
                        UInt64.BYTES +
                        memo.getLength());
        buffer.put(TransactionType.TRANSFER_WITH_MEMO.getValue());
        buffer.put(getReceiver().getBytes());
        buffer.put(memo.getBytes());
        buffer.put(getAmount().getValue().getBytes());
        return buffer.array();
    }
    public static TransferWithMemo fromBytes(ByteBuffer source) {
        val receiver = AccountAddress.fromBytes(source);
        val memo = Memo.fromBytes(source);
        val amount = GTUAmount.fromBytes(source);
        return new TransferWithMemo(receiver, amount, memo);
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return BASE_ENERGY_COST;
    }

    private final static UInt64 BASE_ENERGY_COST = UInt64.from(300);

}
