package com.concordium.sdk;

import com.google.common.base.Strings;
import io.grpc.CallCredentials;
import io.grpc.Metadata;
import lombok.*;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;

@Getter
@Setter
public class Credentials extends CallCredentials {
    private final CallCredentials callCredentials;

    private Credentials(CallCredentials credentials) {
        this.callCredentials = credentials;
    }

    public static Credentials from(String authenticationToken) {
        val callCredential = new CallCredentials(authenticationToken);
        return new Credentials(callCredential);
    }

    /**
     * Create a {@link Credentials} object with additional headers to be used
     * within the underlying gRPC connection.
     *
     * @param additionalHeaders a set of extra headers to use within the connection.
     */
    public static Credentials from(Set<Header> additionalHeaders) {
        val callCredential = new CallCredentials(additionalHeaders);
        return new Credentials(callCredential);
    }

    /**
     * Create a {@link Credentials} object with additional headers to be used
     * within the underlying gRPC connection.
     *
     * @param authenticationToken   the authentication token i.e., the header 'Authentication' is set with this value.
     *                              If the value is not provided header will not be set.
     * @param withAdditionalHeaders an optional set of extra headers to use within the connection.
     */
    @Builder
    public Credentials(String authenticationToken, @Singular Set<Header> withAdditionalHeaders) {
        this.callCredentials = new CallCredentials(authenticationToken, withAdditionalHeaders);
    }

    public Credentials() {
        this.callCredentials = new CallCredentials();
    }

    @Override
    public final synchronized void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        this.callCredentials.applyRequestMetadata(requestInfo, executor, metadataApplier);
    }

    @Override
    public void thisUsesUnstableApi() {
        this.callCredentials.thisUsesUnstableApi();
    }


    private static final class CallCredentials extends io.grpc.CallCredentials {
        private final String authenticationToken;
        private final Set<Header> additionalHeaders;

        private Metadata cachedMetadata;

        private final Metadata.Key<String> AUTHENTICATION_META_DATA_KEY = Metadata.Key.of((Header.AUTHENTICATION_HEADER), Metadata.ASCII_STRING_MARSHALLER);

        CallCredentials() {
            this.authenticationToken = "";
            this.additionalHeaders = Collections.emptySet();
        }

        CallCredentials(final String authenticationToken) {
            this.authenticationToken = Strings.nullToEmpty(authenticationToken);
            this.additionalHeaders = Collections.emptySet();
        }

        CallCredentials(final String authenticationToken, final Set<Header> additionalHeaders) {
            this.authenticationToken = Strings.nullToEmpty(authenticationToken);
            this.additionalHeaders = additionalHeaders;
        }

        CallCredentials(final Set<Header> additionalHeaders) {
            this.authenticationToken = "";
            this.additionalHeaders = additionalHeaders;
        }

        @Override
        public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
            metadataApplier.apply(getMetadata());
        }

        private Metadata getMetadata() {
            if (Objects.isNull(this.cachedMetadata)) {
                this.cachedMetadata = createMetadata();
            }
            return this.cachedMetadata;
        }

        private Metadata createMetadata() {
            val metadata = new Metadata();
            if (!Strings.isNullOrEmpty(authenticationToken)) {
                metadata.put(AUTHENTICATION_META_DATA_KEY, this.authenticationToken);
            }
            for (Header header : additionalHeaders) {
                addHeader(metadata, header);
            }
            return metadata;
        }

        private void addHeader(Metadata metadata, Header header) {
            Metadata.Key<String> key = Metadata.Key.of(header.getKey(), Metadata.ASCII_STRING_MARSHALLER);
            metadata.put(key, header.getValue());
        }

        @Override
        public void thisUsesUnstableApi() {

        }
    }

}
