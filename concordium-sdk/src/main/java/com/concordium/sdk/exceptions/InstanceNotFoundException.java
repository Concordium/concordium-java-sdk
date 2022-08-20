package com.concordium.sdk.exceptions;

import com.concordium.sdk.requests.getinstanceinfo.ContractAddress;
import com.concordium.sdk.requests.getinstanceinfo.GetInstanceInfoRequest;
import com.concordium.sdk.transactions.Hash;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class InstanceNotFoundException  extends Exception {

    /**
     * Block hash at which the instance information is to be fetched.
     */
    private final Hash blockHash;

    /**
     * Smart Contract Address.
     */
    private final ContractAddress address;

    private InstanceNotFoundException(GetInstanceInfoRequest req) {
        super(String.format("Contract Instance (index: %d, subIndex: %d) not found for block %s",
                req.getAddress().getIndex(),
                req.getAddress().getSubindex(),
                req.getBlockHash().asHex()));
        this.blockHash = req.getBlockHash();
        this.address = req.getAddress();
    }

    public static InstanceNotFoundException from(GetInstanceInfoRequest req) {
        return new InstanceNotFoundException(req);
    }
}
