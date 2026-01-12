package com.concordium.sdk.transactions;

import lombok.*;
import org.apache.commons.lang3.NotImplementedException;

import java.nio.ByteBuffer;

import static com.google.common.primitives.Bytes.concat;

/**
 * An extended transaction format originating from a particular account.
 * Such a transaction can be sponsored, in which case its cost is not paid
 * by the sender.
 */
@Builder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountTransactionV1 extends BlockItem {

    /**
     * Transaction payload, defines what this transaction does.
     */
    private final Payload payload;

    /**
     * Transaction header data.
     */
    private final TransactionHeaderV1 header;

    /**
     * The signatures on the transaction by the source account and optionally a sponsor account.
     */
    private final TransactionSignaturesV1 signatures;

    public AccountTransactionV1(@NonNull Payload payload,
                                @NonNull TransactionHeaderV1 header,
                                @NonNull TransactionSignaturesV1 signatures) {
        super(BlockItemType.ACCOUNT_TRANSACTION_V1);
        this.payload = payload;
        this.header = header;
        this.signatures = signatures;
    }

    @Override
    final byte[] getBlockItemBytes() {
        return concat(
                signatures.getBytes(),
                header.getBytes(),
                payload.getBytes()
        );
    }

    public static AccountTransactionV1 fromBytes(ByteBuffer source) {
        // TODO implement payload from bytes
        throw new NotImplementedException("TODO");
    }
}
