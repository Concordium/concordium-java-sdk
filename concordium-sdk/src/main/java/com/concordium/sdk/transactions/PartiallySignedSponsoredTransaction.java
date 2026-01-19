package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.*;
import org.bouncycastle.util.encoders.Hex;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * A sponsored transaction ({@link AccountTransactionV1}) signed by either sender or sponsor,
 * must be completed by the other side. JSON-serializable.
 * If a transaction is signed by the sender, it must be completed by the sponsor.
 * If a transaction is signed by the sponsor, it must be completed by the sender.
 *
 * @see TransactionFactory Transaction factory, that lets create and complete sponsored transactions
 */
@Getter
@ToString
@EqualsAndHashCode
@JsonSerialize(using = PartiallySignedSponsoredTransaction.Serializer.class)
@JsonDeserialize(using = PartiallySignedSponsoredTransaction.Deserializer.class)
public class PartiallySignedSponsoredTransaction {
    @Nullable
    private final TransactionSignature senderSignature;
    @Nullable
    private final TransactionSignature sponsorSignature;
    @NonNull
    private final TransactionHeaderV1 header;
    @NonNull
    private final RawPayload payload;

    public PartiallySignedSponsoredTransaction(@Nullable TransactionSignature senderSignature,
                                               @Nullable TransactionSignature sponsorSignature,
                                               @NonNull TransactionHeaderV1 header,
                                               @NonNull Payload payload) {
        if (sponsorSignature == null && senderSignature == null) {
            throw TransactionCreationException.from(new IllegalArgumentException(
                    "There must be either sponsor or sender signature"
            ));
        } else if (sponsorSignature != null && senderSignature != null) {
            throw TransactionCreationException.from(new IllegalArgumentException(
                    "If there are both signatures, create AccountTransactionV1 instead"
            ));
        }
        this.senderSignature = senderSignature;
        this.sponsorSignature = sponsorSignature;
        this.header = header;
        this.payload = new RawPayload(payload.getBytes());
    }

    @SuppressWarnings("unused")
    public Optional<TransactionSignature> getSenderSignature() {
        return Optional.ofNullable(senderSignature);
    }

    @SuppressWarnings("unused")
    public Optional<TransactionSignature> getSponsorSignature() {
        return Optional.ofNullable(sponsorSignature);
    }

    static class Serializer extends StdSerializer<PartiallySignedSponsoredTransaction> {
        protected Serializer() {
            super(PartiallySignedSponsoredTransaction.class);
        }

        @Override
        public void serialize(PartiallySignedSponsoredTransaction tx,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            if (tx.senderSignature != null) {
                jsonGenerator.writeObjectField(
                        "senderSignature",
                        Hex.toHexString(tx.senderSignature.getBytes())
                );
            }
            if (tx.sponsorSignature != null) {
                jsonGenerator.writeObjectField(
                        "sponsorSignature",
                        Hex.toHexString(tx.sponsorSignature.getBytes())
                );
            }
            jsonGenerator.writeObjectField("header", Hex.toHexString(tx.header.getBytes()));
            jsonGenerator.writeObjectField("payload", Hex.toHexString(tx.payload.getBytes()));
            jsonGenerator.writeEndObject();
        }
    }

    static class Deserializer extends StdDeserializer<PartiallySignedSponsoredTransaction> {
        protected Deserializer() {
            super(PartiallySignedSponsoredTransaction.class);
        }

        @Override
        public PartiallySignedSponsoredTransaction deserialize(JsonParser jsonParser,
                                                               DeserializationContext deserializationContext) throws IOException {
            val node = jsonParser.getCodec().readTree(jsonParser);
            if (!node.isObject()) {
                throw new JsonParseException(jsonParser, "Instantiation of PartiallySignedSponsoredTransaction failed due to invalid structure");
            }
            val objectNode = (ObjectNode) node;
            return new PartiallySignedSponsoredTransaction(
                    (objectNode.has("senderSignature"))
                            ? TransactionSignature.fromBytes(ByteBuffer.wrap(Hex.decode(objectNode.get("senderSignature").asText())))
                            : null,
                    (objectNode.has("sponsorSignature"))
                            ? TransactionSignature.fromBytes(ByteBuffer.wrap(Hex.decode(objectNode.get("sponsorSignature").asText())))
                            : null,
                    TransactionHeaderV1.fromBytes(ByteBuffer.wrap(Hex.decode(objectNode.get("header").asText()))),
                    new RawPayload(Hex.decode(objectNode.get("payload").asText()))
            );
        }
    }
}
