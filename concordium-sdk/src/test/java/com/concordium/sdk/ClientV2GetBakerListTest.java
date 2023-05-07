package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.BakerId;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetBakerListTest {
    private static final com.concordium.grpc.v2.BakerId GRPC_BAKER_ID = com.concordium.grpc.v2.BakerId.newBuilder()
            .setValue(51)
            .build();
    private static final BakerId BAKER_ID_CLIENT = BakerId.from(51);
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getBakerList(
                        com.concordium.grpc.v2.BlockHashInput request,
                        StreamObserver<com.concordium.grpc.v2.BakerId> responseObserver) {
                    responseObserver.onNext(GRPC_BAKER_ID);
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
    public void getBakerList() throws BlockNotFoundException {
        var bakerList = client.getBakerList(BlockHashInput.BEST);

        verify(serviceImpl).getBakerList(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(bakerList), ImmutableList.of(BAKER_ID_CLIENT));
    }

}
