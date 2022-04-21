package com.concordium.sdk.exceptions;

import com.concordium.sdk.BlocksAtHeightRequest;
import com.concordium.sdk.transactions.Hash;
import lombok.Getter;

@Getter
public final class BlockNotFoundException extends Exception {
    private Hash blockHash;
    private BlocksAtHeightRequest atHeight;

    /**
     * Creates a new {@link BlockNotFoundException} from a {@link Hash}
     * This happens when a block could not be found.
     * <p>
     * Use {@link BlockNotFoundException#from(Hash)} to instantiate.
     *
     * @param blockHash The block hash
     */
    private BlockNotFoundException(Hash blockHash) {
        super("Block not found: " + blockHash.asHex());
        this.blockHash = blockHash;
    }

    private BlockNotFoundException(BlocksAtHeightRequest height) {
        super("Block not found: " + height.toPrettyExceptionMessage());
        this.atHeight = height;
    }

    public static BlockNotFoundException from(Hash blockHash) {
        return new BlockNotFoundException(blockHash);
    }

    public static BlockNotFoundException from(BlocksAtHeightRequest height) {
        return new BlockNotFoundException(height);
    }
}
