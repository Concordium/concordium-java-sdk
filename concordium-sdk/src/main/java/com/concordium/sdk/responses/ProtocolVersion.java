package com.concordium.sdk.responses;

/**
 * Protocol versions supported by the chain.
 */
public enum ProtocolVersion {
    V1,
    /**
     * https://github.com/Concordium/concordium-update-proposals/blob/main/updates/P2.txt
     */
    V2,
    /**
     * https://github.com/Concordium/concordium-update-proposals/blob/main/updates/P3.txt
     */
    V3,
    /**
     * https://github.com/Concordium/concordium-update-proposals/blob/main/updates/P4.txt
     */
    V4,
    /**
     * https://github.com/Concordium/concordium-update-proposals/blob/main/updates/P5.txt
     */
    V5,
    /**
     * https://github.com/Concordium/concordium-update-proposals/blob/main/updates/P6.txt
     */
    V6;

    /**
     * Parses {@link com.concordium.grpc.v2.ProtocolVersion} to {@link ProtocolVersion}.
     *
     * @param protocolVersion {@link com.concordium.grpc.v2.ProtocolVersion} returned by the GRPC V2 API.
     * @return parsed {@link ProtocolVersion}.
     */
    public static ProtocolVersion parse(com.concordium.grpc.v2.ProtocolVersion protocolVersion) {
        switch (protocolVersion) {
            case PROTOCOL_VERSION_1:
                return V1;
            case PROTOCOL_VERSION_2:
                return V2;
            case PROTOCOL_VERSION_3:
                return V3;
            case PROTOCOL_VERSION_4:
                return V4;
            case PROTOCOL_VERSION_5:
                return V5;
            case PROTOCOL_VERSION_6:
                return V6;
            default:
                throw new IllegalArgumentException("Unrecognized protocol version " + protocolVersion);
        }
    }

    /**
     * Get the consensus version by the protocol version
     *
     * @param protocolVersion the protocol version
     * @return the consensus version
     */
    public static ConsensusVersion getConsensusVersion(ProtocolVersion protocolVersion) {
        switch (protocolVersion) {
            case V1:
            case V2:
            case V3:
            case V4:
            case V5:
                return ConsensusVersion.V1;
            case V6:
                return ConsensusVersion.V2;
        }
        throw new IllegalArgumentException("Unrecognized protocol version " + protocolVersion);
    }

    public enum ConsensusVersion {
        // Consensus version 1 used in protocol versions 1-5.
        V1,
        // Consensus version 2 used in protocol version 6 and onwards.
        V2
    }
}
