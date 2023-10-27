package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.responses.BlockIdentifier;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetBlocksTest {
    private static final byte[] BLOCK_HASH_1 = new byte[]{1, 1, 1};
    private static final byte[] BLOCK_HASH_2 = new byte[]{2, 2, 2};
    private static final UInt64 BLOCK_HEIGHT_1 = UInt64.from(1);
    private static final UInt64 BLOCK_HEIGHT_2 = UInt64.from(2);
    private static final ArrivedBlockInfo BLOCK_1_GRPC = ArrivedBlockInfo.newBuilder()
            .setHash(BlockHash.newBuilder().setValue(ByteString.copyFrom(BLOCK_HASH_1)).build())
            .setHeight(AbsoluteBlockHeight.newBuilder().setValue(BLOCK_HEIGHT_1.getValue()).build())
            .build();
    private static final ArrivedBlockInfo BLOCK_2_GRPC = ArrivedBlockInfo.newBuilder()
            .setHash(BlockHash.newBuilder().setValue(ByteString.copyFrom(BLOCK_HASH_2)).build())
            .setHeight(AbsoluteBlockHeight.newBuilder().setValue(BLOCK_HEIGHT_2.getValue()).build())
            .build();
    private static final FinalizedBlockInfo FINALIZED_BLOCK_1_GRPC = FinalizedBlockInfo.newBuilder()
            .setHash(BlockHash.newBuilder().setValue(ByteString.copyFrom(BLOCK_HASH_1)).build())
            .setHeight(AbsoluteBlockHeight.newBuilder().setValue(BLOCK_HEIGHT_1.getValue()).build())
            .build();
    private static final FinalizedBlockInfo FINALIZED_BLOCK_2_GRPC = FinalizedBlockInfo.newBuilder()
            .setHash(BlockHash.newBuilder().setValue(ByteString.copyFrom(BLOCK_HASH_2)).build())
            .setHeight(AbsoluteBlockHeight.newBuilder().setValue(BLOCK_HEIGHT_2.getValue()).build())
            .build();
    private static final BlockIdentifier BLOCK_INFO_1 =
            BlockIdentifier.builder().blockHash(Hash.from(BLOCK_HASH_1)).blockHeight(BLOCK_HEIGHT_1).build();
    private static final BlockIdentifier BLOCK_INFO_2 =
            BlockIdentifier.builder().blockHash(Hash.from(BLOCK_HASH_2)).blockHeight(BLOCK_HEIGHT_2).build();
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getBlocks(Empty request, StreamObserver<ArrivedBlockInfo> responseObserver) {
                    responseObserver.onNext(BLOCK_1_GRPC);
                    responseObserver.onNext(BLOCK_2_GRPC);
                }

                @Override
                public void getFinalizedBlocks(Empty request, StreamObserver<FinalizedBlockInfo> responseObserver) {
                    responseObserver.onNext(FINALIZED_BLOCK_1_GRPC);
                    responseObserver.onNext(FINALIZED_BLOCK_2_GRPC);
                }
            }
    ));

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
    private ClientV2 client;

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel, Optional.empty());
    }

    @Test
    public void getBlocks() {
        var blocks = client.getBlocks(1000);

        verify(serviceImpl).getBlocks(any(Empty.class), any(StreamObserver.class));
        assertEquals(BLOCK_INFO_1, blocks.next());
        assertEquals(BLOCK_INFO_2, blocks.next());
    }

    @Test
    public void getFinalizedBlocks() {
        var blocks = client.getFinalizedBlocks(1000);

        verify(serviceImpl).getFinalizedBlocks(any(Empty.class), any(StreamObserver.class));
        assertEquals(BLOCK_INFO_1, blocks.next());
        assertEquals(BLOCK_INFO_2, blocks.next());
    }
}
