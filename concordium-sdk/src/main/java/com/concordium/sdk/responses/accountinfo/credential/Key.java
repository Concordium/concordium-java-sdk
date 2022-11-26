package com.concordium.sdk.responses.accountinfo.credential;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.transactions.PublicKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@ToString
public final class Key {
    private final ED25519PublicKey verifyKey;
    private final String schemeId;

    @JsonCreator
    public Key(@JsonProperty("verifyKey") String verifyKey,
        @JsonProperty("schemeId") String schemeId) {
        this.verifyKey = ED25519PublicKey.from(verifyKey);
        this.schemeId = schemeId;
    }

    @SneakyThrows
    public byte[] getBytes() {
        val verifyKeyBytes = verifyKey.getBytes();
        val buffer = ByteBuffer.allocate(PublicKey.BYTES + verifyKeyBytes.length);
        buffer.put(PublicKey.ED25519.getValue());
        buffer.put(verifyKeyBytes);
        return buffer.array();
    }
}
