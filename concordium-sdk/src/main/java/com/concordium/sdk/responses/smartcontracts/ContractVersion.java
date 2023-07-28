package com.concordium.sdk.responses.smartcontracts;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.ToString;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Smart contract versions that exist on the chain.
 */
@ToString
public enum ContractVersion {
    V0, V1;

    private static final List<ContractVersion> contractVersions = new ArrayList<>();

    static {
        contractVersions.add(ContractVersion.V0);
        contractVersions.add(ContractVersion.V1);
    }


    @JsonCreator
    public static ContractVersion forValue(int contractVersion) {
        val version = ContractVersion.contractVersions.get(contractVersion);
        if (Objects.isNull(version)) {
            throw new IllegalArgumentException("Unrecognized contract version " + contractVersion);
        }
        return version;
    }

    public static ContractVersion from(com.concordium.grpc.v2.ContractVersion contractVersion) {
        switch (contractVersion) {
            case V0:
                return V0;
            case V1:
                return V1;
            default:
                throw new IllegalArgumentException("Unrecognized contract version.");
        }

    }
}
