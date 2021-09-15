package com.concordium.sdk.exceptions;

import com.concordium.sdk.transactions.Hash;
import lombok.Getter;

public final class BlockNotFoundException extends Exception {
    @Getter
    private final Hash blockHash;

    /**
     * Creates a new {@link BlockNotFoundException} from a {@link Hash}
     * This happens when a block could not be found.
     *
     * Use {@link BlockNotFoundException#from(Hash)} to instantiate.
     *
     * @param blockHash The block hash
     */
    private BlockNotFoundException(Hash blockHash) {
        super("Block not found " + blockHash.asHex());
        this.blockHash = blockHash;
    }

    public static BlockNotFoundException from(Hash blockHash) {
        return new BlockNotFoundException(blockHash);
    }
}
