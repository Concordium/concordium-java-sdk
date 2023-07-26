package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.KeyValurPair;
import com.concordium.sdk.types.ContractAddress;
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

/**
 * Mocks the GRPC Interface on Node.
 * Tests Mapping of Request and Response from
 * {@link ClientV2#getInstanceState(BlockQuery, ContractAddress)} and
 * {@link ClientV2#instanceStateLookup(BlockQuery, ContractAddress, byte[])}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetInstanceStateTest {
    private static final byte[] INSTANCE_STATE_KEY = new byte[]{1, 1, 1};
    private static final byte[] INSTANCE_STATE_VALUE = new byte[]{2, 2, 2};
    private static final long CONTRACT_INDEX = 1;
    private static final long CONTRACT_SUBINDEX = 0;

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getInstanceState(InstanceInfoRequest request, StreamObserver<InstanceStateKVPair> responseObserver) {
                    responseObserver.onNext(InstanceStateKVPair.newBuilder()
                            .setKey(ByteString.copyFrom(INSTANCE_STATE_KEY))
                            .setValue(ByteString.copyFrom(INSTANCE_STATE_VALUE))
                            .build());
                    responseObserver.onCompleted();
                }

                @Override
                public void instanceStateLookup(InstanceStateLookupRequest request, StreamObserver<InstanceStateValueAtKey> responseObserver) {
                    responseObserver.onNext(InstanceStateValueAtKey.newBuilder()
                            .setValue(ByteString.copyFrom(INSTANCE_STATE_VALUE))
                            .build());
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
    public void getInstanceState() {
        var instanceState = client.getInstanceState(
                BlockQuery.BEST,
                ContractAddress.from(CONTRACT_INDEX, CONTRACT_SUBINDEX));

        verify(serviceImpl).getInstanceState(eq(InstanceInfoRequest.newBuilder()
                .setBlockHash(com.concordium.grpc.v2.BlockHashInput.newBuilder()
                        .setBest(Empty.getDefaultInstance())
                        .build())
                .setAddress(com.concordium.grpc.v2.ContractAddress.newBuilder()
                        .setIndex(CONTRACT_INDEX)
                        .setSubindex(CONTRACT_SUBINDEX)
                        .build())
                .build()), any(StreamObserver.class));
        assertEquals(KeyValurPair.builder()
                .key(INSTANCE_STATE_KEY)
                .value(INSTANCE_STATE_VALUE)
                .build(), instanceState.next());
    }

    @Test
    public void instanceStateLookup() {
        var value = client.instanceStateLookup(
                BlockQuery.BEST,
                ContractAddress.from(CONTRACT_INDEX, CONTRACT_SUBINDEX),
                INSTANCE_STATE_KEY);

        verify(serviceImpl).instanceStateLookup(
                eq(InstanceStateLookupRequest.newBuilder()
                        .setBlockHash(com.concordium.grpc.v2.BlockHashInput.newBuilder()
                                .setBest(Empty.getDefaultInstance())
                                .build())
                        .setAddress(com.concordium.grpc.v2.ContractAddress.newBuilder()
                                .setIndex(CONTRACT_INDEX)
                                .setSubindex(CONTRACT_SUBINDEX)
                                .build())
                        .setKey(ByteString.copyFrom(INSTANCE_STATE_KEY))
                        .build()),
                any(StreamObserver.class));

        assertArrayEquals(INSTANCE_STATE_VALUE, value);
    }
}
