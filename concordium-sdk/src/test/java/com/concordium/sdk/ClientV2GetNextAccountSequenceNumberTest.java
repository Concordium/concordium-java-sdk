package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.transactions.AccountNonce;
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
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientV2GetNextAccountSequenceNumberTest {

    private static final com.concordium.sdk.transactions.AccountAddress ACCOUNT_ADDRESS
            = com.concordium.sdk.transactions.AccountAddress.from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");
    private static final long NONCE_VALUE = 821;

    private static final NextAccountSequenceNumber GRPC_NEXT_ACCOUNT_SEQUENCE = NextAccountSequenceNumber.newBuilder()
            .setSequenceNumber(
                    SequenceNumber.newBuilder()
                            .setValue(NONCE_VALUE)
            )
            .setAllFinal(true)
            .build();

    private static final AccountNonce CONSENSUS_INFO_RES_EXPECTED = AccountNonce.from(NONCE_VALUE);

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getNextAccountSequenceNumber(
                        AccountAddress request,
                        StreamObserver<NextAccountSequenceNumber> responseObserver) {
                    responseObserver.onNext(GRPC_NEXT_ACCOUNT_SEQUENCE);
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
    public void getNextAccountSequenceNumber() {
        var res = client.getNextAccountSequenceNumber(ACCOUNT_ADDRESS);

        verify(serviceImpl).getNextAccountSequenceNumber(any(AccountAddress.class), any(StreamObserver.class));
        assertEquals(CONSENSUS_INFO_RES_EXPECTED.toString(), res.toString());
    }
}
