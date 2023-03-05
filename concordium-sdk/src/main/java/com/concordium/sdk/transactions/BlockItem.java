package com.concordium.sdk.transactions;

import lombok.*;

import java.nio.ByteBuffer;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
final class BlockItem extends AbstractTransaction {
    BlockItem(
            final @NonNull BlockItemType blockItemType,
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull TransactionType transactionType,
            @NonNull final byte[] payloadBytes) {
        super(blockItemType, header, signature, transactionType, payloadBytes);
    }

    public static BlockItem from(AccountTransaction accountTransaction) {
        return new BlockItem(
                accountTransaction.getBlockItemType(),
                accountTransaction.getHeader(),
                accountTransaction.getSignature(),
                accountTransaction.getTransactionType(),
                accountTransaction.getPayloadBytes());
    }

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
