package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = true)
public class DeployModule extends Payload {
    /**
     * A compiled Smart Contract Module in WASM with source and version.
     */
    private final WasmModule module;


    private DeployModule(@NonNull final WasmModule module) {
        this.module = module;
    }

    /**
     * It creates a new instance of the DeployModule class.
     *
     * @param module        The module to be deployed.
     * @return A new DeployModule object.
     */
    @Builder
    static DeployModule createNew(final WasmModule module) {
        return new DeployModule(module);
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.DEPLOY_MODULE;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        return module.getBytes();
    }
}
