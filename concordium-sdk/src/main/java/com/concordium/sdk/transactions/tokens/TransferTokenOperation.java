package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.io.IOException;
import java.util.Optional;

@Getter
@Jacksonized
@Builder
@EqualsAndHashCode
@JsonSerialize(
        using = TransferTokenOperation.CborSerializer.class
        // And deserialization works via @Jacksonized
        // as it is a simple key-value object without tags, numbered fields, etc.
)
public class TransferTokenOperation implements TokenOperation {

    /**
     * Amount to be transferred.
     * It very important that the decimals in it match the actual value of the token.
     */
    @NonNull
    @JsonProperty(AMOUNT_FIELD)
    private final TokenOperationAmount amount;

    /**
     * Recipient of the transfer.
     */
    @NonNull
    @JsonProperty(RECIPIENT_FIELD)
    private final TaggedTokenHolderAccount recipient;

    /**
     * Optional memo (message) to be included to the transfer,
     * which will be <b>publicly available</b> on the blockchain.
     */
    @JsonProperty(MEMO_FIELD)
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

    static final String TYPE = "transfer";
    private static final String AMOUNT_FIELD = "amount";
    private static final String RECIPIENT_FIELD = "recipient";
    private static final String MEMO_FIELD = "memo";
}
