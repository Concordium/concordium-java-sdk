package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * A {@link DeployModuleTransaction} deploys compiled WASM smart contract module to chain.
 */
@Getter
class DeployModuleTransaction extends AbstractTransaction {
    /**
     * A compiled Smart Contract Module in WASM with source and version.
     */
    private final WasmModule module;

    /**
     * Maximum energy **allowed** for the transaction to use.
     */
    private final UInt64 maxEnergyCost;

    @Builder
    public DeployModuleTransaction(
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer,
            final WasmModule module,
            final UInt64 maxEnergyCost) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(module)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Module cannot be null"));
        }
        this.module = module;
        this.maxEnergyCost = maxEnergyCost;
    }

    /**
     * This function returns the block item associated with this block.
     */
    @Override
    public BlockItem getBlockItem() {
        return DeployModule.createNew(getModule(), getMaxEnergyCost())
                .withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}
