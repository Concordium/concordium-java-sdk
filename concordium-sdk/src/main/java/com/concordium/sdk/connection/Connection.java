package com.concordium.sdk.connection;

import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

@Getter
public final class Connection {
    private final String host;
    private final int port;
    private int timeout;

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
            // setting a default timeout
            if (connection.timeout == 0) {
                connection.timeout = DEFAULT_TIMEOUT_MS;
            }
            return connection;
        }
    }

    private static final int DEFAULT_TIMEOUT_MS = 1500;
}
