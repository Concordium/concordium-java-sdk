package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.bulletproof.BulletproofGenerators;
import com.concordium.sdk.crypto.pedersencommitment.PedersenCommitmentKey;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.requests.BlockHashInput;
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
public class ClientV2CryptographicParameters {

    private static final String  BULLETPROOF_GENERATORS = "0000010098855a650637f2086157d32536f646b758a3c45a2f299a4dad7ea3dbd1c4cfb4ba42aca5461f8e45aab911";
    private static final String ON_CHAIN_COMMITMENT_KEY = "b14cbfe44a02c6b1f78711176d5f437295367aa4f2a8c2551ee10d25a03adc69d61a332a058971919dad7312e1fc94";
    private static final String GENESIS_STRING = "Concordium Testnet Version 5";

    private static final CryptographicParameters GRPC_PARAMETERS = CryptographicParameters.newBuilder()
            .setGenesisString(GENESIS_STRING)
            .setBulletproofGenerators(ByteString.copyFromUtf8(BULLETPROOF_GENERATORS))
            .setOnChainCommitmentKey(ByteString.copyFromUtf8(ON_CHAIN_COMMITMENT_KEY))
            .build();
    private static final com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters PARAMETERS_CLIENT = com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters.builder()
            .genesisString(GENESIS_STRING)
            .onChainCommitmentKey(PedersenCommitmentKey.from(ON_CHAIN_COMMITMENT_KEY))
            .bulletproofGenerators(BulletproofGenerators.from(BULLETPROOF_GENERATORS))
            .build();
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getCryptographicParameters(
                        com.concordium.grpc.v2.BlockHashInput request,
                        io.grpc.stub.StreamObserver<com.concordium.grpc.v2.CryptographicParameters> responseObserver) {
                    responseObserver.onNext(GRPC_PARAMETERS);
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
    public void GetBlockChainParameters() throws BlockNotFoundException {
        var parameters = client.getCryptographicParameters(BlockHashInput.BEST);

        verify(serviceImpl).getCryptographicParameters(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(parameters.toString(), PARAMETERS_CLIENT.toString());
    }

}
