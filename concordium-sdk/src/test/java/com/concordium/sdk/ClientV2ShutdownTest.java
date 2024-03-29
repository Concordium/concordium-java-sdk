package com.concordium.sdk;

import com.concordium.grpc.v2.Empty;
import com.concordium.grpc.v2.QueriesGrpc;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

/**
 * Mocks the GRPC Interface of the Node.
 * Tests the mapping of Requests and Responses for {@link ClientV2#shutdown()}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2ShutdownTest {
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void shutdown(Empty request, StreamObserver<Empty> responseObserver) {
                    responseObserver.onNext(Empty.getDefaultInstance());
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
        client = new ClientV2(10000, channel, Optional.empty());
    }

    @Test
    public void shutdown() {
        client.shutdown();

        verify(serviceImpl).shutdown(eq(Empty.getDefaultInstance()), any(StreamObserver.class));
    }

}
