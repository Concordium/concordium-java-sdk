package com.concordium.sdk.examples.contractexample.cis2nft;

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
        //if (Integer.toString(value).length() % 2 != 0) {throw new IllegalArgumentException("TokenIdU32 must be odd number of digits");}
        return new TokenIdU32(UInt32.from(value));
    }

    public static class TokenIdU32Serializer extends JsonSerializer<TokenIdU32> {
        @Override
        public void serialize(TokenIdU32 value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(Hex.encodeHexString(value.getId().getBytes()));
        }
    }
}
