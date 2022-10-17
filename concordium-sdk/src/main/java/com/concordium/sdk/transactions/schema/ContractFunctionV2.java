package com.concordium.sdk.transactions.schema;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Builder
@Getter
@ToString
class ContractFunctionV2 implements ContractFunction {
    private final Optional<SchemaType> parameter;
    private final Optional<SchemaType> returnValue;
    private final Optional<SchemaType> error;
}
