package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
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

    private final TransferPayload payload;

    private Transfer(AccountAddress receiver, CCDAmount amount) {
        this.payload = TransferPayload.from(receiver, amount);
    }

    public static Transfer fromBytes(ByteBuffer source) {
        val receiver = AccountAddress.fromBytes(source);
        val amount = CCDAmount.fromBytes(source);
        return new Transfer(receiver, amount);
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.SIMPLE_TRANSFER;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        return payload.getBytes();
    }

    static Transfer createNew(AccountAddress receiver, CCDAmount amount) {
        return new Transfer(receiver, amount);
    }

}
