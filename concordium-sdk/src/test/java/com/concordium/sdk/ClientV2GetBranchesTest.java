package com.concordium.sdk;

import com.concordium.grpc.v2.BlockHash;
import com.concordium.grpc.v2.Branch;
import com.concordium.grpc.v2.Empty;
import com.concordium.grpc.v2.QueriesGrpc;
import com.concordium.sdk.transactions.Hash;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
public class ClientV2GetBranchesTest {
    private static final byte[] BLOCK_HASH_1 = new byte[]{1, 1, 1};
    private static final byte[] BLOCK_HASH_2 = new byte[]{2, 2, 2};
    private static final byte[] BLOCK_HASH_3 = new byte[]{3, 3, 3};

    private static final Branch BRANCH_GRPC = Branch.newBuilder()
            .setBlockHash(BlockHash.newBuilder().setValue(ByteString.copyFrom(BLOCK_HASH_1)).build())
            .addAllChildren(Lists.newArrayList(
                    Branch.newBuilder()
                            .setBlockHash(BlockHash.newBuilder().setValue(ByteString.copyFrom(BLOCK_HASH_2)).build())
                            .build(),
                    Branch.newBuilder()
                            .setBlockHash(BlockHash.newBuilder().setValue(ByteString.copyFrom(BLOCK_HASH_3)).build())
                            .build()
            ))
            .build();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getBranches(Empty request, StreamObserver<Branch> responseObserver) {
                    responseObserver.onNext(BRANCH_GRPC);
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
        client = new ClientV2(10000, channel);
    }

    @Test
    public void getBranches() {
        var expectedBranches = com.concordium.sdk.responses.branch.Branch.builder()
                .blockHash(Hash.from(BLOCK_HASH_1))
                .children(ImmutableList.of(
                        com.concordium.sdk.responses.branch.Branch.builder()
                                .blockHash(Hash.from(BLOCK_HASH_2))
                                .build(),
                        com.concordium.sdk.responses.branch.Branch.builder()
                                .blockHash(Hash.from(BLOCK_HASH_3))
                                .build()
                ))
                .build();
        var branches = client.getBranches();

        verify(serviceImpl).getBranches(eq(Empty.getDefaultInstance()), any(StreamObserver.class));
        assertEquals(expectedBranches, branches);
    }
}
