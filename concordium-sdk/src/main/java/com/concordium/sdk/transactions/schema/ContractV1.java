package com.concordium.sdk.transactions.schema;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
class ContractV1 extends Contract<ContractFunctionV1> {

    @Builder
    ContractV1(Optional<ContractFunctionV1> init, Map<String, ContractFunctionV1> receive) {
        super(init, receive);
    }
}
