package com.concordium.sdk;

import lombok.Getter;

import java.util.Objects;
import java.util.Set;

/**
 * An HTTP header {@link Connection} for the underlying gRPC connection.
 * Note. The header {@link Header#AUTHENTICATION_HEADER} is reserved.
 * To set the value of this `Authentication` header use {@link Credentials#from(String)} or {@link Credentials#Credentials(String, Set)}.
 */
@Getter
public class Header {
    private final String key;
    private final String value;

    private Header(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static Header from(String header, String value) {
        if (Objects.isNull(header) || Objects.isNull(value)) {
            throw new IllegalArgumentException("Header or value must be non-null.");
        }
        if (header.equals(AUTHENTICATION_HEADER)) {
            throw new IllegalArgumentException(AUTHENTICATION_HEADER + " header is reserved");
        }
        return new Header(header, value);
    }

    public static String AUTHENTICATION_HEADER = "Authentication";
}
