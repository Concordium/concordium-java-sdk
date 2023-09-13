package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.Schema;
import com.concordium.sdk.transactions.SchemaParameter;
import com.concordium.sdk.types.AccountAddress;
import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

public class AccountAddressTest {
    static Schema SCHEMA;

    static {
        try {
            SCHEMA = Schema.from(Files.readAllBytes(Paths.get("./src/test/java/com/concordium/sdk/smartcontract/schema-unit-test.schema.bin")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @SneakyThrows
    @Test
    public void shouldSerialize() {
        ReceiveName receiveName = ReceiveName.from("java_sdk_schema_unit_test","account_address_test" );
        AccountAddress address =AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
        AccountAddressTest.AccountAddressContainer accountAddressContainer = new AccountAddressTest.AccountAddressContainer(SCHEMA, receiveName, address);
        try {
            accountAddressContainer.initialize();
        }
        catch (Exception e) {
            fail();
        }
    }

    @Getter
    private static class AccountAddressContainer extends SchemaParameter {

        private AccountAddress address;

        public AccountAddressContainer(Schema schema, ReceiveName receiveName, AccountAddress address) {
            super(schema, receiveName);
            this.address = address;
        }
    }
}
