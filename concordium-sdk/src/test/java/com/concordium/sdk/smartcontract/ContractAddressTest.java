package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.Schema;
import com.concordium.sdk.transactions.SchemaParameter;
import com.concordium.sdk.types.ContractAddress;
import lombok.Getter;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

/**
 * Ensures correct serialization of {@link ContractAddress}.
 */
public class ContractAddressTest {

    static Schema SCHEMA;

    static {
        try {
            SCHEMA = Schema.from(Files.readAllBytes(Paths.get("./src/test/java/com/concordium/sdk/smartcontract/unit-test.schema.bin")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldSerialize() {
        ReceiveName receiveName = ReceiveName.from("java_sdk_schema_unit_test", "contract_address_test");
        ContractAddress contractAddress = ContractAddress.from(1, 0);
        ContractAddressParameter contractAddressContainer = new ContractAddressParameter(SCHEMA, receiveName, contractAddress);
        try {
            contractAddressContainer.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Parameter containing only a ContractAddress. Is to ensure proper serialization of ContractAddress.
     */
    @Getter
    public static class ContractAddressParameter extends SchemaParameter {

        private final ContractAddress address;

        public ContractAddressParameter(Schema schema, ReceiveName receiveName, ContractAddress contractAddress) {
            super(schema, receiveName);
            this.address = contractAddress;
        }
    }
}
