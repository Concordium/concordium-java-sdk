package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

@ToString
@EqualsAndHashCode
public final class AccountAddress {
    public static final int BYTES = 32;
    private final static int VERSION = 1;
    private final static int ACCOUNT_ADDRESS_PREFIX_SIZE = 29;
    private final static int ALIAS_MAX_VALUE = (2 << 23); // 2^24

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

    public AccountAddress newAlias(int alias) {
        if (alias < 0) {
            throw new NumberFormatException("Alias must be non negative.");
        }
        if (alias > ALIAS_MAX_VALUE) {
            throw new IllegalArgumentException("Alias too large, the provided alias must not be larger than 2^24.");
        }
        val buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(alias);
        val newAlias = this.bytes.clone();
        System.arraycopy(buffer.array(), 1, newAlias, ACCOUNT_ADDRESS_PREFIX_SIZE, 3);
        return AccountAddress.from(newAlias);
    }

    public boolean isAliasOf(AccountAddress other) {
        return Arrays.equals(Arrays.copyOfRange(this.bytes, 0, ACCOUNT_ADDRESS_PREFIX_SIZE),
                Arrays.copyOfRange(other.bytes, 0, ACCOUNT_ADDRESS_PREFIX_SIZE));
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
