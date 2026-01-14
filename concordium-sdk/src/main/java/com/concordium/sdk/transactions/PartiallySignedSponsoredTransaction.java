package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
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
 * can be completed by the remaining side. JSON-serializable.
 *
 * @see TransactionFactory Transaction factory, that also supports sponsored transactions
 * @see PartiallySignedSponsoredTransaction#builderForSender() Build on the sender side, to be completed by the sponsor
 * @see PartiallySignedSponsoredTransaction#builderForSponsor() Build on the sponsor side, to be completed by the sender
 * @see PartiallySignedSponsoredTransaction#builderForCompletion() Build on the side that completes the transaction
 * @see PartiallySignedSponsoredTransaction#complete(TransactionSigner) Complete with the remaining signer
 * @see PartiallySignedSponsoredTransaction#complete(TransactionSignature) Complete with the remaining signature
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

    /**
     * Creates a partially signed sponsored transaction on the completion side
     * (i.e. when received by sender from sponsor or vice versa)
     *
     * @param senderSignature  signature of the transaction sender,
     *                         which means the transaction must be completed by the sponsor
     * @param sponsorSignature signature of the transaction sponsor,
     *                         which means the transaction must be completed by the sender
     * @param header           {@link AccountTransactionV1#getHeader() AccountTransactionV1 header}
     * @param payload          {@link AccountTransactionV1#getPayload() AccountTransactionV1 payload}
     * @throws TransactionCreationException if something goes wrong
     * @see PartiallySignedSponsoredTransaction#complete(TransactionSigner) Complete with the remaining signer
     * @see PartiallySignedSponsoredTransaction#complete(TransactionSignature) Complete with the remaining signature
     */
    @Builder(
            builderMethodName = "builderForCompletion",
            builderClassName = "PartiallySignedSponsoredTransactionBuilderForCompletion"
    )
    public PartiallySignedSponsoredTransaction(@Nullable TransactionSignature senderSignature,
                                               @Nullable TransactionSignature sponsorSignature,
                                               @NonNull TransactionHeaderV1 header,
                                               @NonNull Payload payload) {
        if (sponsorSignature == null && senderSignature == null) {
            throw TransactionCreationException.from(new IllegalArgumentException(
                    "There must be either sponsor or sender signature"
            ));
        }
        this.senderSignature = senderSignature;
        this.sponsorSignature = sponsorSignature;
        this.header = header;
        this.payload = new RawPayload(payload.getBytes());
    }

    public Optional<TransactionSignature> getSenderSignature() {
        return Optional.ofNullable(senderSignature);
    }

    public Optional<TransactionSignature> getSponsorSignature() {
        return Optional.ofNullable(sponsorSignature);
    }

    public AccountTransactionV1 complete(@NonNull TransactionSignature remainingSignature) {
        return AccountTransactionV1.from(this, remainingSignature);
    }

    public AccountTransactionV1 complete(@NonNull TransactionSigner remainingSigner) {
        return AccountTransactionV1.from(this, remainingSigner);
    }

    /**
     * Creates a partially signed sponsored transaction on the sponsor side.
     * The transaction then needs to be completed by the sender.
     *
     * @param sender                  The address of the account that is the source of the transaction.
     * @param nonce                   The sequence number of the transaction, sender (source) account nonce.
     * @param expiry                  A Unix timestamp indicating when the transaction should expire.
     * @param senderSignatureCount    Expected number of signatures by the sender, in a Singlesig wallet it is 1
     * @param payload                 Transaction payload, defines what this transaction does.
     * @param transactionSpecificCost Cost of executing this specific payload.
     * @param sponsor                 The address of the account that sponsors the transaction (pays the cost).
     * @param sponsorSigner           Signer of the sponsor.
     * @return partially signed sponsored transaction that can be completed by the sender
     * @throws TransactionCreationException if something goes wrong
     */
    @Builder(
            builderMethodName = "builderForSponsor",
            builderClassName = "PartiallySignedSponsoredTransactionBuilderForSponsor"
    )
    public static PartiallySignedSponsoredTransaction from(@NonNull AccountAddress sender,
                                                           @NonNull Nonce nonce,
                                                           @NonNull Expiry expiry,
                                                           @NonNull Integer senderSignatureCount,
                                                           @NonNull Payload payload,
                                                           @NonNull UInt64 transactionSpecificCost,
                                                           @NonNull AccountAddress sponsor,
                                                           @NonNull TransactionSigner sponsorSigner) {
        try {
            val payloadBytes = payload.getBytes();
            val rawPayload = new RawPayload(payloadBytes);
            val header = TransactionHeaderV1
                    .builder()
                    .sender(sender)
                    .nonce(nonce)
                    .expiry(expiry)
                    .payloadSize(UInt32.from(payloadBytes.length))
                    .sponsor(sponsor)
                    .maxEnergyCost(
                            TransactionHeaderV1.calculateMaxEnergyCost(
                                    senderSignatureCount,
                                    sponsorSigner.size(),
                                    payloadBytes.length,
                                    transactionSpecificCost
                            )
                    )
                    .build();
            val sponsorSignature = sponsorSigner.sign(AccountTransactionV1.getDataToSign(header, rawPayload));
            return new PartiallySignedSponsoredTransaction(
                    null,
                    sponsorSignature,
                    header,
                    rawPayload
            );
        } catch (Exception e) {
            throw TransactionCreationException.from(e);
        }
    }

    /**
     * Creates a partially signed sponsored transaction on the sender side.
     * The transaction then needs to be completed by the sponsor.
     *
     * @param sender                  The address of the account that is the source of the transaction.
     * @param nonce                   The sequence number of the transaction, sender (source) account nonce.
     * @param expiry                  A Unix timestamp indicating when the transaction should expire.
     * @param senderSigner            Signer of the sender.
     * @param payload                 Transaction payload, defines what this transaction does.
     * @param transactionSpecificCost Cost of executing this specific payload.
     * @param sponsor                 The address of the account that sponsors the transaction (pays the cost).
     * @param sponsorSignatureCount   Expected number of signatures by the sponsor, in a Singlesig wallet it is 1
     * @return partially signed sponsored transaction that can be completed by the sender
     * @throws TransactionCreationException if something goes wrong
     */
    @Builder(
            builderMethodName = "builderForSender",
            builderClassName = "PartiallySignedSponsoredTransactionBuilderForSender"
    )
    public static PartiallySignedSponsoredTransaction from(@NonNull AccountAddress sender,
                                                           @NonNull Nonce nonce,
                                                           @NonNull Expiry expiry,
                                                           @NonNull TransactionSigner senderSigner,
                                                           @NonNull Payload payload,
                                                           @NonNull UInt64 transactionSpecificCost,
                                                           @NonNull AccountAddress sponsor,
                                                           @NonNull Integer sponsorSignatureCount) {
        try {
            val payloadBytes = payload.getBytes();
            val rawPayload = new RawPayload(payloadBytes);
            val header = TransactionHeaderV1
                    .builder()
                    .sender(sender)
                    .nonce(nonce)
                    .expiry(expiry)
                    .payloadSize(UInt32.from(payloadBytes.length))
                    .sponsor(sponsor)
                    .maxEnergyCost(
                            TransactionHeaderV1.calculateMaxEnergyCost(
                                    senderSigner.size(),
                                    sponsorSignatureCount,
                                    payloadBytes.length,
                                    transactionSpecificCost
                            )
                    )
                    .build();
            val senderSignature = senderSigner.sign(AccountTransactionV1.getDataToSign(header, rawPayload));
            return new PartiallySignedSponsoredTransaction(
                    senderSignature,
                    null,
                    header,
                    rawPayload
            );
        } catch (Exception e) {
            throw TransactionCreationException.from(e);
        }
    }


    public static PartiallySignedSponsoredTransaction from(@Nullable TransactionSignature senderSignature,
                                                           @Nullable TransactionSignature sponsorSignature,
                                                           @NonNull TransactionHeaderV1 header,
                                                           @NonNull Payload payload) {
        if (sponsorSignature == null && senderSignature == null) {
            throw TransactionCreationException.from(new IllegalArgumentException(
                    "There must be either sponsor or sender signature"
            ));
        }
        return new PartiallySignedSponsoredTransaction(
                senderSignature,
                sponsorSignature,
                header,
                new RawPayload(payload.getBytes())
        );
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
