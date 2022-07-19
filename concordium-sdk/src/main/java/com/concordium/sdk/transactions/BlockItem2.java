package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.account.IAccountTransaction2;
import com.concordium.sdk.transactions.account.IAccountTransactionPayload2;
import com.google.common.primitives.Ints;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
public abstract class BlockItem2<P extends IAccountTransactionPayload2> {
    private final TransactionVersion2 version;
    private final IAccountTransaction2<P> transaction;

    public byte[] serialize() {
        byte[] accountTransactionBytes = transaction.serialize();
        byte[] versionBytes = this.version.serialize();
        val buffer = ByteBuffer.allocate(
                versionBytes.length
                        + BlockItemType.BYTES
                        + accountTransactionBytes.length
        );
        buffer.put(versionBytes);
        buffer.putInt(this.transaction.getPayload().getType().ordinal());
        buffer.put(accountTransactionBytes);
        return buffer.array();
    }
}
