package com.concordium.sdk.transactions;

import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@ToString
@Getter
final class SimpleTransfer extends Payload {

    private final static TransactionType TYPE = TransactionType.SIMPLE_TRANSFER;
    private final AccountAddress receiver;
    private final UInt64 amount;

    private SimpleTransfer(AccountAddress receiver, UInt64 amount) {
        this.receiver = receiver;
        this.amount = amount;
    }

    @Override
    byte[] getBytes() {
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + AccountAddress.BYTES + UInt64.BYTES);
        buffer.put(TransactionType.SIMPLE_TRANSFER.getValue());
        buffer.put(receiver.getBytes());
        buffer.put(amount.getBytes());
        return buffer.array();
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return BASE_ENERGY_COST;
    }

    static SimpleTransfer makeNew(AccountAddress receiver, UInt64 amount) {
        return new SimpleTransfer(receiver, amount);
    }

    private final static UInt64 BASE_ENERGY_COST = UInt64.from(300);
}
