package com.concordium.sdk.transactions;

import lombok.*;

import java.nio.ByteBuffer;

import static com.google.common.primitives.Bytes.concat;

/**
 * Represents a single item in any Block in Concordium.
 */
@EqualsAndHashCode
@ToString
@Getter
public abstract class BlockItem implements Transaction {
    /**
     * Type of Block Item. Account, Credential Deployment Or Update Instruction.
     */
    private final BlockItemType blockItemType;

    BlockItem(final @NonNull BlockItemType blockItemType) {
        this.blockItemType = blockItemType;
    }

    /**
     * Get the serialized bytes for this {@link BlockItem}.
     * Which is a concatenation of {@link BlockItemType#getByte()} + {@link BlockItem#getBlockItemBytes()}
     *
     * @return serialized block item prepended with the type of the block item, see {@link BlockItemType}
     */
    final public byte[] getBytes() {
        return concat(new byte[]{blockItemType.getByte()}, getBlockItemBytes());
    }

    abstract byte[] getBlockItemBytes();

    static BlockItem fromBytes(ByteBuffer source) {
        val kind = BlockItemType.fromBytes(source);
        switch (kind) {
            case ACCOUNT_TRANSACTION:
                return AccountTransaction.fromBytes(source);
            case ACCOUNT_TRANSACTION_V1:
                return AccountTransactionV1.fromBytes(source);
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
