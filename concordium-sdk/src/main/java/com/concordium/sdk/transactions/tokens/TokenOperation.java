package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.transactions.TokenUpdate;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import lombok.val;

import java.io.IOException;

/**
 * A protocol-level token (PLT) operation used in {@link TokenUpdate}.
 * <p>
 * An operation must implement CBOR serialization writing its type and body.
 * And for the deserialization, only the body must be deserialized
 * as the type is being read by {@link TokenOperation.CborDeserializer}.
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

    class CborDeserializer extends StdDeserializer<TokenOperation> {

        protected CborDeserializer() {
            super(TokenOperation.class);
        }

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

            TokenOperation parsedOperation;
            switch (type) {
                case TransferTokenOperation.TYPE:
                    parsedOperation = cborParser.readValueAs(TransferTokenOperation.class);
                    break;
                default:
                    throw new JsonParseException(
                            jsonParser,
                            "This operation is not supported: " + type
                    );
            }

            if (cborParser.nextToken() != JsonToken.END_OBJECT) {
                throw new JsonParseException(
                        jsonParser,
                        "Expected END_OBJECT, but read " + cborParser.currentToken()
                );
            }

            return parsedOperation;
        }
    }
}
