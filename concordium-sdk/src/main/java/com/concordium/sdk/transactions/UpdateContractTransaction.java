package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.*;

/**
 * Construct a transaction to update a smart contract instance.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UpdateContractTransaction extends AbstractAccountTransaction {
    private UpdateContractTransaction(
            @NonNull final UpdateContractPayload payload,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer,
            @NonNull final UInt64 maxEnergyCost) {
        super(sender, nonce, expiry, signer, UpdateContract.createNew(payload, maxEnergyCost));
    }

    private UpdateContractTransaction(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull UpdateContractPayload payload) {
        super(header,
                signature,
                TransactionType.DEPLOY_MODULE,
                payload.getBytes());
    }

    /**
     * Creates a new instance of {@link UpdateContractTransaction}.
     *
     * @param payload       {@link UpdateContractPayload} of the transaction.
     * @param sender        Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce         Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry        {@link Expiry} of this transaction.
     * @param signer        {@link Signer} of this transaction.
     * @param maxEnergyCost Allowed energy of the transaction.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder(builderClassName = "UpdateContractTransactionBuilder")
    public static UpdateContractTransaction from(
            final UpdateContractPayload payload,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer,
            final UInt64 maxEnergyCost) {
        try {
            return new UpdateContractTransaction(payload, sender, nonce, expiry, signer, maxEnergyCost);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }

    /**
     * Creates a new instance of {@link UpdateContractTransaction}.
     * Using {@link TransactionHeader}, {@link TransactionSignature} and Payload {@link UpdateContractPayload}.
     *
     * @param header    {@link TransactionHeader}.
     * @param signature {@link TransactionSignature}.
     * @param payload   {@link UpdateContractPayload} Payload for this transaction.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder(builderMethodName = "builderBlockItem", builderClassName = "UpdateContractBlockItemBuilder")
    public static UpdateContractTransaction from(
            final TransactionHeader header,
            final TransactionSignature signature,
            final UpdateContractPayload payload) {
        try {
            return new UpdateContractTransaction(header, signature, payload);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
