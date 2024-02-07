package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import lombok.*;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RegisterDataTransaction extends AccountTransaction {
    private RegisterDataTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final Data data,
            @NonNull final Nonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, RegisterData.createNew(data), TransactionTypeCost.REGISTER_DATA.getValue());
    }

    private RegisterDataTransaction(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull Data payload) {
        super(header,
                signature,
                RegisterData.createNew(payload));
    }

    /**
     * Creates a new instance of {@link RegisterDataTransaction}.
     *
     * @param data   {@link Data} for {@link RegisterDataTransaction}.
     * @param sender Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce  Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry {@link Expiry} of this transaction.
     * @param signer {@link Signer} of this transaction.
     * @return Initialized {@link RegisterDataTransaction}
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder(builderClassName = "RegisterDataTransactionBuilder")
    public static RegisterDataTransaction from(
            final AccountAddress sender,
            final Data data,
            final Nonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        try {
            return new RegisterDataTransaction(sender, data, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }

    /**
     * Creates a new instance of {@link RegisterDataTransaction}.
     * Using {@link TransactionHeader}, {@link TransactionSignature} and Payload {@link Data}.
     *
     * @param header    {@link TransactionHeader}.
     * @param signature {@link TransactionSignature}.
     * @param payload   {@link Data} Payload for this transaction.
     * @return
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder(builderMethodName = "builderBlockItem", builderClassName = "RegisterDataBlockItemBuilder")
    public static RegisterDataTransaction from(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull Data payload) {
        try {
            return new RegisterDataTransaction(header, signature, payload);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
