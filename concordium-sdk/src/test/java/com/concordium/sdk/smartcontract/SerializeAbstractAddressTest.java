package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.transactions.smartcontracts.parameters.AddressParam;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

/**
 * Ensures correct serialization of {@link AbstractAddress} when annotated correctly.
 * First 2 tests ensures correct serialization of parameters containing {@link AbstractAddress}.
 * Last 2 tests ensures correct serialization when {@link AbstractAddress} is passed directly as a parameter using the wrapper {@link AddressParam}.
 */
public class SerializeAbstractAddressTest {
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
    public void shouldSerializeContainerWithAccountAddress() {
        ReceiveName receiveName = ReceiveName.from(CONTRACT_NAME, "abstract_address_container_test");
        AccountAddress address = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
        AbstractAddressContainer abstractAddressContainer = new AbstractAddressContainer(SCHEMA, receiveName, address);
        try {
            abstractAddressContainer.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldSerializeContainerWithContractAddress() {
        ReceiveName receiveName = ReceiveName.from(CONTRACT_NAME, "abstract_address_container_test");
        ContractAddress address = ContractAddress.from(1, 0);
        AbstractAddressContainer abstractAddressContainer = new AbstractAddressContainer(SCHEMA, receiveName, address);
        try {
            abstractAddressContainer.initialize();
        } catch (Exception e) {
            fail();
        }
    }



    @Test
    public void shouldSerializeWithAccountAddress() {
        ReceiveName receiveName = ReceiveName.from(CONTRACT_NAME, "abstract_address_test");
        AccountAddress address = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
        AddressParam addressParam = new AddressParam(SCHEMA, receiveName, address);
        try {
            addressParam.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldSerializeWithContractAddress() {
        ReceiveName receiveName = ReceiveName.from(CONTRACT_NAME, "abstract_address_test");
        ContractAddress address = ContractAddress.from(1, 0);
        AddressParam addressParam = new AddressParam(SCHEMA, receiveName, address);
        try {
            addressParam.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Parameter containing only a {@link AbstractAddress}.
     */
    @Getter
    private static class AbstractAddressContainer extends SchemaParameter {

        @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
        private final AbstractAddress address;

        public AbstractAddressContainer(Schema schema, ReceiveName receiveName, AbstractAddress address) {
            super(schema, receiveName);
            this.address = address;
        }
    }
}
