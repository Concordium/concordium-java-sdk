package com.concordium.sdk.responses.accountinfo.credential;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@ToString
@EqualsAndHashCode
public final class Key {
    private final byte[] verifyKey;

    public byte[] getVerifyKey() {
        return Arrays.copyOf(verifyKey, verifyKey.length);
    }

    private final VerificationScheme schemeId;

    @Builder
    Key(final byte[] verifyKey, final VerificationScheme schemeId) {
        this.verifyKey = Arrays.copyOf(verifyKey, verifyKey.length);
        this.schemeId = schemeId;
    }
}
