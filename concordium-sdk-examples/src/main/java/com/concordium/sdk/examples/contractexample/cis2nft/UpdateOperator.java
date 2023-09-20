package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.types.AbstractAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public class UpdateOperator {

    private OperatorUpdate update;
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
