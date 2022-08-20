package com.concordium.sdk.requests.getinstanceinfo;

import com.concordium.sdk.transactions.Hash;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Getter;

/**
 * Query Request for method.
 * {@link concordium.P2PGrpc.P2PBlockingStub#getInstanceInfo(ConcordiumP2PRpc.GetAddressInfoRequest)}.
 */
@Builder
@Getter
public class GetInstanceInfoRequest {

    /**
     * Block hash at which the instance information is to be fetched.
     */
    private final Hash blockHash;

    /**
     * Smart Contract Address.
     */
    private final ContractAddress address;
}
