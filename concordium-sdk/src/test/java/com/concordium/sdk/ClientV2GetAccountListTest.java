package com.concordium.sdk;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.BlockHash;
import com.concordium.grpc.v2.Empty;
import com.concordium.grpc.v2.QueriesGrpc;
import com.concordium.sdk.requests.BlockHashInput;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetAccountListTest {

    private static final com.concordium.sdk.transactions.AccountAddress CLIENT_ACCOUNT_ADDRESS = com.concordium.sdk.transactions.AccountAddress
            .from(
                    "37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");
    private static final AccountAddress GRPC_ACCOUNT_ADDRESS = AccountAddress.newBuilder()
            .setValue(ByteString.copyFrom(CLIENT_ACCOUNT_ADDRESS.getBytes()))
            .build();
    private static final byte[] BLOCK_HASH = new byte[]{1, 1, 1};
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class,
            delegatesTo(
                    new QueriesGrpc.QueriesImplBase() {
                        @Override
                        public void getAccountList(
                                com.concordium.grpc.v2.BlockHashInput request,
                                StreamObserver<AccountAddress> responseObserver) {
                            responseObserver.onNext(GRPC_ACCOUNT_ADDRESS);
                            responseObserver.onCompleted();
                        }
                    }));
    private static final com.concordium.grpc.v2.BlockHashInput BEST_BLOCK = com.concordium.grpc.v2.BlockHashInput
            .newBuilder().setBest(Empty.newBuilder().build()).build();
    private static final com.concordium.grpc.v2.BlockHashInput LAST_FINAL_BLOCK = com.concordium.grpc.v2.BlockHashInput
            .newBuilder().setLastFinal(Empty.newBuilder().build()).build();
    private static final com.concordium.grpc.v2.BlockHashInput GIVEN_BLOCK_GRPC = com.concordium.grpc.v2.BlockHashInput
            .newBuilder()
            .setGiven(
                    BlockHash
                            .newBuilder()
                            .setValue(ByteString.copyFrom(BLOCK_HASH)).build())
            .build();

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
    public void getAccountList_BestBlock() {
        var output = client.getAccountList(BlockHashInput.BEST);

        verify(serviceImpl).getAccountList(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(output), ImmutableList.of(CLIENT_ACCOUNT_ADDRESS));
    }

    @Test
    public void getAccountList_LastFinalBlock() {
        var output = client.getAccountList(BlockHashInput.LAST_FINAL);

        verify(serviceImpl).getAccountList(eq(LAST_FINAL_BLOCK), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(output), ImmutableList.of(CLIENT_ACCOUNT_ADDRESS));
    }

    @Test
    public void getAccountList_GivenBlock() {
        var output = client.getAccountList(
                BlockHashInput.GIVEN(Hash.from(BLOCK_HASH)));

        verify(serviceImpl).getAccountList(eq(GIVEN_BLOCK_GRPC), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(output), ImmutableList.of(CLIENT_ACCOUNT_ADDRESS));
    }
}
