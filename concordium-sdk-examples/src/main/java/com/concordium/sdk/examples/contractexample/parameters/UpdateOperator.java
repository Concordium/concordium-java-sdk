package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

/**
 * Represents a '<a href = "https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/concordium-cis2/src/lib.rs#L1120">UpdateOperator</a>' used in different smart contracts
 */
@Getter
@AllArgsConstructor
public class UpdateOperator {

    private OperatorUpdate update;
    /**
     * The address which is either added or removed as an operator. An {@link AbstractAddress} is either an {@link AccountAddress} or a {@link ContractAddress}.
     * Fields of smart contract parameters containing {@link AbstractAddress} must be annotated with '@JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)'
     * to ensure correct serialization.
     */
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private AbstractAddress operator;


    @JsonSerialize(using = OperatorUpdate.OperatorUpdateSerializer.class)
    public enum OperatorUpdate {
        @JsonProperty("Remove")
        REMOVE,
        @JsonProperty("Add")
        ADD;

        public static class OperatorUpdateSerializer extends JsonSerializer<OperatorUpdate> {
            @Override
            public void serialize(OperatorUpdate operatorUpdate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeStartObject();
                switch (operatorUpdate) {
                    case REMOVE:
                        jsonGenerator.writeStringField("Remove", "");
                        break;
                    case ADD:
                        jsonGenerator.writeStringField("Add", "");
                        break;
                }
                jsonGenerator.writeEndObject();
            }
        }
    }

}
