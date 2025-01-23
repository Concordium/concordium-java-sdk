package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.BLSSignature;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.Round;
import com.concordium.sdk.responses.blockcertificates.QuorumCertificate;
import com.concordium.sdk.transactions.Hash;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Rule;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientV2GetBlockCertificatesTest {

    // ---- Test Values ----
    private static final byte[] HASH_1 = new byte[]{1, 2, 3};
    private static final byte[] HASH_2 = new byte[]{4, 5, 6};
    private static final long ROUND_1 = 1;
    private static final long ROUND_2 = 2;
    private static final long ROUND_3 = 2;
    private static final long ROUND_4 = 2;
    private static final long EPOCH_1 = 1;
    private static final long EPOCH_2 = 2;
    private static final byte[] QUORUM_SIG_1 = bytesOfLength(48, 1);
    private static final byte[] QUORUM_SIG_2 = bytesOfLength(48, 2);
    private static final byte[] TIMEOUT_SIG_1 = bytesOfLength(48, 3);
    private static final byte[] SUCCESSOR_PROOF_1 = bytesOfLength(32, 4);
    private static final long BAKER_ID_1 = 1;
    private static final long BAKER_ID_2 = 2;
    private static final long BAKER_ID_3 = 3;
    private static final long BAKER_ID_4 = 4;
    private static final long BAKER_ID_5 = 5;
    private static final long BAKER_ID_6 = 6;
    private static final long BAKER_ID_7 = 7;
    private static final long BAKER_ID_8 = 8;


    // ---- Client values ----

    private static final List<BakerId> CLIENT_BAKER_ID_LIST_1 = new ArrayList<>(Arrays.asList(
            BakerId.from(BAKER_ID_1), BakerId.from(BAKER_ID_2)
    ));
    private static final List<BakerId> CLIENT_BAKER_ID_LIST_2 = new ArrayList<>(Arrays.asList(
            BakerId.from(BAKER_ID_3), BakerId.from(BAKER_ID_4)
    ));
    private static final List<BakerId> CLIENT_BAKER_ID_LIST_3 = new ArrayList<>(Arrays.asList(
            BakerId.from(BAKER_ID_5), BakerId.from(BAKER_ID_6)
    ));
    private static final List<BakerId> CLIENT_BAKER_ID_LIST_4 = new ArrayList<>(Arrays.asList(
            BakerId.from(BAKER_ID_7), BakerId.from(BAKER_ID_8)
    ));

    private static final com.concordium.sdk.responses.blockcertificates.FinalizerRound CLIENT_FINALIZER_ROUND_1 =
            com.concordium.sdk.responses.blockcertificates.FinalizerRound.builder()
                    .round(Round.from(ROUND_1))
                    .finalizers(CLIENT_BAKER_ID_LIST_1)
                    .build();
    private static final com.concordium.sdk.responses.blockcertificates.FinalizerRound CLIENT_FINALIZER_ROUND_2 =
            com.concordium.sdk.responses.blockcertificates.FinalizerRound.builder()
                    .round(Round.from(ROUND_2))
                    .finalizers(CLIENT_BAKER_ID_LIST_2)
                    .build();
    private static final com.concordium.sdk.responses.blockcertificates.FinalizerRound CLIENT_FINALIZER_ROUND_3 =
            com.concordium.sdk.responses.blockcertificates.FinalizerRound.builder()
                    .round(Round.from(ROUND_3))
                    .finalizers(CLIENT_BAKER_ID_LIST_3)
                    .build();
    private static final com.concordium.sdk.responses.blockcertificates.FinalizerRound CLIENT_FINALIZER_ROUND_4 =
            com.concordium.sdk.responses.blockcertificates.FinalizerRound.builder()
                    .round(Round.from(ROUND_4))
                    .finalizers(CLIENT_BAKER_ID_LIST_4)
                    .build();
    private static final List<com.concordium.sdk.responses.blockcertificates.FinalizerRound> CLIENT_FINALIZER_ROUND_LIST_1 =
            new ArrayList<>(Arrays.asList(CLIENT_FINALIZER_ROUND_1, CLIENT_FINALIZER_ROUND_2));
    private static final List<com.concordium.sdk.responses.blockcertificates.FinalizerRound> CLIENT_FINALIZER_ROUND_LIST_2 =
            new ArrayList<>(Arrays.asList(CLIENT_FINALIZER_ROUND_3, CLIENT_FINALIZER_ROUND_4));
    private static final QuorumCertificate CLIENT_QUORUM_CERTIFICATE_1 =
            QuorumCertificate.builder()
                    .blockHash(Hash.from(HASH_1))
                    .round(Round.from(ROUND_1))
                    .epoch(Epoch.from(EPOCH_1))
                    .aggregateSignature(com.concordium.sdk.responses.blockcertificates.QuorumSignature
                            .builder().value(BLSSignature.from(QUORUM_SIG_1))
                            .build())
                    .signatories(CLIENT_BAKER_ID_LIST_1)
                    .build();
    private static final QuorumCertificate CLIENT_QUORUM_CERTIFICATE_2 =
            QuorumCertificate.builder()
                    .blockHash(Hash.from(HASH_2))
                    .round(Round.from(ROUND_2))
                    .epoch(Epoch.from(EPOCH_2))
                    .aggregateSignature(com.concordium.sdk.responses.blockcertificates.QuorumSignature
                            .builder().value(BLSSignature.from(QUORUM_SIG_2))
                            .build())
                    .signatories(CLIENT_BAKER_ID_LIST_2)
                    .build();

    private static final com.concordium.sdk.responses.blockcertificates.TimeoutCertificate CLIENT_TIMEOUT_CERTIFICATE =
            com.concordium.sdk.responses.blockcertificates.TimeoutCertificate.builder()
                    .round(Round.from(ROUND_2))
                    .minEpoch(Epoch.from(EPOCH_2))
                    .qcRoundsFirstEpoch(CLIENT_FINALIZER_ROUND_LIST_1)
                    .qcRoundsSecondEpoch(CLIENT_FINALIZER_ROUND_LIST_2)
                    .aggregateSignature(com.concordium.sdk.responses.blockcertificates.TimeoutSignature
                            .builder().value(BLSSignature.from(TIMEOUT_SIG_1))
                            .build())
                    .build();

    private static final com.concordium.sdk.responses.blockcertificates.EpochFinalizationEntry CLIENT_FINALIZATION_ENTRY =
            com.concordium.sdk.responses.blockcertificates.EpochFinalizationEntry.builder()
                    .finalizedQc(CLIENT_QUORUM_CERTIFICATE_1)
                    .successorQc(CLIENT_QUORUM_CERTIFICATE_2)
                    .successorProof(com.concordium.sdk.responses.blockcertificates.SuccessorProof
                            .builder().value(Hash.from(SUCCESSOR_PROOF_1)).build())
                    .build();
    // Client Block Certificates
    private static final com.concordium.sdk.responses.blockcertificates.BlockCertificates CLIENT_BLOCK_CERT_WITH_QUORUM =
            com.concordium.sdk.responses.blockcertificates.BlockCertificates.builder()
                    .quorumCertificate(CLIENT_QUORUM_CERTIFICATE_1).build();
    private static final com.concordium.sdk.responses.blockcertificates.BlockCertificates CLIENT_BLOCK_CERT_WITH_TIMEOUT =
            com.concordium.sdk.responses.blockcertificates.BlockCertificates.builder()
                    .timeoutCertificate(CLIENT_TIMEOUT_CERTIFICATE).build();
    private static final com.concordium.sdk.responses.blockcertificates.BlockCertificates CLIENT_BLOCK_CERT_WITH_FINALIZATION =
            com.concordium.sdk.responses.blockcertificates.BlockCertificates.builder()
                    .epochFinalizationEntry(CLIENT_FINALIZATION_ENTRY).build();
    private static final com.concordium.sdk.responses.blockcertificates.BlockCertificates CLIENT_BLOCK_CERT_WITH_ALL =
            com.concordium.sdk.responses.blockcertificates.BlockCertificates.builder()
                    .quorumCertificate(CLIENT_QUORUM_CERTIFICATE_1)
                    .timeoutCertificate(CLIENT_TIMEOUT_CERTIFICATE)
                    .epochFinalizationEntry(CLIENT_FINALIZATION_ENTRY)
                    .build();


    // ---- GRPC Values ----

    private static final List<com.concordium.grpc.v2.BakerId> GRPC_BAKER_ID_LIST_1 = new ArrayList<>(Arrays.asList(
            com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID_1).build(),
            com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID_2).build()
    ));
    private static final List<com.concordium.grpc.v2.BakerId> GRPC_BAKER_ID_LIST_2 = new ArrayList<>(Arrays.asList(
            com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID_3).build(),
            com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID_4).build()
    ));
    private static final List<com.concordium.grpc.v2.BakerId> GRPC_BAKER_ID_LIST_3 = new ArrayList<>(Arrays.asList(
            com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID_5).build(),
            com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID_6).build()
    ));
    private static final List<com.concordium.grpc.v2.BakerId> GRPC_BAKER_ID_LIST_4 = new ArrayList<>(Arrays.asList(
            com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID_7).build(),
            com.concordium.grpc.v2.BakerId.newBuilder().setValue(BAKER_ID_8).build()
    ));

    private static final FinalizerRound GRPC_FINALIZER_ROUND_1 = FinalizerRound.newBuilder()
            .setRound(com.concordium.grpc.v2.Round.newBuilder().setValue(ROUND_1).build())
            .addAllFinalizers(GRPC_BAKER_ID_LIST_1)
            .build();
    private static final FinalizerRound GRPC_FINALIZER_ROUND_2 = FinalizerRound.newBuilder()
            .setRound(com.concordium.grpc.v2.Round.newBuilder().setValue(ROUND_2).build())
            .addAllFinalizers(GRPC_BAKER_ID_LIST_2)
            .build();
    private static final FinalizerRound GRPC_FINALIZER_ROUND_3 = FinalizerRound.newBuilder()
            .setRound(com.concordium.grpc.v2.Round.newBuilder().setValue(ROUND_3).build())
            .addAllFinalizers(GRPC_BAKER_ID_LIST_3)
            .build();
    private static final FinalizerRound GRPC_FINALIZER_ROUND_4 = FinalizerRound.newBuilder()
            .setRound(com.concordium.grpc.v2.Round.newBuilder().setValue(ROUND_4).build())
            .addAllFinalizers(GRPC_BAKER_ID_LIST_4)
            .build();

    private static final List<FinalizerRound> GRPC_FINALIZER_ROUND_LIST_1 = new ArrayList<>(Arrays.asList(
            GRPC_FINALIZER_ROUND_1, GRPC_FINALIZER_ROUND_2
    ));
    private static final List<FinalizerRound> GRPC_FINALIZER_ROUND_LIST_2 = new ArrayList<>(Arrays.asList(
            GRPC_FINALIZER_ROUND_3, GRPC_FINALIZER_ROUND_4
    ));


    private static final com.concordium.grpc.v2.QuorumCertificate GRPC_QUORUM_CERTIFICATE_1 =
            com.concordium.grpc.v2.QuorumCertificate.newBuilder()
                    .setBlockHash(
                            BlockHash.newBuilder().setValue(ByteString.copyFrom(HASH_1))
                                    .build())
                    .setRound(com.concordium.grpc.v2.Round.newBuilder()
                            .setValue(ROUND_1)
                            .build())
                    .setEpoch(com.concordium.grpc.v2.Epoch.newBuilder()
                            .setValue(EPOCH_1)
                            .build())
                    .setAggregateSignature(QuorumSignature.newBuilder()
                            .setValue(ByteString.copyFrom(QUORUM_SIG_1)).build())
                    .addAllSignatories(GRPC_BAKER_ID_LIST_1)
                    .build();
    private static final com.concordium.grpc.v2.QuorumCertificate GRPC_QUORUM_CERTIFICATE_2 =
            com.concordium.grpc.v2.QuorumCertificate.newBuilder()
                    .setBlockHash(
                            BlockHash.newBuilder().setValue(ByteString.copyFrom(HASH_2))
                                    .build())
                    .setRound(com.concordium.grpc.v2.Round.newBuilder()
                            .setValue(ROUND_2)
                            .build())
                    .setEpoch(com.concordium.grpc.v2.Epoch.newBuilder()
                            .setValue(EPOCH_2)
                            .build())
                    .setAggregateSignature(QuorumSignature.newBuilder()
                            .setValue(ByteString.copyFrom(QUORUM_SIG_2)).build())
                    .addAllSignatories(GRPC_BAKER_ID_LIST_2)
                    .build();

    private static final TimeoutCertificate GRPC_TIMEOUT_CERTIFICATE =
            TimeoutCertificate.newBuilder()
                    .setRound(com.concordium.grpc.v2.Round.newBuilder().setValue(ROUND_2).build())
                    .setMinEpoch(com.concordium.grpc.v2.Epoch.newBuilder().setValue(EPOCH_2).build())
                    .addAllQcRoundsFirstEpoch(GRPC_FINALIZER_ROUND_LIST_1)
                    .addAllQcRoundsSecondEpoch(GRPC_FINALIZER_ROUND_LIST_2)
                    .setAggregateSignature(TimeoutSignature.newBuilder().setValue(ByteString.copyFrom(TIMEOUT_SIG_1)).build())
                    .build();

    private static final EpochFinalizationEntry GRPC_FINALIZATION_ENTRY =
            EpochFinalizationEntry.newBuilder()
                    .setFinalizedQc(GRPC_QUORUM_CERTIFICATE_1)
                    .setSuccessorQc(GRPC_QUORUM_CERTIFICATE_2)
                    .setSuccessorProof(SuccessorProof.newBuilder()
                            .setValue(ByteString.copyFrom(SUCCESSOR_PROOF_1)).build())
                    .build();
    // GRPC Block Certificates
    private static final BlockCertificates GRPC_BLOCK_CERT_WITH_QUORUM = BlockCertificates.newBuilder()
            .setQuorumCertificate(GRPC_QUORUM_CERTIFICATE_1).build();
    private static final BlockCertificates GRPC_BLOCK_CERT_WITH_TIMEOUT = BlockCertificates.newBuilder()
            .setTimeoutCertificate(GRPC_TIMEOUT_CERTIFICATE).build();
    private static final BlockCertificates GRPC_BLOCK_CERT_WITH_FINALIZATION = BlockCertificates.newBuilder()
            .setEpochFinalizationEntry(GRPC_FINALIZATION_ENTRY).build();
    private static final BlockCertificates GRPC_BLOCK_CERT_WITH_ALL = BlockCertificates.newBuilder()
            .setQuorumCertificate(GRPC_QUORUM_CERTIFICATE_1)
            .setTimeoutCertificate(GRPC_TIMEOUT_CERTIFICATE)
            .setEpochFinalizationEntry(GRPC_FINALIZATION_ENTRY)
            .build();

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
    private ClientV2 client;

    // Must be first line of every test as it acts as a setup method as well
    private QueriesGrpc.QueriesImplBase configureResponseAndSetup(BlockCertificates response) throws Exception {
        QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
                new QueriesGrpc.QueriesImplBase() {
                    @Override
                    public void getBlockCertificates(BlockHashInput request, StreamObserver<BlockCertificates> responseObserver) {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                }));
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel, Optional.empty());
        return serviceImpl;
    }

    @SneakyThrows
    @Test
    public void getQuorumBlockCert() {
        test(GRPC_BLOCK_CERT_WITH_QUORUM, CLIENT_BLOCK_CERT_WITH_QUORUM);
    }

    @SneakyThrows
    @Test
    public void getTimeoutBlockCert() {
        test(GRPC_BLOCK_CERT_WITH_TIMEOUT, CLIENT_BLOCK_CERT_WITH_TIMEOUT);
    }

    @SneakyThrows
    @Test
    public void getQuorumFinalizationCert() {
        test(GRPC_BLOCK_CERT_WITH_FINALIZATION, CLIENT_BLOCK_CERT_WITH_FINALIZATION);
    }

    @Test
    public void getCertWithAll() {
        test(GRPC_BLOCK_CERT_WITH_ALL, CLIENT_BLOCK_CERT_WITH_ALL);
    }

    // Helper method for testing different return values
    @SneakyThrows
    private void test(BlockCertificates response, com.concordium.sdk.responses.blockcertificates.BlockCertificates expected) {
        val serviceImpl = configureResponseAndSetup(response);
        val res = client.getBlockCertificates(BlockQuery.BEST);

        verify(serviceImpl).getBlockCertificates(any(BlockHashInput.class), any(StreamObserver.class));

        assertEquals(expected, res);
    }

    // fills a byte[] of length 'len' with 'val' at every pos
    static byte[] bytesOfLength(int len, int val) {
        val buffer = ByteBuffer.allocate(len);
        Arrays.fill(buffer.array(), (byte) val);
        return buffer.array();
    }
}
