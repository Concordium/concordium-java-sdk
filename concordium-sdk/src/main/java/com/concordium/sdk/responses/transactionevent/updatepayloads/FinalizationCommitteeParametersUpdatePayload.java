package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.FinalizationCommitteeParameters;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.types.UInt32;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class FinalizationCommitteeParametersUpdatePayload implements UpdatePayload {

    /**
     * The minimum size of a finalization committee before 'finalizer relative take threshold' takes effect.
     */
    private UInt32 minimumFinalizers;

    /**
     * The maximum size of a finalization committee.
     */
    private UInt32 maximumFinalizers;

    /**
     * The threshold for determining the stake required for being eligible in the finalization committee.
     * The amount is given by 'total stake in pools * finalizer relative stake threshold'
     */
    private PartsPerHundredThousand finalizerRelativeStakeThreshold;

    /**
     * Parses {@link FinalizationCommitteeParameters} to {@link FinalizationCommitteeParametersUpdatePayload}.
     *
     * @param finalizationCommitteeParameters {@link FinalizationCommitteeParameters} returned by the GRPC V2 API.
     * @return parsed {@link FinalizationCommitteeParametersUpdatePayload}.
     */
    public static FinalizationCommitteeParametersUpdatePayload parse(FinalizationCommitteeParameters finalizationCommitteeParameters){
        return FinalizationCommitteeParametersUpdatePayload.builder()
                .minimumFinalizers(UInt32.from(finalizationCommitteeParameters.getMinimumFinalizers()))
                .maximumFinalizers(UInt32.from(finalizationCommitteeParameters.getMaximumFinalizers()))
                .finalizerRelativeStakeThreshold(PartsPerHundredThousand.parse(finalizationCommitteeParameters.getFinalizerRelativeStakeThreshold()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.FINALIZATION_COMMITTEE_PARAMETERS_UPDATE;
    }
}
