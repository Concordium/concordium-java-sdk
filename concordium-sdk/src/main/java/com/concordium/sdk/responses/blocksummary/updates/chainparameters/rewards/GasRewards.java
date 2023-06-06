package com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards;

import com.concordium.sdk.responses.transactionevent.updatepayloads.UpdatePayload;
import com.concordium.sdk.responses.transactionevent.updatepayloads.UpdateType;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;

/**
 * Distribution of gas rewards for chain parameters version 0 and 1.
 */
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Getter
public final class GasRewards implements UpdatePayload {
    /**
     * Fraction paid for including an update transaction in a block.
     */
    private final PartsPerHundredThousand chainUpdate;
    /**
     * Fraction paid for including each account creation transaction in a block.
     */
    private final PartsPerHundredThousand accountCreation;
    /**
     * Fraction paid to the baker.
     */
    private final PartsPerHundredThousand baker;
    /**
     * Fraction paid for including a finalization proof in a block.
     */
    private final PartsPerHundredThousand finalizationProof;

    @JsonCreator
    GasRewards(@JsonProperty("chainUpdate") BigInteger chainUpdate,
               @JsonProperty("accountCreation") BigInteger accountCreation,
               @JsonProperty("baker") BigInteger baker,
               @JsonProperty("finalizationProof") BigInteger finalizationProof) {
        this.chainUpdate = PartsPerHundredThousand.from(chainUpdate);
        this.accountCreation = PartsPerHundredThousand.from(accountCreation);
        this.baker = PartsPerHundredThousand.from(baker);
        this.finalizationProof = PartsPerHundredThousand.from(finalizationProof);
    }

    /**
     * Parses {@link com.concordium.grpc.v2.GasRewards} to {@link GasRewards}.
     * param gasRewards {@link com.concordium.grpc.v2.GasRewards} returned by the GRPC V2 API.
     * @return parsed {@link GasRewards}.
    */
    public static GasRewards parse(com.concordium.grpc.v2.GasRewards gasRewards) {
        return GasRewards.builder()
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
