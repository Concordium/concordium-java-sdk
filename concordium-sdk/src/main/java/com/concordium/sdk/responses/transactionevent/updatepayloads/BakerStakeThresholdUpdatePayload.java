package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.BakerStakeThreshold;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class BakerStakeThresholdUpdatePayload implements UpdatePayload {

    /**
     * Minimum threshold required for registering as a baker. Only applies to protocol version 1-3.
     */
    private CCDAmount bakerStakeThreshold;

    /**
     * Parses {@link BakerStakeThreshold} to {@link BakerStakeThresholdUpdatePayload}.
     * @param bakerStakeThreshold {@link BakerStakeThreshold} returned by the GRPC V2 API.
     * @return parsed {@link BakerStakeThresholdUpdatePayload}.
     */
    public static BakerStakeThresholdUpdatePayload parse(BakerStakeThreshold bakerStakeThreshold) {
        return BakerStakeThresholdUpdatePayload.builder()
                .bakerStakeThreshold(CCDAmount.fromMicro(bakerStakeThreshold.getBakerStakeThreshold().getValue()))
                .build();
    }
    @Override
    public UpdateType getType() {
        return UpdateType.BAKER_STAKE_THRESHOLD_UPDATE;
    }
}
