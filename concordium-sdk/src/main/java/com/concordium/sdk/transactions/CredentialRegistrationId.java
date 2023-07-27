package com.concordium.sdk.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


/**
 * A credential registration id
 */
@EqualsAndHashCode
public class CredentialRegistrationId {
    //the length of `CredentialRegistrationId` bytes
    private static final int LENGTH = 48;
    @Getter
    private final byte[] regId;

    private CredentialRegistrationId(byte[] regId) {
        this.regId = regId;
    }

    /**
     * Create a {@link CredentialRegistrationId} from the hex encoded registration id.
     */
    public static CredentialRegistrationId from(String encoded) {
        try {
            val regId = Hex.decodeHex(encoded);
            return CredentialRegistrationId.fromBytes(regId);
        } catch (DecoderException e) {
            throw new IllegalArgumentException("CredentialRegistrationId is not a valid HEX encoding", e);
        }
    }

    public static CredentialRegistrationId from(com.concordium.grpc.v2.CredentialRegistrationId regId) {
        return new CredentialRegistrationId(regId.getValue().toByteArray());
    }

    /**
     * Get the hex encoded credential registration id
     *
     * @return the hex encoded credential registration id
     */
    public String getEncoded() {
        return Hex.encodeHexString(regId);
    }

    /**
     * Create a {@link CredentialRegistrationId} from raw bytes
     * <p>
     * Note. This is not the most optimal check as in fact the credential registration id
     * is a group element in the G1 curve of the BLS pairing.
     * Hence, not every series of bytes (with the correct size) is a valid credential registration id.
     * <p>
     * Maintainer note. A future implementation could make use of the above comment and
     * provide stronger verification of the credential registration id.
     *
     * @param bytes the credential registration id bytes
     * @return a {@link CredentialRegistrationId}
     */
    public static CredentialRegistrationId fromBytes(byte[] bytes) {
        if (bytes.length != LENGTH) {
            throw new IllegalArgumentException("Unexpected CredentialRegistrationId bytes length. " +
                    "Expected " + LENGTH + " but was " + bytes.length);
        }
        return new CredentialRegistrationId(bytes);
    }

    @JsonCreator
    CredentialRegistrationId(String regId) {
        this.regId = CredentialRegistrationId.from(regId).getRegId();
    }

    @Override
    public String toString() {
        return Hex.encodeHexString(regId);
    }
}
