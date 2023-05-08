package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.Energy;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class BlockEnergyLimitUpdatePayload implements UpdatePayload {

    /**
     * The block energy limit.
     */
    private UInt64 blockEnergyLimit;

    /**
     * Parses {@link Energy} to {@link BlockEnergyLimitUpdatePayload}.
     * @param energy {@link Energy} returned by the GRPC V2 API.
     * @return parsed {@link BlockEnergyLimitUpdatePayload}.
     */
    public static BlockEnergyLimitUpdatePayload parse(Energy energy) {
        return BlockEnergyLimitUpdatePayload.builder()
                .blockEnergyLimit(UInt64.from(energy.getValue()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.BLOCK_ENERGY_LIMIT_UPDATE;
    }
}
