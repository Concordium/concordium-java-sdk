package com.concordium.sdk;

import com.concordium.grpc.v2.AccountInfo;
import com.concordium.grpc.v2.BakerPoolInfo;
import com.concordium.grpc.v2.CommissionRates;
import com.concordium.grpc.v2.Commitment;
import com.concordium.grpc.v2.CredentialPublicKeys;
import com.concordium.grpc.v2.Policy;
import com.concordium.grpc.v2.ReleaseSchedule;
import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.requests.getaccountinfo.AccountRequest;
import com.concordium.sdk.responses.accountinfo.*;
import com.concordium.sdk.responses.accountinfo.credential.CredentialType;
import com.concordium.sdk.responses.accountinfo.credential.*;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.EncryptedAmountIndex;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.transactions.smartcontracts.WasmModuleVersion;
import com.concordium.sdk.types.Nonce;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;

import static com.concordium.sdk.Constants.UTC_ZONE;
import static com.concordium.sdk.responses.accountinfo.credential.AttributeType.FIRST_NAME;
import static com.concordium.sdk.transactions.CredentialRegistrationId.fromBytes;
import static java.time.YearMonth.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetModuleSourceTest {
    private static final byte[] MODULE_REF = new byte[]{10, 10, 10};
    private static final byte[] MODULE_SOURCE = new byte[]{1, 1, 1};
    private static final VersionedModuleSource GRPC_RES_1 = VersionedModuleSource.newBuilder()
            .setV1(VersionedModuleSource.ModuleSourceV1.newBuilder()
                    .setValue(ByteString.copyFrom(MODULE_SOURCE))
                    .build())
            .build();

    private static final WasmModule CLIENT_RES = WasmModule.from(MODULE_SOURCE, WasmModuleVersion.V1);

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
    public void getModuleSourceTest() {
        var actual = client.getModuleSource(BlockHashInput.BEST, ModuleRef.from(MODULE_REF));
        verify(serviceImpl).getModuleSource(eq(ModuleSourceRequest.newBuilder()
                        .setBlockHash(com.concordium.grpc.v2.BlockHashInput.newBuilder()
                                .setBest(Empty.getDefaultInstance())
                                .build())
                        .setModuleRef(com.concordium.grpc.v2.ModuleRef.newBuilder()
                                .setValue(ByteString.copyFrom(MODULE_REF))
                                .build())
                        .build()),
                any(StreamObserver.class));

        assertEquals(CLIENT_RES, actual);
    }
}
