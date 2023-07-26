package com.concordium.sdk;


import com.concordium.grpc.v2.*;
import com.concordium.sdk.exceptions.BlockNotFoundException;
import com.concordium.sdk.requests.BlockQuery;
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
 * Mocks the GRPC interface of the node.
 * Tests mapping of Requests and Responses from {@link ClientV2#getAncestors(BlockQuery, long)} to the Node.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetAncestorsTest {

    private static final byte[] BLOCK_BYTES = new byte[]{0, 0, 1};

    private static final com.concordium.grpc.v2.BlockHash GRPC_BLOCK_HASH = com.concordium.grpc.v2.BlockHash.newBuilder()
            .setValue(ByteString.copyFrom(BLOCK_BYTES))
            .build();
    private static final Hash BLOCK_HASH_CLIENT = Hash.from(BLOCK_BYTES);
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getAncestors(
                        com.concordium.grpc.v2.AncestorsRequest request,
                        StreamObserver<com.concordium.grpc.v2.BlockHash> responseObserver) {
                    responseObserver.onNext(GRPC_BLOCK_HASH);
                    responseObserver.onCompleted();
                }
            }
    ));
    private static final com.concordium.grpc.v2.AncestorsRequest BEST_BLOCK = AncestorsRequest.newBuilder()
            .setBlockHash(com.concordium.grpc.v2.BlockHashInput.newBuilder().setBest(Empty.newBuilder().build()).build())
            .setAmount(1)
            .build();

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
        client = new ClientV2(10000, channel);
    }


    @Test
    public void getAncestors() throws BlockNotFoundException {
        var ancestors = client.getAncestors(BlockQuery.BEST, 1);


        verify(serviceImpl).getAncestors(eq(BEST_BLOCK), any(StreamObserver.class));
        assertEquals(ImmutableList.copyOf(ancestors), ImmutableList.of(BLOCK_HASH_CLIENT));
    }
}