package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.transactions.smartcontracts.parameters.AccountAddressParam;
import com.concordium.sdk.types.AccountAddress;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Ensures correct retrieval of {@link Schema} from {@link WasmModule} if embedded.
 */
public class GetSchemaTest {

    static WasmModule MODULE_WITH_SCHEMA;

    static WasmModule MODULE_WITHOUT_SCHEMA;

    static {
        try {
            MODULE_WITH_SCHEMA = WasmModule.from("./src/test/testresources/smartcontractschema/unit-test-with-schema.wasm");
            MODULE_WITHOUT_SCHEMA = WasmModule.from("./src/test/testresources/smartcontractschema/unit-test.wasm");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldFindSchema() {
        Optional<Schema> optionalSchema = MODULE_WITH_SCHEMA.getSchema();
        assert(optionalSchema.isPresent());
        Schema schema = optionalSchema.get();
        ReceiveName receiveName = ReceiveName.from("java_sdk_schema_unit_test", "account_address_test");
        AccountAddress address = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
        AccountAddressParam accountAddressParam = new AccountAddressParam(schema, receiveName, address);
        try {
            // Asserts that the extracted Schema is actually a valid Schema.
            accountAddressParam.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldNotFindSchema() {
        Optional<Schema> optionalSchema = MODULE_WITHOUT_SCHEMA.getSchema();
        assertEquals(optionalSchema, Optional.empty());
    }


}
