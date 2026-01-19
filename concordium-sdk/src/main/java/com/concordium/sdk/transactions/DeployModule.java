package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.smartcontracts.WasmModule;
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

    public DeployModule(@NonNull final WasmModule module) {
        super(TransactionType.DEPLOY_MODULE);
        this.module = module;
    }

    @Override
    protected byte[] getPayloadBytes() {
        return module.getBytes();
    }
}
