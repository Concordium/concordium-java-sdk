package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.transactions.smartcontracts.parameters.ContractAddressParam;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import lombok.Getter;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

/**
 * Ensures correct serialization of {@link ContractAddress}.
 * First test ensures correct serialization of parameters containing {@link AccountAddress}.
 * Last tests ensures correct serialization when {@link ContractAddress} is passed directly as a parameter using the wrapper {@link ContractAddressParam}.
 */
public class SerializeContractAddressTest {

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
    public void shouldSerializeContainer() {
        ReceiveName receiveName = ReceiveName.from(CONTRACT_NAME, "contract_address_container_test");
        ContractAddress contractAddress = ContractAddress.from(1, 0);
        ContractAddressContainer contractAddressContainer = new ContractAddressContainer(SCHEMA, receiveName, contractAddress);
        try {
            contractAddressContainer.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldSerialize() {
        ReceiveName receiveName = ReceiveName.from(CONTRACT_NAME, "contract_address_test");
        ContractAddress contractAddress = ContractAddress.from(1, 0);
        ContractAddressParam contractAddressParam = new ContractAddressParam(SCHEMA, receiveName, contractAddress);
        try {
            contractAddressParam.initialize();
        } catch (Exception e) {
            fail();
        }
    }


    /**
     * Parameter containing only a {@link ContractAddress}.
     */
    @Getter
    public static class ContractAddressContainer extends SchemaParameter {

        private final ContractAddress address;

        public ContractAddressContainer(Schema schema, ReceiveName receiveName, ContractAddress contractAddress) {
            super(schema, receiveName);
            this.address = contractAddress;
        }
    }
}
