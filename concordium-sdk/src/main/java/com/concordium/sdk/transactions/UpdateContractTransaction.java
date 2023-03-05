package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Construct a transaction to update a smart contract instance.
 */
@Getter
public class UpdateContractTransaction extends AbstractTransaction {
    @Builder
    public UpdateContractTransaction(
            @NonNull final UpdateContractPayload payload,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer,
            @NonNull final UInt64 maxEnergyCost) {
        super(sender, nonce, expiry, signer, UpdateContract.createNew(payload, maxEnergyCost));
    }
}
