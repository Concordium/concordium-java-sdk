package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
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

    /**
     * Create a new {@link AccountAddress} given the base58 encoded address.
     * PRECONDITION: The encoded account address must conform to the {@link AccountAddress#VERSION}
     * @param address A base58 encoded address.
     * @return The created {@link AccountAddress}
     */
    public static AccountAddress from(String address) {
        val addressBytes = Base58.decodeChecked(VERSION, address);
        return AccountAddress.from(addressBytes);
    }

    /**
     * Create a {@link AccountAddress} from the supplied {@link CredentialRegistrationId}
     * @param regId the credential registration id to derive the {@link AccountAddress} from
     * @return the derived {@link AccountAddress}
     */
    public static AccountAddress from(CredentialRegistrationId regId) {
        return AccountAddress.from(SHA256.hash(regId.getRegId()));
    }

    /**
     * Create a new alias.
     * @param alias the counter to be used for the alias.
     *              The alias counter must be non-negative and the max value allowed is {@link AccountAddress#ALIAS_MAX_VALUE}
     * @return a new {@link AccountAddress} for the specified alias.
     *
     */
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

    /**
     * Check if one {@link AccountAddress} is an <i>alias</i> of another.
     * @param other the other {@link AccountAddress}
     * @return whether this {@link AccountAddress} is an alias of the other.
     */
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

    public static AccountAddress fromBytes(ByteBuffer source) {
        byte[] addressBytes = new byte[BYTES];
        source.get(addressBytes);
        return AccountAddress.from(addressBytes);
    }
}
