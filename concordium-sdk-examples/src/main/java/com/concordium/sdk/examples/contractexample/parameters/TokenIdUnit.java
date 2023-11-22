package com.concordium.sdk.examples.contractexample.parameters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

@Getter
@JsonSerialize(using = TokenIdUnit.TokenIdUnitSerializer.class)
public class TokenIdUnit implements TokenId {

    private final byte id;

    public TokenIdUnit() {
        id = 0;
    }

    public static class TokenIdUnitSerializer extends JsonSerializer<TokenIdUnit> {
        @Override
        public void serialize(TokenIdUnit value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString("");
        }
    }
}
