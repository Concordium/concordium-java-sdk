package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.UInt64;
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
    private final CCDAmount mintBakingReward;
    private final CCDAmount mintFinalizationReward;
    private final CCDAmount mintPlatformDevelopmentCharge;
    private AccountAddress foundationAccount;
    private final List<FinalizationReward> finalizationRewards;
    private final CCDAmount remainder;
    private final CCDAmount transactionFees;
    private final CCDAmount oldGASAccount;
    private final CCDAmount newGASAccount;
    private final CCDAmount bakerReward;
    private final CCDAmount foundationCharge;
    private AccountAddress baker;
    private final List<BakerReward> bakerRewards;
    private final UInt64 bakerId;
    private final CCDAmount passiveReward;
    private final CCDAmount developmentCharge;
    private final CCDAmount finalizationReward;
    private final Integer poolOwner;
    private AccountAddress account;

    @JsonCreator
    SpecialEvent(@JsonProperty("tag") String tag,
                 @JsonProperty("mintBakingReward") CCDAmount mintBakingReward,
                 @JsonProperty("mintFinalizationReward") CCDAmount mintFinalizationReward,
                 @JsonProperty("mintPlatformDevelopmentCharge") CCDAmount mintPlatformDevelopmentCharge,
                 @JsonProperty("foundationAccount") String foundationAccount,
                 @JsonProperty("finalizationRewards") List<FinalizationReward> finalizationRewards,
                 @JsonProperty("remainder") CCDAmount remainder,
                 @JsonProperty("transactionFees") CCDAmount transactionFees,
                 @JsonProperty("oldGASAccount") CCDAmount oldGASAccount,
                 @JsonProperty("newGASAccount") CCDAmount newGASAccount,
                 @JsonProperty("bakerReward") CCDAmount bakerReward,
                 @JsonProperty("foundationCharge") CCDAmount foundationCharge,
                 @JsonProperty("baker") String baker,
                 @JsonProperty("bakerRewards") List<BakerReward> bakerRewards,
                 @JsonProperty("bakerId") long bakerId,
                 @JsonProperty("passiveReward") CCDAmount passiveReward,
                 @JsonProperty("developmentCharge") CCDAmount developmentCharge,
                 @JsonProperty("finalizationReward") CCDAmount finalizationReward,
                 @JsonProperty("poolOwner") Integer poolOwner,
                 @JsonProperty("account") String account) {
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
        this.bakerId = UInt64.from(bakerId);
        this.passiveReward = passiveReward;
        this.developmentCharge = developmentCharge;
        this.finalizationReward = finalizationReward;
        this.poolOwner = poolOwner;
        if(!Objects.isNull(account)) {
            this.account = AccountAddress.from(account);
        }
    }
}
