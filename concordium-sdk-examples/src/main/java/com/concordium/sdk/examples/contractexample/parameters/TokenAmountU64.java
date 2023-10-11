package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.IOException;

@Getter
@JsonSerialize(using = TokenAmountU64.TokenAmountU64Serializer.class)
public class TokenAmountU64 implements TokenAmount{

    private final UInt64 amount;

    private TokenAmountU64(UInt64 amount) {
        this.amount = amount;
    }

    public static TokenAmountU64 from(int value) {return new TokenAmountU64(UInt64.from(value));}

    public static class TokenAmountU64Serializer extends JsonSerializer<TokenAmountU64> {
        @Override
        public void serialize(TokenAmountU64 value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getAmount().toString());
        }
    }
}
