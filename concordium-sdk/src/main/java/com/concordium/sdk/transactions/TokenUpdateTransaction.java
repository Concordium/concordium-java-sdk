package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import lombok.*;

/**
 * A protocol-level token (PLT) transaction.
 * It is used to execute operations on a single PLT,
 * such as transferring, minting, etc.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TokenUpdateTransaction extends AccountTransaction {

    private TokenUpdateTransaction(
            @NonNull final TokenUpdate payload,
            @NonNull final AccountAddress sender,
            @NonNull final Nonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer) {
        super(
                sender,
                nonce,
                expiry,
                signer,
                payload,
                TransactionTypeCost.TOKEN_UPDATE_BASE_COST.getValue()
                                .plus(payload.getOperationsBaseCost())
        );
    }

    /**
     * Creates new {@link TokenUpdateTransaction}.
     *
     * @param payload Payload for this Transaction.
     * @param sender  Sender ({@link AccountAddress}) of this Transaction.
     * @param nonce   Nonce {@link Nonce} Of the Sender Account.
     * @param expiry  {@link Expiry} of this transaction.
     * @param signer  {@link Signer} of this transaction.
     * @return Initialized {@link TokenUpdateTransaction}
     * @throws TransactionCreationException On failure to create the Transaction from input params.
     *                                      Ex when any of the input param is NULL.
     */
    @Builder
    public static TokenUpdateTransaction from(final TokenUpdate payload,
                                              final AccountAddress sender,
                                              final Nonce nonce,
                                              final Expiry expiry,
                                              final TransactionSigner signer) {
        try {
            return new TokenUpdateTransaction(payload, sender, nonce, expiry, signer);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
