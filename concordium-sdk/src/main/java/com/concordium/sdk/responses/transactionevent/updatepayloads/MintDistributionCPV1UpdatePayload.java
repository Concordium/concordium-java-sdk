package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.MintDistributionCpv1;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class MintDistributionCPV1UpdatePayload implements UpdatePayload {

    /**
     * The fraction of newly created CCD allocated to baker rewards.
     */
    private PartsPerHundredThousand bakingReward;
    /**
     * The fraction of newly created CCD allocated to finalization rewards.
     */
    private PartsPerHundredThousand finalizationReward;

    /**
     * Parses {@link MintDistributionCpv1} to {@link MintDistributionCPV1UpdatePayload}.
     * @param mintDistributionCpv1 {@link MintDistributionCpv1} returned by the GRPC V2 API.
     * @return parsed {@link MintDistributionCPV1UpdatePayload}.
     */
    public static MintDistributionCPV1UpdatePayload parse (MintDistributionCpv1 mintDistributionCpv1) {
        return MintDistributionCPV1UpdatePayload.builder()
                .bakingReward(PartsPerHundredThousand.parse(mintDistributionCpv1.getBakingReward()))
                .finalizationReward(PartsPerHundredThousand.parse(mintDistributionCpv1.getFinalizationReward()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.MINT_DISTRIBUTION_CPV_1_UPDATE;
    }
}
