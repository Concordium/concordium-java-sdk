package com.concordium.sdk.requests;

import com.concordium.sdk.transactions.Hash;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

/**
 * Type of Block to query an API with. For a list of types see {@link BlockHashInputType}
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockHashInput {

    /**
     * Creates {@link BlockHashInput} representing Best Block on the connected node
     */
    public static final BlockHashInput BEST = new BlockHashInput(BlockHashInputType.BEST, null);

    /**
     * Creates {@link BlockHashInput} representing Last Finalized Block on the connected node
     */
    public static final BlockHashInput LAST_FINAL = new BlockHashInput(BlockHashInputType.LAST_FINAL, null);

    /**
     * Creates a new {@link BlockHashInput} from input block {@link Hash}
     *
     * @param blockHash Block {@link Hash}
     * @return Instance of {@link BlockHashInput}
     */
    public static BlockHashInput GIVEN(Hash blockHash) {
        return new BlockHashInput(BlockHashInputType.GIVEN, blockHash);
    }

    /**
     * Type of {@link BlockHashInput}
     */
    @Getter
    private final BlockHashInputType type;

    /**
     * Hash of the block. This will only be set if the type is {@literal BlockHashInputType.GIVEN}
     */
    @Getter
    @Nullable
    private final Hash blockHash;
}

