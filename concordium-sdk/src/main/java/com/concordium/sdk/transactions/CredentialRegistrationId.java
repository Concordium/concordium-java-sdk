package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

/**
 * A credential registration id
 */
@ToString
@EqualsAndHashCode
public class CredentialRegistrationId {
    @Getter
    private final byte[] regId;

    private CredentialRegistrationId(byte[] regId) {
        this.regId = regId;
    }

    /**
     * Create a {@link CredentialRegistrationId} from the hex encoded registration id.
     */
    @SneakyThrows
    public static CredentialRegistrationId from(String encoded) {
        return new CredentialRegistrationId(Hex.decodeHex(encoded));
    }

    /**
     * Create a {@link CredentialRegistrationId} from raw bytes
     *
     * @param bytes the credential registration id bytes
     * @return a {@link CredentialRegistrationId}
     */
    public static CredentialRegistrationId fromBytes(byte[] bytes) {
        return new CredentialRegistrationId(bytes);
    }
}
