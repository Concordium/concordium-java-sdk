package com.concordium.sdk;

import com.concordium.grpc.v2.AbsoluteBlockHeight;
import com.concordium.grpc.v2.ArInfo;
import com.concordium.grpc.v2.BlockHash;
import com.concordium.grpc.v2.Empty;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.Description;
import com.concordium.sdk.responses.getblocks.ArrivedBlockInfo;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.protobuf.ByteString;
import lombok.var;

import java.util.Iterator;
import java.util.Objects;

/**
 * Object Mapping Extensions. Maps from GRPC types to client types and vice versa.
 */
interface ClientV2MapperExtensions {
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
                if (Objects.isNull(input.getBlockHash())) {
                    throw new IllegalArgumentException("Block Hash should be set if type is GIVEN");
                }

                builder.setGiven(to(input.getBlockHash()));
            default:
                throw new IllegalArgumentException("Invalid type");
        }

        return builder.build();
    }

    static com.concordium.grpc.v2.BlockHash to(final Hash blockHash) {
        return com.concordium.grpc.v2.BlockHash.newBuilder().setValue(to(blockHash.getBytes())).build();
    }

    static ByteString to(final byte[] bytes) {
        return ByteString.copyFrom(bytes);
    }

    static AnonymityRevokerInfo to(final ArInfo arInfo) {
        return AnonymityRevokerInfo.builder()
                .arIdentity(to(arInfo.getIdentity()))
                .description(to(arInfo.getDescription()))
                .arPublicKey(to(arInfo.getPublicKey()))
                .build();
    }

    static ElgamalPublicKey to(final ArInfo.ArPublicKey publicKey) {
        return ElgamalPublicKey.from(publicKey.getValue().toByteArray());
    }

    static Description to(final com.concordium.grpc.v2.Description description) {
        return Description.builder()
                .description(description.getDescription())
                .url(description.getUrl())
                .name(description.getName())
                .build();
    }

    static int to(final ArInfo.ArIdentity identity) {
        return identity.getValue();
    }

    static <T1, T2> Iterator<T2> to(final Iterator<T1> iterator, final Function<? super T1, ? extends T2> to) {
        return Iterators.transform(iterator, to);
    }

    static ArrivedBlockInfo to(com.concordium.grpc.v2.ArrivedBlockInfo arrivedBlockInfo) {
        return ArrivedBlockInfo.builder()
                .blockHash(to(arrivedBlockInfo.getHash()))
                .blockHeight(to(arrivedBlockInfo.getHeight()))
                .build();
    }

    static UInt64 to(AbsoluteBlockHeight height) {
        return UInt64.from(height.getValue());
    }

    static Hash to(BlockHash hash) {
        return Hash.from(hash.toByteArray());
    }
}
