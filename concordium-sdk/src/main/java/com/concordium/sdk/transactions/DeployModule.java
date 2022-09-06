package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeployModule extends Payload {

    private final WasmModule module;

    private final UInt64 maxEnergyCost;

    @Builder
    private DeployModule(WasmModule module, UInt64 maxEnergyCost) {
        this.module = module;
        this.maxEnergyCost = maxEnergyCost;
    }

    static DeployModule createNew(WasmModule module, UInt64 maxEnergyCost) {
        return new DeployModule(module, maxEnergyCost);
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
        return this.maxEnergyCost;
    }
}
