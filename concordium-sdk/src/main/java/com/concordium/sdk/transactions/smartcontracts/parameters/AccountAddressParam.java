package com.concordium.sdk.transactions.smartcontracts.parameters;

import com.concordium.sdk.transactions.InitName;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.IOException;

/**
 * Wrapper class allowing {@link AccountAddress} to be passed directly as a smart contract parameter.
 */
@Getter
@JsonSerialize(using = AccountAddressParam.AccountAddressParamSerializer.class)
public class AccountAddressParam extends SchemaParameter {
    private final AccountAddress accountAddress;

    public AccountAddressParam(Schema schema, InitName initName, AccountAddress accountAddress) {
        super(schema, initName);
        this.accountAddress = accountAddress;
    }

    public AccountAddressParam(Schema schema, ReceiveName receiveName, AccountAddress accountAddress) {
        super(schema, receiveName);
        this.accountAddress = accountAddress;
    }

    public static class AccountAddressParamSerializer extends JsonSerializer<AccountAddressParam> {
        @Override
        public void serialize(AccountAddressParam accountAddressParam, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
            jsonGenerator.writeObject(accountAddressParam.getAccountAddress());
        }
    }
}
