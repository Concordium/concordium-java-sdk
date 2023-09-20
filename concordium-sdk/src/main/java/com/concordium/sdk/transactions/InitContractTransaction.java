package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;


/**
 * Construct a transaction to initialise a smart contract.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class InitContractTransaction extends AccountTransaction {

    /**
     * A constructor of {@link InitContractTransaction} class.
     */
    private InitContractTransaction(
            @NonNull final InitContractPayload payload,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer,
            @NonNull final UInt64 maxEnergyCost) {
        super(sender, nonce, expiry, signer, InitContract.createNew(payload, maxEnergyCost));
    }

    private InitContractTransaction(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull InitContractPayload payload,
            final @NonNull UInt64 maxEnergyCost) {
        super(header,
                signature,
                InitContract.createNew(payload, maxEnergyCost));
    }

    /**
     * @param payload       {@link InitContractPayload} for the transaction
     * @param sender        Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce         Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry        {@link Expiry} of this transaction.
     * @param signer        {@link Signer} of this transaction.
     * @param maxEnergyCost Allowed energy for the transaction.
     * @return Initialized {@link InitContractTransaction}.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder(builderClassName = "InitContractTransactionBuilder")
    public static InitContractTransaction from(
            final InitContractPayload payload,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer,
            final UInt64 maxEnergyCost) {
        try {
            return new InitContractTransaction(payload, sender, nonce, expiry, signer, maxEnergyCost);
        } catch (NullPointerException ex) {
            throw TransactionCreationException.from(ex);
        }
    }

    /**
     * Creates a new instance of {@link InitContractTransaction}.
     * Using {@link TransactionHeader}, {@link TransactionSignature} and Payload {@link InitContractPayload}.
     *
     * @param header    {@link TransactionHeader}.
     * @param signature {@link TransactionSignature}.
     * @param payload   {@link InitContractPayload} Payload for this transaction.
     * @return Instantiated {@link InitContractTransaction}.
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder(builderMethodName = "builderBlockItem", builderClassName = "InitContractBlockItemBuilder")
    public static InitContractTransaction from(
            final TransactionHeader header,
            final TransactionSignature signature,
            final InitContractPayload payload,
            final UInt64 maxEnergyCost) {
        try {
            return new InitContractTransaction(header, signature, payload, maxEnergyCost);
        } catch (NullPointerException ex) {
            throw TransactionCreationException.from(ex);
        }
    }
}
