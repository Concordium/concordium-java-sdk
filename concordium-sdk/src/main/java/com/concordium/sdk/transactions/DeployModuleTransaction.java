package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * A {@link DeployModuleTransaction} deploys compiled WASM smart contract module to chain.
 */
@Getter
class DeployModuleTransaction extends AbstractAccountTransaction {
    @Builder
    public DeployModuleTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer,
            @NonNull final WasmModule module,
            @NonNull final UInt64 maxEnergyCost) {
        super(sender, nonce, expiry, signer, DeployModule.createNew(module, maxEnergyCost));
    }
}
