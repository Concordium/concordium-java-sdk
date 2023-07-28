package com.concordium.sdk.responses.chainparameters;

import com.concordium.grpc.v2.MintDistributionCpv1;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
public class MintDistributionCpV1 extends MintDistribution {
    /**
     * The fraction of newly created CCD allocated to baker rewards.
     */
    private final double bakingReward;

    /**
     * The fraction of newly created CCD allocated to finalization rewards.
     */
    private final double finalizationReward;

    public static MintDistributionCpV1 from(MintDistributionCpv1 update) {
        return MintDistributionCpV1
                .builder()
                .bakingReward(PartsPerHundredThousand.from(update.getBakingReward().getPartsPerHundredThousand()).asDouble())
                .finalizationReward(PartsPerHundredThousand.from(update.getFinalizationReward().getPartsPerHundredThousand()).asDouble())
                .build();
    }

    @Override
    public MintDistributionType getType() {
        return MintDistributionType.V2;
    }
}
