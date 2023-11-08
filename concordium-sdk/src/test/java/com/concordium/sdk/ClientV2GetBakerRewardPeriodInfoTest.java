package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.bls.BLSPublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.crypto.vrf.VRFPublicKey;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.accountinfo.CommissionRates;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.transactions.CCDAmount;
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

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;

/**
 * The test asserts that {@link ClientV2#getBakersRewardPeriod(BlockQuery)}
 * correctly converts the {@link BakerRewardPeriodInfo} returned by the node to {@link com.concordium.sdk.responses.bakersrewardperiod.BakerRewardPeriodInfo}
 */
public class ClientV2GetBakerRewardPeriodInfoTest {
    // ---- Test Values ----

    // For creating BakerInfo
    private static final int BAKER_ID_1 = 1;
    private static final int BAKER_ID_2 = 2;

    private static final byte[] BAKER_ELECTION_VERIFY_KEY_1
            = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();
    private static final byte[] BAKER_ELECTION_VERIFY_KEY_2
            = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();
    private static final byte[] BAKER_SIGNATURE_VERIFY_KEY_1
            = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();
    private static final byte[] BAKER_SIGNATURE_VERIFY_KEY_2
            = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();
    private static final byte[] BAKER_AGGREGATION_KEY_1
            = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();
    private static final byte[] BAKER_AGGREGATION_KEY_2
            = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();


    private static final int EFFECTIVE_STAKE_1 = 100;
    private static final int EFFECTIVE_STAKE_2 = 200;

    // For creating Commission Rates
    private static final int COM_FINAL_1 = 100;
    private static final int COM_FINAL_2 = 200;
    private static final int COM_BAKING_1 = 100;
    private static final int COM_BAKING_2 = 200;
    private static final int COM_TRAN_1 = 100;
    private static final int COM_TRAN_2 = 200;

    private static final int EQUITY_1 = 100;
    private static final int EQUITY_2 = 200;
    private static final int DELEGATED_1 = 100;
    private static final int DELEGATED_2 = 200;
    private static final boolean IS_FINALIZER_1 = true;
    private static final boolean IS_FINALIZER_2 = false;

    // ---- Clientside Test Values ----
    private static final com.concordium.sdk.responses.bakersrewardperiod.BakerInfo CLIENT_BAKER_INFO_1
            = com.concordium.sdk.responses.bakersrewardperiod.BakerInfo.builder()
            .bakerId(BakerId.from(BAKER_ID_1))
            .bakerElectionVerifyKey(VRFPublicKey.from(BAKER_ELECTION_VERIFY_KEY_1))
            .bakerSignatureVerifyKey(ED25519PublicKey.from(BAKER_SIGNATURE_VERIFY_KEY_1))
            .bakerAggregationVerifyKey(BLSPublicKey.from(BAKER_AGGREGATION_KEY_1))
            .build();

    private static final com.concordium.sdk.responses.bakersrewardperiod.BakerInfo CLIENT_BAKER_INFO_2
            = com.concordium.sdk.responses.bakersrewardperiod.BakerInfo.builder()
            .bakerId(BakerId.from(BAKER_ID_2))
            .bakerElectionVerifyKey(VRFPublicKey.from(BAKER_ELECTION_VERIFY_KEY_2))
            .bakerSignatureVerifyKey(ED25519PublicKey.from(BAKER_SIGNATURE_VERIFY_KEY_2))
            .bakerAggregationVerifyKey(BLSPublicKey.from(BAKER_AGGREGATION_KEY_2))
            .build();

    private static final com.concordium.sdk.responses.bakersrewardperiod.BakerRewardPeriodInfo CLIENT_INFO_1 =
            com.concordium.sdk.responses.bakersrewardperiod.BakerRewardPeriodInfo.builder()
                    .baker(CLIENT_BAKER_INFO_1)
                    .effectiveStake(CCDAmount.fromMicro(EFFECTIVE_STAKE_1))
                    .commissionRates(CommissionRates.builder()
                            .finalizationCommission(PartsPerHundredThousand.from(COM_FINAL_1).asDouble())
                            .bakingCommission(PartsPerHundredThousand.from(COM_BAKING_1).asDouble())
                            .transactionCommission(PartsPerHundredThousand.from(COM_TRAN_1).asDouble())
                            .build())
                    .equityCapital(CCDAmount.fromMicro(EQUITY_1))
                    .delegatedCapital(CCDAmount.fromMicro(DELEGATED_1))
                    .isFinalizer(IS_FINALIZER_1)
                    .build();
    private static final com.concordium.sdk.responses.bakersrewardperiod.BakerRewardPeriodInfo CLIENT_INFO_2 =
            com.concordium.sdk.responses.bakersrewardperiod.BakerRewardPeriodInfo.builder()
                    .baker(CLIENT_BAKER_INFO_2)
                    .effectiveStake(CCDAmount.fromMicro(EFFECTIVE_STAKE_2))
                    .commissionRates(CommissionRates.builder()
                            .finalizationCommission(PartsPerHundredThousand.from(COM_FINAL_2).asDouble())
                            .bakingCommission(PartsPerHundredThousand.from(COM_BAKING_2).asDouble())
                            .transactionCommission(PartsPerHundredThousand.from(COM_TRAN_2).asDouble())
                            .build())
                    .equityCapital(CCDAmount.fromMicro(EQUITY_2))
                    .delegatedCapital(CCDAmount.fromMicro(DELEGATED_2))
                    .isFinalizer(IS_FINALIZER_2)
                    .build();

