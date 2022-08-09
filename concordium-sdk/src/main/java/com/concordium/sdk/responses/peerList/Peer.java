package com.concordium.sdk.responses.peerList;

import com.concordium.sdk.types.UInt16;
import concordium.ConcordiumP2PRpc;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents the parsed form of {@link ConcordiumP2PRpc.PeerElement}
 */
@Builder
@Getter
public class Peer {

    private final String nodeId;
    private final UInt16 port;
    private final IPAddress ipAddress;
    private final PeerCatchupStatus catchupStatus;

    public static Peer parse(ConcordiumP2PRpc.PeerElement p) {
        return Peer.builder()
                .nodeId(p.getNodeId().getValue())
                .port(UInt16.from(p.getPort().getValue()))
                .ipAddress(new IPAddressString(p.getIp().getValue()).getAddress())
                .catchupStatus(PeerCatchupStatus.parse(p.getCatchupStatus()))
                .build();
    }
}
