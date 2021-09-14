package com.concordium.sdk.responsetypes.transactionstatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RejectReasonType {
    @JsonProperty("AmountTooLarge")
    AMOUNT_TOO_LARGE,
    @JsonProperty("DuplicateAggregationKey")
    DUPLICATE_AGGREGATION_KEY,
    @JsonProperty("BakerInCooldown")
    BAKER_IN_COOLDOWN,
    @JsonProperty("SerializationFailure")
    SERIALIZATION_FAILURE,
    @JsonProperty("InvalidAccountReference")
    INVALID_ACCOUNT_REFERENCE,
    @JsonProperty("InvalidEncryptedAmountTransferProof")
    INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF,
    @JsonProperty("StakeUnderMinimumThresholdForBaking")
    STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING,
    @JsonProperty("ModuleNotWF")
    MODULE_NOT_WF
}
