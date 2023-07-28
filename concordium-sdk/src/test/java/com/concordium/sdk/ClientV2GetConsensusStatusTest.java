package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.transactions.Hash;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientV2GetConsensusStatusTest {

    private static final byte[] BLOCK_HASH_1 = new byte[]{1, 1, 1};
    private static final byte[] BLOCK_HASH_2 = new byte[]{2, 2, 2};
    private static final byte[] BLOCK_HASH_3 = new byte[]{3, 3, 3};
    private static final byte[] BLOCK_HASH_4 = new byte[]{4, 4, 4};
    private static final int BLOCK_HEIGHT_1 = 1;
    private static final int BLOCK_HEIGHT_2 = 2;
    private static final long GENESIS_TIME = 1655114400000L;
    private static final long SLOT_DURATION = 250;
    private static final long EPOCH_DURATION = 3600000;
    private static final int BLOCKS_RECEIVED_COUNT = 760779;
    private static final long BLOCK_LAST_RECEIVED_TIME = 1678610173039L;
    private static final double BLOCK_RECEIVE_LATENCY_EMA = 0.16338705415134258;
    private static final double BLOCK_RECEIVE_LATENCY_EMSD = 2.4258358131021986;
    private static final double BLOCK_RECEIVE_PERIOD_EMA = 7.996527954472463;
    private static final double BLOCK_RECEIVE_PERIOD_EMSD = 8.583432940170946;
    private static final int BLOCKS_VERIFIED_COUNT = 762270;
    private static final long BLOCK_LAST_ARRIVED_TIME = 1678610173045L;
    private static final double BLOCK_ARRIVE_LATENCY_EMSD = 0.1262293411348596;
    private static final double BLOCK_ARRIVE_LATENCY_EMA = 0.16938180129275301;
    private static final double BLOCK_ARRIVE_PERIOD_EMA = 7.996570206499742;
    private static final double BLOCK_ARRIVE_PERIOD_EMSD = 8.583607847328718;
    private static final double TRANSACTIONS_PER_BLOCK_EMA = 8.6468349726549;
    private static final double TRANSACTIONS_PER_BLOCK_EMSD = 0.0029405374006973577;
    private static final int FINALIZATION_COUNT = 701050;
    private static final long LAST_FINALIZED_TIME = 1678610173968L;
    private static final double FINALIZATION_PERIOD_EMA = 8.578865859539938;
    private static final double FINALIZATION_PERIOD_EMSD = 9.053079310896797;
    private static final ProtocolVersion PROTOCOL_VERSION = ProtocolVersion.V4;
    private static final int GENESIS_INDEX = 2;
    private static final long CURRENT_ERA_GENESIS_TIME = 1669115119500L;

    private static final ConsensusInfo GRPC_CONSENSUS = ConsensusInfo.newBuilder()
            .setBestBlock(
                    BlockHash.newBuilder()
                            .setValue(ByteString.copyFrom(BLOCK_HASH_1)).build()
            )
            .setGenesisBlock(
                    BlockHash.newBuilder()
                            .setValue(ByteString.copyFrom(BLOCK_HASH_4)).build()
            )
            .setGenesisTime(
                    Timestamp.newBuilder()
                            .setValue(GENESIS_TIME).build()
            )
            .setSlotDuration(
                    Duration.newBuilder()
                            .setValue(SLOT_DURATION).build()
            )
            .setEpochDuration(
                    Duration.newBuilder()
                            .setValue(EPOCH_DURATION).build()
            )
            .setLastFinalizedBlock(
                    BlockHash.newBuilder()
                            .setValue(ByteString.copyFrom(BLOCK_HASH_2)).build()
            )
            .setBestBlockHeight(
                    AbsoluteBlockHeight.newBuilder()
                            .setValue(BLOCK_HEIGHT_1).build()
            )
            .setLastFinalizedBlockHeight(
                    AbsoluteBlockHeight.newBuilder()
                            .setValue(BLOCK_HEIGHT_2).build()
            )
            .setBlocksReceivedCount(BLOCKS_RECEIVED_COUNT)
            .setBlockLastReceivedTime(
                    Timestamp.newBuilder()
                            .setValue(BLOCK_LAST_RECEIVED_TIME).build()
            )
            .setBlockReceiveLatencyEma(BLOCK_RECEIVE_LATENCY_EMA)
            .setBlockReceiveLatencyEmsd(BLOCK_RECEIVE_LATENCY_EMSD)
            .setBlockReceivePeriodEma(BLOCK_RECEIVE_PERIOD_EMA)
            .setBlockReceivePeriodEmsd(BLOCK_RECEIVE_PERIOD_EMSD)
            .setBlocksVerifiedCount(BLOCKS_VERIFIED_COUNT)
            .setBlockLastArrivedTime(
                    Timestamp.newBuilder()
                            .setValue(BLOCK_LAST_ARRIVED_TIME).build()
            )
            .setBlockArriveLatencyEma(BLOCK_ARRIVE_LATENCY_EMA)
            .setBlockArriveLatencyEmsd(BLOCK_ARRIVE_LATENCY_EMSD)
            .setBlockArrivePeriodEma(BLOCK_ARRIVE_PERIOD_EMA)
            .setBlockArrivePeriodEmsd(BLOCK_ARRIVE_PERIOD_EMSD)
            .setTransactionsPerBlockEma(TRANSACTIONS_PER_BLOCK_EMA)
            .setTransactionsPerBlockEmsd(TRANSACTIONS_PER_BLOCK_EMSD)
            .setFinalizationCount(FINALIZATION_COUNT)
            .setLastFinalizedTime(
                    Timestamp.newBuilder()
                            .setValue(LAST_FINALIZED_TIME).build()
            )
            .setFinalizationPeriodEma(FINALIZATION_PERIOD_EMA)
            .setFinalizationPeriodEmsd(FINALIZATION_PERIOD_EMSD)
            .setProtocolVersion(
                    com.concordium.grpc.v2.ProtocolVersion.PROTOCOL_VERSION_4
            )
            .setGenesisIndex(
                    GenesisIndex.newBuilder()
                            .setValue(GENESIS_INDEX).build()
            )
            .setCurrentEraGenesisBlock(
                    BlockHash.newBuilder()
                            .setValue(ByteString.copyFrom(BLOCK_HASH_3)).build()
            )
            .setCurrentEraGenesisTime(
                    Timestamp.newBuilder()
                            .setValue(CURRENT_ERA_GENESIS_TIME).build()
            )
            .build();

    private static final ConsensusStatus CONSENSUS_INFO_RES_EXPECTED = ConsensusStatus.builder()
            .bestBlock(Hash.from(BLOCK_HASH_1))
            .genesisBlock(Hash.from(BLOCK_HASH_4))
            .genesisTime(com.concordium.sdk.types.Timestamp.newMillis(GENESIS_TIME))
            .slotDuration(java.time.Duration.ofMillis(SLOT_DURATION))
            .epochDuration(java.time.Duration.ofMillis(EPOCH_DURATION))
            .lastFinalizedBlock(Hash.from(BLOCK_HASH_2))
            .bestBlockHeight(BLOCK_HEIGHT_1)
            .lastFinalizedBlockHeight(BLOCK_HEIGHT_2)
            .blocksReceivedCount(BLOCKS_RECEIVED_COUNT)
            .blockLastReceivedTime(com.concordium.sdk.types.Timestamp.newMillis(BLOCK_LAST_RECEIVED_TIME))
            .blockReceiveLatencyEMA(BLOCK_RECEIVE_LATENCY_EMA)
            .blockReceiveLatencyEMSD(BLOCK_RECEIVE_LATENCY_EMSD)
            .blockReceivePeriodEMA(BLOCK_RECEIVE_PERIOD_EMA)
            .blockReceivePeriodEMSD(BLOCK_RECEIVE_PERIOD_EMSD)
            .blocksVerifiedCount(BLOCKS_VERIFIED_COUNT)
            .blockLastArrivedTime(com.concordium.sdk.types.Timestamp.newMillis(BLOCK_LAST_ARRIVED_TIME))
            .blockArriveLatencyEMA(BLOCK_ARRIVE_LATENCY_EMA)
            .blockArriveLatencyEMSD(BLOCK_ARRIVE_LATENCY_EMSD)
            .blockArrivePeriodEMA(BLOCK_ARRIVE_PERIOD_EMA)
            .blockArrivePeriodEMSD(BLOCK_ARRIVE_PERIOD_EMSD)
            .transactionsPerBlockEMA(TRANSACTIONS_PER_BLOCK_EMA)
            .transactionsPerBlockEMSD(TRANSACTIONS_PER_BLOCK_EMSD)
            .finalizationCount(FINALIZATION_COUNT)
            .lastFinalizedTime(com.concordium.sdk.types.Timestamp.newMillis(LAST_FINALIZED_TIME))
            .finalizationPeriodEMA(FINALIZATION_PERIOD_EMA)
            .finalizationPeriodEMSD(FINALIZATION_PERIOD_EMSD)
            .protocolVersion(PROTOCOL_VERSION)
            .genesisIndex(GENESIS_INDEX)
            .currentEraGenesisBlock(Hash.from(BLOCK_HASH_3))
            .currentEraGenesisTime(com.concordium.sdk.types.Timestamp.newMillis(CURRENT_ERA_GENESIS_TIME))
            .build();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getConsensusInfo(
                        Empty request,
                        StreamObserver<ConsensusInfo> responseObserver) {
                    responseObserver.onNext(GRPC_CONSENSUS);
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
        client = new ClientV2(10000, channel);
    }


    @Test
    public void getConsensusStatus() {
        var res = client.getConsensusInfo();

        verify(serviceImpl).getConsensusInfo(any(Empty.class), any(StreamObserver.class));
        assertEquals(CONSENSUS_INFO_RES_EXPECTED, res);
    }
}
