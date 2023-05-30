package com.concordium.sdk.requests;

import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
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
    public static final BlockHashInput BEST = new BlockHashInput(BlockHashInputType.BEST, null, null, null);

    /**
     * Creates {@link BlockHashInput} representing Last Finalized Block on the connected node
     */
    public static final BlockHashInput LAST_FINAL = new BlockHashInput(BlockHashInputType.LAST_FINAL, null, null, null);

    /**
     * Creates a new {@link BlockHashInput} from input block {@link Hash}
     *
     * @param blockHash Block {@link Hash}
     * @return Instance of {@link BlockHashInput}
     */
    public static BlockHashInput GIVEN(Hash blockHash) {
        return new BlockHashInput(BlockHashInputType.GIVEN, blockHash, null, null);
    }

    /**
     * Creates a new {@link BlockHashInput} from input {@link RelativeHeight}.
     * @param relativeHeight {@link RelativeHeight} of the block.
     * @return Instance of {@link BlockHashInput}.
     */
    public static BlockHashInput RELATIVE(RelativeHeight relativeHeight) {
        return new BlockHashInput(BlockHashInputType.RELATIVE, null, relativeHeight, null);
    }

    public static BlockHashInput ABSOLUTE(UInt64 absoluteHeight) {
        return new BlockHashInput(BlockHashInputType.ABSOLUTE, null, null, absoluteHeight);
    }

    /**
     * Type of {@link BlockHashInput}
     */
    @Getter
    private final BlockHashInputType type;

    /**
     * Hash of the block. This will only be set if the type is {@link BlockHashInputType#GIVEN}.
     */
    @Getter
    @Nullable
    private final Hash blockHash;

    /**
     * Relative height of the block. This will only be set if the type is {@link BlockHashInputType#RELATIVE}.
     */
    @Getter
    @Nullable
    private final RelativeHeight relativeHeight;

    /**
     * Absolute height of the block. This will only be set if the type is {@link BlockHashInputType#ABSOLUTE}.
     */
    @Getter
    @Nullable
    private final UInt64 absoluteHeight;
}

