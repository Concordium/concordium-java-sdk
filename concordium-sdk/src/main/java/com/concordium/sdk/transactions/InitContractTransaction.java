package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;


/**
 * Construct a transaction to initialise a smart contract.
 */
@Getter
public class InitContractTransaction extends AbstractAccountTransaction {

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
            final @NonNull InitContractPayload payload) {
        super(header,
                signature,
                TransactionType.INITIALIZE_SMART_CONTRACT_INSTANCE,
                payload.getBytes());
    }

    @Builder
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

    @Builder(builderMethodName = "builderBlockItem")
    public static InitContractTransaction from(
            final @NonNull TransactionHeader header,
            final @NonNull TransactionSignature signature,
            final @NonNull InitContractPayload payload) {
        try {
            return new InitContractTransaction(header, signature, payload);
        } catch (NullPointerException ex) {
            throw TransactionCreationException.from(ex);
        }
    }
}
