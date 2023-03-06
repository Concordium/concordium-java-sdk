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
public class TransferWithMemo extends Payload {

    private final static TransactionType TYPE = TransactionType.TRANSFER_WITH_MEMO;

    private final TransferWithMemoPayload payload;

    private TransferWithMemo(AccountAddress receiver, CCDAmount amount, Memo memo) {
        this.payload = TransferWithMemoPayload.from(receiver, amount, memo);
    }

    static TransferWithMemo createNew(AccountAddress receiver, CCDAmount amount, Memo memo) {
        return new TransferWithMemo(receiver, amount, memo);
    }

    @Override
    public PayloadType getType() {
        return PayloadType.TRANSFER_WITH_MEMO;
    }

    public static TransferWithMemo fromBytes(ByteBuffer source) {
        val receiver = AccountAddress.fromBytes(source);
        val memo = Memo.fromBytes(source);
        val amount = CCDAmount.fromBytes(source);
        return new TransferWithMemo(receiver, amount, memo);
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return TransactionTypeCost.TRANSFER_WITH_MEMO.getValue();
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TRANSFER_WITH_MEMO;
    }

    @Override
    public byte[] getTransactionPayloadBytes() {
        return payload.getBytes();
    }
}
