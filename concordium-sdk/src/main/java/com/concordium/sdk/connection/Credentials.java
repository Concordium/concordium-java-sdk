package com.concordium.sdk.connection;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.Executor;

@Getter
public class Credentials extends CallCredentials {
    private final CallCredentials callCredentials;

    @Builder
    private Credentials(String username) {
        this.callCredentials = new CallCredentials(username);
    }

    @Override
    public synchronized void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        this.callCredentials.applyRequestMetadata(requestInfo, executor, metadataApplier);
    }

    @Override
    public void thisUsesUnstableApi() {
    }

    private static final class CallCredentials extends io.grpc.CallCredentials {
        private final String username;

        private final Metadata.Key<String> META_DATA_KEY = Metadata.Key.of(("Authentication"), Metadata.ASCII_STRING_MARSHALLER);

        public CallCredentials(final String username) {
            this.username = username;
        }

        @Override
        public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
            Metadata metadata = new Metadata();
            metadata.put(META_DATA_KEY, this.username);
            metadataApplier.apply(metadata);
        }

        @Override
        public void thisUsesUnstableApi() {

        }
    }

}
