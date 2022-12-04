package com.concordium.sdk.responses.accountinfo.credential;

import com.concordium.sdk.transactions.PublicKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

@Getter
@ToString
public final class Key {
    private final String verifyKey;
    private final String schemeId;

    private final String ED25519 = "ed25519";

    @JsonCreator
    public Key(@JsonProperty("verifyKey") String verifyKey,
        @JsonProperty("schemeId") String schemeId) {
        this.verifyKey = verifyKey;
        this.schemeId = schemeId;
    }

    byte[] getSchemeIdBytes() {
        val buffer = ByteBuffer.allocate(PublicKey.BYTES);
        switch (this.schemeId.toLowerCase()) {
            case ED25519:
                buffer.put(PublicKey.ED25519.getValue());
                break;
            default:
                throw new IllegalStateException("Invalid AccountRequest Type " + this.schemeId.toLowerCase());

        }
        return buffer.array();
    }

    public byte[] getBytes() {
        byte[] verifyKeyBytes = new byte[0];
        try {
            verifyKeyBytes = Hex.decodeHex(verifyKey);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        val schemeIdBytes = getSchemeIdBytes();
        val buffer = ByteBuffer.allocate(PublicKey.BYTES + verifyKeyBytes.length);
        buffer.put(schemeIdBytes);
        buffer.put(verifyKeyBytes);
        return buffer.array();
    }
}
