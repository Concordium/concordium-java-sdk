package com.concordium.sdk.responses.transactionstatus;


// These types must correspond the 'RejectReason' types found here
// https://github.com/Concordium/concordium-base/blob/f0e1275dfde8502e3aabdb153b3246c18bee59f9/haskell-src/Concordium/Types/Execution.hs#L737
public enum RejectReasonType {
    AMOUNT_TOO_LARGE,
    DUPLICATE_AGGREGATION_KEY,
    BAKER_IN_COOLDOWN,
    SERIALIZATION_FAILURE,
    INVALID_ACCOUNT_REFERENCE,
    INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF,
    STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING,
    MODULE_NOT_WF,
    REJECTED_RECEIVE,
    INSUFFICIENT_BALANCE_FOR_BAKER_STAKE,
    MODULE_HASH_ALREADY_EXISTS,
    OUT_OF_ENERGY,
    INVALID_RECEIVE_METHOD,
    NOT_ALLOWED_TO_RECEIVE_ENCRYPTED,
    INVALID_INIT_METHOD,
    REJECTED_INIT,
    INVALID_CREDENTIALS,
    INVALID_TRANSFER_TO_PUBLIC_PROOF,
    ZERO_SCHEDULED_AMOUNT,
    NOT_ALLOWED_MULTIPLE_CREDENTIALS,
    NOT_ALLOWED_TO_HANDLE_ENCRYPTED,
    DUPLICATE_CRED_IDS,
    FIRST_SCHEDULED_RELEASE_EXPIRED,
    ENCRYPTED_AMOUNT_SELF_TRANSFER,
    INVALID_MODULE_REFERENCE,
    INVALID_CONTRACT_ADDRESS,
    RUNTIME_FAILURE,
    NON_EXISTENT_REWARD_ACCOUNT,
    INVALID_PROOF,
    ALREADY_A_BAKER,
    NOT_A_BAKER,
    NON_EXISTENT_CREDENTIAL_ID,
    KEY_INDEX_ALREADY_IN_USE,
    INVALID_ACCOUNT_THRESHOLD,
    INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD,
    INVALID_INDEX_ON_ENCRYPTED_TRANSFER,
    NON_INCREASING_SCHEDULE,
    SCHEDULED_SELF_TRANSFER,
    NON_EXISTENT_CRED_IDS,
    REMOVE_FIRST_CREDENTIAL,
    CREDENTIAL_HOLDER_DID_NOT_SIGN,
    MISSING_BAKER_ADD_PARAMETERS,
    FINALIZATION_REWARD_COMMISSION_NOT_IN_RANGE,
    BAKING_REWARD_COMMISSION_NOT_IN_RANGE,
    TRANSACTION_FEE_COMMISSION_NOT_IN_RANGE,
    ALREADY_A_DELEGATOR,
    INSUFFICIENT_BALANCE_FOR_DELEGATION_STAKE,
    MISSING_DELEGATION_ADD_PARAMETERS,
    INSUFFICIENT_DELEGATION_STAKE,
    DELEGATOR_IN_COOLDOWN,
    NOT_A_DELEGATOR,
    DELEGATION_TARGET_NOT_A_BAKER,
    STAKE_OVER_MAXIMUM_THRESHOLD_FOR_POOL,
    POOL_WOULD_BECOME_OVER_DELEGATED,
    POOL_CLOSED,
    NOT_EXISTENT_TOKEN_ID,
    TOKEN_UPDATE_TRANSACTION_FAILED,
    ;

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
            case NON_EXISTENT_TOKEN_ID:
                return NOT_EXISTENT_TOKEN_ID;
            case TOKEN_UPDATE_TRANSACTION_FAILED:
                return TOKEN_UPDATE_TRANSACTION_FAILED;
            case REASON_NOT_SET:
                throw new IllegalArgumentException("No reject reason present.");
        }
        throw new IllegalArgumentException("Unrecognized reject reason.");
    }

    // Convenience methods for do 'safe' casting.
    public <T> T convert(RejectReason reason) {
        if (this != reason.getType()) {
            throw new IllegalArgumentException("Unexpected conversion. Expected " + this + " but received " + reason.getType() + ".");
        }
        return (T) reason;
    }
}
