package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.transactions.AccountNonce;
import com.concordium.sdk.types.Nonce;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientV2GetNextAccountSequenceNoTest {
    private static final long SEQ_NUMBER = 2;
    private static final boolean IS_ALL_FINAL = true;
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getNextAccountSequenceNumber(AccountAddress request, StreamObserver<NextAccountSequenceNumber> responseObserver) {
                    responseObserver.onNext(NextAccountSequenceNumber.newBuilder()
                            .setSequenceNumber(SequenceNumber.newBuilder().setValue(SEQ_NUMBER).build())
                            .setAllFinal(IS_ALL_FINAL)
                            .build());
                    responseObserver.onCompleted();
                }
            }
    ));
    private static final com.concordium.sdk.transactions.AccountAddress ACCOUNT_ADDRESS = com.concordium.sdk.transactions.AccountAddress.from(
            "3bkTmK6GBWprhq6z2ukY6dEi1xNoBEMPDyyMQ6j8xrt8yaF7F2");

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
    public void getNextAccountSequenceNumber() {
        final AccountNonce res = client.getNextAccountSequenceNo(
                ACCOUNT_ADDRESS);

        var grpcAccountAddress = AccountAddress.newBuilder()
                .setValue(ByteString.copyFrom(ACCOUNT_ADDRESS.getBytes()))
                .build();
        verify(serviceImpl).getNextAccountSequenceNumber(eq(grpcAccountAddress), any(StreamObserver.class));
        assertTrue(res.isAllFinal());
        assertEquals(res.getNonce(), Nonce.from(SEQ_NUMBER));
    }
}
