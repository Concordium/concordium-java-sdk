package com.concordium.sdk.responses.nodeinfo;

import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Node Information.
 */
@Builder
@Getter
@ToString
public class NodeInfo {

    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    /**
     * Node Id.
     */
    private final String nodeId;

    /**
     * Current local time of the node.
     */
    private final ZonedDateTime localTime;

    /**
     * Peer Details.
     */
    private final PeerDetails peerDetails;

    /**
     * Parses {@link concordium.ConcordiumP2PRpc.NodeInfoResponse} to {@link NodeInfo}.
     * @param value {@link concordium.ConcordiumP2PRpc.NodeInfoResponse}.
     * @return Parsed {@link NodeInfo}.
     */
    public static NodeInfo parse(ConcordiumP2PRpc.NodeInfoResponse value) {
        return NodeInfo.builder()
                .nodeId(value.getNodeId().getValue())
                .localTime(parseTime(value.getCurrentLocaltime()))
                .peerDetails(parsePeerDetails(value))
                .build();
    }

    /**
     * Parses {@link concordium.ConcordiumP2PRpc.NodeInfoResponse} into {@link PeerDetails}.
     * @param value {@link concordium.ConcordiumP2PRpc.NodeInfoResponse}.
     * @return Parsed {@link PeerDetails}.
     */
    private static PeerDetails parsePeerDetails(ConcordiumP2PRpc.NodeInfoResponse value) {
        switch (value.getPeerType()) {
            case "Bootstrapper" : return new PeerDetails(PeerType.BOOTSTRAPPER);
            case "Node" : return PeerDetailsNode.parse(value);
            default: throw new IllegalArgumentException(
                    String.format("Invalid Peer Type : %s", value.getPeerType()));
        }
    }

    /**
     * Parses time to {@link ZonedDateTime} from the input value.
     * @param currentLocaltime Input value.
     * @return Parsed {@link ZonedDateTime}.
     */
    private static ZonedDateTime parseTime(long currentLocaltime) {
        return Instant.EPOCH.plusSeconds(currentLocaltime).atZone(UTC_ZONE);
    }
}
