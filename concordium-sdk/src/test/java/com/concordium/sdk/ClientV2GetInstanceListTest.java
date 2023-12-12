package com.concordium.sdk;

import com.concordium.grpc.v2.ContractAddress;
import com.concordium.grpc.v2.Empty;
import com.concordium.grpc.v2.QueriesGrpc;
import com.concordium.sdk.requests.BlockQuery;
import com.google.common.collect.ImmutableList;
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

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

/**
 * Mocks the GRPC Interface of the Node.
 * Tests the mapping of Requests and Responses for {@link ClientV2#getInstanceList(BlockQuery)}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetInstanceListTest {
    private static final ContractAddress GRPC_CONTRACT_ADDRESS = ContractAddress.newBuilder().setIndex(1).setSubindex(0).build();
    private static final com.concordium.sdk.types.ContractAddress CONTRACT_ADDRESS_CLIENT = com.concordium.sdk.types.ContractAddress.from(1, 0);
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getInstanceList(
                        com.concordium.grpc.v2.BlockHashInput request,
                        io.grpc.stub.StreamObserver<com.concordium.grpc.v2.ContractAddress> responseObserver) {
                    responseObserver.onNext(GRPC_CONTRACT_ADDRESS);
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
        client = new ClientV2(10000, channel, Optional.empty());
    }

    @Test
    public void getInstanceList() {
        var instanceList = client.getInstanceList(BlockQuery.BEST);

        verify(serviceImpl).getInstanceList(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(instanceList), ImmutableList.of(CONTRACT_ADDRESS_CLIENT));
    }

}