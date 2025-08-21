package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.transactions.TokenUpdate;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import lombok.val;

import java.io.IOException;

/**
 * A protocol-level token (PLT) operation used in {@link TokenUpdate}.
 */
@JsonSerialize(using = TokenOperation.CborSerializer.class)
public interface TokenOperation {

    /**
     * @return operation type name, e.g. "transfer", "mint", etc.
     */
    String getType();

    /**
     * @return A CBOR-serializable (Jackson) operation body.
     */
    Object getBody();

    /**
     * @return the base energy cost of this operation.
     */
    UInt64 getBaseCost();

    class CborSerializer extends JsonSerializer<TokenOperation> {

        @Override
        public void serialize(TokenOperation tokenOperation,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            val cborGenerator = (CBORGenerator) jsonGenerator;
            cborGenerator.writeStartObject(tokenOperation, 1);
            cborGenerator.writeFieldName(tokenOperation.getType());
            cborGenerator.writeObject(tokenOperation.getBody());
            cborGenerator.writeEndObject();
        }
    }
}
