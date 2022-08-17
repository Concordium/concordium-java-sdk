package com.concordium.sdk.responses.peerlist;

import com.concordium.sdk.types.UInt16;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Represents the parsed form of {@link ConcordiumP2PRpc.PeerElement}
 */
@Builder
@Getter
@ToString
public class Peer {

    private final String nodeId;
    private final UInt16 port;
    private final InetAddress ipAddress;
    private final PeerCatchupStatus catchupStatus;

    public static Peer parse(ConcordiumP2PRpc.PeerElement p) throws UnknownHostException {
        return Peer.builder()
                .nodeId(p.getNodeId().getValue())
                .port(UInt16.from(p.getPort().getValue()))
                .ipAddress(InetAddress.getByName(p.getIp().getValue()))
                .catchupStatus(PeerCatchupStatus.parse(p.getCatchupStatus()))
                .build();
    }
}
