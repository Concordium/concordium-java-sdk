package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
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
public class ClientV2GetAnonymityRevokersTest {

    private static final Integer AR_IDENTITY = 1;
    private static final String AR_NAME = "AR1";
    private static final String AR_DESCRIPTION = "AR Description";
    private static final String AR_URL = "AR_URL";
    private static final byte[] AR_PUBLIC_KEY = new byte[]{1, 2, 3};
    private static final ArInfo GRPC_AR = ArInfo.newBuilder()
            .setIdentity(ArInfo.ArIdentity.newBuilder()
                    .setValue(AR_IDENTITY)
                    .build())
            .setDescription(Description.newBuilder()
                    .setName(AR_NAME)
                    .setDescription(AR_DESCRIPTION)
                    .setUrl(AR_URL)
                    .build())
            .setPublicKey(ArInfo.ArPublicKey.newBuilder().setValue(ByteString.copyFrom(AR_PUBLIC_KEY)).build())
            .build();
    private static final AnonymityRevokerInfo AR_CLIENT = AnonymityRevokerInfo.builder()
            .arIdentity(AR_IDENTITY)
            .arPublicKey(ElgamalPublicKey.from(AR_PUBLIC_KEY))
            .description(com.concordium.sdk.responses.blocksummary.updates.queues.Description.builder()
                    .name(AR_NAME)
                    .url(AR_URL)
                    .description(AR_DESCRIPTION)
                    .build())
            .build();
    private static final byte[] BLOCK_HASH = new byte[]{1, 1, 1};
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getAnonymityRevokers(
                        com.concordium.grpc.v2.BlockHashInput request,
                        StreamObserver<ArInfo> responseObserver) {
                    responseObserver.onNext(GRPC_AR);
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
    public void getAnonymityRevokers_BestBlock() {
        var arInfos = client.getAnonymityRevokers(BlockHashInput.BEST);

        verify(serviceImpl).getAnonymityRevokers(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(arInfos), ImmutableList.of(AR_CLIENT));
    }

    @Test
    public void getAnonymityRevokers_LastFinalBlock() {
        var arInfos = client.getAnonymityRevokers(BlockHashInput.LAST_FINAL);

        verify(serviceImpl).getAnonymityRevokers(eq(LAST_FINAL_BLOCK), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(arInfos), ImmutableList.of(AR_CLIENT));
    }

    @Test
    public void getAnonymityRevokers_GivenBlock() {
        var arInfos = client.getAnonymityRevokers(
                BlockHashInput.GIVEN(Hash.from(BLOCK_HASH)));

        verify(serviceImpl).getAnonymityRevokers(eq(GIVEN_BLOCK_GRPC), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(arInfos), ImmutableList.of(AR_CLIENT));
    }
}
