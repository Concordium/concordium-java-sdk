package com.concordium.sdk.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.val;

import java.util.*;

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
    V5;

    @JsonCreator
    public static ProtocolVersion forValue(int protocolVersion) {
        switch (protocolVersion){
            case 1: return ProtocolVersion.V1;
            case 2: return ProtocolVersion.V2;
            case 3: return ProtocolVersion.V3;
            case 4: return ProtocolVersion.V4;
            case 5: return ProtocolVersion.V5;
            default:
                throw new IllegalArgumentException("Unrecognized protocol version " + protocolVersion);
        }
    }

    /**
     * Parses {@link com.concordium.grpc.v2.ProtocolVersion} to {@link ProtocolVersion}.
     * @param protocolVersion {@link com.concordium.grpc.v2.ProtocolVersion} returned by the GRPC V2 API.
     * @return parsed {@link ProtocolVersion}.
     */
    public static ProtocolVersion parse(com.concordium.grpc.v2.ProtocolVersion protocolVersion) {
        return null;
    }
}
