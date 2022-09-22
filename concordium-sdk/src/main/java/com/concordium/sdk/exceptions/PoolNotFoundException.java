package com.concordium.sdk.exceptions;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.transactions.Hash;
import lombok.Getter;

public final class PoolNotFoundException extends Exception {
    @Getter
    private final BakerId bakerId;
    @Getter
    private final Hash blockHash;

    /**
     * Creates a new {@link PoolNotFoundException} from a {@link BakerId} and a {@link Hash}.
     * This happens when the Pool could not be found for the given block.
     * <p>
     * Use {@link PoolNotFoundException#from(BakerId, Hash)} to instantiate.
     *
     * @param bakerId   The Baker Id for which the pool could not be found.
     * @param blockHash The block hash
     */
    private PoolNotFoundException(BakerId bakerId, Hash blockHash) {
        super("Pool for Baker (" + bakerId.toString() + ") not found for block " + blockHash.asHex());
        this.bakerId = bakerId;
        this.blockHash = blockHash;
    }

    public static PoolNotFoundException from(BakerId bakerId, Hash blockHash) {
        return new PoolNotFoundException(bakerId, blockHash);
    }
}
