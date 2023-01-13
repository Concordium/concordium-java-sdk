package com.concordium.sdk.v2.types;

import com.concordium.grpc.v2.Empty;
import com.google.protobuf.ByteString;
import lombok.var;

public interface MapperExtensions {
    static ArInfo to(final com.concordium.grpc.v2.ArInfo input) {
        return ArInfo.builder()
                .identity(to(input.getIdentity()))
                .description(to(input.getDescription()))
                .publicKey(to(input.getPublicKey()))
                .build();
    }

    static PublicKey to(final com.concordium.grpc.v2.ArInfo.ArPublicKey input) {
        return new PublicKey(input.toByteArray());
    }

    static Description to(final com.concordium.grpc.v2.Description input) {
        return Description.builder()
                .name(input.getName())
                .url(input.getUrl())
                .description(input.getDescription())
                .build();
    }

    static Identity to(final com.concordium.grpc.v2.ArInfo.ArIdentity input) {
        return Identity.builder().value(input.getValue()).build();
    }

    static com.concordium.grpc.v2.BlockHashInput to(final BlockHashInput input) {
        var builder = com.concordium.grpc.v2.BlockHashInput.newBuilder();
        switch (input.getType()) {
            case BEST:
                builder.setBest(Empty.getDefaultInstance());
                break;
            case LAST_FINAL:
                builder.setLastFinal(Empty.getDefaultInstance());
                break;
            case GIVEN:
                builder.setGiven(to(input.getBlockHash()));
        }

        return builder.build();
    }

    static com.concordium.grpc.v2.BlockHash to(final BlockHash blockHash) {
        return com.concordium.grpc.v2.BlockHash.newBuilder().setValue(to(blockHash.getBytes())).build();
    }

    static ByteString to(byte[] bytes) {
        return ByteString.copyFrom(bytes);
    }
}
