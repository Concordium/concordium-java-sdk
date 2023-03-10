package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.*;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferWithMemoTransaction extends AbstractAccountTransaction {
    private TransferWithMemoTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountAddress receiver,
            @NonNull final CCDAmount amount,
            @NonNull final Memo memo,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferWithMemo.createNew(receiver, amount, memo));
    }

    private TransferWithMemoTransaction(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull TransferWithMemoPayload payload) {
        super(header, signature, TransactionType.TRANSFER_WITH_MEMO, payload.getBytes());
    }

    @Builder(builderClassName = "TransferWithMemoTransactionBuilder")
    public static TransferWithMemoTransaction from(final AccountAddress sender,
                                                   final AccountAddress receiver,
                                                   final CCDAmount amount,
                                                   final Memo memo,
                                                   final AccountNonce nonce,
                                                   final Expiry expiry,
                                                   final TransactionSigner signer) {
        try {
            return new TransferWithMemoTransaction(sender, receiver, amount, memo, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }

    @Builder(builderMethodName = "builderBlockItem", builderClassName = "TransferWithMemoBlockItemBuilder")
    public static TransferWithMemoTransaction from(final TransactionHeader header,
                                                   final TransactionSignature signature,
                                                   final TransferWithMemoPayload payload) {
        try {
            return new TransferWithMemoTransaction(header, signature, payload);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}

