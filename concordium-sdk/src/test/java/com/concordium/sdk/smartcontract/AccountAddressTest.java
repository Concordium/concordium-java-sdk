package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.transactions.smartcontracts.parameters.AccountAddressParam;
import com.concordium.sdk.types.AccountAddress;
import lombok.Getter;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

/**
 * Ensures correct serialization of {@link AccountAddress}.
 * First test ensures correct serialization of parameters containing {@link AccountAddress}.
 * Last tests ensures correct serialization when {@link AccountAddress} is passed directly as a parameter using the wrapper {@link AccountAddressParam}.
 */
public class AccountAddressTest {
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
        ReceiveName receiveName = ReceiveName.from(CONTRACT_NAME, "account_address_container_test");
        AccountAddress address = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
        AccountAddressContainer accountAddressContainer = new AccountAddressContainer(SCHEMA, receiveName, address);
        try {
            accountAddressContainer.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldSerialize() {
        ReceiveName receiveName = ReceiveName.from(CONTRACT_NAME, "account_address_test");
        AccountAddress address = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
        AccountAddressParam accountAddressParam = new AccountAddressParam(SCHEMA, receiveName, address);
        try {
            accountAddressParam.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Parameter containing only a {@link AccountAddress}.
     */
    @Getter
    private static class AccountAddressContainer extends SchemaParameter {

        private final AccountAddress address;

        public AccountAddressContainer(Schema schema, ReceiveName receiveName, AccountAddress address) {
            super(schema, receiveName);
            this.address = address;
        }
    }
}
