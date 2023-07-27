package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.AccountTransactionEffects;

/**
 * The transaction failed
 */
public class FailedOutcome {
    public static FailedOutcome from(AccountTransactionEffects.None reason) {
        switch (reason.getRejectReason().getReasonCase()){
            case MODULE_NOT_WF:
                break;
            case MODULE_HASH_ALREADY_EXISTS:
                break;
            case INVALID_ACCOUNT_REFERENCE:
                break;
            case INVALID_INIT_METHOD:
                break;
            case INVALID_RECEIVE_METHOD:
                break;
            case INVALID_MODULE_REFERENCE:
                break;
            case INVALID_CONTRACT_ADDRESS:
                break;
            case RUNTIME_FAILURE:
                break;
            case AMOUNT_TOO_LARGE:
                break;
            case SERIALIZATION_FAILURE:
                break;
            case OUT_OF_ENERGY:
                break;
            case REJECTED_INIT:
                break;
            case REJECTED_RECEIVE:
                break;
            case INVALID_PROOF:
                break;
            case ALREADY_A_BAKER:
                break;
            case NOT_A_BAKER:
                break;
            case INSUFFICIENT_BALANCE_FOR_BAKER_STAKE:
                break;
            case STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING:
                break;
            case BAKER_IN_COOLDOWN:
                break;
            case DUPLICATE_AGGREGATION_KEY:
                break;
            case NON_EXISTENT_CREDENTIAL_ID:
                break;
            case KEY_INDEX_ALREADY_IN_USE:
                break;
            case INVALID_ACCOUNT_THRESHOLD:
                break;
            case INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD:
                break;
            case INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF:
                break;
            case INVALID_TRANSFER_TO_PUBLIC_PROOF:
                break;
            case ENCRYPTED_AMOUNT_SELF_TRANSFER:
                break;
            case INVALID_INDEX_ON_ENCRYPTED_TRANSFER:
                break;
            case ZERO_SCHEDULEDAMOUNT:
                break;
            case NON_INCREASING_SCHEDULE:
                break;
            case FIRST_SCHEDULED_RELEASE_EXPIRED:
                break;
            case SCHEDULED_SELF_TRANSFER:
                break;
            case INVALID_CREDENTIALS:
                break;
            case DUPLICATE_CRED_IDS:
                break;
            case NON_EXISTENT_CRED_IDS:
                break;
            case REMOVE_FIRST_CREDENTIAL:
                break;
            case CREDENTIAL_HOLDER_DID_NOT_SIGN:
                break;
            case NOT_ALLOWED_MULTIPLE_CREDENTIALS:
                break;
            case NOT_ALLOWED_TO_RECEIVE_ENCRYPTED:
                break;
            case NOT_ALLOWED_TO_HANDLE_ENCRYPTED:
                break;
            case MISSING_BAKER_ADD_PARAMETERS:
                break;
            case FINALIZATION_REWARD_COMMISSION_NOT_IN_RANGE:
                break;
            case BAKING_REWARD_COMMISSION_NOT_IN_RANGE:
                break;
            case TRANSACTION_FEE_COMMISSION_NOT_IN_RANGE:
                break;
            case ALREADY_A_DELEGATOR:
                break;
            case INSUFFICIENT_BALANCE_FOR_DELEGATION_STAKE:
                break;
            case MISSING_DELEGATION_ADD_PARAMETERS:
                break;
            case INSUFFICIENT_DELEGATION_STAKE:
                break;
            case DELEGATOR_IN_COOLDOWN:
                break;
            case NOT_A_DELEGATOR:
                break;
            case DELEGATION_TARGET_NOT_A_BAKER:
                break;
            case STAKE_OVER_MAXIMUM_THRESHOLD_FOR_POOL:
                break;
            case POOL_WOULD_BECOME_OVER_DELEGATED:
                break;
            case POOL_CLOSED:
                break;
            case REASON_NOT_SET:
                break;
        }
        return null;
    }
}
