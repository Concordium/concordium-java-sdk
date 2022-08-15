package com.concordium.sdk.responses.nodeinfo;

import com.concordium.sdk.responses.AccountIndex;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Node Information.
 */
@Builder
@Getter
@ToString
public class NodeInfo {

    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    /**
     * The node id.
     * This is a hex encoded u64 value.
     */
    private final String nodeId;

    /**
     * The current local time of the node.
     */
    private final ZonedDateTime localTime;

    /**
     * The type of the node.
     */
    private final Type type;

    /**
     * Whether the consensus is running on the node.
     * This is true iff. the node supports the current protocol running
     * on the chain.
     */
    private final boolean consensusRunning;

    /**
     * Baking information of the peer.
     * The {@link BakerInfo} is present iff. the node has been configured
     * with baker credentials.
     */
    private final Optional<BakerInfo> bakerInfo;


    /**
     * Parses {@link concordium.ConcordiumP2PRpc.NodeInfoResponse} to {@link NodeInfo}.
     * @param response {@link concordium.ConcordiumP2PRpc.NodeInfoResponse}.
     * @return Parsed {@link NodeInfo}.
     */
    public static NodeInfo parse(ConcordiumP2PRpc.NodeInfoResponse response) {
        val builder = NodeInfo.builder()
                .nodeId(response.getNodeId().getValue())
                .localTime(parseTime(response.getCurrentLocaltime()))
                .type(Type.from(response.getPeerType()))
                .consensusRunning(response.getConsensusRunning());
        val consensusType = ConsensusType.getConsensusType(response.getConsensusType());
        if (consensusType == ConsensusType.PASSIVE) {
            return builder.build();
        }
        return builder.bakerInfo(Optional.of(
                new BakerInfo(AccountIndex.from(
                        response.getConsensusBakerId().getValue()),
                        BakerInfo.Status.fromCode(response.getConsensusBakerCommittee().getNumber()),
                        response.getConsensusFinalizerCommittee()))).build();

    }


    /**
     * Parses time to {@link ZonedDateTime} from the given UNIX timestamp.
     * @param currentLocaltime the UNIX timestamp.
     * @return Parsed {@link ZonedDateTime}.
     */
    private static ZonedDateTime parseTime(long currentLocaltime) {
        return Instant.EPOCH.plusSeconds(currentLocaltime).atZone(UTC_ZONE);
    }

    /**
     * Represents type of the node.
     */
    @ToString
    public enum Type {
        /**
         * The node is a bootstrapper and
         * is not participating in consensus.
         */
        BOOTSTRAPPER,

        /**
         * The node is eligible for participating
         * in consensus.
         */
        NODE;

        public static Type from(String type) {
            switch (type) {
                case "Bootstrapper":
                    return BOOTSTRAPPER;
                case "Node":
                    return NODE;
                default:
                    throw new IllegalStateException("Unexpected node type: " + type);
            }
        }
    }
}
