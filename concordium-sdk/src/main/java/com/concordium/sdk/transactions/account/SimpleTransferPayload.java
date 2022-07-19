package com.concordium.sdk.transactions.account;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.TransactionType;
import com.concordium.sdk.types.UInt64;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
public class SimpleTransferPayload implements IAccountTransactionPayload2 {
    @Getter
    private final TransactionType type = TransactionType.SIMPLE_TRANSFER;
    private final AccountAddress receiver;
    private final CCDAmount amount;

    @Override
    public byte[] serialize() {
        val buffer = ByteBuffer.allocate(type.BYTES + AccountAddress.BYTES + UInt64.BYTES);
        buffer.put(type.getValue());
        buffer.put(receiver.getBytes());
        buffer.put(amount.getBytes());
        return buffer.array();
    }
}
