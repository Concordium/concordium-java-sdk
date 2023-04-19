package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.blockinfo.BlockInfo;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.Timestamp;
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

import java.time.OffsetDateTime;

import static com.concordium.sdk.Constants.UTC_ZONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetBlockInfoTest {

    private static final byte[] BLOCK_HASH = new byte[]{1, 1, 1};
    private static final int BLOCK_HEIGHT = 1;
    private static final int TRANSACTION_ENERGY_COST = 1;
    private static final int BLOCK_BAKER = 1;
    private static final byte[] BLOCK_STATE_HASH = new byte[]{3, 6, 7};
    private static final byte[] PARENT_BLOCK_HASH = new byte[]{3, 6, 7};
    private static final Timestamp BLOCK_SLOT_TIME = Timestamp.newSeconds(1000000);
    private static final Timestamp BLOCK_RECEIVE_TIME = Timestamp.newSeconds(1420000);
    private static final int GENESIS_INDEX = 2;
    private static final int BLOCK_SLOT = 3;
    private static final boolean FINALIZED = true;
    private static final int ERA_BLOCK_HEIGHT = 7;
    private static final byte[] LAST_FINALIZED_BLOCK_HASH = new byte[]{3, 6, 7};
    private static final Timestamp BLOCK_ARRIVAL_TIME = Timestamp.newSeconds(12111000);
    private static final int TRANSACTION_SIZE = 221;
    private static final int TRANSACTION_COUNT = 13;

    private static final com.concordium.grpc.v2.BlockInfo GRPC_BLOCK_INFO = com.concordium.grpc.v2.BlockInfo.newBuilder()
            .setHash(
                    BlockHash.newBuilder()
                            .setValue(ByteString.copyFrom(BLOCK_HASH)).build()
            )
            .setHeight(
                    AbsoluteBlockHeight.newBuilder()
                            .setValue(BLOCK_HEIGHT).build()
            )
            .setParentBlock(
                    BlockHash.newBuilder()
                            .setValue(ByteString.copyFrom(PARENT_BLOCK_HASH)).build()
            )
            .setLastFinalizedBlock(
                    BlockHash.newBuilder()
                            .setValue(ByteString.copyFrom(LAST_FINALIZED_BLOCK_HASH)).build()
            )
            .setGenesisIndex(
                    GenesisIndex.newBuilder()
                            .setValue(GENESIS_INDEX).build()
            )
            .setEraBlockHeight(
                    BlockHeight.newBuilder()
                            .setValue(ERA_BLOCK_HEIGHT).build()
            )
            .setReceiveTime(
                    com.concordium.grpc.v2.Timestamp.newBuilder()
                            .setValue(BLOCK_RECEIVE_TIME.getMillis()).build()
            )
            .setArriveTime(
                    com.concordium.grpc.v2.Timestamp.newBuilder()
                            .setValue(BLOCK_ARRIVAL_TIME.getMillis()).build()
            )
            .setSlotNumber(
                    Slot.newBuilder()
                            .setValue(BLOCK_SLOT).build()
            )
            .setSlotTime(
                    com.concordium.grpc.v2.Timestamp.newBuilder()
                            .setValue(BLOCK_SLOT_TIME.getMillis()).build()
            )
            .setBaker(
                    BakerId.newBuilder()
                            .setValue(BLOCK_BAKER).build()
            )
            .setFinalized(FINALIZED)
            .setTransactionCount(TRANSACTION_COUNT)
            .setTransactionsEnergyCost(
                    Energy.newBuilder()
                            .setValue(TRANSACTION_ENERGY_COST).build()
            )
            .setTransactionsSize(TRANSACTION_SIZE)
            .setStateHash(
                    StateHash.newBuilder()
                            .setValue(ByteString.copyFrom(BLOCK_STATE_HASH)).build()
            )

            .build();
    private static final com.concordium.sdk.responses.blockinfo.BlockInfo GET_BLOCK_INFO_RESPONSE = BlockInfo.builder()
            .blockHash(Hash.from(BLOCK_HASH))
            .blockHeight(UInt64.from(BLOCK_HEIGHT))
            .transactionEnergyCost(TRANSACTION_ENERGY_COST)
            .blockBaker(BLOCK_BAKER)
            .blockStateHash(Hash.from(BLOCK_STATE_HASH))
            .blockSlotTime(OffsetDateTime.ofInstant(BLOCK_SLOT_TIME.getDate().toInstant(), UTC_ZONE))
            .blockParent(Hash.from(PARENT_BLOCK_HASH))
            .blockReceiveTime(OffsetDateTime.ofInstant(BLOCK_RECEIVE_TIME.getDate().toInstant(), UTC_ZONE))
            .genesisIndex(GENESIS_INDEX)
            .blockSlot(BLOCK_SLOT)
            .finalized(FINALIZED)
            .eraBlockHeight(ERA_BLOCK_HEIGHT)
            .blockLastFinalized(Hash.from(LAST_FINALIZED_BLOCK_HASH))
            .transactionsSize(TRANSACTION_SIZE)
            .transactionCount(TRANSACTION_COUNT)
            .blockArriveTime(OffsetDateTime.ofInstant(BLOCK_ARRIVAL_TIME.getDate().toInstant(), UTC_ZONE))
            .build();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getBlockInfo(
                        com.concordium.grpc.v2.BlockHashInput request,
                        StreamObserver responseObserver) {
                    responseObserver.onNext(GRPC_BLOCK_INFO);
                    responseObserver.onCompleted();
                }
            }
    ));
    private static final com.concordium.grpc.v2.BlockHashInput BEST_BLOCK =
            com.concordium.grpc.v2.BlockHashInput.newBuilder().setBest(Empty.newBuilder().build()).build();
    private static final com.concordium.grpc.v2.BlockHashInput LAST_FINAL_BLOCK =
            com.concordium.grpc.v2.BlockHashInput.newBuilder().setLastFinal(Empty.newBuilder().build()).build();
    private static final com.concordium.grpc.v2.BlockHashInput GIVEN_BLOCK_GRPC = com.concordium.grpc.v2.BlockHashInput
            .newBuilder()
            .setGiven(
                    BlockHash
                            .newBuilder()
                            .setValue(ByteString.copyFrom(BLOCK_HASH)).build()).build();

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
    public void getBlockInfo_BestBlock() throws BlockNotFoundException {
        var blockInfo = client.getBlockInfo(BlockHashInput.BEST);

        verify(serviceImpl).getBlockInfo(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(blockInfo, GET_BLOCK_INFO_RESPONSE);
    }

    @Test
    public void getBlockInfo_LastFinalBlock() throws BlockNotFoundException {
        var blockInfo = client.getBlockInfo(BlockHashInput.LAST_FINAL);

        verify(serviceImpl).getBlockInfo(eq(LAST_FINAL_BLOCK), any(StreamObserver.class));
        assertEquals(blockInfo, GET_BLOCK_INFO_RESPONSE);
    }

    @Test
    public void getBlockInfo_GivenBlock() throws BlockNotFoundException {
        var blockInfo = client.getBlockInfo(
                BlockHashInput.GIVEN(Hash.from(BLOCK_HASH)));

        verify(serviceImpl).getBlockInfo(eq(GIVEN_BLOCK_GRPC), any(StreamObserver.class));
        assertEquals(blockInfo, GET_BLOCK_INFO_RESPONSE);
    }
}