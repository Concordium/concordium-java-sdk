package com.concordium.sdk.transactions.schema;

import java.util.Map;

class ModuleSchemaV2 extends ModuleSchema<ContractV2> {
    ModuleSchemaV2(final Map<String, ContractV2> contracts) {
        super(SchemaVersion.V2, contracts);
    }
}
