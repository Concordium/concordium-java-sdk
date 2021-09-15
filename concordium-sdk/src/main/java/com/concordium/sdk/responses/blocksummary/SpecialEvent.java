package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@ToString
public class SpecialEvent {
    private final String tag;
    private final String mintBakingReward;
    private final String mintFinalizationReward;
    private final String mintPlatformDevelopmentCharge;
    private AccountAddress foundationAccount;
    private final List<FinalizationReward> finalizationRewards;
    private final String remainder;
    private final String transactionFees;
    private final String oldGASAccount;
    private final String newGASAccount;
    private final String bakerReward;
    private final String foundationCharge;
    private AccountAddress baker;
    private final List<BakerReward> bakerRewards;

    @JsonCreator
    SpecialEvent(@JsonProperty("tag") String tag,
                 @JsonProperty("mintBakingReward") String mintBakingReward,
                 @JsonProperty("mintFinalizationReward") String mintFinalizationReward,
                 @JsonProperty("mintPlatformDevelopmentCharge") String mintPlatformDevelopmentCharge,
                 @JsonProperty("foundationAccount") String foundationAccount,
                 @JsonProperty("finalizationRewards") List<FinalizationReward> finalizationRewards,
                 @JsonProperty("remainder") String remainder,
                 @JsonProperty("transactionFees") String transactionFees,
                 @JsonProperty("oldGASAccount") String oldGASAccount,
                 @JsonProperty("newGASAccount") String newGASAccount,
                 @JsonProperty("bakerReward") String bakerReward,
                 @JsonProperty("foundationCharge") String foundationCharge,
                 @JsonProperty("baker") String baker,
                 @JsonProperty("bakerRewards") List<BakerReward> bakerRewards) {
        if (!Objects.isNull(foundationAccount)) {
            this.foundationAccount = AccountAddress.from(foundationAccount);
        }
        this.tag = tag;
        this.mintBakingReward = mintBakingReward;
        this.mintFinalizationReward = mintFinalizationReward;
        this.mintPlatformDevelopmentCharge = mintPlatformDevelopmentCharge;
        this.finalizationRewards = finalizationRewards;
        this.remainder = remainder;
        this.transactionFees = transactionFees;
        this.oldGASAccount = oldGASAccount;
        this.newGASAccount = newGASAccount;
        this.bakerReward = bakerReward;
        this.bakerRewards = bakerRewards;
        this.foundationCharge = foundationCharge;
        if (!Objects.isNull(baker)) {
            this.baker = AccountAddress.from(baker);
        }

    }
}
