package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * A {@link DeployModuleTransaction} deploys compiled WASM smart contract module to chain.
 */
@Getter
public class DeployModuleTransaction extends AbstractTransaction {
    DeployModuleTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer,
            @NonNull final WasmModule module,
            @NonNull final UInt64 maxEnergyCost) {
        super(sender, nonce, expiry, signer, DeployModule.createNew(module, maxEnergyCost));
    }

    @Builder
    public static DeployModuleTransaction from(final AccountAddress sender,
                                               final AccountNonce nonce,
                                               final Expiry expiry,
                                               final TransactionSigner signer,
                                               final WasmModule module,
                                               final UInt64 maxEnergyCost) {
        try {
            return new DeployModuleTransaction(sender, nonce, expiry, signer, module, maxEnergyCost);
        } catch (NullPointerException nullPointerException) {
            throw TransactionCreationException.from(nullPointerException);
        }
    }
}
