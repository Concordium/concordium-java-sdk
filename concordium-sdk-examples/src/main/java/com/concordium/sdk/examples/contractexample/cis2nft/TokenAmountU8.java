package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.types.UInt8;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.IOException;

@Getter
@JsonSerialize(using = TokenAmountU8.TokenAmountU8Serializer.class)
public class TokenAmountU8 implements TokenAmount {

    private final UInt8 amount;

    private TokenAmountU8(UInt8 amount) {
        this.amount = amount;
    }

    public static TokenAmountU8 from(int value) {
        return new TokenAmountU8(UInt8.from(value));
    }

    public static class TokenAmountU8Serializer extends JsonSerializer<TokenAmountU8> {

        @Override
        public void serialize(TokenAmountU8 value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getAmount().toString());
        }
    }
}
