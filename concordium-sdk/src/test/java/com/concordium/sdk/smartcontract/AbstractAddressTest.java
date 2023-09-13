package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.Schema;
import com.concordium.sdk.transactions.SchemaParameter;
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

public class AbstractAddressTest {
    static Schema SCHEMA;

    static {
        try {
            SCHEMA = Schema.from(Files.readAllBytes(Paths.get("./src/test/java/com/concordium/sdk/smartcontract/schema-unit-test.schema.bin")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldSerializeWithAccountAddress() {
        ReceiveName receiveName = ReceiveName.from("java_sdk_schema_unit_test", "abstract_address_test");
        AccountAddress address = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
        AbstractAddressTest.AbstractAddressContainer abstractAddressContainer = new AbstractAddressTest.AbstractAddressContainer(SCHEMA, receiveName, address);
        try {
            abstractAddressContainer.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldSerializeWithContractAddress() {
        ReceiveName receiveName = ReceiveName.from("java_sdk_schema_unit_test", "abstract_address_test");
        ContractAddress address = ContractAddress.from(1, 0);
        AbstractAddressTest.AbstractAddressContainer abstractAddressContainer = new AbstractAddressTest.AbstractAddressContainer(SCHEMA, receiveName, address);
        try {
            abstractAddressContainer.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    @Getter
    private static class AbstractAddressContainer extends SchemaParameter {

        @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
        private AbstractAddress address;

        public AbstractAddressContainer(Schema schema, ReceiveName receiveName, AbstractAddress address) {
            super(schema, receiveName);
            this.address = address;
        }
    }
}
