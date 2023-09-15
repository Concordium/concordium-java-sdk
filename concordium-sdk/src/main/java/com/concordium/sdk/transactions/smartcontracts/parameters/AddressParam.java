package com.concordium.sdk.transactions.smartcontracts.parameters;

import com.concordium.sdk.transactions.InitName;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.AbstractAddress;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.IOException;

/**
 * Wrapper class allowing {@link AbstractAddress} to be passed directly as a smart contract parameter.
 */
@JsonSerialize(using = AddressParam.AddressParamSerializer.class)
@Getter
public class AddressParam extends SchemaParameter {

    private final AbstractAddress address;

    public AddressParam(Schema schema, InitName initName, AbstractAddress address) {
        super(schema, initName);
        this.address = address;
    }

    public AddressParam(Schema schema, ReceiveName receiveName, AbstractAddress address) {
        super(schema, receiveName);
        this.address = address;
    }


    public static class AddressParamSerializer extends JsonSerializer<AddressParam> {
        @Override
        public void serialize(AddressParam addressParam, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            switch (addressParam.getAddress().getType()) {
                case ADDRESS_ACCOUNT:
                    jsonGenerator.writeFieldName("Account");

                    break;
                case ADDRESS_CONTRACT:
                    jsonGenerator.writeFieldName("Contract");
                    break;
            }
            jsonGenerator.writeStartArray();
            jsonGenerator.writeObject(addressParam.getAddress());
            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
        }
    }
}
