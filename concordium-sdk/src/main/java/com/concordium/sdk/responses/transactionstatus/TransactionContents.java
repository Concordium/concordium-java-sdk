package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
enum TransactionContents {
    @JsonProperty("transferWithMemo")
    TRANSFER_WITH_MEMO,
    @JsonProperty("transferWithScheduleAndMemo")
    TRANSFER_WITH_SCHEDULE_AND_MEMO,
    @JsonProperty("encryptedAmountTransferWithMemo")
    ENCRYPTED_AMOUNT_TRANSFER_WITH_MEMO,
    @JsonProperty("updateCredentials")
    UPDATE_CREDENTIALS,
    @JsonProperty("transferWithSchedule")
    TRANSFER_WITH_SCHEDULE,
    @JsonProperty("transferToPublic")
    TRANSFER_TO_PUBLIC,
    @JsonProperty("encryptedAmountTransfer")
    ENCRYPTED_AMOUNT_TRANSFER,
    @JsonProperty("transferToEncrypted")
    TRANSFER_TO_ENCRYPTED,
    @JsonProperty("updateCredentialKeys")
    UPDATE_CREDENTIAL_KEYS,
    @JsonProperty("deployModule")
    DEPLOY_MODULE,
    @JsonProperty("transfer")
    TRANSFER,
    @JsonProperty("initial")
    INITIAL,
    @JsonProperty("normal")
    NORMAL,
    @JsonProperty("addBaker")
    ADD_BAKER,
    @JsonProperty("updateBakerStake")
    UPDATE_BAKER_STAKE,
    @JsonProperty("updateBakerKeys")
    UPDATE_BAKER_KEYS,
    @JsonProperty("updateBakerRestakeEarnings")
    UPDATE_BAKER_RESTAKE_EARNINGS,
    @JsonProperty("updateProtocol")
    UPDATE_PROTOCOL,
    @JsonProperty("initContract")
    CONTRACT_INITIALIZED,
    @JsonProperty("registerData")
    REGISTER_DATA,
    @JsonProperty("removeBaker")
    REMOVE_BAKER,
    @JsonProperty("update")
    CONTRACT_UPDATED,
    @JsonProperty("updateMicroGTUPerEuro")
    UPDATE_MICRO_GTU_PER_EURO,
    @JsonProperty("updateBakerStakeThreshold")
    UPDATE_BAKER_STAKE_THRESHOLD,
    @JsonProperty("updateElectionDifficulty")
    UPDATE_ELECTION_DIFFICULTY,
    @JsonProperty("gASRewards")
    GAS_REWARDS,
    @JsonProperty("updateGASRewards")
    UPDATE_GAS_REWARDS,
    @JsonProperty("updateFoundationAccount")
    UPDATE_FOUNDATION_ACCOUNT,
    @JsonProperty("updateTransactionFeeDistribution")
    UPDATE_TRANSACTION_FEE_DISTRIBUTION,
    @JsonProperty("updateEuroPerEnergy")
    UPDATE_EURO_PER_ENERGY,
    @JsonProperty("updateLevel2Keys")
    UPDATE_LEVEL_2_KEYS,
    @JsonProperty("updateLevel1Keys")
    UPDATE_LEVEL_1_KEYS,
    @JsonProperty("updateMintDistribution")
    UPDATE_MINT_DISTRIBUTION,
    @JsonProperty("updateAddAnonymityRevoker")
    UPDATE_ADD_ANONYMITY_REVOKER,
    @JsonProperty("addIdentityProvider")
    ADD_IDENTITY_PROVIDER,
    @JsonProperty("updateAddIdentityProvider")
    UPDATE_ADD_IDENTITY_PROVIDER;
}
