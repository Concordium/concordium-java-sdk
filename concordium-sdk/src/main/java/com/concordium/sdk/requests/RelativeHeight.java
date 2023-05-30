package com.concordium.sdk.requests;

import com.concordium.grpc.v2.BlockHeight;
import com.concordium.grpc.v2.GenesisIndex;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * For creating {@link BlockHashInput} with a relative block height.
 */
@Builder
@EqualsAndHashCode
@ToString
@Getter
public class RelativeHeight {

    /**
     * Genesis index to start from.
     */
    private UInt32 genesisIndex;

    /**
     * Height starting from the genesis block at the genesis index.
     */
    private UInt64 height;

    /**
     * Whether to return results only from the specified genesis index (`true`),
     * or allow results from more recent genesis indices as well (`false`).
     */
    private boolean restrict;

    /**
     * Converts {@link RelativeHeight} to equivalent GRPC V2 API {@link com.concordium.grpc.v2.BlockHashInput.RelativeHeight} object.
     * @param relativeHeight {@link RelativeHeight} to convert.
     * @return converted {@link com.concordium.grpc.v2.BlockHashInput.RelativeHeight}.
     */
    public static com.concordium.grpc.v2.BlockHashInput.RelativeHeight convertToGRPC(RelativeHeight relativeHeight) {
        return com.concordium.grpc.v2.BlockHashInput.RelativeHeight.newBuilder()
                .setGenesisIndex(GenesisIndex.newBuilder().setValue(relativeHeight.getGenesisIndex().getValue()))
                .setHeight(BlockHeight.newBuilder().setValue(relativeHeight.getHeight().getValue()))
                .setRestrict(relativeHeight.isRestrict())
                .build();
    }

}
