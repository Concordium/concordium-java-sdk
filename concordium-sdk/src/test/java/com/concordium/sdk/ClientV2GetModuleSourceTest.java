package com.concordium.sdk;

import com.concordium.grpc.v2.Empty;
import com.concordium.grpc.v2.ModuleSourceRequest;
import com.concordium.grpc.v2.QueriesGrpc;
import com.concordium.grpc.v2.VersionedModuleSource;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.transactions.smartcontracts.WasmModuleVersion;
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

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

/**
 * Mocks the GRPC interface of the Node.
 * Tests mapping of Request and Response from {@link ClientV2#getModuleSource(ModuleRef)}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetModuleSourceTest {
    private static final byte[] MODULE_REF = new byte[]{10, 10, 10};
    private static final byte[] MODULE_SOURCE = new byte[]{1, 1, 1};
    private static final VersionedModuleSource GRPC_RES_1 = VersionedModuleSource.newBuilder()
            .setV1(VersionedModuleSource.ModuleSourceV1.newBuilder()
                    .setValue(ByteString.copyFrom(MODULE_SOURCE))
                    .build())
            .build();
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getModuleSource(
                        ModuleSourceRequest request,
                        StreamObserver<VersionedModuleSource> responseObserver) {
                    responseObserver.onNext(GRPC_RES_1);
                    responseObserver.onCompleted();
                }
            }
    ));
    private static final WasmModule CLIENT_RES = WasmModule.from(MODULE_SOURCE, WasmModuleVersion.V1);
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
    public void getModuleSourceTest() {
        var actual = client.getModuleSource(BlockQuery.LAST_FINAL, ModuleRef.from(MODULE_REF));
        verify(serviceImpl).getModuleSource(eq(ModuleSourceRequest.newBuilder()
                        .setBlockHash(com.concordium.grpc.v2.BlockHashInput.newBuilder()
                                .setLastFinal(Empty.getDefaultInstance())
                                .build())
                        .setModuleRef(com.concordium.grpc.v2.ModuleRef.newBuilder()
                                .setValue(ByteString.copyFrom(MODULE_REF))
                                .build())
                        .build()),
                any(StreamObserver.class));

        assertEquals(CLIENT_RES, actual);
    }
}
