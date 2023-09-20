package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;
import com.concordium.sdk.types.ContractAddress;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Ensures correct serialization of {@link ListParam}.
 */
public class SerializeListParamTest {

    static Schema SCHEMA;
    static String CONTRACT_NAME = "java_sdk_schema_unit_test";

    static {
        try {
            SCHEMA = Schema.from(Files.readAllBytes(Paths.get("./src/test/testresources/smartcontractschema/unit-test.schema.bin")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldSerialize() {
        ReceiveName receiveName = ReceiveName.from(CONTRACT_NAME, "list_param_test");
        List<ContractAddress> list = new ArrayList<>();
        list.add(ContractAddress.from(1, 0));
        list.add(ContractAddress.from(2, 0));
        SchemaParameter parameter = new ContractListParam(SCHEMA, receiveName, list);
        try {
            parameter.initialize(true);
        } catch (Exception e) {
            fail();
        }
    }

    private static class ContractListParam extends ListParam {
        public ContractListParam(Schema schema, ReceiveName receiveName, List<ContractAddress> list) {
            super(schema, receiveName, list);
        }
    }
}
