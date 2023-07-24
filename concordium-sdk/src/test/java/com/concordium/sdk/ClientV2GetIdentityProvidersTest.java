package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
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
public class ClientV2GetIdentityProvidersTest {

    private static final Integer IP_IDENTITY = 1;
    private static final String IP_NAME = "IP1";
    private static final String IP_DESCRIPTION = "IP Description";
    private static final String IP_URL = "IP_URL";
    private static final byte[] IP_CDI_VERIFY_KEY = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();
    private static final byte[] IP_VERIFY_KEY = new byte[]{4, 5, 6};
    private static final IpInfo GRPC_AR = IpInfo.newBuilder()
            .setIdentity(IpIdentity.newBuilder()
                    .setValue(IP_IDENTITY)
                    .build())
            .setDescription(Description.newBuilder()
                    .setName(IP_NAME)
                    .setDescription(IP_DESCRIPTION)
                    .setUrl(IP_URL)
                    .build())
            .setCdiVerifyKey(IpInfo.IpCdiVerifyKey.newBuilder()
                    .setValue(ByteString.copyFrom(IP_CDI_VERIFY_KEY))
                    .build())
            .setVerifyKey(IpInfo.IpVerifyKey.newBuilder()
                    .setValue(ByteString.copyFrom(IP_VERIFY_KEY))
                    .build()
            )
            .build();
    private static final IdentityProviderInfo AR_CLIENT = IdentityProviderInfo.builder()
            .ipIdentity(IP_IDENTITY)
            .description(com.concordium.sdk.responses.blocksummary.updates.queues.Description.builder()
                    .name(IP_NAME)
                    .url(IP_URL)
                    .description(IP_DESCRIPTION)
                    .build())
            .ipCdiVerifyKey(ED25519PublicKey.from(IP_CDI_VERIFY_KEY))
            .ipVerifyKey(PSPublicKey.from(IP_VERIFY_KEY))
            .build();
    private static final byte[] BLOCK_HASH = new byte[]{1, 1, 1};
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getIdentityProviders(
                        com.concordium.grpc.v2.BlockHashInput request,
                        StreamObserver<IpInfo> responseObserver) {
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
        client = new ClientV2(10000, channel);
    }

    @Test
    public void getIdentityProviders_BestBlock() {
        var IpInfos = client.getIdentityProviders(BlockHashInput.BEST);

        verify(serviceImpl).getIdentityProviders(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(IpInfos), ImmutableList.of(AR_CLIENT));
    }

    @Test
    public void getIdentityProviders_LastFinalBlock() {
        var IpInfos = client.getIdentityProviders(BlockHashInput.LAST_FINAL);

        verify(serviceImpl).getIdentityProviders(eq(LAST_FINAL_BLOCK), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(IpInfos), ImmutableList.of(AR_CLIENT));
    }

    @Test
    public void getIdentityProviders_GivenBlock() {
        var IpInfos = client.getIdentityProviders(
                BlockHashInput.GIVEN(Hash.from(BLOCK_HASH)));

        verify(serviceImpl).getIdentityProviders(eq(GIVEN_BLOCK_GRPC), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(IpInfos), ImmutableList.of(AR_CLIENT));
    }
}
