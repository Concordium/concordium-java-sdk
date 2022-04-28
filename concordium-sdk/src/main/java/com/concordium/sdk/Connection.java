package com.concordium.sdk;


import io.grpc.*;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.util.Objects;

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

    /**
     * The {@link TLSConfig} to use.
     * If this is not set i.e. 'null' then the underlying HTTP connection can fall back to 'plain-text'
     * if the servers certificate cannot be verified against the standard CA root.
     * <p>
     * Otherwise, if this is set then the connection will use the supplied {@link TLSConfig} for
     * setting up trust and possibly mTLS. See {@link TLSConfig} for more configuration details.
     */
    private final TLSConfig tlsConfig;

    /**
     * Create a {@link Connection}
     *
     * @param host        the host to connect to.
     * @param port        the port to use.
     * @param timeout     the timeout for each request.
     * @param credentials The {@link Credentials} to use for the connection.
     *                    This includes the 'Authentication' header and
     *                    possibly additional HTTP headers.
     * @param useTLS      Whether to enforce the usage of TLS and use the specified TLS configuration.
     */
    @Builder
    public Connection(String host, int port, int timeout, Credentials credentials, TLSConfig useTLS) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.credentials = credentials;
        this.tlsConfig = useTLS;
    }

    /**
     * Whether to enforce TLS or not.
     *
     * @return true if TLS must be used for the underlying connection.
     */
    boolean enforceTLS() {
        return !Objects.isNull(tlsConfig);
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
            // throws an `IllegalArgumentException` if the configuration is
            // deemed invalid.
            if (connection.enforceTLS()) {
                connection.getTlsConfig().assertOk();
            }
            return connection;
        }
    }

    /**
     * Create the channel to be used based on the {@link Connection} configuration.
     *
     * @return a new {@link ManagedChannel}
     */
    ManagedChannel newChannel() throws IOException {
        ManagedChannel channel;
        if (enforceTLS()) {
            val tlsConfig = getTlsConfig();
            ChannelCredentials tlsChannel = getTLSChannel(tlsConfig);
            return Grpc.newChannelBuilderForAddress(getHost(), getPort(), tlsChannel).build();
        } else {
            channel = ManagedChannelBuilder.
                    forAddress(getHost(), getPort())
                    .usePlaintext()
                    .build();
        }
        return channel;
    }

    private ChannelCredentials getTLSChannel(TLSConfig tlsConfig) throws IOException {
        if (Objects.isNull(tlsConfig.getServerCert())) {
            return TlsChannelCredentials.create();
        }else {
            val builder = TlsChannelCredentials.newBuilder();
            if(!Objects.isNull(tlsConfig.getClientCert())) {
                builder.trustManager(tlsConfig.getServerCert());
            }
            if (getTlsConfig().isMTLS()) {
                builder.keyManager(tlsConfig.getClientCert(), tlsConfig.getClientKeyFile());
            }
            return builder.build();
        }

    }

    private static final int DEFAULT_TIMEOUT_MS = 15000;
}
