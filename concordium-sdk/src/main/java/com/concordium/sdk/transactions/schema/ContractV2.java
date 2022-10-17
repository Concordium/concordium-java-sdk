package com.concordium.sdk.transactions.schema;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

/**
 * Describes all the schemas of a V1 smart contract with a V2 schema.
 */
@Getter
class ContractV2 extends Contract<ContractFunctionV2> {

    @Builder
    ContractV2(Optional<ContractFunctionV2> init, Map<String, ContractFunctionV2> receive) {
        super(init, receive);
    }
}
