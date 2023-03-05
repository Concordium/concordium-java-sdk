package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
final class BlockItem extends AbstractTransaction {
    // todo: in practice this is true, but in the future this could different as the version is variable length encoded.
    private static final int VERSION_SIZE = 1;
    private static final int VERSION = 0;
    private final AccountTransaction accountTransaction;

    private BlockItem(AccountTransaction accountTransaction) {
        super(
                accountTransaction.getBlockItemType(),
                accountTransaction.getHeader(),
                accountTransaction.getSignature(),
                accountTransaction.getTransactionType(),
                accountTransaction.getPayloadBytes());
        this.accountTransaction = accountTransaction;
    }

    /**
     * Retrieve the account transaction from the block item, if the block item contains it. Otherwise, returns null.
     */
    @Nullable
    public AccountTransaction getAccountTransaction() {
        if (getBlockItemType() == BlockItemType.ACCOUNT_TRANSACTION) {
            return accountTransaction;
        } else {
            return null;
        }
    }

    public static BlockItem from(AccountTransaction accountTransaction) {
        return new BlockItem(accountTransaction);
    }

    public Hash getHash() {
        return Hash.from(SHA256.hash(getBytes()));
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
        if ((int) tag == VERSION) {
            return BlockItem.fromBytes(source);
        } else {
            throw new UnsupportedOperationException("Only block items in version 0 format are supported.");
        }
    }
}
