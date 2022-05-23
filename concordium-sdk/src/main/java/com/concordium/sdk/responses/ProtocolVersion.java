package com.concordium.sdk.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.val;

import java.util.*;

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
    V4;

    private static final List<ProtocolVersion> protocolVersions = new ArrayList<>();

    static {
        protocolVersions.add(ProtocolVersion.V1);
        protocolVersions.add(ProtocolVersion.V2);
        protocolVersions.add(ProtocolVersion.V3);
        protocolVersions.add(ProtocolVersion.V4);
    }


    @JsonCreator
    public static ProtocolVersion forValue(int protocolVersion) {
        // The protocol starts with version 1.
        if (protocolVersion < 1) {
            throw new IllegalArgumentException("Unrecognized protocol version " + protocolVersion);
        }
        val version = ProtocolVersion.protocolVersions.get(protocolVersion);
        if (Objects.isNull(version)) {
            throw new IllegalArgumentException("Unrecognized protocol version " + protocolVersion);
        }
        return version;
    }
}
