package com.concordium.sdk.transactions.schema;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
@Builder
class ContractFunctionV0 implements ContractFunction {
    private final Optional<SchemaType> parameter;

    @Override
    public Optional<SchemaType> getReturnValue() {
        return Optional.empty();
    }
}
