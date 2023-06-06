package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.GasRewardsCpv2;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The gas rewards were updated (chain parameters version 2).
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class GasRewardsCPV2UpdatePayload implements UpdatePayload {

    /**
     * Fraction paid to the baker.
     */
    private PartsPerHundredThousand baker;
    /**
     * Fraction paid for including each account transaction in a block.
     */
    private PartsPerHundredThousand accountCreation;
    /**
     * Fraction paid for including an update transaction in a block.
     */
    private PartsPerHundredThousand chainUpdate;


    /**
     * Parses {@link GasRewardsCpv2} to {@link GasRewardsCPV2UpdatePayload}.
     * @param gasRewardsCpv2 {@link GasRewardsCpv2} returned by the GRPC V2 API.
     * @return parsed {@link GasRewardsCPV2UpdatePayload}.
     */
    public static GasRewardsCPV2UpdatePayload parse(GasRewardsCpv2 gasRewardsCpv2) {
        return GasRewardsCPV2UpdatePayload.builder()
                .baker(PartsPerHundredThousand.parse(gasRewardsCpv2.getBaker()))
                .accountCreation(PartsPerHundredThousand.parse(gasRewardsCpv2.getAccountCreation()))
                .chainUpdate(PartsPerHundredThousand.parse(gasRewardsCpv2.getChainUpdate()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.GAS_REWARDS_CPV_2_UPDATE;
    }
}
