package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.requests.EpochQuery;
import com.google.common.collect.ImmutableList;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 */
public class ClientV2GetWinningBakersEpochTest {

    // ----- Test Values -----
    private static final boolean PRESENT_1 = true;
    private static final boolean PRESENT_2 = false;
    private static final long WINNER_1 = 1;
    private static final long WINNER_2 = 2;
    private static final long ROUND_1 = 1;
    private static final long ROUND_2 = 2;



    // ----- Client Values -----
    private static final com.concordium.sdk.responses.winningbaker.WinningBaker CLIENT_WINNING_BAKER_1 = com.concordium.sdk.responses.winningbaker.WinningBaker.builder()
            .round(com.concordium.sdk.responses.Round.from(ROUND_1))
            .winner(com.concordium.sdk.responses.BakerId.from(WINNER_1))
            .present(PRESENT_1)
            .build();
    private static final com.concordium.sdk.responses.winningbaker.WinningBaker CLIENT_WINNING_BAKER_2 = com.concordium.sdk.responses.winningbaker.WinningBaker.builder()
            .round(com.concordium.sdk.responses.Round.from(ROUND_2))
            .winner(com.concordium.sdk.responses.BakerId.from(WINNER_2))
            .present(PRESENT_2)
            .build();

    // ----- GRPC Values -----
    private static final EpochRequest GRPC_REQUEST = EpochRequest.newBuilder()
            .setBlockHash(
                    BlockHashInput.newBuilder().setBest(Empty.newBuilder().build())
            )
            .build();
    private static final WinningBaker GRPC_WINNING_BAKER_1 = WinningBaker.newBuilder()
            .setRound(Round.newBuilder().setValue(ROUND_1))
            .setWinner(BakerId.newBuilder().setValue(WINNER_1))
            .setPresent(PRESENT_1)
            .build();
    private static final WinningBaker GRPC_WINNING_BAKER_2 = WinningBaker.newBuilder()
            .setRound(Round.newBuilder().setValue(ROUND_2))
            .setWinner(BakerId.newBuilder().setValue(WINNER_2))
            .setPresent(PRESENT_2)
            .build();
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getWinningBakersEpoch(
                        com.concordium.grpc.v2.EpochRequest request,
                        StreamObserver<WinningBaker> responseObserver) {
                    responseObserver.onNext(GRPC_WINNING_BAKER_1);
                    responseObserver.onNext(GRPC_WINNING_BAKER_2);
                    responseObserver.onCompleted();
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
    public void getWinningBakersEpoch() {
        var query = EpochQuery.BLOCK_HASH(BlockQuery.BEST);
        var res = client.getWinningBakersEpoch(query);

        verify(serviceImpl).getWinningBakersEpoch(eq(GRPC_REQUEST), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(res), ImmutableList.of(CLIENT_WINNING_BAKER_1, CLIENT_WINNING_BAKER_2));
    }
}
