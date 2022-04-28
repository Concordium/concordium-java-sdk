package com.concordium.sdk;

import com.sun.istack.internal.NotNull;
import io.grpc.TlsChannelCredentials;
import lombok.Getter;
import lombok.val;

import java.io.File;
import java.util.Objects;

/**
 * Configuration to be used for a TLS enforced {@link Connection}
 */
@Getter
public class TLSConfig {
    /**
     * The servers certificate or some CA that authorized it.
     * See {@link TlsChannelCredentials.Builder#trustManager(File)} for details of the
     * expected file format.
     */
    private final File serverCert;

    /**
     * If mTLS is to be used then this should be set to the
     * certificate of the client.
     * See {@link TlsChannelCredentials.Builder#keyManager(File, File)} for details of the
     * expected file format.
     */
    private File clientCert;
    /**
     * If mTLS is to be used then this should be set to the
     * private key of the client.
     * See {@link TlsChannelCredentials.Builder#keyManager(File, File)} for details of the
     * expected file format.
     */
    private File clientKeyFile;

    boolean isMTLS() {
        return !Objects.isNull(clientCert);
    }

    private TLSConfig(File serverCert) {
        this.serverCert = serverCert;
    }

    /**
     * Constructor for establishing a basic TLS connection.
     * @param serverCert The X509 certificate of the server.
     * @return the TLSConfig
     */
    public static TLSConfig from(File serverCert) {
        return new TLSConfig(serverCert);
    }

    /**
     * Constructor for establishing a mTLS connection.
     * @param serverCert The pem encoded certificate of the server.
     *                   Supply 'null' here if the default JVM trust store should
     *                   be used for the connection.
     * @param clientCert The pem encoded certificate of the client
     * @param clientKeyFile The PEM encoded PKCS#8 private key for the client.
     * @return the TLSConfig
     */
    public static TLSConfig mTLS(File serverCert, @NotNull File clientCert, @NotNull File clientKeyFile) {
        val tlsConfig = new TLSConfig(serverCert);
        tlsConfig.clientCert = clientCert;
        tlsConfig.clientKeyFile = clientKeyFile;
        return tlsConfig;
    }

    /**
     * The underlying connection is forced to use TLS.
     * Note. the servers certificate must be trusted by the default jks CA.
     *
     * See {@link TLSConfig#from(File)} or {@link TLSConfig#mTLS(File, File, File)} for configuring
     * custom trust or mTLS.
     * @return the TLSConfig
     */
    public static TLSConfig auto() {
        return new TLSConfig(null);
    }

    public void assertOk() {
        if (Objects.isNull(this.clientCert) && !Objects.isNull(this.clientKeyFile)) {
            throw new IllegalArgumentException("mTLS was configured. But a client key certificate was missing.");
        }
        if (!Objects.isNull(this.clientCert) && Objects.isNull(this.clientKeyFile)) {
            throw new IllegalArgumentException("mTLS was configured. But a client key file was missing.");
        }
    }
}
