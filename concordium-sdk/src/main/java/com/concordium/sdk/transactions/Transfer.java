package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
public final class Transfer extends Payload {

    private final AccountAddress receiver;
    private final CCDAmount amount;

    private Transfer(AccountAddress receiver, CCDAmount amount) {
        this.receiver = receiver;
        this.amount = amount;
    }

    @Override
    public PayloadType getType() {
        return PayloadType.TRANSFER;
    }

    public static Transfer fromBytes(ByteBuffer source) {
        val receiver = AccountAddress.fromBytes(source);
        val amount = CCDAmount.fromBytes(source);
        return new Transfer(receiver, amount);
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return BASE_ENERGY_COST;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.SIMPLE_TRANSFER;
    }

    @Override
    public byte[] getTransactionPayloadBytes() {
        val buffer = ByteBuffer.allocate(AccountAddress.BYTES + UInt64.BYTES);
        buffer.put(receiver.getBytes());
        buffer.put(amount.getBytes());

        return buffer.array();
    }

    static Transfer createNew(AccountAddress receiver, CCDAmount amount) {
        return new Transfer(receiver, amount);
    }

    private final static UInt64 BASE_ENERGY_COST = UInt64.from(300);
}
