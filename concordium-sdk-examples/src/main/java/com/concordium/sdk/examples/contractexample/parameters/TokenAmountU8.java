package com.concordium.sdk.examples.contractexample.parameters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.IOException;

@Getter
@JsonSerialize(using = TokenAmountU8.TokenAmountU8Serializer.class)
public class TokenAmountU8 implements TokenAmount {

    private final int amount;

    private TokenAmountU8(int amount) {
        this.amount = amount;
    }

    public static TokenAmountU8 from(int value) {
        if (value < 0) {
            throw new NumberFormatException("Value of TokenAmountU8 cannot be negative");
        }
        if (value > 255) {
            throw new NumberFormatException("Value of TokenAmountU8 cannot exceed 2^8");
        }
        return new TokenAmountU8(value);
    }

    public static class TokenAmountU8Serializer extends JsonSerializer<TokenAmountU8> {

        @Override
        public void serialize(TokenAmountU8 value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.valueOf(value.getAmount()));
        }
    }
}
