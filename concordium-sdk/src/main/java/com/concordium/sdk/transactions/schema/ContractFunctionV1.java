package com.concordium.sdk.transactions.schema;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Builder
@Getter
@RequiredArgsConstructor
class ContractFunctionV1 implements ContractFunction {
    private final Optional<SchemaType> parameter;
    private final Optional<SchemaType> returnValue;
}
