package com.concordium.sdk.responses.transactionstatus;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

// These types must correspond the 'RejectReason' types found here
// https://github.com/Concordium/concordium-base/blob/f0e1275dfde8502e3aabdb153b3246c18bee59f9/haskell-src/Concordium/Types/Execution.hs#L737
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
    MODULE_NOT_WF,
    @JsonProperty("RejectedReceive")
    REJECTED_RECEIVE,
    @JsonProperty("InsufficientBalanceForBakerStake")
    INSUFFICIENT_BALANCE_FOR_BAKER_STAKE,
    @JsonProperty("ModuleHashAlreadyExists")
    MODULE_HASH_ALREADY_EXISTS,
    @JsonProperty("OutOfEnergy")
    OUT_OF_ENERGY,
    @JsonProperty("InvalidReceiveMethod")
    INVALID_RECEIVE_METHOD,
    @JsonProperty("NotAllowedToReceiveEncrypted")
    NOT_ALLOWED_TO_RECEIVE_ENCRYPTED,
    @JsonProperty("InvalidInitMethod")
    INVALID_INIT_METHOD,
    @JsonProperty("RejectedInit")
    REJECTED_INIT,
    @JsonProperty("InvalidCredentials")
    INVALID_CREDENTIALS,
    @JsonProperty("InvalidTransferToPublicProof")
    INVALID_TRANSFER_TO_PUBLIC_PROOF,
    @JsonProperty("ZeroScheduledAmount")
    ZERO_SCHEDULED_AMOUNT,
    @JsonProperty("NotAllowedMultipleCredentials")
    NOT_ALLOWED_MULTIPLE_CREDENTIALS,
    @JsonProperty("NotAllowedToHandleEncrypted")
    NOT_ALLOWED_TO_HANDLE_ENCRYPTED,
    @JsonProperty("DuplicateCredIDs")
    DUPLICATE_CRED_IDS,
    @JsonProperty("FirstScheduledReleaseExpired")
    FIRST_SCHEDULED_RELEASE_EXPIRED,
    @JsonProperty("EncryptedAmountSelfTransfer")
    ENCRYPTED_AMOUNT_SELF_TRANSFER,
    @JsonProperty("InvalidModuleReference")
    INVALID_MODULE_REFERENCE,
    @JsonProperty("InvalidContractAddress")
    INVALID_CONTRACT_ADDRESS,
    @JsonProperty("RuntimeFailure")
    RUNTIME_FAILURE,
    @JsonProperty("NonExistentRewardAccount")
    NON_EXISTENT_REWARD_ACCOUNT,
    @JsonProperty("InvalidProof")
    INVALID_PROOF,
    @JsonProperty("AlreadyABaker")
    ALREADY_A_BAKER,
    @JsonProperty("NotABaker")
    NOT_A_BAKER,
    @JsonProperty("NonExistentCredentialID")
    NON_EXISTENT_CREDENTIAL_ID,
    @JsonProperty("KeyIndexAlreadyInUse")
    KEY_INDEX_ALREADY_IN_USE,
    @JsonProperty("InvalidAccountThreshold")
    INVALID_ACCOUNT_THRESHOLD,
    @JsonProperty("InvalidCredentialKeySignThreshold")
    INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD,
    @JsonProperty("InvalidIndexOnEncryptedTransfer")
    INVALID_INDEX_ON_ENCRYPTED_TRANSFER,
    @JsonProperty("NonIncreasingSchedule")
    NON_INCREASING_SCHEDULE,
    @JsonProperty("ScheduledSelfTransfer")
    SCHEDULED_SELF_TRANSFER,
    @JsonProperty("NonExistentCredIDs")
    NON_EXISTENT_CRED_IDS,
    @JsonProperty("RemoveFirstCredential")
    REMOVE_FIRST_CREDENTIAL,
    @JsonProperty("CredentialHolderDidNotSign")
    CREDENTIAL_HOLDER_DID_NOT_SIGN;

    // Convenience methods for do 'safe' casting.
    public <T> T convert(RejectReason reason) {
        if (this != reason.getType()) {
            throw new IllegalArgumentException("Unexpected conversion. Expected " + this + " but received " + reason.getType());
        }
        return (T) reason;
    }
}
