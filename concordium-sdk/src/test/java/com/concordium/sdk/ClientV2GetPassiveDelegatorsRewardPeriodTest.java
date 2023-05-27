package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.transactions.CCDAmount;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

/**
 * Mocks the GRPC Interface of the Node.
 * Tests the mapping of Requests and Responses for
 * {@link ClientV2#getPassiveDelegatorsRewardPeriod(BlockHashInput)}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetPassiveDelegatorsRewardPeriodTest {
    private static final com.concordium.sdk.transactions.AccountAddress ACCOUNT_ADDRESS_1
            = com.concordium.sdk.transactions.AccountAddress.from(
            "37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");
    private static final com.concordium.sdk.transactions.AccountAddress ACCOUNT_ADDRESS_2
            = com.concordium.sdk.transactions.AccountAddress.from(
            "3VQCZrqCGsUnKD4DXSWDu1ynsKqfRrrfF7cN51KszryYkHytt8");
    private static final long ACCOUNT_1_AMOUNT = 10;
    private static final long ACCOUNT_2_AMOUNT = 20;
    private static final com.concordium.sdk.responses.DelegatorRewardPeriodInfo POOL_DELEGATOR_1
            = com.concordium.sdk.responses.DelegatorRewardPeriodInfo.builder()
            .account(ACCOUNT_ADDRESS_1)
            .stake(CCDAmount.fromMicro(ACCOUNT_1_AMOUNT))
            .build();
    private static final com.concordium.sdk.responses.DelegatorRewardPeriodInfo POOL_DELEGATOR_2
            = com.concordium.sdk.responses.DelegatorRewardPeriodInfo.builder()
            .account(ACCOUNT_ADDRESS_2)
            .stake(CCDAmount.fromMicro(ACCOUNT_2_AMOUNT))
            .build();


    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getPassiveDelegatorsRewardPeriod(com.concordium.grpc.v2.BlockHashInput request, StreamObserver<DelegatorRewardPeriodInfo> responseObserver) {
                    responseObserver.onNext(DelegatorRewardPeriodInfo.newBuilder()
                            .setAccount(AccountAddress.newBuilder()
                                    .setValue(ByteString.copyFrom(ACCOUNT_ADDRESS_1.getBytes()))
                                    .build())
                            .setStake(Amount.newBuilder().setValue(ACCOUNT_1_AMOUNT).build())
                            .build());
                    responseObserver.onNext(DelegatorRewardPeriodInfo.newBuilder()
                            .setAccount(AccountAddress.newBuilder()
                                    .setValue(ByteString.copyFrom(ACCOUNT_ADDRESS_2.getBytes()))
                                    .build())
                            .setStake(Amount.newBuilder().setValue(ACCOUNT_2_AMOUNT).build())
                            .build());
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
        client = new ClientV2(10000, channel, Credentials.builder().build());
    }

    @Test
    public void getPassiveDelegatorsRewardPeriodTest() {
        var poolDelegatorsRewardPeriod = client.getPassiveDelegatorsRewardPeriod(BlockHashInput.BEST);

        verify(serviceImpl).getPassiveDelegatorsRewardPeriod(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(
                ImmutableList.copyOf(poolDelegatorsRewardPeriod),
                ImmutableList.of(POOL_DELEGATOR_1, POOL_DELEGATOR_2));
    }
}