    // ---- GRPC Test Values ----
    private static final BakerInfo GRPC_BAKER_1 = BakerInfo.newBuilder()
            .setBakerId(com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID_1))
            .setElectionKey(BakerElectionVerifyKey.newBuilder()
                    .setValue(ByteString.copyFrom(BAKER_ELECTION_VERIFY_KEY_1)).build())
            .setSignatureKey(BakerSignatureVerifyKey.newBuilder()
                    .setValue(ByteString.copyFrom(BAKER_SIGNATURE_VERIFY_KEY_1)).build())
            .setAggregationKey(BakerAggregationVerifyKey.newBuilder()
                    .setValue(ByteString.copyFrom(BAKER_AGGREGATION_KEY_1)).build())
            .build();
    private static final BakerInfo GRPC_BAKER_2 = BakerInfo.newBuilder()
            .setBakerId(com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID_2))
            .setElectionKey(BakerElectionVerifyKey.newBuilder()
                    .setValue(ByteString.copyFrom(BAKER_ELECTION_VERIFY_KEY_2)).build())
            .setSignatureKey(BakerSignatureVerifyKey.newBuilder()
                    .setValue(ByteString.copyFrom(BAKER_SIGNATURE_VERIFY_KEY_2)).build())
            .setAggregationKey(BakerAggregationVerifyKey.newBuilder()
                    .setValue(ByteString.copyFrom(BAKER_AGGREGATION_KEY_2)).build())
            .build();

    private static final BakerRewardPeriodInfo GRPC_INFO_1 =
            BakerRewardPeriodInfo.newBuilder()
                    .setBaker(GRPC_BAKER_1)
                    .setEffectiveStake(Amount.newBuilder().setValue(EFFECTIVE_STAKE_1))
                    .setCommissionRates(com.concordium.grpc.v2.CommissionRates.newBuilder()
                            .setFinalization(AmountFraction.newBuilder().setPartsPerHundredThousand(COM_FINAL_1))
                            .setBaking(AmountFraction.newBuilder().setPartsPerHundredThousand(COM_BAKING_1))
                            .setTransaction(AmountFraction.newBuilder().setPartsPerHundredThousand(COM_TRAN_1)))
                    .setEquityCapital(Amount.newBuilder().setValue(EQUITY_1))
                    .setDelegatedCapital(Amount.newBuilder().setValue(DELEGATED_1))
                    .setIsFinalizer(IS_FINALIZER_1)
                    .build();
    private static final BakerRewardPeriodInfo GRPC_INFO_2 =
            BakerRewardPeriodInfo.newBuilder()
                    .setBaker(GRPC_BAKER_2)
                    .setEffectiveStake(Amount.newBuilder().setValue(EFFECTIVE_STAKE_2))
                    .setCommissionRates(com.concordium.grpc.v2.CommissionRates.newBuilder()
                            .setFinalization(AmountFraction.newBuilder().setPartsPerHundredThousand(COM_FINAL_2))
                            .setBaking(AmountFraction.newBuilder().setPartsPerHundredThousand(COM_BAKING_2))
                            .setTransaction(AmountFraction.newBuilder().setPartsPerHundredThousand(COM_TRAN_2)))
                    .setEquityCapital(Amount.newBuilder().setValue(EQUITY_2))
                    .setDelegatedCapital(Amount.newBuilder().setValue(DELEGATED_2))
                    .setIsFinalizer(IS_FINALIZER_2)
                    .build();

   // ---- Setup ----
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getBakersRewardPeriod(
                        com.concordium.grpc.v2.BlockHashInput request,
                        StreamObserver<BakerRewardPeriodInfo> responseObserver) {
                    responseObserver.onNext(GRPC_INFO_1);
                    responseObserver.onNext(GRPC_INFO_2);
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
    public void shouldParseCorrectly() {
        val res = client.getBakersRewardPeriod(BlockQuery.BEST);
        val res1 = res.get(0);
        val res2 = res.get(1);
        assertEquals(CLIENT_INFO_1, res1);
        assertEquals(CLIENT_INFO_2, res2);
    }

}
