package com.concordium.sdk.responses;

import com.fasterxml.jackson.annotation.JsonCreator;

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

    @JsonCreator
    public static ProtocolVersion forValue(int protocolVersion) {
        switch (protocolVersion) {
            case 1:
                return ProtocolVersion.V1;
            case 2:
                return ProtocolVersion.V2;
            case 3:
                return ProtocolVersion.V3;
            case 4:
                return ProtocolVersion.V4;
            case 5:
                return ProtocolVersion.V5;
            case 6:
                return ProtocolVersion.V6;
            default:
                throw new IllegalArgumentException("Unrecognized protocol version " + protocolVersion);
        }
    }
}
