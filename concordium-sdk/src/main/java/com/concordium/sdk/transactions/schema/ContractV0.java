package com.concordium.sdk.transactions.schema;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
class ContractV0 extends Contract<ContractFunctionV0> {
    private final Optional<SchemaType> state;

    @Builder
    ContractV0(
            final Optional<ContractFunctionV0> init,
            final Map<String, ContractFunctionV0> receive,
            final Optional<SchemaType> state) {
        super(init, receive);
        this.state = state;
    }
}
