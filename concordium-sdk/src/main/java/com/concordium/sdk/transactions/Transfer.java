package com.concordium.sdk.transactions;

import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@ToString
@Getter
final class Transfer extends Payload {

    private final AccountAddress receiver;
    private final UInt64 amount;

    private Transfer(AccountAddress receiver, UInt64 amount) {
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

    static Transfer createNew(AccountAddress receiver, UInt64 amount) {
        return new Transfer(receiver, amount);
    }

    private final static UInt64 BASE_ENERGY_COST = UInt64.from(300);
}
