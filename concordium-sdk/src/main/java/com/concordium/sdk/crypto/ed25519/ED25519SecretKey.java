package com.concordium.sdk.crypto.ed25519;

import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.transactions.Signer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

@Getter
@EqualsAndHashCode
public final class ED25519SecretKey implements Signer {

    private final byte[] bytes;

    private ED25519SecretKey(byte[] bytes) {
        this.bytes = bytes;
    }

    public static ED25519SecretKey from(String hexKey) {
        try {
            return new ED25519SecretKey(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create ED25519SecretKey", e);
        }
    }

    public static ED25519SecretKey from(byte[] bytes) throws ED25519Exception {
        if (bytes.length != ED25519.KEY_SIZE) {
            throw ED25519Exception.from(ED25519ResultCode.MALFORMED_SECRET_KEY);
        }
        return new ED25519SecretKey(bytes);
    }

    @Override
    public byte[] sign(byte[] message) throws ED25519Exception {
        return ED25519.sign(this, message);
    }

    public static ED25519SecretKey createNew() throws ED25519Exception {
        return ED25519.makeSecretKey();
    }
}
