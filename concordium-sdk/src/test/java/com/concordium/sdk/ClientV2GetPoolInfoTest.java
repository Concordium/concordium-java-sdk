package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.poolstatus.BakerPoolStatus;
import com.concordium.sdk.responses.poolstatus.CurrentPaydayStatus;
import com.concordium.sdk.responses.poolstatus.PendingChangeReduceBakerCapital;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.UInt64;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.NonNull;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

/**
 * Tests Mapping from GRPC response using {@link ClientV2MapperExtensions#to(PoolInfoResponse)}
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetPoolInfoTest {

    private static final com.concordium.sdk.types.AccountAddress ACCOUNT_ADDRESS_1
            = com.concordium.sdk.types.AccountAddress.from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");
    private static final long BAKER_ID = 1;
    private static final String BAKER_POOL_URL = "www.example-baker-pool.com";
    private static final int COMMISSION_BAKING_PPHT = 10;
    private static final int COMMISSION_FINALIZATION_PPHT = 11;
    private static final int COMMISSION_TRANSACTION_PPHT = 12;
    private static final long BAKER_REDUCE_STAKE_AMOUNT = 100;
    private static final long BAKER_REDUCE_STAKE_TIME = 2;
    private static final long POOL_TOTAL_CAPITAL = 10;
    private static final long EQUITY_CAPITAL = 11;
    private static final long BLOCKS_BAKED = 12;
    private static final long DELEGATED_CAPITAL = 13;
    private static final long EFFECTIVE_STAKE = 14;
    private static final double LOTTERY_POWER = 15.90;
    private static final boolean FINALIZATION_LIVE = true;
    private static final long DELEGATED_CAPITAL_CAP = 16;
    private static final long TRANSACTION_FEES = 17;

    private static final PoolPendingChange PENDING_CHANGE = PoolPendingChange.newBuilder()
            .setReduce(PoolPendingChange.Reduce.newBuilder()
                    .setEffectiveTime(Timestamp.newBuilder()
                            .setValue(BAKER_REDUCE_STAKE_TIME)
                            .build())
                    .setReducedEquityCapital(to(BAKER_REDUCE_STAKE_AMOUNT))
                    .build())
            .build();
    private static final PoolInfoResponse POOL_INFO_GRPC = PoolInfoResponse.newBuilder()
            .setBaker(com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID).build())
            .setAddress(AccountAddress.newBuilder()
                    .setValue(ByteString.copyFrom(ACCOUNT_ADDRESS_1.getBytes()))
                    .build())
            .setPoolInfo(BakerPoolInfo.newBuilder()
                    .setUrl(BAKER_POOL_URL)
                    .setCommissionRates(CommissionRates.newBuilder()
                            .setBaking(toAmountFrac(COMMISSION_BAKING_PPHT))
                            .setFinalization(toAmountFrac(COMMISSION_FINALIZATION_PPHT))
                            .setTransaction(toAmountFrac(COMMISSION_TRANSACTION_PPHT))
                            .build())
                    .setOpenStatus(OpenStatus.OPEN_STATUS_OPEN_FOR_ALL)
                    .build())
            .setAllPoolTotalCapital(to(POOL_TOTAL_CAPITAL))
            .setCurrentPaydayInfo(PoolCurrentPaydayInfo.newBuilder()
                    .setBakerEquityCapital(to(EQUITY_CAPITAL))
                    .setBlocksBaked(BLOCKS_BAKED)
                    .setDelegatedCapital(to(DELEGATED_CAPITAL))
                    .setEffectiveStake(to(EFFECTIVE_STAKE))
                    .setLotteryPower(LOTTERY_POWER)
                    .setFinalizationLive(FINALIZATION_LIVE)
                    .setTransactionFeesEarned(to(TRANSACTION_FEES))
                    .build())
            .setDelegatedCapital(to(DELEGATED_CAPITAL))
            .setDelegatedCapitalCap(to(DELEGATED_CAPITAL_CAP))
            .setEquityCapital(to(EQUITY_CAPITAL))
            .setEquityPendingChange(PENDING_CHANGE)
            .build();
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getPoolInfo(PoolInfoRequest request, StreamObserver<PoolInfoResponse> responseObserver) {
                    responseObserver.onNext(POOL_INFO_GRPC);
                    responseObserver.onCompleted();
                }
            }
    ));

    private static final BakerPoolStatus POOL_INFO_EXPECTED = BakerPoolStatus.builder()
            .bakerId(BakerId.from(BAKER_ID))
            .bakerAddress(ACCOUNT_ADDRESS_1)
            .bakerStakePendingChange(PendingChangeReduceBakerCapital.builder()
                    .effectiveTime(com.concordium.sdk.types.Timestamp.newMillis(BAKER_REDUCE_STAKE_TIME))
                    .bakerEquityCapital(CCDAmount.fromMicro(BAKER_REDUCE_STAKE_AMOUNT))
                    .build())
            .delegatedCapitalCap(CCDAmount.fromMicro(DELEGATED_CAPITAL))
            .delegatedCapitalCap(CCDAmount.fromMicro(DELEGATED_CAPITAL_CAP))
            .allPoolTotalCapital(CCDAmount.fromMicro(POOL_TOTAL_CAPITAL))
            .bakerEquityCapital(CCDAmount.fromMicro(EQUITY_CAPITAL))
            .currentPaydayStatus(CurrentPaydayStatus.builder()
                    .transactionFeesEarned(CCDAmount.fromMicro(TRANSACTION_FEES))
                    .lotteryPower(LOTTERY_POWER)
                    .finalizationLive(FINALIZATION_LIVE)
                    .effectiveStake(CCDAmount.fromMicro(EFFECTIVE_STAKE))
                    .delegatedCapital(CCDAmount.fromMicro(DELEGATED_CAPITAL))
                    .blocksBaked(UInt64.from(BLOCKS_BAKED))
                    .bakerEquityCapital(CCDAmount.fromMicro(EQUITY_CAPITAL))
                    .build())
            .delegatedCapital(CCDAmount.fromMicro(DELEGATED_CAPITAL))
            .poolInfo(com.concordium.sdk.responses.accountinfo.BakerPoolInfo.builder()
                    .openStatus(com.concordium.sdk.responses.transactionstatus.OpenStatus.OPEN_FOR_ALL)
                    .commissionRates(com.concordium.sdk.responses.accountinfo.CommissionRates.builder()
                            .bakingCommission((double) COMMISSION_BAKING_PPHT / 100_000D)
                            .finalizationCommission((double) COMMISSION_FINALIZATION_PPHT / 100_000D)
                            .transactionCommission((double) COMMISSION_TRANSACTION_PPHT / 100_000D)
                            .build())
                    .metadataUrl(BAKER_POOL_URL)
                    .build())
            .build();
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
    private ClientV2 client;

    @NonNull
    private static Amount to(long value) {
        return Amount.newBuilder()
                .setValue(value)
                .build();
    }

    private static AmountFraction toAmountFrac(int commissionPartsPerHundredThousand) {
        return AmountFraction.newBuilder().setPartsPerHundredThousand(commissionPartsPerHundredThousand).build();
    }


    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel);
    }

    @Test
    public void getPoolInfoTest() {
        var res = client.getPoolInfo(BlockQuery.BEST, BakerId.from(BAKER_ID));

        verify(serviceImpl).getPoolInfo(eq(PoolInfoRequest.newBuilder()
                .setBaker(com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID).build())
                .setBlockHash(com.concordium.grpc.v2.BlockHashInput.newBuilder().setBest(Empty.getDefaultInstance()).build())
                .build()), any(StreamObserver.class));
        assertEquals(POOL_INFO_EXPECTED, res);
    }
}
