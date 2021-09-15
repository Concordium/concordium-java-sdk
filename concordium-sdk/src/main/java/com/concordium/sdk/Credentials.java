package com.concordium.sdk;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import lombok.Getter;
import lombok.val;

import java.util.concurrent.Executor;

@Getter
public class Credentials extends CallCredentials {
    private final CallCredentials callCredentials;

    private Credentials(CallCredentials credentials) {
        this.callCredentials = credentials;
    }

    public static Credentials from(String username) {
        val callCredential = new CallCredentials(username);
        return new Credentials(callCredential);
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
        private final String username;

        private final Metadata.Key<String> META_DATA_KEY = Metadata.Key.of(("Authentication"), Metadata.ASCII_STRING_MARSHALLER);

        CallCredentials(final String username) {
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
