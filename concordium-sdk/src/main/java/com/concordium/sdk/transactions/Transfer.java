package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@ToString
@Getter
@EqualsAndHashCode
final class Transfer extends Payload {

    private final AccountAddress receiver;
    private final GTUAmount amount;

    private Transfer(AccountAddress receiver, GTUAmount amount) {
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

    public static Transfer fromBytes(ByteBuffer source) {
        val receiver = AccountAddress.fromBytes(source);
        val amount = GTUAmount.fromBytes(source);
        return new Transfer(receiver, amount);
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return BASE_ENERGY_COST;
    }

    static Transfer createNew(AccountAddress receiver, GTUAmount amount) {
        return new Transfer(receiver, amount);
    }

    private final static UInt64 BASE_ENERGY_COST = UInt64.from(300);
}
