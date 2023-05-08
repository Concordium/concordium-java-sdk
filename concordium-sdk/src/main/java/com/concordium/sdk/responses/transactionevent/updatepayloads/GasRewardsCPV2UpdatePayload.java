package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.GasRewardsCpv2;
import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class GasRewardsCPV2UpdatePayload implements UpdatePayload {

    /**
     * Fraction paid to the baker.
     */
    private Fraction baker;
    /**
     * Fraction paid for including each account transaction in a block.
     */
    private Fraction accountCreation;
    /**
     * Fraction paid for including an update transaction in a block.
     */
    private Fraction chainUpdate;


    /**
     * Parses {@link GasRewardsCpv2} to {@link GasRewardsCPV2UpdatePayload}.
     * @param gasRewardsCpv2 {@link GasRewardsCpv2} returned by the GRPC V2 API.
     * @return parsed {@link GasRewardsCPV2UpdatePayload}.
     */
    public static GasRewardsCPV2UpdatePayload parse(GasRewardsCpv2 gasRewardsCpv2) {
        return GasRewardsCPV2UpdatePayload.builder()
                .baker(Fraction.from(gasRewardsCpv2.getBaker()))
                .accountCreation(Fraction.from(gasRewardsCpv2.getAccountCreation()))
                .chainUpdate(Fraction.from(gasRewardsCpv2.getChainUpdate()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.GAS_REWARDS_CPV_2_UPDATE;
    }
}
