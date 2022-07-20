package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeployModule extends Payload {

    private final WasmModule module;

    @Builder
    private DeployModule(WasmModule module) {
        this.module = module;
    }

    static DeployModule createNew(WasmModule module) {
        return new DeployModule(module);
    }

    @Override
    public PayloadType getType() {
        return PayloadType.DEPLOY_MODULE;
    }

    @Override
    byte[] getBytes() {
        return module.getBytes();
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return this.module.getCost();
    }
}
