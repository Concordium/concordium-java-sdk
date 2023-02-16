package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

@Getter
@ToString
@EqualsAndHashCode
public final class Key {
    private byte[] verifyKey;

    public byte[] getVerifyKey() {
        return Arrays.copyOf(verifyKey, verifyKey.length);
    }

    private final VerificationScheme schemeId;

    @JsonCreator
    Key(@JsonProperty("verifyKey") String verifyKey,
        @JsonProperty("schemeId") VerificationScheme schemeId) throws DecoderException {
        this.verifyKey = Hex.decodeHex(verifyKey);
        this.schemeId = schemeId;
    }

    @Builder
    Key(final byte[] verifyKey, final VerificationScheme schemeId) {
        this.verifyKey = Arrays.copyOf(verifyKey, verifyKey.length);
        this.schemeId = schemeId;
    }
}
