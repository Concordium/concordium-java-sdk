package com.concordium.sdk.responses.smartcontracts;

import com.concordium.grpc.v2.ContractVersion;

/**
 * Smart contract versions.
 */
public enum SmartContractVersion {
    /**
     * V0 contracts exists across all protocol versions.
     */
    V0,
    /**
     * V1 contracts got introduced from protocol version 4.
     */
    V1;

    public static SmartContractVersion from(ContractVersion contractVersion) {
        switch (contractVersion){
            case V0:
                return V0;
            case V1:
                return V1;
            case UNRECOGNIZED:
                throw new IllegalArgumentException("Unrecognized contract tag.");
        }
        throw new IllegalArgumentException("Unrecognized contract version.");
    }

    public static SmartContractVersion from(int version) {
        switch (version) {
            case 0: return V0;
            case 1: return V1;
            default:
                throw new IllegalStateException("Unrecognized smart contract version " + version);
        }
    }
}
