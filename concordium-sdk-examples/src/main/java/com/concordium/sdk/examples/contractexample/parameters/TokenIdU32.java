package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

@Getter
@JsonSerialize(using = TokenIdU32.TokenIdU32Serializer.class)
public class TokenIdU32 implements TokenId {

    private final UInt32 id;

    private TokenIdU32(UInt32 id) {
        this.id = id;
    }

    public static TokenIdU32 from(int value) {
        return new TokenIdU32(UInt32.from(value));
    }

    public static TokenIdU32 from(String value) {
        return new TokenIdU32(UInt32.from(value));
    }

    //TokenIdU32 is serialized as a hex of the uint32 value
    public static class TokenIdU32Serializer extends JsonSerializer<TokenIdU32> {
        @Override
        public void serialize(TokenIdU32 value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(Hex.encodeHexString(value.getId().getBytes()));
        }
    }
}
