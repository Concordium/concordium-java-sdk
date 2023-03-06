package com.concordium.sdk.transactions;

import lombok.*;

import java.nio.ByteBuffer;

@EqualsAndHashCode
@ToString
@Getter
public abstract class BlockItem implements Transaction {
    private final BlockItemType blockItemType;

    BlockItem(final @NonNull BlockItemType blockItemType) {
        this.blockItemType = blockItemType;
    }

    public static BlockItem from(AccountTransaction accountTransaction) {
        return accountTransaction;
    }

    final public byte[] getBytes() {
        val blockItemBytes = getBlockItemBytes();
        val buffer = ByteBuffer.allocate(BlockItemType.BYTES + blockItemBytes.length);
        buffer.put(blockItemType.getByte());
        buffer.put(blockItemBytes);

        return buffer.array();
    }

    abstract byte[] getBlockItemBytes();

    static BlockItem fromBytes(ByteBuffer source) {
        val kind = BlockItemType.fromBytes(source);
        switch (kind) {
            case ACCOUNT_TRANSACTION:
                val at = AccountTransaction.fromBytes(source);
                return BlockItem.from(at);
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
        if ((int) tag == VERSION) {
            return BlockItem.fromBytes(source);
        } else {
            throw new UnsupportedOperationException("Only block items in version 0 format are supported.");
        }
    }
}
