package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.sdk.types.Energy;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The block energy limit was updated (chain parameters version 2)
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class BlockEnergyLimitUpdatePayload implements UpdatePayload {

    /**
     * The new block energy limit.
     */
    private Energy blockEnergyLimit;

    /**
     * Parses {@link com.concordium.grpc.v2.Energy} to {@link BlockEnergyLimitUpdatePayload}.
     * @param energy {@link com.concordium.grpc.v2.Energy} returned by the GRPC V2 API.
     * @return parsed {@link BlockEnergyLimitUpdatePayload}.
     */
    public static BlockEnergyLimitUpdatePayload parse(com.concordium.grpc.v2.Energy energy) {
        return BlockEnergyLimitUpdatePayload.builder()
                .blockEnergyLimit(Energy.parse(energy))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.BLOCK_ENERGY_LIMIT_UPDATE;
    }
}
