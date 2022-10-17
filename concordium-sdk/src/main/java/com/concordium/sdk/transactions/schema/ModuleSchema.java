package com.concordium.sdk.transactions.schema;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public abstract class ModuleSchema<TContract extends Contract> {

    /**
     * Schema Version.
     */
    private final SchemaVersion version;

    /**
     * Contract in the schema.
     */
    private final Map<String, TContract> contracts;

    /**
     * Gets a single contract by name. Or null of contract does not exist.
     *
     * @param contractName Name of the contract in Module.
     * @return Parsed {@link Contract}
     */
    public TContract getContract(String contractName) {
        return contracts.get(contractName);
    }
}
