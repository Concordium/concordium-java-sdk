package com.concordium.sdk.transactions.tokens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.util.Optional;

@Builder
@Getter
@JsonSerialize(using = TransferTokenOperation.CborSerializer.class)
public class TransferTokenOperation {

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

    static class CborSerializer extends JsonSerializer<TransferTokenOperation> {

        @Override
        public void serialize(TransferTokenOperation operation,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            val cborGenerator = (CBORGenerator) jsonGenerator;
            cborGenerator.writeStartObject(operation, 1);
            cborGenerator.writeFieldName("transfer");
            cborGenerator.writeStartObject(operation, 3);
            cborGenerator.writeFieldId(1);
            cborGenerator.writeObject(operation.amount);
            cborGenerator.writeFieldId(2);
            cborGenerator.writeObject(operation.recipient);
            cborGenerator.writeFieldId(3);
            cborGenerator.writeObject(operation.memo);
            cborGenerator.writeEndObject();
            cborGenerator.writeEndObject();
        }
    }
}
