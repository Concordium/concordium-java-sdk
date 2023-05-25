package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.concordium.grpc.v2.UpdatePublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * Root or level1 keys.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public final class VerificationKey {
    /**
     * The verification key
     */
    private final byte[] verifyKey;

    /**
     * The scheme
     */
    private final SigningScheme schemeId;

    @JsonCreator
    VerificationKey(@JsonProperty("verifyKey") String verifyKey,
                    @JsonProperty("schemeId") SigningScheme schemeId) {
        try {
            this.verifyKey = Hex.decodeHex(verifyKey);
            this.schemeId = schemeId;
        } catch (DecoderException e) {
            throw new IllegalArgumentException("The verification key is not properly HEX encoded.", e);
        }
    }

    /**
     * Gets the {@link ED25519PublicKey} wrapping {@link VerificationKey#verifyKey}.
     *
     * @return Created {@link ED25519PublicKey}.
     * @throws ED25519Exception if the {@link VerificationKey#verifyKey} is does not represent a {@link ED25519PublicKey}
     */
    public ED25519PublicKey getPublicKey() {
        return ED25519PublicKey.from(verifyKey);
    }

    /**
     * Get the hex encoded verification key
     *
     * @return a hex string representing the public key.
     */
    public String asHex() {
        return Hex.encodeHexString(verifyKey);
    }

    /**
     * Parses {@link UpdatePublicKey} to {@link VerificationKey}.
     * @param updatePublicKey {@link UpdatePublicKey} returned by the GRPC V2 API.
     * @return parsed {@link UpdatePublicKey}.
     */
    public static VerificationKey parse(UpdatePublicKey updatePublicKey) {
        return VerificationKey.builder()
                .verifyKey(updatePublicKey.getValue().toByteArray())
                .schemeId(SigningScheme.ED25519)
                .build();
    }
}
