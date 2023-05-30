package com.concordium.sdk;

import com.concordium.grpc.v2.Empty;
import com.concordium.grpc.v2.NextUpdateSequenceNumbers;
import com.concordium.grpc.v2.QueriesGrpc;
import com.concordium.grpc.v2.SequenceNumber;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.types.Nonce;
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
 * Tests Mapping from GRPC response using {@link ClientV2MapperExtensions#to(NextUpdateSequenceNumbers)}
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetNextUpdateSequenceNumbersTest {

    private static final NextUpdateSequenceNumbers GRPC_NEXT_UPDATE_SEQ_NOS = NextUpdateSequenceNumbers.newBuilder()
            .setRootKeys(toSeqNo(1))
            .setLevel1Keys(toSeqNo(2))
            .setLevel2Keys(toSeqNo(3))
            .setProtocol(toSeqNo(4))
            .setElectionDifficulty(toSeqNo(5))
            .setEuroPerEnergy(toSeqNo(6))
            .setMicroCcdPerEuro(toSeqNo(7))
            .setFoundationAccount(toSeqNo(8))
            .setMintDistribution(toSeqNo(9))
            .setTransactionFeeDistribution(toSeqNo(10))
            .setGasRewards(toSeqNo(11))
            .setPoolParameters(toSeqNo(12))
            .setAddAnonymityRevoker(toSeqNo(13))
            .setAddIdentityProvider(toSeqNo(14))
            .setCooldownParameters(toSeqNo(15))
            .setTimeParameters(toSeqNo(16))
            .build();
    private static final com.concordium.sdk.responses.NextUpdateSequenceNumbers EXPECTED
            = com.concordium.sdk.responses.NextUpdateSequenceNumbers.builder()
            .rootKeys(toNonce(1))
            .level1Keys(toNonce(2))
            .level2Keys(toNonce(3))
            .protocol(toNonce(4))
            .electionDifficulty(toNonce(5))
            .euroPerEnergy(toNonce(6))
            .microCcdPerEuro(toNonce(7))
            .foundationAccount(toNonce(8))
            .mintDistribution(toNonce(9))
            .transactionFeesDistribution(toNonce(10))
            .gasRewards(toNonce(11))
            .poolParameters(toNonce(12))
            .addAnonymityRevoker(toNonce(13))
            .addIdentityProvider(toNonce(14))
            .cooldownParameters(toNonce(15))
            .timeParameters(toNonce(16))
            .build();
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getNextUpdateSequenceNumbers(
                        com.concordium.grpc.v2.BlockHashInput request,
                        StreamObserver<NextUpdateSequenceNumbers> responseObserver) {
                    responseObserver.onNext(GRPC_NEXT_UPDATE_SEQ_NOS);
                    responseObserver.onCompleted();
                }
            }
    ));
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
    private ClientV2 client;

    private static Nonce toNonce(long i) {
        return Nonce.from(i);
    }

    private static SequenceNumber toSeqNo(long i) {
        return SequenceNumber.newBuilder().setValue(i).build();
    }

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
    public void getInstanceInfoTest() {
        var res = client.getNextUpdateSequenceNumbers(BlockHashInput.BEST);

        verify(serviceImpl).getNextUpdateSequenceNumbers(eq(
                        com.concordium.grpc.v2.BlockHashInput.newBuilder().setBest(Empty.getDefaultInstance()).build()),
                any(StreamObserver.class));
        assertEquals(EXPECTED, res);
    }
}
