package com.concordium.sdk.queries;

import com.concordium.sdk.responses.peerlist.Peer;
import com.concordium.sdk.responses.peerlist.PeerCatchupStatus;
import com.concordium.sdk.types.UInt16;
import com.google.protobuf.StringValue;
import com.google.protobuf.UInt32Value;
import concordium.ConcordiumP2PRpc;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.net.InetAddress;
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;

public class GetBannedPeersTest {

    @SneakyThrows
    @Test
    public void getBannedPeersTest() {
        val PEER_ELEMENT_1 = ConcordiumP2PRpc.PeerElement.newBuilder()
                .setNodeId(StringValue.of("1"))
                .setIp(StringValue.of("127.0.0.1"))
                .setCatchupStatus(ConcordiumP2PRpc.PeerElement.CatchupStatus.PENDING)
                .setPort(UInt32Value.of(10001)).build();
        val PEER_1 = Peer.builder()
                .nodeId("1")
                .ipAddress(InetAddress.getByName("127.0.0.1"))
                .port(UInt16.from(10001))
                .catchupStatus(PeerCatchupStatus.PENDING)
                .build();

        val PEER_ELEMENT_2 = ConcordiumP2PRpc.PeerElement.newBuilder()
                .setNodeId(StringValue.of("2"))
                .setIp(StringValue.of("127.0.0.2"))
                .setCatchupStatus(ConcordiumP2PRpc.PeerElement.CatchupStatus.UPTODATE)
                .setPort(UInt32Value.of(10001)).build();
        val PEER_2 = Peer.builder()
                .nodeId("2")
                .ipAddress(InetAddress.getByName("127.0.0.2"))
                .port(UInt16.from(10001))
                .catchupStatus(PeerCatchupStatus.UP_TO_DATE)
                .build();

        val res = new ArrayList<ConcordiumP2PRpc.PeerElement>();
        res.add(PEER_ELEMENT_1);
        res.add(PEER_ELEMENT_2);

        val parsed = Peer.toList(res);

        assertArrayEquals(parsed.toArray(), new Peer[]{PEER_1, PEER_2});
    }
}
