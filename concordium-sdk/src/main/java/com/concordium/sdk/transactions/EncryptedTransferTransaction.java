package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import lombok.*;

/**
 * @deprecated Encrypted transfers are deprecated and partially removed since protocol version 7
 */
@Deprecated
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EncryptedTransferTransaction extends AccountTransaction {

    /**
     * A constructor of {@link EncryptedTransferTransaction} class.
     */
    private EncryptedTransferTransaction(
            @NonNull final EncryptedAmountTransferData data,
            @NonNull final AccountAddress receiver,
            @NonNull final AccountAddress sender,
            @NonNull final Nonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(sender, nonce, expiry, signer, EncryptedTransfer.createNew(data, receiver), TransactionTypeCost.ENCRYPTED_TRANSFER.getValue());
    }

    /**
     * @param data     Parameters needed for Encrypted Transfer in {@link EncryptedAmountTransferData} format.
     * @param receiver Receiver {@link AccountAddress} of the Encrypted Amount
     * @param sender   Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce    Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry   {@link Expiry} of this transaction.
     * @param signer   {@link Signer} of this transaction.
     * @return Initialized {@link EncryptedTransferTransaction}.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     * @deprecated Encrypted transfers are deprecated and partially removed since protocol version 7
     */
    @Deprecated
    @Builder
    public static EncryptedTransferTransaction from(final EncryptedAmountTransferData data,
                                                    final AccountAddress receiver,
                                                    final AccountAddress sender,
                                                    final Nonce nonce,
                                                    final Expiry expiry,
                                                    final TransactionSigner signer) {
        try {
            return new EncryptedTransferTransaction(data, receiver, sender, nonce, expiry, signer);
        } catch (NullPointerException ex) {
            throw TransactionCreationException.from(ex);
        }
    }
}
