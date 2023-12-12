package com.concordium.sdk.examples.contractexample.parameters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

/**
 * Represents <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/1737a0d56d1a8fdb95045b00c58bd8fa3edd4308/concordium-cis2/src/lib.rs#L417C26-L417C26">TokenIdUnit</a> used in the cis2 <a href="https://github.com/Concordium/concordium-rust-smart-contracts/tree/main/examples">smart contract examples</a>.
 */
@Getter
@JsonSerialize(using = TokenIdUnit.TokenIdUnitSerializer.class)
public class TokenIdUnit implements TokenId {

    public TokenIdUnit() {}

    public static class TokenIdUnitSerializer extends JsonSerializer<TokenIdUnit> {
        @Override
        public void serialize(TokenIdUnit value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString("");
        }
    }
}
