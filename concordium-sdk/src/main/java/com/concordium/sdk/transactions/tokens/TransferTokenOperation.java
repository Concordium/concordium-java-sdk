package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.util.Optional;

@Getter
@Builder
@EqualsAndHashCode
@JsonSerialize(using = TransferTokenOperation.CborSerializer.class)
@JsonDeserialize(using = TransferTokenOperation.CborBodyDeserializer.class)
public class TransferTokenOperation implements TokenOperation {

    /**
     * Amount to be transferred.
     * It very important that the decimals in it match the actual value of the token.
     */
    private final TokenOperationAmount amount;

    /**
     * Recipient of the transfer.
     */
    private final TaggedTokenHolderAccount recipient;

    /**
     * Optional memo (message) to be included to the transfer,
     * which will be <b>publicly available</b> on the blockchain.
     */
    private final CborMemo memo;

    public Optional<CborMemo> getMemo() {
        return Optional.ofNullable(memo);
    }

    @Override
    public UInt64 getBaseCost() {
        return UInt64.from(100);
    }

    static class CborSerializer extends StdSerializer<TransferTokenOperation> {

        protected CborSerializer() {
            super(TransferTokenOperation.class);
        }

        @Override
        public void serialize(TransferTokenOperation value,
                              JsonGenerator gen,
                              SerializerProvider serializers) throws IOException {

            // Serializer for a token operation writes an object
            // having the only field, operation type,
            // with the value of the operation body.

            gen.writeStartObject(value, 1);
            gen.writeFieldName(TYPE);

            gen.writeStartObject(value, (value.memo != null) ? 3 : 2);
            gen.writeObjectField(AMOUNT_FIELD, value.amount);
            gen.writeObjectField(RECIPIENT_FIELD, value.recipient);
            if (value.memo != null) {
                gen.writeObjectField(MEMO_FIELD, value.memo);
            }
            gen.writeEndObject();

            gen.writeEndObject();
        }
    }

    static class CborBodyDeserializer extends StdDeserializer<TransferTokenOperation> {

        protected CborBodyDeserializer() {
            super(TransferTokenOperation.class);
        }

        @Override
        public TransferTokenOperation deserialize(JsonParser parser,
                                                  DeserializationContext ctxt) throws IOException {
            TokenOperationAmount amount = null;
            TaggedTokenHolderAccount recipient = null;
            CborMemo memo = null;

            // Deserializer for a token operation only reads the operation body.
            // It skips the type as at this moment it is already read
            // by TokenOperation deserializer.

            if (parser.currentToken() != JsonToken.START_OBJECT) {
                throw new JsonParseException(
                        parser,
                        "Expected START_OBJECT, but read " + parser.currentToken()
                );
            }

            while (parser.currentToken() != JsonToken.END_OBJECT) {
                if (parser.currentToken() == JsonToken.FIELD_NAME) {
                    val fieldName = parser.getText();
                    parser.nextToken();
                    switch (fieldName) {
                        case AMOUNT_FIELD:
                            amount = parser.readValueAs(TokenOperationAmount.class);
                            break;
                        case RECIPIENT_FIELD:
                            recipient = parser.readValueAs(TaggedTokenHolderAccount.class);
                            break;
                        case MEMO_FIELD:
                            memo = parser.readValueAs(CborMemo.class);
                            break;
                        default:
                            throw new JsonParseException(
                                    parser,
                                    "Unexpected field " + fieldName
                            );
                    }
                }
                parser.nextToken();
            }

            // Consume the END_OBJECT.
            parser.nextToken();

            if (amount == null) {
                throw new JsonParseException(
                        parser,
                        "Missing amount"
                );
            }

            if (recipient == null) {
                throw new JsonParseException(
                        parser,
                        "Missing recipient"
                );
            }

            return new TransferTokenOperation(amount, recipient, memo);
        }
    }

    static final String TYPE = "transfer";
    private static final String AMOUNT_FIELD = "amount";
    private static final String RECIPIENT_FIELD = "recipient";
    private static final String MEMO_FIELD = "memo";
}
