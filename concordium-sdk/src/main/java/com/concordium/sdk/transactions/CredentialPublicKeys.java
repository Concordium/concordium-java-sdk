package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.val;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The public credential keys belonging to a credential holder
 */
@Getter
@ToString
@NoArgsConstructor
@JsonDeserialize(using = CredentialPublicKeys.CredentialPublicKeysDeserializer.class)
public class CredentialPublicKeys {

    /**
     * Credential keys (i.e. account holder keys).
     */
    private Map<Index, ED25519PublicKey> keys;
    /**
     * The account threshold.
     */
    // @JsonProperty("recovcationThreshold")
    private int threshold;

    CredentialPublicKeys(Map<Index, ED25519PublicKey> keys,
                         int threshold) {
        this.keys = keys;
        this.threshold = threshold;
    }

    /**
     * Creates a new `CredentialPublicKeys` object with the given keys and threshold.
     *
     * @param keys      the credential keys (i.e. account holder keys).
     * @param threshold the account threshold.
     * @return a new `CredentialPublicKeys` object with the given keys and threshold.
     */
    public static CredentialPublicKeys from(Map<Index, ED25519PublicKey> keys, int threshold) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("Threshold must be greater than 0");
        }
        return new CredentialPublicKeys(keys, threshold);
    }

    byte[] getSchemeIdBytes() {
        val buffer = ByteBuffer.allocate(TransactionType.BYTES);
        buffer.put((byte) 0);
        return buffer.array();
    }

    @JsonIgnore
    public byte[] getBytes() {
        val keysLenBytes = keys.keySet().size();
        int keyBufferSize = TransactionType.BYTES;
        val schemeIdBytes = getSchemeIdBytes();
        for (Index key : keys.keySet()) {
            ED25519PublicKey value = keys.get(key);
            keyBufferSize += TransactionType.BYTES + value.getBytes().length + TransactionType.BYTES;
        }
        val buffer = ByteBuffer.allocate(
                keyBufferSize +
                        TransactionType.BYTES);
        buffer.put((byte) keysLenBytes);
        for (Index key : keys.keySet()) {
            ED25519PublicKey value = keys.get(key);
            buffer.put(schemeIdBytes);
            buffer.put(key.getValue());
            buffer.put(value.getBytes());
        }

        buffer.put((byte) threshold);
        return buffer.array();
    }

    @Getter
    static class LocalKey {
        private String verifyKey;
        private String schemeId;
    }

    @Getter
    static class Testing {
        private Map<Index, LocalKey> keys;
        private int threshold;
    }

    static class CredentialPublicKeysDeserializer extends JsonDeserializer<CredentialPublicKeys> {

        // TODO The deserializer should also be able to handle the normal serialization where the keys
        // are just direct hex strings.
        @Override
        public CredentialPublicKeys deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {

            Testing test = p.readValueAs(Testing.class);

            System.out.println("Inside deserializer");
            System.out.println(test.getThreshold());
            System.out.println(test.getKeys().get(Index.from(0)));

            Map<Index, ED25519PublicKey> keys = new HashMap<>();
            for (Entry<Index, LocalKey> key : test.keys.entrySet()) {
                keys.put(key.getKey(), ED25519PublicKey.from(key.getValue().getVerifyKey()));
            }

            return CredentialPublicKeys.from(keys, test.threshold);
        }
    }
}
