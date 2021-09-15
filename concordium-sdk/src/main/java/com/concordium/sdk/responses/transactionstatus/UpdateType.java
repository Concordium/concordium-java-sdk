package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonProperty;

enum UpdateType {
    @JsonProperty("protocol")
    PROTOCOL,
    @JsonProperty("microGTUPerEuro")
    MICRO_GTU_PER_EURO,
    @JsonProperty("bakerStakeThreshold")
    BAKER_STAKE_THRESHOLD,
    @JsonProperty("electionDifficulty")
    ELECTION_DIFFICULTY,
    @JsonProperty("gASRewards")
    GAS_REWARDS,
    @JsonProperty("foundationAccount")
    FOUNDATION_ACCOUNT,
    @JsonProperty("transactionFeeDistribution")
    TRANSACTION_FEE_DISTRIBUTION,
    @JsonProperty("euroPerEnergy")
    EURO_PER_ENERGY,
    @JsonProperty("root")
    ROOT,
    @JsonProperty("level1")
    LEVEL_1,
    @JsonProperty("level2")
    LEVEL_2,
    @JsonProperty("mintDistribution")
    MINT_DISTRIBUTION,
    @JsonProperty("addAnonymityRevoker")
    ADD_ANONYMITY_REVOKER,
    @JsonProperty("addIdentityProvider")
    ADD_IDENTITY_PROVIDER;
}
