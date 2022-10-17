package com.concordium.sdk.transactions.schema;

import java.util.Map;

class ModuleSchemaV1 extends ModuleSchema<ContractV1> {
    public ModuleSchemaV1(final Map<String, ContractV1> contracts) {
        super(SchemaVersion.V1, contracts);
    }
}
