package com.concordium.sdk.transactions;

import com.concordium.sdk.Base58;
import lombok.Getter;
import lombok.val;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AccountAddress {
    public static final int BYTES = 32;
    private final static int VERSION = 1;

    @Getter
    private final byte[] bytes;

    private AccountAddress(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getEncodedBytes() {
        return Base58.encodeChecked(VERSION, bytes).getBytes(StandardCharsets.UTF_8);
    }

    public String encoded() {
        return Base58.encodeChecked(VERSION, bytes);
    }

    public static AccountAddress from(String address) {
        val addressBytes = Base58.decodeChecked(VERSION, address);
        return AccountAddress.from(addressBytes);
    }

    public static AccountAddress from(byte[] addressBytes) {
        if (Objects.isNull(addressBytes)) {
            throw new IllegalArgumentException("Address bytes must not be null.");
        }
        if (addressBytes.length != BYTES) {
            throw new IllegalArgumentException("Address bytes must be exactly 32 bytes long. Was: " + addressBytes.length);
        }
        return new AccountAddress(addressBytes);
    }
}
