package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.election.ElectionInfo;
import com.concordium.sdk.responses.election.ElectionInfoBaker;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
public class ClientV2GetElectionInfoTest {

    private static final byte[] BLOCK_HASH = new byte[]{1, 1, 1};
    private static final com.concordium.sdk.types.AccountAddress ACCOUNT_ADDRESS_1
            = com.concordium.sdk.types.AccountAddress.from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");
    private static final int ELECTION_DIFFICULTY = 100;

    private static final AmountFraction GRPC_ELECTION_DIFFICULTY_FACTION = AmountFraction.newBuilder()
            .setPartsPerHundredThousand(ELECTION_DIFFICULTY)
            .build();
    private static final byte[] ELECTION_NONCE = new byte[]{9, 9, 9};
    private static final long BAKER_ID = 1L;
    private static final double LOTTERY_POWER = 0.25D;
    private static final com.concordium.grpc.v2.ElectionInfo GRPC_ELECTION_INFO = com.concordium.grpc.v2.ElectionInfo
            .newBuilder()
            .setElectionDifficulty(ElectionDifficulty.newBuilder()
                    .setValue(GRPC_ELECTION_DIFFICULTY_FACTION)
                    .build())
            .setElectionNonce(LeadershipElectionNonce.newBuilder()
                    .setValue(ByteString.copyFrom(ELECTION_NONCE))
                    .build())
            .addAllBakerElectionInfo(Lists.newArrayList(com.concordium.grpc.v2.ElectionInfo.Baker.newBuilder()
                    .setBaker(BakerId.newBuilder().setValue(BAKER_ID).build())
                    .setAccount(AccountAddress
                            .newBuilder()
                            .setValue(ByteString.copyFrom(ACCOUNT_ADDRESS_1.getBytes()))
                            .build())
                    .setLotteryPower(LOTTERY_POWER)
                    .build()))
            .build();

    private static final ElectionInfo ELECTION_INFO_EXPECTED = ElectionInfo.builder()
            .electionDifficulty(PartsPerHundredThousand.from(ELECTION_DIFFICULTY).asDouble())
            .leadershipElectionNonce(ELECTION_NONCE)
            .bakerElectionInfo(ImmutableList.of(ElectionInfoBaker.builder()
                    .baker(com.concordium.sdk.responses.BakerId.from(BAKER_ID))
                    .lotteryPower(LOTTERY_POWER)
                    .account(ACCOUNT_ADDRESS_1)
                    .build()))
            .build();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getElectionInfo(
                        com.concordium.grpc.v2.BlockHashInput request,
                        StreamObserver responseObserver) {
                    responseObserver.onNext(GRPC_ELECTION_INFO);
                    responseObserver.onCompleted();
                }
            }
    ));
    private static final com.concordium.grpc.v2.BlockHashInput BEST_BLOCK =
            com.concordium.grpc.v2.BlockHashInput.newBuilder().setBest(Empty.newBuilder().build()).build();

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
    public void getElectionInfo() {
        var electionInfo = client.getElectionInfo(BlockQuery.BEST);

        verify(serviceImpl).getElectionInfo(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(ELECTION_INFO_EXPECTED, electionInfo);
    }
}
