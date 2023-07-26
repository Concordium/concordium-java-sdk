package com.concordium.sdk.requests;

import com.concordium.sdk.responses.blocksatheight.BlocksAtHeightRequest;
import com.concordium.sdk.transactions.Hash;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

/**
 * Type of Block to query an API with. For a list of types see {@link BlockQueryType}
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockQuery {

    /**
     * Creates {@link BlockQuery} representing Best Block on the connected node
     */
    public static final BlockQuery BEST = new BlockQuery(BlockQueryType.BEST, null, null);

    /**
     * Creates {@link BlockQuery} representing Last Finalized Block on the connected node
     */
    public static final BlockQuery LAST_FINAL = new BlockQuery(BlockQueryType.LAST_FINAL, null, null);

    /**
     * Creates a new {@link BlockQuery} from input block {@link Hash}
     *
     * @param blockHash Block {@link Hash}
     * @return Instance of {@link BlockQuery}
     */
    public static BlockQuery HASH(Hash blockHash) {
        return new BlockQuery(BlockQueryType.GIVEN, blockHash, null);
    }

    public static BlockQuery HEIGHT(BlocksAtHeightRequest height) {
        return new BlockQuery(BlockQueryType.HEIGHT, null, height);
    }

    /**
     * Type of {@link BlockQuery}
     */
    @Getter
    private final BlockQueryType type;

    /**
     * Hash of the block. This will only be set if the type is {@literal BlockHashInputType.GIVEN}
     */
    @Getter
    @Nullable
    private final Hash blockHash;

    @Nullable
    @Getter
    private final BlocksAtHeightRequest height;
}

