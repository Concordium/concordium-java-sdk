package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
import lombok.val;

import java.nio.ByteBuffer;

public final class BlockItem {
    private static final int VERSION = 0;
    private final BlockItemType type;
    private final AccountTransaction accountTransaction;

    private BlockItem(AccountTransaction accountTransaction) {
        this.type = BlockItemType.ACCOUNT_TRANSACTION;
        this.accountTransaction = accountTransaction;
    }

    public static BlockItem from(AccountTransaction accountTransaction) {
        return new BlockItem(accountTransaction);
    }

    public byte[] getVersionedBytes() {
        val accountTransactionBytes = accountTransaction.getBytes();
        int VERSION = 1;
        val buffer = ByteBuffer.allocate(VERSION + BlockItemType.BYTES + accountTransactionBytes.length);
        buffer.put((byte) BlockItem.VERSION);
        buffer.put(type.getByte());
        buffer.put(accountTransactionBytes);
        return buffer.array();
    }

    public BlockItemHash getHash() {
        return BlockItemHash.from(SHA256.hash(getBytes()));
    }

    byte[] getBytes() {
        val accountTransactionBytes = accountTransaction.getBytes();
        val buffer = ByteBuffer.allocate(BlockItemType.BYTES + accountTransactionBytes.length);
        buffer.put(type.getByte());
        buffer.put(accountTransactionBytes);
        return buffer.array();
    }
}
