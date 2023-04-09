package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.responses.rewardstatus.RewardsOverview;
import com.concordium.sdk.transactions.CCDAmount;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientV2GetRewardsOverviewTest {
    private static final int TOTAL_AMOUNT = 10000;
    private static final int TOTAL_ENCRYPTED_AMOUNT = 3110;
    private static final int BAKING_REWARD_AMOUNT = 100;
    private static final int FINALIZATION_REWARD_AMOUNT = 340;
    private static final int GAS_AMOUNT = 10;
    private static final int FOUNDATION_TRANSACTIONS_AMOUNT = 13100;
    private static final int STAKED_CAPITAL = 850;
    private static final long NEXT_PAYDAY_TIME = 1669115119500L;
    private static final int MANTISSA = 231;
    private static final int EXPONENT = 4;
    private static final int PROTOCOL_VERSION = 4;


    private static final TokenomicsInfo GRPC_TOKENOMICS_INFO = TokenomicsInfo.newBuilder()
            .setV1(TokenomicsInfo.V1.newBuilder()
                    .setTotalAmount(Amount.newBuilder().setValue(TOTAL_AMOUNT).build())
                    .setTotalEncryptedAmount(Amount.newBuilder().setValue(TOTAL_ENCRYPTED_AMOUNT).build())
                    .setBakingRewardAccount(Amount.newBuilder().setValue(BAKING_REWARD_AMOUNT).build())
                    .setFinalizationRewardAccount(Amount.newBuilder().setValue(FINALIZATION_REWARD_AMOUNT).build())
                    .setGasAccount(Amount.newBuilder().setValue(GAS_AMOUNT).build())
                    .setFoundationTransactionRewards(Amount.newBuilder().setValue(FOUNDATION_TRANSACTIONS_AMOUNT).build())
                    .setNextPaydayTime(Timestamp.newBuilder().setValue(NEXT_PAYDAY_TIME).build())
                    .setNextPaydayMintRate(MintRate.newBuilder().setMantissa(MANTISSA).setExponent(EXPONENT).build())
                    .setTotalStakedCapital(Amount.newBuilder().setValue(STAKED_CAPITAL).build())
                    .setProtocolVersion(ProtocolVersion.valueOf(PROTOCOL_VERSION))
                    .build())
            .build();

    private static final RewardsOverview TOKENOMICS_INFO_EXPECTED = RewardsOverview.builder()
            .bakingRewardAccount(CCDAmount.fromMicro(BAKING_REWARD_AMOUNT))
            .finalizationRewardAccount(CCDAmount.fromMicro(FINALIZATION_REWARD_AMOUNT))
            .foundationTransactionRewards(CCDAmount.fromMicro(FOUNDATION_TRANSACTIONS_AMOUNT))
            .gasAccount(CCDAmount.fromMicro(GAS_AMOUNT))
            .nextPaydayTime(com.concordium.sdk.types.Timestamp.newMillis(NEXT_PAYDAY_TIME).getDate())
            .nextPaydayMintRate((double)MANTISSA * Math.pow(10, -1 * EXPONENT))
            .protocolVersion(com.concordium.sdk.responses.ProtocolVersion.forValue(PROTOCOL_VERSION + 1))
            .totalAmount(CCDAmount.fromMicro(TOTAL_AMOUNT))
            .totalEncryptedAmount(CCDAmount.fromMicro(TOTAL_ENCRYPTED_AMOUNT))
            .totalStakedCapital(CCDAmount.fromMicro(STAKED_CAPITAL))
            .build();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getTokenomicsInfo(
                        com.concordium.grpc.v2.BlockHashInput request,
                        io.grpc.stub.StreamObserver<com.concordium.grpc.v2.TokenomicsInfo> responseObserver) {
                    responseObserver.onNext(GRPC_TOKENOMICS_INFO);
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
        client = new ClientV2(10000, channel, Credentials.builder().build());
    }


    @Test
    public void getTokenomicsInfo() throws BlockNotFoundException {
        var res = client.getRewardStatus(com.concordium.sdk.requests.BlockHashInput.BEST);

        verify(serviceImpl).getTokenomicsInfo(any(BlockHashInput.class), any(StreamObserver.class));
        assertEquals(TOKENOMICS_INFO_EXPECTED.toString(), res.toString());
    }
}
