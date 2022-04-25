package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;

@EqualsAndHashCode
@ToString
final class BlockItem {
    private static final int VERSION_SIZE = 1;
    private static final int VERSION = 0;
    private final BlockItemType type;
    private final AccountTransaction accountTransaction;

    private BlockItem(AccountTransaction accountTransaction) {
        this.type = BlockItemType.ACCOUNT_TRANSACTION;
        this.accountTransaction = accountTransaction;
    }

    /**
     * Retrieve the account transaction from the block item, if the block item contains it. Otherwise, returns null.
     */
    @Nullable
    public AccountTransaction getAccountTransaction() {
        if (type == BlockItemType.ACCOUNT_TRANSACTION) {
            return accountTransaction;
        } else {
            return null;
        }
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

    static BlockItem fromBytes(ByteBuffer source) {
        val kind = BlockItemType.fromBytes(source);
        switch (kind) {
            case ACCOUNT_TRANSACTION:
                val at = AccountTransaction.fromBytes(source);
                return new BlockItem(at);
            case CREDENTIAL_DEPLOYMENT:
                throw new UnsupportedOperationException("Only account transactions are supported in this version. Credential deployments are not.");
            case UPDATE_INSTRUCTION:
                throw new UnsupportedOperationException("Only account transactions are supported in this version. Update instructions are not.");
            default:
                throw new UnsupportedOperationException("Unrecognized block item type.");
        }
    }

    public static BlockItem fromVersionedBytes(ByteBuffer source) {
        byte tag = source.get();
        if ((int)tag == VERSION) {
            return BlockItem.fromBytes(source);
        } else {
            throw new UnsupportedOperationException("Only block items in version 0 format are supported.");
        }
    }
}
