package com.concordium.sdk.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.val;

import java.util.*;

public enum ProtocolVersion {
    V0,
    V1,
    V2,
    V3,
    V4;

    private static final List<ProtocolVersion> protocolVersions = new ArrayList<>();

    static {
        protocolVersions.add(ProtocolVersion.V0);
        protocolVersions.add(ProtocolVersion.V1);
        protocolVersions.add(ProtocolVersion.V2);
        protocolVersions.add(ProtocolVersion.V3);
        protocolVersions.add(ProtocolVersion.V4);
    }


    @JsonCreator
    public static ProtocolVersion forValue(int protocolVersion) {
        val version = ProtocolVersion.protocolVersions.get(protocolVersion);
        if (Objects.isNull(version)) {
            throw new IllegalArgumentException("Unrecognized protocol version " + protocolVersion);
        }
        return version;
    }
}
