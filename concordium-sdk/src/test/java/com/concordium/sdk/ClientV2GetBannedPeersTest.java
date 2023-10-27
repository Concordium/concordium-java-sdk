package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.google.common.collect.ImmutableList;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientV2GetBannedPeersTest {


    private static final String IP_ONE = "127.0.0.1";
    private static final String IP_TWO = "178.128.170.151";
    private static final String IP_THREE = "221.0.3.1";

    private static final InetAddress CLIENT_BANNED_PEER_ONE;

    static {
        try {
            CLIENT_BANNED_PEER_ONE = InetAddress.getByName(IP_ONE);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static final InetAddress CLIENT_BANNED_PEER_TWO;


    static {
        try {
            CLIENT_BANNED_PEER_TWO = InetAddress.getByName(IP_TWO);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static final InetAddress CLIENT_BANNED_PEER_THREE;


    static {
        try {
            CLIENT_BANNED_PEER_THREE = InetAddress.getByName(IP_THREE);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ImmutableList<InetAddress> CLIENT_BANNED_PEERS = new ImmutableList.Builder<InetAddress>()
            .add(CLIENT_BANNED_PEER_ONE)
            .add(CLIENT_BANNED_PEER_TWO)
            .add(CLIENT_BANNED_PEER_THREE)
            .build();

    private static final BannedPeers GRPC_BANNED_PEERS = BannedPeers.newBuilder()
            .addPeers(BannedPeer.newBuilder().setIpAddress(IpAddress.newBuilder().setValue(IP_ONE).build()))
            .addPeers(BannedPeer.newBuilder().setIpAddress(IpAddress.newBuilder().setValue(IP_TWO).build()))
            .addPeers(BannedPeer.newBuilder().setIpAddress(IpAddress.newBuilder().setValue(IP_THREE).build()))
            .build();

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getBannedPeers(
                        Empty request,
                        StreamObserver<BannedPeers> responseObserver) {
                    responseObserver.onNext(GRPC_BANNED_PEERS);
                    responseObserver.onCompleted();
                }
            }
    ));

    private ClientV2 client;

    @Before
    public void setUp() throws IOException {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel, Optional.empty());
    }

    @Test
    public void getBannedPeers() throws UnknownHostException {
        val res = client.getBannedPeers();

        verify(serviceImpl).getBannedPeers(any(Empty.class), any(StreamObserver.class));
        assertEquals(CLIENT_BANNED_PEERS, res);

    }
}
