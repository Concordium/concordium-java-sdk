package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.GasRewards;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class GasRewardsUpdatePayload implements UpdatePayload {

    /**
     * Fraction paid to the baker.
     */
    private PartsPerHundredThousand baker;

    /**
     * Fraction paid for including a finalization proof in a block.
     */
    private PartsPerHundredThousand finalizationProof;

    /**
     * Fraction paid for including each account creation transaction in a block.
     */
    private PartsPerHundredThousand accountCreation;

    /**
     * Fraction paid for including an update transaction in a block.
     */
    private PartsPerHundredThousand chainUpdate;

    /**
     * TODO remove class
     * Parses {@link GasRewards} to {@link GasRewardsUpdatePayload}.
     * @param gasRewards {@link GasRewards} returned by the GRPC V2 API.
     * @return parsed {@link GasRewardsUpdatePayload}.
     */
    public static GasRewardsUpdatePayload parse(GasRewards gasRewards) {
        return GasRewardsUpdatePayload.builder()
                .baker(PartsPerHundredThousand.parse(gasRewards.getBaker()))
                .finalizationProof(PartsPerHundredThousand.parse(gasRewards.getFinalizationProof()))
                .accountCreation(PartsPerHundredThousand.parse(gasRewards.getAccountCreation()))
                .chainUpdate(PartsPerHundredThousand.parse(gasRewards.getChainUpdate()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.GAS_REWARDS_UPDATE;
    }
}
