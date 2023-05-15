package com.concordium.sdk.responses.transactionstatus;

import lombok.Data;

// Used for tagging the underlying RejectReasonContent
@Data
public abstract class RejectReason {

    public abstract RejectReasonType getType();

    /**
     * Parses {@link com.concordium.grpc.v2.RejectReason} to {@link RejectReason}.
     * @param rejectReason {@link com.concordium.grpc.v2.RejectReason} returned by the GRPC V2 API.
     * @return parsed {@link RejectReason}.
     */
    public static RejectReason parse(com.concordium.grpc.v2.RejectReason rejectReason) {
        switch (rejectReason.getReasonCase()) {
            case MODULE_NOT_WF: return new RejectReasonModuleNotWF();
            case MODULE_HASH_ALREADY_EXISTS: return RejectReasonModuleHashAlreadyExists.parse(rejectReason.getModuleHashAlreadyExists());
            case INVALID_ACCOUNT_REFERENCE: return RejectReasonInvalidAccountReference.parse(rejectReason.getInvalidAccountReference());
            case INVALID_INIT_METHOD: return RejectReasonInvalidInitMethod.parse(rejectReason.getInvalidInitMethod());
            case INVALID_RECEIVE_METHOD: return RejectReasonInvalidReceiveMethod.parse(rejectReason.getInvalidReceiveMethod());
            case INVALID_MODULE_REFERENCE: return RejectReasonInvalidModuleReference.parse(rejectReason.getInvalidModuleReference());
            case INVALID_CONTRACT_ADDRESS: return RejectReasonInvalidContractAddress.parse(rejectReason.getInvalidContractAddress());
            case RUNTIME_FAILURE: return new RejectReasonRuntimeFailure();
            case AMOUNT_TOO_LARGE: return RejectReasonAmountTooLarge.parse(rejectReason.getAmountTooLarge());
            case SERIALIZATION_FAILURE: return new RejectReasonSerializationFailure();
            case OUT_OF_ENERGY: return new RejectReasonOutOfEnergy();
            case REJECTED_INIT: return RejectReasonRejectedInit.parse(rejectReason.getRejectedInit());
            case REJECTED_RECEIVE: return RejectReasonRejectedReceive.parse(rejectReason.getRejectedReceive());
            case INVALID_PROOF: return new RejectReasonInvalidProof();
            case ALREADY_A_BAKER: return RejectReasonAlreadyABaker.parse(rejectReason.getAlreadyABaker());
            case NOT_A_BAKER: return RejectReasonNotABaker.parse(rejectReason.getNotABaker());
            case INSUFFICIENT_BALANCE_FOR_BAKER_STAKE: return new RejectReasonInsufficientBalanceForBakerStake();
            case STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING: return new RejectReasonStakeUnderMinimumThresholdForBaking();
            case BAKER_IN_COOLDOWN: return new RejectReasonBakerInCooldown();
            case DUPLICATE_AGGREGATION_KEY: return RejectReasonDuplicateAggregationKey.parse(rejectReason.getDuplicateAggregationKey()); // TODO
            case NON_EXISTENT_CREDENTIAL_ID: return new RejectReasonNonExistentCredentialID();
            case KEY_INDEX_ALREADY_IN_USE: return new RejectReasonKeyIndexAlreadyInUse();
            case INVALID_ACCOUNT_THRESHOLD: return new RejectReasonInvalidAccountThreshold();
            case INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD: return new RejectReasonInvalidCredentialKeySignThreshold();
            case INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF: return new RejectReasonInvalidEncryptedAmountTransferProof();
            case INVALID_TRANSFER_TO_PUBLIC_PROOF: return new RejectReasonInvalidTransferToPublicProof();
            case ENCRYPTED_AMOUNT_SELF_TRANSFER: return RejectReasonEncryptedAmountSelfTransfer.parse(rejectReason.getEncryptedAmountSelfTransfer());
            case INVALID_INDEX_ON_ENCRYPTED_TRANSFER: return new RejectReasonInvalidIndexOnEncryptedTransfer();
            case ZERO_SCHEDULEDAMOUNT: return new RejectReasonZeroScheduledAmount();
            case NON_INCREASING_SCHEDULE: return new RejectReasonNonIncreasingSchedule();
            case FIRST_SCHEDULED_RELEASE_EXPIRED: return new RejectReasonFirstScheduledReleaseExpired();
            case SCHEDULED_SELF_TRANSFER: return RejectReasonScheduledSelfTransfer.parse(rejectReason.getScheduledSelfTransfer());
            case INVALID_CREDENTIALS: return new RejectReasonInvalidCredentials();
            case DUPLICATE_CRED_IDS: return RejectReasonDuplicateCredIDs.parse(rejectReason.getDuplicateCredIds());
            case NON_EXISTENT_CRED_IDS: return RejectReasonNonExistentCredIDs.parse(rejectReason.getNonExistentCredIds());
            case REMOVE_FIRST_CREDENTIAL: return new RejectReasonRemoveFirstCredential();
            case CREDENTIAL_HOLDER_DID_NOT_SIGN: return new RejectReasonCredentialHolderDidNotSign();
            case NOT_ALLOWED_MULTIPLE_CREDENTIALS: return new RejectReasonNotAllowedMultipleCredentials();
            case NOT_ALLOWED_TO_RECEIVE_ENCRYPTED: return new RejectReasonNotAllowedToReceiveEncrypted();
            case NOT_ALLOWED_TO_HANDLE_ENCRYPTED: return new RejectReasonNotAllowedToHandleEncrypted();
            case MISSING_BAKER_ADD_PARAMETERS: return new RejectReasonMissingBakerAddParameters();
            case FINALIZATION_REWARD_COMMISSION_NOT_IN_RANGE: return new RejectReasonFinalizationRewardCommissionNotInRange();
            case BAKING_REWARD_COMMISSION_NOT_IN_RANGE: return new RejectReasonBakingRewardCommissionNotInRange();
            case TRANSACTION_FEE_COMMISSION_NOT_IN_RANGE: return new RejectReasonTransactionFeeCommissionNotInRange();
            case ALREADY_A_DELEGATOR: return new RejectReasonAlreadyADelegator();
            case INSUFFICIENT_BALANCE_FOR_DELEGATION_STAKE: return new RejectReasonInsufficientBalanceForDelegationStake();
            case MISSING_DELEGATION_ADD_PARAMETERS: return new RejectReasonMissingDelegationAddParameters();
            case INSUFFICIENT_DELEGATION_STAKE: return new RejectReasonInsufficientDelegationStake();
            case DELEGATOR_IN_COOLDOWN: return new RejectReasonDelegatorInCooldown();
            case NOT_A_DELEGATOR: return RejectReasonNotADelegator.parse(rejectReason.getNotADelegator());
            case DELEGATION_TARGET_NOT_A_BAKER: return RejectReasonDelegationTargetNotABaker.parse(rejectReason.getDelegationTargetNotABaker());
            case STAKE_OVER_MAXIMUM_THRESHOLD_FOR_POOL: return new RejectReasonStakeOverMaximumThresholdForPool();
            case POOL_WOULD_BECOME_OVER_DELEGATED: return new RejectReasonPoolWouldBecomeOverDelegated();
            case POOL_CLOSED: return new RejectReasonPoolClosed();
            default: throw new IllegalArgumentException();
        }
    }
}
