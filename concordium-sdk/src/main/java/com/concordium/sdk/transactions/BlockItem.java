package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
import lombok.val;

import java.nio.ByteBuffer;

final class BlockItem {
    private static final int VERSION_SIZE = 1;
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
        val buffer = ByteBuffer.allocate(VERSION_SIZE + BlockItemType.BYTES + accountTransactionBytes.length);
        buffer.put((byte) BlockItem.VERSION);
        buffer.put(type.getByte());
        buffer.put(accountTransactionBytes);
        return buffer.array();
    }

    public Hash getHash() {
        return Hash.from(SHA256.hash(getBytes()));
    }

    byte[] getBytes() {
        val accountTransactionBytes = accountTransaction.getBytes();
        val buffer = ByteBuffer.allocate(BlockItemType.BYTES + accountTransactionBytes.length);
        buffer.put(type.getByte());
        buffer.put(accountTransactionBytes);
        return buffer.array();
    }
}
