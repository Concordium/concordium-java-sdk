package com.concordium.sdk;

import com.concordium.grpc.v2.Empty;
import com.concordium.grpc.v2.ModuleRef;
import com.concordium.grpc.v2.QueriesGrpc;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.requests.BlockHashInput;
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
 * Tests the mapping of Requests and Responses for {@link ClientV2#getModuleList(BlockHashInput)}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetModuleListTest {
    private static final byte[] MODULE_REF_1 = new byte[]{1, 1, 1};
    private static final byte[] MODULE_REF_2 = new byte[]{2, 2, 2};
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getModuleList(
                        com.concordium.grpc.v2.BlockHashInput request,
                        StreamObserver<ModuleRef> responseObserver) {
                    responseObserver.onNext(ModuleRef.newBuilder()
                            .setValue(ByteString.copyFrom(MODULE_REF_1))
                            .build());
                    responseObserver.onNext(ModuleRef.newBuilder()
                            .setValue(ByteString.copyFrom(MODULE_REF_2))
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
    public void getModuleList() {
        var moduleList = client.getModuleList(BlockHashInput.BEST);

        verify(serviceImpl).getModuleList(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(moduleList), ImmutableList.of(
                com.concordium.sdk.responses.modulelist.ModuleRef.from(MODULE_REF_1),
                com.concordium.sdk.responses.modulelist.ModuleRef.from(MODULE_REF_2)));
    }

}
