package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import lombok.*;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferWithMemoTransaction extends AccountTransaction {
    private TransferWithMemoTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountAddress receiver,
            @NonNull final CCDAmount amount,
            @NonNull final Memo memo,
            @NonNull final Nonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, TransferWithMemo.createNew(receiver, amount, memo), TransactionTypeCost.TRANSFER_WITH_MEMO.getValue());
    }

    private TransferWithMemoTransaction(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull TransferWithMemoPayload payload) {
        super(header, signature, TransferWithMemo.createNew(payload.getReceiver(), payload.getAmount(), payload.getMemo()));
    }

    /**
     * @param amount   {@link CCDAmount} being transferred.
     * @param receiver Receiver {@link AccountAddress} of the Encrypted Amount
     * @param memo     {@link Memo}.
     * @param sender   Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce    Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry   {@link Expiry} of this transaction.
     * @param signer   {@link Signer} of this transaction.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder(builderClassName = "TransferWithMemoTransactionBuilder")
    public static TransferWithMemoTransaction from(final AccountAddress sender,
                                                   final AccountAddress receiver,
                                                   final CCDAmount amount,
                                                   final Memo memo,
                                                   final Nonce nonce,
                                                   final Expiry expiry,
                                                   final TransactionSigner signer) {
        try {
            return new TransferWithMemoTransaction(sender, receiver, amount, memo, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }

    /**
     * Creates a new instance of {@link TransferWithMemoTransaction}.
     * Using {@link TransactionHeader}, {@link TransactionSignature} and Payload {@link TransferWithMemoPayload}.
     *
     * @param header    {@link TransactionHeader}.
     * @param signature {@link TransactionSignature}.
     * @param payload   {@link TransferWithMemoPayload} Payload for this transaction.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
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

