package com.concordium.sdk.requests;

import com.concordium.sdk.responses.Epoch;
import lombok.*;

import javax.annotation.Nullable;

/**
 * Input to queries that take an {@link Epoch} as a parameter.
 */
@Builder(access = AccessLevel.PRIVATE)
public class EpochQuery {

    /**
     * Query for the epoch of a specified block.
     * @param block the specific block to query.
     */
    public static EpochQuery BLOCK_HASH(BlockQuery block) {
        return EpochQuery.builder()
            .blockHashInput(block)
            .type(EpochQueryType.BLOCK_HASH).build();
    }

    /**
     * Query by genesis index and epoch.
     * @param genesisIndex the genesis index to query at. The query is restricted to this genesis index, and will not return results for other indices even if the epoch number is out of bounds.
     * @param epoch the {@link Epoch} to query at.
     */
    public static EpochQuery RELATIVE_EPOCH(int genesisIndex, Epoch epoch) {
        return EpochQuery.builder()
                .type(EpochQueryType.RELATIVE_EPOCH)
                .genesisIndex(genesisIndex)
                .epoch(epoch).build();
    }

    /**
     * Type of {@link EpochQuery}
     */
    @Getter
    @NonNull
    private final EpochQueryType type;

    /**
     * Block with epoch to query. Will only be set if the type is {@link EpochQueryType#BLOCK_HASH}
     */
    @Getter
    @Nullable
    private final BlockQuery blockHashInput;


    /**
     * Epoch to query. Will only be set if the type is {@link EpochQueryType#RELATIVE_EPOCH}
     */
    @Getter
    @Nullable
    private final Epoch epoch;


    /**
     * Genesis index to query at. Will only be set if the type is {@link EpochQueryType#RELATIVE_EPOCH}
     */
    @Getter
    private final int genesisIndex;


    public enum EpochQueryType {
        BLOCK_HASH,
        RELATIVE_EPOCH
    }
}
