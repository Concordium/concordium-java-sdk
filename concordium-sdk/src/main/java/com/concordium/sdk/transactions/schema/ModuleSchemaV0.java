package com.concordium.sdk.transactions.schema;

import java.util.Map;

class ModuleSchemaV0 extends ModuleSchema<ContractV0> {
    public ModuleSchemaV0(final Map<String, ContractV0> contracts) {
        super(SchemaVersion.V0, contracts);
    }
}
