package com.concordium.sdk.transactions;


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
    @Builder
    public InitContractTransaction(
            @NonNull final InitContractPayload payload,
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer,
            @NonNull final UInt64 maxEnergyCost) {
        super(sender, nonce, expiry, signer, InitContract.createNew(payload, maxEnergyCost));
    }
}
