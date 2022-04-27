package com.concordium.sdk;


import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Connection properties
 */
@Getter
public final class Connection {
    /**
     * The host to connect to.
     */
    private final String host;
    /**
     * The port to use.
     */
    private final int port;
    /**
     * The timeout for each request (in milliseconds).
     * The default timeout is 15000 milliseconds if a non-positive value is supplied then
     * the default value will be used.
     */
    private int timeout;

    /**
     * The gRPC `Authentication` token.
     */
    private final Credentials credentials;

    @Builder
    public Connection(String host, int port, int timeout, Credentials credentials) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.credentials = credentials;
    }

    public static Connection.ConnectionBuilder builder() {
        return new Connection.ValidatingConnectionBuilder();
    }

    private static class ValidatingConnectionBuilder extends ConnectionBuilder {
        @Override
        public Connection build() {
            val connection = super.build();
            if (Objects.isNull(connection.host)) {
                throw new IllegalArgumentException("Connection host cannot be null");
            }
            if (connection.port == 0) {
                throw new IllegalArgumentException("Connection port must be set");
            }
            if (Objects.isNull(connection.credentials)) {
                throw new IllegalArgumentException("Connection credentials cannot be null");
            }
            // setting a default timeout of 15 000 ms
            if (connection.timeout < 1) {
                connection.timeout = DEFAULT_TIMEOUT_MS;
            }
            return connection;
        }
    }

    private static final int DEFAULT_TIMEOUT_MS = 15000;
}
