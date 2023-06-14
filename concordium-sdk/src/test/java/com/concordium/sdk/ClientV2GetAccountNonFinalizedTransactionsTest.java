package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.transactions.Hash;
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
 * Mocks the Node's GRPC Interface and tests Mapping of Requests and Responses from {@link ClientV2}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetAccountNonFinalizedTransactionsTest {

    private static final com.concordium.sdk.types.AccountAddress ACCOUNT_ADDRESS
            = com.concordium.sdk.types.AccountAddress.from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");
    private static final byte[] TRANSACTION_HASH = new byte[]{0, 0, 3};
    private static final TransactionHash GRPC_NON_FINAL_TRANSACTIONS = TransactionHash.newBuilder()
            .setValue(ByteString.copyFrom(TRANSACTION_HASH))
            .build();

    private static final Hash TRANSACTION_HASH_CLIENT = Hash.from(TRANSACTION_HASH);
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getAccountNonFinalizedTransactions(
                        com.concordium.grpc.v2.AccountAddress request,
                        io.grpc.stub.StreamObserver<com.concordium.grpc.v2.TransactionHash> responseObserver) {
                    responseObserver.onNext(GRPC_NON_FINAL_TRANSACTIONS);
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
    public void getAccountNonFinalizedTransactions() {
        var address = client.getAccountNonFinalizedTransactions(ACCOUNT_ADDRESS);

        verify(serviceImpl).getAccountNonFinalizedTransactions(any(AccountAddress.class), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(address), ImmutableList.of(TRANSACTION_HASH_CLIENT));
    }
}
