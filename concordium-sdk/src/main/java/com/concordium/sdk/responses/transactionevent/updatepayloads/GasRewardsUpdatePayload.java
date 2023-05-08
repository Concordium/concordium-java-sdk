package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.GasRewards;
import com.concordium.sdk.responses.blocksummary.updates.Fraction;
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
     * Fraction paid to the baker
     */
    private Fraction baker;

    /**
     * Fraction paid for including a finalization proof in a block
     */
    private Fraction finalizationProof;

    /**
     * Fraction paid for including each account creation transaction in a block
     */
    private Fraction accountCreation;

    /**
     * Fraction paid for including an update transaction in a block
     */
    private Fraction chainUpdate;

    /**
     * Parses {@link GasRewards} to {@link GasRewardsUpdatePayload}
     * @param gasRewards {@link GasRewards} returned by the GRPC V2 API
     * @return parsed {@link GasRewardsUpdatePayload}
     */
    public static GasRewardsUpdatePayload parse(GasRewards gasRewards) {
        return GasRewardsUpdatePayload.builder()
                .baker(Fraction.from(gasRewards.getBaker()))
                .finalizationProof(Fraction.from(gasRewards.getFinalizationProof()))
                .accountCreation(Fraction.from(gasRewards.getAccountCreation()))
                .chainUpdate(Fraction.from(gasRewards.getChainUpdate()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.GAS_REWARDS_UPDATE;
    }
}
