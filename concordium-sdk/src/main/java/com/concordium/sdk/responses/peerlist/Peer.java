package com.concordium.sdk.responses.peerlist;

import com.concordium.sdk.types.UInt16;
import com.google.common.collect.ImmutableList;
import concordium.ConcordiumP2PRpc;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Represents the parsed form of {@link ConcordiumP2PRpc.PeerElement}
 */
@SuperBuilder
@Getter
@ToString
@EqualsAndHashCode
public class Peer {

    /**
     * The self-chosen identifier of the peer.
     */
    private final String nodeId;
    /**
     * The port of the peer
     */
    private final UInt16 port;
    /**
     * The ip address of the peer
     */
    private final InetAddress ipAddress;
    /**
     * The catchup status of the peer
     */
    private final PeerCatchupStatus catchupStatus;

    public static ImmutableList<Peer> toList(List<ConcordiumP2PRpc.PeerElement> response) throws UnknownHostException {
        val list = new ImmutableList.Builder<Peer>();

        for (ConcordiumP2PRpc.PeerElement p : response) {
            list.add(Peer.parse(p));
        }

        return list.build();
    }

    private static Peer parse(ConcordiumP2PRpc.PeerElement p) throws UnknownHostException {
        return Peer.builder()
                .nodeId(p.getNodeId().getValue())
                .port(UInt16.from(p.getPort().getValue()))
                .ipAddress(InetAddress.getByName(p.getIp().getValue()))
                .catchupStatus(PeerCatchupStatus.parse(p.getCatchupStatus()))
                .build();
    }

}
