package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.transactions.TokenUpdate;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import lombok.val;

import java.io.IOException;

/**
 * A protocol-level token (PLT) operation used in {@link TokenUpdate}.
 * <p>
 * An operation must implement CBOR serialization writing its type and body.
 * And for the deserialization, it must only deserialize the body
 * as the type is read by {@link TokenOperation.CborDeserializer}.
 * <p>
 * To deserialize an operation from CBOR, always use <code>TokenOperation.class</code>
 * even if you know the exact type.
 *
 * @see TransferTokenOperation TransferTokenOperation as an example
 */
@JsonDeserialize(using = TokenOperation.CborDeserializer.class)
public interface TokenOperation {

    /**
     * @return the base energy cost of this operation.
     */
    UInt64 getBaseCost();

    class CborDeserializer extends JsonDeserializer<TokenOperation> {

        @Override
        public TokenOperation deserialize(JsonParser jsonParser,
                                          DeserializationContext ctxt) throws IOException, JacksonException {
            val cborParser = (CBORParser) jsonParser;

            val type = cborParser.nextFieldName();
            if (type == null) {
                throw new JsonParseException(
                        jsonParser,
                        "Missing operation type"
                );
            }

            cborParser.nextToken();

            switch (type) {
                case TransferTokenOperation.TYPE:
                    return cborParser.readValueAs(TransferTokenOperation.class);
                default:
                    throw new JsonParseException(
                            jsonParser,
                            "This operation is not supported: " + type
                    );
            }
        }
    }
}
