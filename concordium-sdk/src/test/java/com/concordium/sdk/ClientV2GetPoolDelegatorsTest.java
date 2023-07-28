package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.accountinfo.ReduceStakeChange;
import com.concordium.sdk.responses.accountinfo.RemoveStakeChange;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Hash;
import com.google.common.collect.ImmutableList;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link ClientV2#getPoolDelegators(BlockQuery, com.concordium.sdk.responses.AccountIndex)}.
 * <br/>
 * Tests the mapping code {@link ClientV2MapperExtensions#to(DelegatorInfo)}
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetPoolDelegatorsTest {
    private static final byte[] BLOCK_HASH = new byte[]{1, 1, 1};
    private static final com.concordium.sdk.types.AccountAddress ACCOUNT_ADDRESS_1
            = com.concordium.sdk.types.AccountAddress.from(
            "37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");
    private static final com.concordium.sdk.types.AccountAddress ACCOUNT_ADDRESS_2
            = com.concordium.sdk.types.AccountAddress.from(
            "3VQCZrqCGsUnKD4DXSWDu1ynsKqfRrrfF7cN51KszryYkHytt8");
    private static final com.concordium.sdk.types.AccountAddress ACCOUNT_ADDRESS_3
            = com.concordium.sdk.types.AccountAddress.from(
            "35CJPZohio6Ztii2zy1AYzJKvuxbGG44wrBn7hLHiYLoF2nxnh");
    private static final long STAKE_AMOUNT = 10L;
    private static final long STAKE_REMOVE_TIME = 1000000;
    private static final DelegatorInfo DELEGATOR_INFO_GRPC_1 = DelegatorInfo.newBuilder()
            .setAccount(AccountAddress.newBuilder().setValue(ByteString.copyFrom(ACCOUNT_ADDRESS_1.getBytes())).build())
            .setStake(Amount.newBuilder().setValue(STAKE_AMOUNT).build())
            .build();

    private static final DelegatorInfo DELEGATOR_INFO_GRPC_2 = DelegatorInfo.newBuilder()
            .setAccount(AccountAddress.newBuilder().setValue(ByteString.copyFrom(ACCOUNT_ADDRESS_2.getBytes())).build())
            .setStake(Amount.newBuilder().setValue(STAKE_AMOUNT).build())
            .setPendingChange(StakePendingChange.newBuilder()
                    .setRemove(Timestamp.newBuilder().setValue(STAKE_REMOVE_TIME).build())
                    .build())
            .build();

    private static final long EFFECTIVE_TIME = 200000000;
    private static final long NEW_STAKE = 20L;
    private static final DelegatorInfo DELEGATOR_INFO_GRPC_3 = DelegatorInfo.newBuilder()
            .setAccount(AccountAddress.newBuilder().setValue(ByteString.copyFrom(ACCOUNT_ADDRESS_3.getBytes())).build())
            .setStake(Amount.newBuilder().setValue(STAKE_AMOUNT).build())
            .setPendingChange(StakePendingChange.newBuilder()
                    .setReduce(StakePendingChange.Reduce.newBuilder()
                            .setEffectiveTime(Timestamp.newBuilder().setValue(EFFECTIVE_TIME).build())
                            .setNewStake(Amount.newBuilder().setValue(NEW_STAKE).build())
                            .build())
                    .build())
            .build();

    private static final List<com.concordium.sdk.responses.DelegatorInfo> EXPECTED_DELEGATORS = ImmutableList.of(
            com.concordium.sdk.responses.DelegatorInfo.builder()
                    .account(ACCOUNT_ADDRESS_1)
                    .stake(CCDAmount.fromMicro(STAKE_AMOUNT))
                    .pendingChange(Optional.empty())
                    .build(),
            com.concordium.sdk.responses.DelegatorInfo.builder()
                    .account(ACCOUNT_ADDRESS_2)
                    .stake(CCDAmount.fromMicro(STAKE_AMOUNT))
                    .pendingChange(Optional.of(RemoveStakeChange.builder()
                            .effectiveTime(com.concordium.sdk.types.Timestamp.newMillis(STAKE_REMOVE_TIME))
                            .build()))
                    .build(),
            com.concordium.sdk.responses.DelegatorInfo.builder()
                    .account(ACCOUNT_ADDRESS_3)
                    .stake(CCDAmount.fromMicro(STAKE_AMOUNT))
                    .pendingChange(Optional.of(
                            ReduceStakeChange.builder()
                                    .newStake(CCDAmount.fromMicro(NEW_STAKE))
                                    .effectiveTime(com.concordium.sdk.types.Timestamp.newMillis(EFFECTIVE_TIME))
                                    .build()
                    ))
                    .build()
    );

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getPoolDelegators(
                        GetPoolDelegatorsRequest request,
                        StreamObserver<DelegatorInfo> responseObserver) {
                    responseObserver.onNext(DELEGATOR_INFO_GRPC_1);
                    responseObserver.onNext(DELEGATOR_INFO_GRPC_2);
                    responseObserver.onNext(DELEGATOR_INFO_GRPC_3);
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
    private static final long BAKER_ID = 2924;

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
    public void getPoolDelegators_BestBlock() {
        var delegators = client.getPoolDelegators(
                BlockQuery.BEST,
                com.concordium.sdk.responses.AccountIndex.from(BAKER_ID));

        verify(serviceImpl).getPoolDelegators(eq(
                GetPoolDelegatorsRequest.newBuilder()
                        .setBlockHash(BEST_BLOCK)
                        .setBaker(BakerId.newBuilder().setValue(BAKER_ID).build())
                        .build()), any(StreamObserver.class));
        assertEquals(EXPECTED_DELEGATORS, ImmutableList.copyOf(delegators));
    }

    @Test
    public void getPoolDelegators_LastFinalBlock() {
        var delegators = client.getPoolDelegators(
                BlockQuery.LAST_FINAL,
                com.concordium.sdk.responses.AccountIndex.from(BAKER_ID));

        verify(serviceImpl).getPoolDelegators(eq(
                GetPoolDelegatorsRequest.newBuilder()
                        .setBlockHash(LAST_FINAL_BLOCK)
                        .setBaker(BakerId.newBuilder().setValue(BAKER_ID).build())
                        .build()), any(StreamObserver.class));
        assertEquals(EXPECTED_DELEGATORS, ImmutableList.copyOf(delegators));
    }

    @Test
    public void getPoolDelegators_GivenBlock() {
        var delegators = client.getPoolDelegators(
                BlockQuery.HASH(Hash.from(BLOCK_HASH)),
                com.concordium.sdk.responses.AccountIndex.from(BAKER_ID));

        verify(serviceImpl).getPoolDelegators(eq(
                GetPoolDelegatorsRequest.newBuilder()
                        .setBlockHash(GIVEN_BLOCK_GRPC)
                        .setBaker(BakerId.newBuilder().setValue(BAKER_ID).build())
                        .build()), any(StreamObserver.class));
        assertEquals(EXPECTED_DELEGATORS, ImmutableList.copyOf(delegators));
    }
}
