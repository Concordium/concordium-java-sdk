package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.exceptions.ED25519Exception;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

/**
 * Root or level1 keys.
 */
@Getter
@ToString
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

    @Builder
    VerificationKey(final byte[] verifyKey, final SigningScheme signingScheme) {
        this.verifyKey = Arrays.copyOf(verifyKey, verifyKey.length);
        this.schemeId = signingScheme;
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
}
