package com.concordium.sdk.responses.modulelist;

import com.concordium.sdk.transactions.Hash;

/**
 * A reference to a module deployed on chain.
 * In particular the {@link ModuleRef} is a {@link Hash} of the deployed module.
 */
public class ModuleRef extends Hash {
    ModuleRef(String encoded) {
        super(encoded);
    }

    ModuleRef(final byte[] bytes) {
        super(bytes);
    }

    public static ModuleRef from(String hexHash) {
        return new ModuleRef(hexHash);
    }

    public static ModuleRef from(final byte[] bytes) {
        return new ModuleRef(bytes);
    }

    public static ModuleRef from(com.concordium.grpc.v2.ModuleRef originRef) {
        return ModuleRef.from(originRef.getValue().toByteArray());
    }
}
