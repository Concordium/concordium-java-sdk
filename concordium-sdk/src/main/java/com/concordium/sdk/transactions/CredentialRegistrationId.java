package com.concordium.sdk.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @SneakyThrows
    public static CredentialRegistrationId from(String encoded) {
        return new CredentialRegistrationId(Hex.decodeHex(encoded));
    }

    public static CredentialRegistrationId fromBytes(byte[] bytes) {
        return new CredentialRegistrationId(bytes);
    }
}
