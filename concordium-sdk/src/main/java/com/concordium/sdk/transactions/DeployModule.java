package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DeployModule extends Payload {
    /**
     * A compiled Smart Contract Module in WASM with source and version.
     */
    private final WasmModule module;

    private final UInt64 maxEnergyCost;

    private DeployModule(@NonNull final WasmModule module, @NonNull final UInt64 maxEnergyCost) {
        this.module = module;
        this.maxEnergyCost = maxEnergyCost;
    }

    /**
     * It creates a new instance of the DeployModule class.
     *
     * @param module        The module to be deployed.
     * @param maxEnergyCost The maximum amount of energy that can be consumed by the contract.
     * @return A new DeployModule object.
     */
    @Builder
    static DeployModule createNew(final WasmModule module, final UInt64 maxEnergyCost) {
        return new DeployModule(module, maxEnergyCost);
    }

    @Override
    protected UInt64 getTransactionTypeCost() {
        return this.maxEnergyCost;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.DEPLOY_MODULE;
    }

    @Override
    public byte[] getRawPayloadBytes() {
        return module.getBytes();
    }
}
