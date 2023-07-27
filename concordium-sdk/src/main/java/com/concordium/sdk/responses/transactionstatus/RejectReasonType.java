package com.concordium.sdk.responses.transactionstatus;


import com.concordium.grpc.v2.AccountTransactionEffects;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    CREDENTIAL_HOLDER_DID_NOT_SIGN,
    @JsonProperty("MissingBakerAddParameters")
    MISSING_BAKER_ADD_PARAMETERS,
    @JsonProperty("FinalizationRewardCommissionNotInRange")
    FINALIZATION_REWARD_COMMISSION_NOT_IN_RANGE,
    @JsonProperty("BakingRewardCommissionNotInRange")
    BAKING_REWARD_COMMISSION_NOT_IN_RANGE,
    @JsonProperty("TransactionFeeCommissionNotInRange")
    TRANSACTION_FEE_COMMISSION_NOT_IN_RANGE,
    @JsonProperty("AlreadyADelegator")
    ALREADY_A_DELEGATOR,
    @JsonProperty("InsufficientBalanceForDelegationStake")
    INSUFFICIENT_BALANCE_FOR_DELEGATION_STAKE,
    @JsonProperty("MissingDelegationAddParameters")
    MISSING_DELEGATION_ADD_PARAMETERS,
    @JsonProperty("InsufficientDelegationStake")
    INSUFFICIENT_DELEGATION_STAKE,
    @JsonProperty("DelegatorInCooldown")
    DELEGATOR_IN_COOLDOWN,
    @JsonProperty("NotADelegator")
    NOT_A_DELEGATOR,
    @JsonProperty("DelegationTargetNotABaker")
    DELEGATION_TARGET_NOT_A_BAKER,
    @JsonProperty("StakeOverMaximumThresholdForPool")
    STAKE_OVER_MAXIMUM_THRESHOLD_FOR_POOL,
    @JsonProperty("PoolWouldBecomeOverDelegated")
    POOL_WOULD_BECOME_OVER_DELEGATED,
    @JsonProperty("PoolClosed")
    POOL_CLOSED;

    public static RejectReasonType from(com.concordium.grpc.v2.RejectReason reason) {
        switch (reason.getReasonCase()) {
            case MODULE_NOT_WF:
                return MODULE_NOT_WF;
            case MODULE_HASH_ALREADY_EXISTS:
                return MODULE_HASH_ALREADY_EXISTS;
            case INVALID_ACCOUNT_REFERENCE:
                return INVALID_ACCOUNT_REFERENCE;
            case INVALID_INIT_METHOD:
                return INVALID_INIT_METHOD;
            case INVALID_RECEIVE_METHOD:
                return INVALID_RECEIVE_METHOD;
            case INVALID_MODULE_REFERENCE:
                return INVALID_MODULE_REFERENCE;
            case INVALID_CONTRACT_ADDRESS:
                return INVALID_CONTRACT_ADDRESS;
            case RUNTIME_FAILURE:
                return RUNTIME_FAILURE;
            case AMOUNT_TOO_LARGE:
                return AMOUNT_TOO_LARGE;
            case SERIALIZATION_FAILURE:
                return SERIALIZATION_FAILURE;
            case OUT_OF_ENERGY:
                return OUT_OF_ENERGY;
            case REJECTED_INIT:
                return REJECTED_INIT;
            case REJECTED_RECEIVE:
                return REJECTED_RECEIVE;
            case INVALID_PROOF:
                return INVALID_PROOF;
            case ALREADY_A_BAKER:
                return ALREADY_A_BAKER;
            case NOT_A_BAKER:
                return NOT_A_BAKER;
            case INSUFFICIENT_BALANCE_FOR_BAKER_STAKE:
                return INSUFFICIENT_BALANCE_FOR_BAKER_STAKE;
            case STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING:
                return STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING;
            case BAKER_IN_COOLDOWN:
                return BAKER_IN_COOLDOWN;
            case DUPLICATE_AGGREGATION_KEY:
                return DUPLICATE_AGGREGATION_KEY;
            case NON_EXISTENT_CREDENTIAL_ID:
                return NON_EXISTENT_CREDENTIAL_ID;
            case KEY_INDEX_ALREADY_IN_USE:
                return KEY_INDEX_ALREADY_IN_USE;
            case INVALID_ACCOUNT_THRESHOLD:
                return INVALID_ACCOUNT_THRESHOLD;
            case INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD:
                return INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD;
            case INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF:
                return INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF;
            case INVALID_TRANSFER_TO_PUBLIC_PROOF:
                return INVALID_TRANSFER_TO_PUBLIC_PROOF;
            case ENCRYPTED_AMOUNT_SELF_TRANSFER:
                return ENCRYPTED_AMOUNT_SELF_TRANSFER;
            case INVALID_INDEX_ON_ENCRYPTED_TRANSFER:
                return INVALID_INDEX_ON_ENCRYPTED_TRANSFER;
            case ZERO_SCHEDULEDAMOUNT:
                return ZERO_SCHEDULED_AMOUNT;
            case NON_INCREASING_SCHEDULE:
                return NON_INCREASING_SCHEDULE;
            case FIRST_SCHEDULED_RELEASE_EXPIRED:
                return FIRST_SCHEDULED_RELEASE_EXPIRED;
            case SCHEDULED_SELF_TRANSFER:
                return SCHEDULED_SELF_TRANSFER;
            case INVALID_CREDENTIALS:
                return INVALID_CREDENTIALS;
            case DUPLICATE_CRED_IDS:
                return DUPLICATE_CRED_IDS;
            case NON_EXISTENT_CRED_IDS:
                return NON_EXISTENT_CRED_IDS;
            case REMOVE_FIRST_CREDENTIAL:
                return REMOVE_FIRST_CREDENTIAL;
            case CREDENTIAL_HOLDER_DID_NOT_SIGN:
                return CREDENTIAL_HOLDER_DID_NOT_SIGN;
            case NOT_ALLOWED_MULTIPLE_CREDENTIALS:
                return NOT_ALLOWED_MULTIPLE_CREDENTIALS;
            case NOT_ALLOWED_TO_RECEIVE_ENCRYPTED:
                return NOT_ALLOWED_TO_RECEIVE_ENCRYPTED;
            case NOT_ALLOWED_TO_HANDLE_ENCRYPTED:
                return NOT_ALLOWED_TO_HANDLE_ENCRYPTED;
            case MISSING_BAKER_ADD_PARAMETERS:
                return MISSING_BAKER_ADD_PARAMETERS;
            case FINALIZATION_REWARD_COMMISSION_NOT_IN_RANGE:
                return FINALIZATION_REWARD_COMMISSION_NOT_IN_RANGE;
            case BAKING_REWARD_COMMISSION_NOT_IN_RANGE:
                return BAKING_REWARD_COMMISSION_NOT_IN_RANGE;
            case TRANSACTION_FEE_COMMISSION_NOT_IN_RANGE:
                return TRANSACTION_FEE_COMMISSION_NOT_IN_RANGE;
            case ALREADY_A_DELEGATOR:
                return ALREADY_A_DELEGATOR;
            case INSUFFICIENT_BALANCE_FOR_DELEGATION_STAKE:
                return INSUFFICIENT_BALANCE_FOR_DELEGATION_STAKE;
            case MISSING_DELEGATION_ADD_PARAMETERS:
                return MISSING_DELEGATION_ADD_PARAMETERS;
            case INSUFFICIENT_DELEGATION_STAKE:
                return INSUFFICIENT_DELEGATION_STAKE;
            case DELEGATOR_IN_COOLDOWN:
                return DELEGATOR_IN_COOLDOWN;
            case NOT_A_DELEGATOR:
                return NOT_A_DELEGATOR;
            case DELEGATION_TARGET_NOT_A_BAKER:
                return DELEGATION_TARGET_NOT_A_BAKER;
            case STAKE_OVER_MAXIMUM_THRESHOLD_FOR_POOL:
                return STAKE_OVER_MAXIMUM_THRESHOLD_FOR_POOL;
            case POOL_WOULD_BECOME_OVER_DELEGATED:
                return POOL_WOULD_BECOME_OVER_DELEGATED;
            case POOL_CLOSED:
                return POOL_CLOSED;
            case REASON_NOT_SET:
                throw new IllegalArgumentException("No reject reason present.");
        }
        throw new IllegalArgumentException("Unrecognized reject reason.");
    }

    // Convenience methods for do 'safe' casting.
    public <T> T convert(RejectReason reason) {
        if (this != reason.getType()) {
            throw new IllegalArgumentException("Unexpected conversion. Expected " + this + " but received " + reason.getType() +".");
        }
        return (T) reason;
    }
}
