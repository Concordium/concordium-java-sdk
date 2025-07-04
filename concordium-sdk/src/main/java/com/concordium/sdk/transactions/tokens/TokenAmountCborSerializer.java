package com.concordium.sdk.transactions.tokens;

import com.concordium.grpc.v2.plt.TokenAmount;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import lombok.val;

import java.io.IOException;

public class TokenAmountCborSerializer extends JsonSerializer<TokenAmount> {
    @Override
    public void serialize(TokenAmount tokenAmount,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        val cborGenerator = (CBORGenerator) jsonGenerator;
        cborGenerator.writeTag(4);
        cborGenerator.writeStartArray();
        cborGenerator.writeNumber(-tokenAmount.getDecimals());
        // TODO Handle unsigned
        cborGenerator.writeNumber(tokenAmount.getValue());
        cborGenerator.writeEndArray();
    }
}
