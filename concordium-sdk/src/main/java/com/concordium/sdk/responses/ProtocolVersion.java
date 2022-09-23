package com.concordium.sdk.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.val;

import java.util.*;

/**
 * Protocol versions supported by the chain.
 * Each protocol version is parameterized by its associated genesis index.
 */
public enum ProtocolVersion {
    V1(0),
    /**
     * https://github.com/Concordium/concordium-update-proposals/blob/main/updates/P2.txt
     */
    V2(1),
    /**
     * https://github.com/Concordium/concordium-update-proposals/blob/main/updates/P3.txt
     */
    V3(2),
    /**
     * https://github.com/Concordium/concordium-update-proposals/blob/main/updates/P4.txt
     */
    V4(3);

    private static final List<ProtocolVersion> protocolVersions = new ArrayList<>();

    static {
        protocolVersions.add(ProtocolVersion.V1);
        protocolVersions.add(ProtocolVersion.V2);
        protocolVersions.add(ProtocolVersion.V3);
        protocolVersions.add(ProtocolVersion.V4);
    }

    /**
     * The genesis index of the protocol.
     */
    @Getter
    private final int genesisIndex;

    ProtocolVersion(int genesisIndex) {
        this.genesisIndex = genesisIndex;
    }


    @JsonCreator
    public static ProtocolVersion forValue(int protocolVersion) {
        // The protocol starts with version 1.
        if (protocolVersion < 1) {
            throw new IllegalArgumentException("Unrecognized protocol version " + protocolVersion);
        }
        val version = ProtocolVersion.protocolVersions.get(protocolVersion - 1);
        if (Objects.isNull(version)) {
            throw new IllegalArgumentException("Unrecognized protocol version " + protocolVersion);
        }
        return version;
    }
}
