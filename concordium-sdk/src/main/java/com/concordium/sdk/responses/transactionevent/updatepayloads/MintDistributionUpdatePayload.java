package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.MintDistributionCpv0;
import com.concordium.grpc.v2.MintRate;
import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class MintDistributionUpdatePayload implements UpdatePayload {

    /**
     * Mint rate per slot
     */
    private double mintRate;

    /**
     * The fraction of newly created CCD allocated to baker rewards
     */
    private Fraction bakingReward;

    /**
     * The fraction of newly created CCD allocated to finalization rewards
     */
    private Fraction finalizationReward;




    /**
     * Parses {@link MintDistributionCpv0} to {@link MintDistributionUpdatePayload}
     * @param mintDistributionCpv0 {@link MintDistributionCpv0} returned by the GRPC V2 API
     * @return parsed {@link MintDistributionUpdatePayload}
     */
    public static MintDistributionUpdatePayload parse (MintDistributionCpv0 mintDistributionCpv0) {
        return MintDistributionUpdatePayload.builder()
                .mintRate(toMintRate(mintDistributionCpv0.getMintPerSlot()))
                .bakingReward(Fraction.from(mintDistributionCpv0.getBakingReward()))
                .finalizationReward(Fraction.from(mintDistributionCpv0.getFinalizationReward()))
        .build();
    }
    @Override
    public UpdateType getType() {
        return UpdateType.MINT_DISTRIBUTION_UPDATE;
    }

    /**
     * Calculates mantissa*10^(-exponent)
     */
    private static double toMintRate (MintRate mintRate) {
        return mintRate.getMantissa()*Math.pow(10, -1 * mintRate.getExponent());
    }
}
