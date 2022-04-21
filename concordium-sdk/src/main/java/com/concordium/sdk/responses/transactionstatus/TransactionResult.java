package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class TransactionResult {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tag")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = CredentialDeployedResult.class, name = "CredentialDeployed"),
            @JsonSubTypes.Type(value = ModuleCreatedResult.class, name = "ModuleDeployed"),
            @JsonSubTypes.Type(value = ContractInitializedResult.class, name = "ContractInitialized"),
            @JsonSubTypes.Type(value = AccountCreatedResult.class, name = "AccountCreated"),
            @JsonSubTypes.Type(value = BakerAddedResult.class, name = "BakerAdded"),
            @JsonSubTypes.Type(value = BakerRemovedResult.class, name = "BakerRemoved"),
            @JsonSubTypes.Type(value = BakerStakeIncreasedResult.class, name = "BakerStakeIncreased"),
            @JsonSubTypes.Type(value = BakerStakeDecreasedResult.class, name = "BakerStakeDecreased"),
            @JsonSubTypes.Type(value = BakerSetRestakeEarningsResult.class, name = "BakerSetRestakeEarnings"),
            @JsonSubTypes.Type(value = BakerKeysUpdatedResult.class, name = "BakerKeysUpdated"),
            @JsonSubTypes.Type(value = CredentialKeysUpdatedResult.class, name = "CredentialKeysUpdated"),
            @JsonSubTypes.Type(value = NewEncryptedAmountResult.class, name = "NewEncryptedAmount"),
            @JsonSubTypes.Type(value = AmountAddedByDecryptionResult.class, name = "AmountAddedByDecryption"),
            @JsonSubTypes.Type(value = EncryptedSelfAmountAddedResult.class, name = "EncryptedSelfAmountAdded"),
            @JsonSubTypes.Type(value = UpdateEnqueuedResult.class, name = "UpdateEnqueued"),
            @JsonSubTypes.Type(value = TransferredWithScheduleResult.class, name = "TransferredWithSchedule"),
            @JsonSubTypes.Type(value = CredentialsUpdatedResult.class, name = "CredentialsUpdated"),
            @JsonSubTypes.Type(value = DataRegisteredResult.class, name = "DataRegistered"),
            @JsonSubTypes.Type(value = TransferredResult.class, name = "Transferred"),
            @JsonSubTypes.Type(value = TransferMemoResult.class, name = "TransferMemo"),
            @JsonSubTypes.Type(value = EncryptedAmountsRemovedResult.class, name = "EncryptedAmountsRemoved"),
            @JsonSubTypes.Type(value = ContractUpdated.class, name = "Updated"),
            @JsonSubTypes.Type(value = BakerSetOpenStatus.class, name = "BakerSetOpenStatus"),
            @JsonSubTypes.Type(value = BakerSetMetadataURL.class, name = "BakerSetMetadataURL"),
            @JsonSubTypes.Type(value = BakerSetTransactionFeeCommission.class, name = "BakerSetTransactionFeeCommission"),
            @JsonSubTypes.Type(value = BakerSetBakingRewardCommission.class, name = "BakerSetBakingRewardCommission"),
            @JsonSubTypes.Type(value = BakerSetFinalizationRewardCommission.class, name ="BakerSetFinalizationRewardCommission"),
            @JsonSubTypes.Type(value = DelegationStakeIncreased.class, name = "DelegationStakeIncreased"),
            @JsonSubTypes.Type(value = DelegationStakeDecreased.class, name = "DelegationStakeDecreased"),
            @JsonSubTypes.Type(value = DelegationSetRestakeEarnings.class, name = "DelegationSetRestakeEarnings"),
            @JsonSubTypes.Type(value = DelegationSetDelegationTarget.class, name = "DelegationSetDelegationTarget"),
            @JsonSubTypes.Type(value = DelegationAdded.class, name = "DelegationAdded"),
            @JsonSubTypes.Type(value = DelegationRemoved.class, name = "DelegationRemoved")
    })
    private final List<TransactionResultEvent> events;
    private final Outcome outcome;


    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tag")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = RejectReasonAmountTooLarge.class, name = "AmountTooLarge"),
            @JsonSubTypes.Type(value = RejectReasonDuplicateAggregationKey.class, name = "DuplicateAggregationKey"),
            @JsonSubTypes.Type(value = RejectReasonBakerInCooldown.class, name = "BakerInCooldown"),
            @JsonSubTypes.Type(value = RejectReasonSerializationFailure.class, name = "SerializationFailure"),
            @JsonSubTypes.Type(value = RejectReasonInvalidAccountReference.class, name = "InvalidAccountReference"),
            @JsonSubTypes.Type(value = RejectReasonInvalidEncryptedAmountTransferProof.class, name = "InvalidEncryptedAmountTransferProof"),
            @JsonSubTypes.Type(value = RejectReasonStakeUnderMinimumThresholdForBaking.class, name = "StakeUnderMinimumThresholdForBaking"),
            @JsonSubTypes.Type(value = RejectReasonModuleNotWF.class, name = "ModuleNotWF"),
            @JsonSubTypes.Type(value = RejectReasonRejectedReceive.class, name = "RejectedReceive"),
            @JsonSubTypes.Type(value = RejectReasonInsufficientBalanceForBakerStake.class, name = "InsufficientBalanceForBakerStake"),
            @JsonSubTypes.Type(value = RejectReasonModuleHashAlreadyExists.class, name = "ModuleHashAlreadyExists"),
            @JsonSubTypes.Type(value = RejectReasonOutOfEnergy.class, name = "OutOfEnergy"),
            @JsonSubTypes.Type(value = RejectReasonInvalidReceiveMethod.class, name = "InvalidReceiveMethod"),
            @JsonSubTypes.Type(value = RejectReasonNotAllowedToReceiveEncrypted.class, name = "NotAllowedToReceiveEncrypted"),
            @JsonSubTypes.Type(value = RejectReasonInvalidInitMethod.class, name = "InvalidInitMethod"),
            @JsonSubTypes.Type(value = RejectReasonRejectedInit.class, name = "RejectedInit"),
            @JsonSubTypes.Type(value = RejectReasonInvalidCredentials.class, name = "InvalidCredentials"),
            @JsonSubTypes.Type(value = RejectReasonInvalidTransferToPublicProof.class, name = "InvalidTransferToPublicProof"),
            @JsonSubTypes.Type(value = RejectReasonZeroScheduledAmount.class, name = "ZeroScheduledAmount"),
            @JsonSubTypes.Type(value = RejectReasonNotAllowedMultipleCredentials.class, name = "NotAllowedMultipleCredentials"),
            @JsonSubTypes.Type(value = RejectReasonNotAllowedToHandleEncrypted.class, name = "NotAllowedToHandleEncrypted"),
            @JsonSubTypes.Type(value = RejectReasonDuplicateCredIDs.class, name = "DuplicateCredIDs"),
            @JsonSubTypes.Type(value = RejectReasonFirstScheduledReleaseExpired.class, name = "FirstScheduledReleaseExpired"),
            @JsonSubTypes.Type(value = RejectReasonEncryptedAmountSelfTransfer.class, name = "EncryptedAmountSelfTransfer"),
            @JsonSubTypes.Type(value = RejectReasonInvalidModuleReference.class, name = "InvalidModuleReference"),
            @JsonSubTypes.Type(value = RejectReasonInvalidContractAddress.class, name = "InvalidContractAddress"),
            @JsonSubTypes.Type(value = RejectReasonRuntimeFailure.class, name = "RuntimeFailure"),
            @JsonSubTypes.Type(value = RejectReasonNonExistentRewardAccount.class, name = "NonExistentRewardAccount"),
            @JsonSubTypes.Type(value = RejectReasonInvalidProof.class, name = "InvalidProof"),
            @JsonSubTypes.Type(value = RejectReasonAlreadyABaker.class, name = "AlreadyABaker"),
            @JsonSubTypes.Type(value = RejectReasonNotABaker.class, name = "NotABaker"),
            @JsonSubTypes.Type(value = RejectReasonNonExistentCredentialID.class, name = "NonExistentCredentialID"),
            @JsonSubTypes.Type(value = RejectReasonKeyIndexAlreadyInUse.class, name = "KeyIndexAlreadyInUse"),
            @JsonSubTypes.Type(value = RejectReasonInvalidAccountThreshold.class, name = "InvalidAccountThreshold"),
            @JsonSubTypes.Type(value = RejectReasonInvalidCredentialKeySignThreshold.class, name = "InvalidCredentialKeySignThreshold"),
            @JsonSubTypes.Type(value = RejectReasonInvalidIndexOnEncryptedTransfer.class, name = "InvalidIndexOnEncryptedTransfer"),
            @JsonSubTypes.Type(value = RejectReasonNonIncreasingSchedule.class, name = "NonIncreasingSchedule"),
            @JsonSubTypes.Type(value = RejectReasonScheduledSelfTransfer.class, name = "ScheduledSelfTransfer"),
            @JsonSubTypes.Type(value = RejectReasonNonExistentCredIDs.class, name = "NonExistentCredIDs"),
            @JsonSubTypes.Type(value = RejectReasonRemoveFirstCredential.class, name = "RemoveFirstCredential"),
            @JsonSubTypes.Type(value = RejectReasonCredentialHolderDidNotSign.class, name = "CredentialHolderDidNotSign")
    })
    private final RejectReason rejectReason;

    @JsonCreator
    TransactionResult(@JsonProperty("events") List<TransactionResultEvent> events,
                      @JsonProperty("outcome") Outcome outcome,
                      @JsonProperty("rejectReason") RejectReason rejectReason) {
        this.events = events;
        this.outcome = outcome;
        this.rejectReason = rejectReason;
    }
}
