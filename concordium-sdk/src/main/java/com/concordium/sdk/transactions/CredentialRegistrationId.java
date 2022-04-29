package com.concordium.sdk.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

    public static CredentialRegistrationId from(String encoded) {
        return new CredentialRegistrationId(Base58.decodeChecked(1, encoded));
    }

    public static CredentialRegistrationId fromBytes(byte[] bytes) {
        return new CredentialRegistrationId(bytes);
    }
}
