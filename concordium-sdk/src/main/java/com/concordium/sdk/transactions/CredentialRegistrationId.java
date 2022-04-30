package com.concordium.sdk.transactions;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;
import lombok.*;
import org.apache.commons.codec.DecoderException;


/**
 * A credential registration id
 */
@ToString
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

    /**
     * Create a {@link CredentialRegistrationId} from raw bytes
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
}
