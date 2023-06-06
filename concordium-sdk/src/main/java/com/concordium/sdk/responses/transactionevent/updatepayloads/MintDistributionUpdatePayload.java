package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.MintDistributionCpv0;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The mint distribution was updated.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class MintDistributionUpdatePayload implements UpdatePayload {

    /**
     * Mint rate per slot.
     */
    private double mintRate;

    /**
     * The fraction of newly created CCD allocated to baker rewards.
     */
    private PartsPerHundredThousand bakingReward;

    /**
     * The fraction of newly created CCD allocated to finalization rewards.
     */
    private PartsPerHundredThousand finalizationReward;




    /**
     * Parses {@link MintDistributionCpv0} to {@link MintDistributionUpdatePayload}.
     * @param mintDistributionCpv0 {@link MintDistributionCpv0} returned by the GRPC V2 API.
     * @return parsed {@link MintDistributionUpdatePayload}.
     */
    public static MintDistributionUpdatePayload parse (MintDistributionCpv0 mintDistributionCpv0) {
        return MintDistributionUpdatePayload.builder()
                .mintRate(ParsingHelper.toMintRate(mintDistributionCpv0.getMintPerSlot()))
                .bakingReward(PartsPerHundredThousand.parse(mintDistributionCpv0.getBakingReward()))
                .finalizationReward(PartsPerHundredThousand.parse(mintDistributionCpv0.getFinalizationReward()))
        .build();
    }
    @Override
    public UpdateType getType() {
        return UpdateType.MINT_DISTRIBUTION_UPDATE;
    }

}
