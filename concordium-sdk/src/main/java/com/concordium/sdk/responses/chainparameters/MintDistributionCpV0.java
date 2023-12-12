package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Mint distribution for chain parameters v0.
 */
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
public class MintDistributionCpV0 extends MintDistribution {

    /**
     * Mint rate per slot.
     */
    private final double mintPerSlot;

    /**
     * The fraction of newly created CCD allocated to baker rewards.
     */
    private final double bakingReward;

    /**
     * The fraction of newly created CCD allocated to finalization rewards.
     */
    private final double finalizationReward;

    public static MintDistributionCpV0 from(com.concordium.grpc.v2.MintDistributionCpv0 value) {
        return MintDistributionCpV0
                .builder()
                .mintPerSlot(value.getMintPerSlot().getMantissa() * Math.pow(10, -1 * value.getMintPerSlot().getExponent()))
                .bakingReward(PartsPerHundredThousand.from(value.getBakingReward().getPartsPerHundredThousand()).asDouble())
                .finalizationReward(PartsPerHundredThousand.from(value.getFinalizationReward().getPartsPerHundredThousand()).asDouble())
                .build();
    }

    @Override
    public MintDistribution.MintDistributionType getType() {
        return MintDistributionType.V1;
    }
}
