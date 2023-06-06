package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@Builder
public final class TransactionResult {

    // Emitted events from transactions
    // https://github.com/Concordium/concordium-base/blob/8dcee8746e40d663222aa3b4b04eaa3710e2779e/haskell-src/Concordium/Types/Execution.hs#L736
    // Please keep them in same order.
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tag")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ModuleDeployedResult.class, name = "ModuleDeployed"),
            @JsonSubTypes.Type(value = ContractInitializedResult.class, name = "ContractInitialized"),
            @JsonSubTypes.Type(value = ContractUpdated.class, name = "Updated"),
            @JsonSubTypes.Type(value = TransferredResult.class, name = "Transferred"),
            @JsonSubTypes.Type(value = AccountCreatedResult.class, name = "AccountCreated"),
            @JsonSubTypes.Type(value = CredentialDeployedResult.class, name = "CredentialDeployed"),
            @JsonSubTypes.Type(value = BakerAddedResult.class, name = "BakerAdded"),
            @JsonSubTypes.Type(value = BakerRemovedResult.class, name = "BakerRemoved"),
            @JsonSubTypes.Type(value = BakerStakeIncreasedResult.class, name = "BakerStakeIncreased"),
            @JsonSubTypes.Type(value = BakerStakeDecreasedResult.class, name = "BakerStakeDecreased"),
            @JsonSubTypes.Type(value = BakerSetRestakeEarningsResult.class, name = "BakerSetRestakeEarnings"),
            @JsonSubTypes.Type(value = BakerKeysUpdatedResult.class, name = "BakerKeysUpdated"),
            @JsonSubTypes.Type(value = CredentialKeysUpdatedResult.class, name = "CredentialKeysUpdated"),
            @JsonSubTypes.Type(value = NewEncryptedAmountResult.class, name = "NewEncryptedAmount"),
            @JsonSubTypes.Type(value = EncryptedAmountsRemovedResult.class, name = "EncryptedAmountsRemoved"),
            @JsonSubTypes.Type(value = AmountAddedByDecryptionResult.class, name = "AmountAddedByDecryption"),
            @JsonSubTypes.Type(value = EncryptedSelfAmountAddedResult.class, name = "EncryptedSelfAmountAdded"),
            @JsonSubTypes.Type(value = UpdateEnqueuedResult.class, name = "UpdateEnqueued"),
            @JsonSubTypes.Type(value = TransferredWithScheduleResult.class, name = "TransferredWithSchedule"),
            @JsonSubTypes.Type(value = CredentialsUpdatedResult.class, name = "CredentialsUpdated"),
            @JsonSubTypes.Type(value = DataRegisteredResult.class, name = "DataRegistered"),
            @JsonSubTypes.Type(value = TransferMemoResult.class, name = "TransferMemo"),
            @JsonSubTypes.Type(value = InterruptedResult.class, name = "Interrupted"),
            @JsonSubTypes.Type(value = ResumedResult.class, name = "Resumed"),
            @JsonSubTypes.Type(value = BakerSetOpenStatus.class, name = "BakerSetOpenStatus"),
            @JsonSubTypes.Type(value = BakerSetMetadataURL.class, name = "BakerSetMetadataURL"),
            @JsonSubTypes.Type(value = BakerSetTransactionFeeCommission.class, name = "BakerSetTransactionFeeCommission"),
            @JsonSubTypes.Type(value = BakerSetBakingRewardCommission.class, name = "BakerSetBakingRewardCommission"),
            @JsonSubTypes.Type(value = BakerSetFinalizationRewardCommission.class, name = "BakerSetFinalizationRewardCommission"),
            @JsonSubTypes.Type(value = DelegationStakeIncreased.class, name = "DelegationStakeIncreased"),
            @JsonSubTypes.Type(value = DelegationStakeDecreased.class, name = "DelegationStakeDecreased"),
            @JsonSubTypes.Type(value = DelegationSetRestakeEarnings.class, name = "DelegationSetRestakeEarnings"),
            @JsonSubTypes.Type(value = DelegationSetDelegationTarget.class, name = "DelegationSetDelegationTarget"),
            @JsonSubTypes.Type(value = DelegationAdded.class, name = "DelegationAdded"),
            @JsonSubTypes.Type(value = DelegationRemoved.class, name = "DelegationRemoved"),
            @JsonSubTypes.Type(value = UpgradedResult.class, name = "Upgraded")
    })

    /**
     * Effects of the transaction.
     * Note, does not contain any elements if outcome is {@link Outcome#REJECT}.
     */
    private final List<TransactionResultEvent> events;
    private final Outcome outcome;

    // Reject reasons https://github.com/Concordium/concordium-base/blob/add1c9884769d2cc84869c8718f215337bdcfafd/haskell-src/Concordium/Types/Execution.hs#L1869
    // Please keep them in the same order.
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tag")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = RejectReasonModuleNotWF.class, name = "ModuleNotWF"),
            @JsonSubTypes.Type(value = RejectReasonModuleHashAlreadyExists.class, name = "ModuleHashAlreadyExists"),
            @JsonSubTypes.Type(value = RejectReasonInvalidAccountReference.class, name = "InvalidAccountReference"),
            @JsonSubTypes.Type(value = RejectReasonInvalidInitMethod.class, name = "InvalidInitMethod"),
            @JsonSubTypes.Type(value = RejectReasonInvalidReceiveMethod.class, name = "InvalidReceiveMethod"),
            @JsonSubTypes.Type(value = RejectReasonInvalidModuleReference.class, name = "InvalidModuleReference"),
            @JsonSubTypes.Type(value = RejectReasonInvalidContractAddress.class, name = "InvalidContractAddress"),
            @JsonSubTypes.Type(value = RejectReasonRuntimeFailure.class, name = "RuntimeFailure"),
            @JsonSubTypes.Type(value = RejectReasonAmountTooLarge.class, name = "AmountTooLarge"),
            @JsonSubTypes.Type(value = RejectReasonSerializationFailure.class, name = "SerializationFailure"),
            @JsonSubTypes.Type(value = RejectReasonOutOfEnergy.class, name = "OutOfEnergy"),
            @JsonSubTypes.Type(value = RejectReasonRejectedInit.class, name = "RejectedInit"),
            @JsonSubTypes.Type(value = RejectReasonRejectedReceive.class, name = "RejectedReceive"),
            @JsonSubTypes.Type(value = RejectReasonNonExistentRewardAccount.class, name = "NonExistentRewardAccount"),
            @JsonSubTypes.Type(value = RejectReasonInvalidProof.class, name = "InvalidProof"),
            @JsonSubTypes.Type(value = RejectReasonAlreadyABaker.class, name = "AlreadyABaker"),
            @JsonSubTypes.Type(value = RejectReasonNotABaker.class, name = "NotABaker"),
            @JsonSubTypes.Type(value = RejectReasonInsufficientBalanceForBakerStake.class, name = "InsufficientBalanceForBakerStake"),
            @JsonSubTypes.Type(value = RejectReasonStakeUnderMinimumThresholdForBaking.class, name = "StakeUnderMinimumThresholdForBaking"),
            @JsonSubTypes.Type(value = RejectReasonBakerInCooldown.class, name = "BakerInCooldown"),
            @JsonSubTypes.Type(value = RejectReasonDuplicateAggregationKey.class, name = "DuplicateAggregationKey"),
            @JsonSubTypes.Type(value = RejectReasonNonExistentCredentialID.class, name = "NonExistentCredentialID"),
            @JsonSubTypes.Type(value = RejectReasonKeyIndexAlreadyInUse.class, name = "KeyIndexAlreadyInUse"),
            @JsonSubTypes.Type(value = RejectReasonInvalidAccountThreshold.class, name = "InvalidAccountThreshold"),
            @JsonSubTypes.Type(value = RejectReasonInvalidCredentialKeySignThreshold.class, name = "InvalidCredentialKeySignThreshold"),
            @JsonSubTypes.Type(value = RejectReasonInvalidEncryptedAmountTransferProof.class, name = "InvalidEncryptedAmountTransferProof"),
            @JsonSubTypes.Type(value = RejectReasonInvalidTransferToPublicProof.class, name = "InvalidTransferToPublicProof"),
            @JsonSubTypes.Type(value = RejectReasonEncryptedAmountSelfTransfer.class, name = "EncryptedAmountSelfTransfer"),
            @JsonSubTypes.Type(value = RejectReasonInvalidIndexOnEncryptedTransfer.class, name = "InvalidIndexOnEncryptedTransfer"),
            @JsonSubTypes.Type(value = RejectReasonZeroScheduledAmount.class, name = "ZeroScheduledAmount"),
            @JsonSubTypes.Type(value = RejectReasonNonIncreasingSchedule.class, name = "NonIncreasingSchedule"),
            @JsonSubTypes.Type(value = RejectReasonFirstScheduledReleaseExpired.class, name = "FirstScheduledReleaseExpired"),
            @JsonSubTypes.Type(value = RejectReasonScheduledSelfTransfer.class, name = "ScheduledSelfTransfer"),
            @JsonSubTypes.Type(value = RejectReasonInvalidCredentials.class, name = "InvalidCredentials"),
            @JsonSubTypes.Type(value = RejectReasonDuplicateCredIDs.class, name = "DuplicateCredIDs"),
            @JsonSubTypes.Type(value = RejectReasonNonExistentCredIDs.class, name = "NonExistentCredIDs"),
            @JsonSubTypes.Type(value = RejectReasonRemoveFirstCredential.class, name = "RemoveFirstCredential"),
            @JsonSubTypes.Type(value = RejectReasonCredentialHolderDidNotSign.class, name = "CredentialHolderDidNotSign"),
            @JsonSubTypes.Type(value = RejectReasonNotAllowedMultipleCredentials.class, name = "NotAllowedMultipleCredentials"),
            @JsonSubTypes.Type(value = RejectReasonNotAllowedToReceiveEncrypted.class, name = "NotAllowedToReceiveEncrypted"),
            @JsonSubTypes.Type(value = RejectReasonNotAllowedToHandleEncrypted.class, name = "NotAllowedToHandleEncrypted"),
            @JsonSubTypes.Type(value = RejectReasonMissingBakerAddParameters.class, name = "MissingBakerAddParameters"),
            @JsonSubTypes.Type(value = RejectReasonFinalizationRewardCommissionNotInRange.class, name = "FinalizationRewardCommissionNotInRange"),
            @JsonSubTypes.Type(value = RejectReasonBakingRewardCommissionNotInRange.class, name = "BakingRewardCommissionNotInRange"),
            @JsonSubTypes.Type(value = RejectReasonTransactionFeeCommissionNotInRange.class, name = "TransactionFeeCommissionNotInRange"),
            @JsonSubTypes.Type(value = RejectReasonAlreadyADelegator.class, name = "AlreadyADelegator"),
            @JsonSubTypes.Type(value = RejectReasonInsufficientBalanceForDelegationStake.class, name = "InsufficientBalanceForDelegationStake"),
            @JsonSubTypes.Type(value = RejectReasonMissingDelegationAddParameters.class, name = "MissingDelegationAddParameters"),
            @JsonSubTypes.Type(value = RejectReasonInsufficientDelegationStake.class, name = "InsufficientDelegationStake"),
            @JsonSubTypes.Type(value = RejectReasonDelegatorInCooldown.class, name = "DelegatorInCooldown"),
            @JsonSubTypes.Type(value = RejectReasonNotADelegator.class, name = "NotADelegator"),
            @JsonSubTypes.Type(value = RejectReasonDelegationTargetNotABaker.class, name = "DelegationTargetNotABaker"),
            @JsonSubTypes.Type(value = RejectReasonStakeOverMaximumThresholdForPool.class, name = "StakeOverMaximumThresholdForPool"),
            @JsonSubTypes.Type(value = RejectReasonPoolWouldBecomeOverDelegated.class, name = "PoolWouldBecomeOverDelegated"),
            @JsonSubTypes.Type(value = RejectReasonPoolClosed.class, name = "PoolClosed"),
    })

    /**
     * Reason for rejection of the transaction.
     * Note, this is populated if and only if outcome is {@link Outcome#REJECT}.
     */
    private final RejectReason rejectReason;
}
