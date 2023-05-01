package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.responses.blocksummary.FinalizationData;
import com.concordium.sdk.responses.blocksummary.Finalizer;
import com.concordium.sdk.transactions.Hash;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * The test asserts that {@link ClientV2#getBlockFinalizationSummary(com.concordium.sdk.requests.BlockHashInput)}
 * correctly converts the {@link BlockFinalizationSummary} returned by the server to {@link FinalizationData}.
 */
public class ClientV2GetBlockFinalizationSummaryTest {

    private static final byte[] BLOCK_HASH = new byte[]{1, 2, 3};
    private static final long FINALIZATION_INDEX = 10;
    private static final long DELAY = 1000;

    private static final long FINALIZER_ONE_ID = 1;
    private static final long FINALIZER_ONE_WEIGHT = 10;

    private static final boolean FINALIZER_ONE_SIGNED = true;
    private static final long FINALIZER_TWO_ID = 2;
    private static final long FINALIZER_TWO_WEIGHT = 5;

    private static final boolean FINALIZER_TWO_SIGNED = true;
    private static final long FINALIZER_THREE_ID = 3;
    private static final long FINALIZER_THREE_WEIGHT = 1;

    private static final boolean FINALIZER_THREE_SIGNED = false;

    private static final FinalizationSummaryParty GRPC_FINALIZER_ONE = FinalizationSummaryParty.newBuilder()
            .setBaker(BakerId.newBuilder().setValue(FINALIZER_ONE_ID).build())
            .setWeight(FINALIZER_ONE_WEIGHT)
            .setSigned(FINALIZER_ONE_SIGNED)
            .build();
    private static final FinalizationSummaryParty GRPC_FINALIZER_TWO = FinalizationSummaryParty.newBuilder()
            .setBaker(BakerId.newBuilder().setValue(FINALIZER_TWO_ID).build())
            .setWeight(FINALIZER_TWO_WEIGHT)
            .setSigned(FINALIZER_TWO_SIGNED)
            .build();
    private static final FinalizationSummaryParty GRPC_FINALIZER_THREE = FinalizationSummaryParty.newBuilder()
            .setBaker(BakerId.newBuilder().setValue(FINALIZER_THREE_ID).build())
            .setWeight(FINALIZER_THREE_WEIGHT)
            .setSigned(FINALIZER_THREE_SIGNED)
            .build();
    private static final List<FinalizationSummaryParty> GRPC_FINALIZERS = new ImmutableList.Builder<FinalizationSummaryParty>()
            .add(GRPC_FINALIZER_ONE)
            .add(GRPC_FINALIZER_TWO)
            .add(GRPC_FINALIZER_THREE)
            .build();
    private static final BlockFinalizationSummary GRPC_FINALIZATION_SUMMARY = BlockFinalizationSummary.newBuilder()
            .setRecord(
                    FinalizationSummary.newBuilder()
                            .setBlock(BlockHash.newBuilder().setValue(ByteString.copyFrom(BLOCK_HASH)).build())
                            .setIndex(FinalizationIndex.newBuilder().setValue(FINALIZATION_INDEX))
                            .setDelay(BlockHeight.newBuilder().setValue(DELAY).build())
                            .addAllFinalizers(GRPC_FINALIZERS)
                            .build()
            ).build();

    private static final Finalizer CLIENT_FINALIZER_ONE = Finalizer.builder()
            .bakerId(com.concordium.sdk.responses.AccountIndex.from(FINALIZER_ONE_ID))
            .weight(BigInteger.valueOf(FINALIZER_ONE_WEIGHT))
            .signed(FINALIZER_ONE_SIGNED).build();
    private static final Finalizer CLIENT_FINALIZER_TWO = Finalizer.builder()
            .bakerId(com.concordium.sdk.responses.AccountIndex.from(FINALIZER_TWO_ID))
            .weight(BigInteger.valueOf(FINALIZER_TWO_WEIGHT))
            .signed(FINALIZER_TWO_SIGNED).build();
    private static final Finalizer CLIENT_FINALIZER_THREE = Finalizer.builder()
            .bakerId(com.concordium.sdk.responses.AccountIndex.from(FINALIZER_THREE_ID))
            .weight(BigInteger.valueOf(FINALIZER_THREE_WEIGHT))
            .signed(FINALIZER_THREE_SIGNED).build();
    private static final List<Finalizer> CLIENT_FINALIZERS = new ImmutableList.Builder<Finalizer>()
            .add(CLIENT_FINALIZER_ONE)
            .add(CLIENT_FINALIZER_TWO)
            .add(CLIENT_FINALIZER_THREE)
            .build();
    private static final FinalizationData CLIENT_FINALIZATION_DATA = FinalizationData.builder()
            .finalizationBlockPointer(Hash.from(BLOCK_HASH).toString())
            .finalizationIndex((int) FINALIZATION_INDEX)
            .finalizationDelay((int) DELAY)
            .finalizers(CLIENT_FINALIZERS).build();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class,
            delegatesTo(
                    new QueriesGrpc.QueriesImplBase() {
                        @Override
                        public void getBlockFinalizationSummary(
                                BlockHashInput request,
                                StreamObserver<BlockFinalizationSummary> responseObserver) {
                            responseObserver.onNext(GRPC_FINALIZATION_SUMMARY);
                            responseObserver.onCompleted();
                        }
                    }));
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
        client = new ClientV2(10000, channel, Credentials.builder().build());
    }

    @Test
    public void getBlockFinalizationSummary() {
        val res = client.getBlockFinalizationSummary(com.concordium.sdk.requests.BlockHashInput.BEST);
        verify(serviceImpl).getBlockFinalizationSummary(any(BlockHashInput.class), any(StreamObserver.class));
        assertEquals(CLIENT_FINALIZATION_DATA, res);

    }
}
