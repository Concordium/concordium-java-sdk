package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import lombok.*;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferTransaction extends AccountTransaction {
    private TransferTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountAddress receiver,
            @NonNull final CCDAmount amount,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, Transfer.createNew(receiver, amount));
    }

    private TransferTransaction(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull TransferPayload payload) {
        super(header, signature, TransactionType.SIMPLE_TRANSFER, payload.getBytes());
    }

    @Builder(builderClassName = "TransferTransactionBuilder")
    public static TransferTransaction from(
            final AccountAddress sender,
            final AccountAddress receiver,
            final CCDAmount amount,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        try {
            return new TransferTransaction(sender, receiver, amount, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }

    @Builder(builderMethodName = "builderBlockItem", builderClassName = "TransferBlockItemBuilder")
    public static TransferTransaction from(final TransactionHeader header,
                                           final TransactionSignature signature,
                                           final TransferPayload payload) {
        try {
            return new TransferTransaction(header, signature, payload);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
