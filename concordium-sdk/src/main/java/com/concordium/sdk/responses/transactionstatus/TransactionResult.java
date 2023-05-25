package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.ImmutableList;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@Builder
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public final class TransactionResult {

    // Emitted events from transactions
    // https://github.com/Concordium/concordium-base/blob/8dcee8746e40d663222aa3b4b04eaa3710e2779e/haskell-src/Concordium/Types/Execution.hs#L736
    // Please keep them in same order.
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tag")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ModuleCreatedResult.class, name = "ModuleDeployed"),
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

    @Getter
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

    /**
     * Type of the failed transaction, if known.
     * Note, for transaction failed due to serialization failure this will not be set.
     */
    private final TransactionResultEventType failedTransactionType;

    public List<TransactionResultEvent> getEvents() {
        if (this.outcome != Outcome.SUCCESS) {
            throw new IllegalArgumentException("Events only present for transactions with outcome " + Outcome.SUCCESS);
        }
        return events;
    }

    public RejectReason getRejectReason() {
        if (this.outcome != Outcome.REJECT) {
            throw new IllegalArgumentException("RejectReason only present for transactions with outcome " + Outcome.REJECT);
        }
        return rejectReason;
    }

    public TransactionResultEventType getFailedTransactionType() {
        if (this.outcome != Outcome.REJECT) {
            throw new IllegalArgumentException("Failed transaction type only present for transactions with outcome " + Outcome.REJECT);
        }
        return failedTransactionType;
    }

    /**
     * Parses {@link AccountTransactionEffects} and {@link AccountAddress} to {@link TransactionResult}.
     * @param effects {@link AccountTransactionEffects} returned by the GRPC V2 API.
     * @param sender {@link AccountAddress} of the Transaction with the corresponding {@link TransactionResult}.
     * @return parsed {@link TransactionResult}.
     */
    public static TransactionResult parse(AccountTransactionEffects effects, AccountAddress sender) {
        val builder = TransactionResult.builder();
        var events = new ImmutableList.Builder<TransactionResultEvent>();
        builder.outcome(effects.hasNone() ? Outcome.REJECT : Outcome.SUCCESS);

        switch (effects.getEffectCase()) {
            case NONE:
                val none = effects.getNone();
                builder.rejectReason(RejectReason.parse(none.getRejectReason()))
                        .failedTransactionType(TransactionResultEventType.parse(none.getTransactionType()));
                break;
            case MODULE_DEPLOYED:
                events.add(ModuleDeployedResult.parse(effects.getModuleDeployed()));
                break;
            case CONTRACT_INITIALIZED:
                events.add(ContractInitializedResult.parse(effects.getContractInitialized()));
                break;
            case CONTRACT_UPDATE_ISSUED:
                updateEventsBuilderWithContractUpdatedEvents(events, effects.getContractUpdateIssued().getEffectsList());
                break;
            case ACCOUNT_TRANSFER:
                val transfer = effects.getAccountTransfer();
                events.add(TransferredResult.parse(transfer, sender));
                if(transfer.hasMemo()) {
                    events.add((TransferMemoResult.parse(transfer.getMemo())));
                }
                break;
            case BAKER_ADDED:
                events.add(BakerAddedResult.parse(effects.getBakerAdded()));
                break;
            case BAKER_REMOVED:
                events.add(BakerRemovedResult.parse(effects.getBakerRemoved(), sender));
                break;
            case BAKER_STAKE_UPDATED:
                val bakerStakeUpdated = effects.getBakerStakeUpdated();
                if (!bakerStakeUpdated.hasUpdate()) {break;} // If the stake was not updated nothing happened.
                if (bakerStakeUpdated.getUpdate().getIncreased()) {
                    events.add(BakerStakeIncreasedResult.parse(bakerStakeUpdated.getUpdate(), sender));
                } else {
                    events.add(BakerStakeDecreasedResult.parse(bakerStakeUpdated.getUpdate(), sender));
                }
                break;
            case BAKER_RESTAKE_EARNINGS_UPDATED:
                events.add(BakerSetRestakeEarningsResult.parse(effects.getBakerRestakeEarningsUpdated(), sender));
                break;
            case BAKER_KEYS_UPDATED:
                events.add(BakerKeysUpdatedResult.parse(effects.getBakerKeysUpdated()));
                break;
            case ENCRYPTED_AMOUNT_TRANSFERRED:
                val encryptedAmountTransferred = effects.getEncryptedAmountTransferred();
                events.add(EncryptedAmountsRemovedResult.parse(encryptedAmountTransferred.getRemoved()));
                events.add(NewEncryptedAmountResult.parse(encryptedAmountTransferred.getAdded()));
                if (encryptedAmountTransferred.hasMemo()) {
                    events.add(TransferMemoResult.parse(encryptedAmountTransferred.getMemo()));
                }
                break;
            case TRANSFERRED_TO_ENCRYPTED:
                events.add(EncryptedSelfAmountAddedResult.parse(effects.getTransferredToEncrypted()));
                break;
            case TRANSFERRED_TO_PUBLIC:
                events.add(EncryptedAmountsRemovedResult.parse(effects.getTransferredToPublic().getRemoved()));
                events.add(AmountAddedByDecryptionResult.parse(effects.getTransferredToPublic()));
                break;
            case TRANSFERRED_WITH_SCHEDULE:
                val transferredWithSchedule = effects.getTransferredWithSchedule();
                events.add(TransferredWithScheduleResult.parse(transferredWithSchedule, sender));
                if (transferredWithSchedule.hasMemo()) {
                    events.add(TransferMemoResult.parse(transferredWithSchedule.getMemo()));
                }
                break;
            case CREDENTIAL_KEYS_UPDATED:
                events.add(CredentialKeysUpdatedResult.parse(effects.getCredentialKeysUpdated()));
                break;
            case CREDENTIALS_UPDATED:
                events.add(CredentialsUpdatedResult.parse(effects.getCredentialsUpdated(), sender));
                break;
            case DATA_REGISTERED:
                events.add(DataRegisteredResult.parse(effects.getDataRegistered()));
                break;
            case BAKER_CONFIGURED:
                updateEventsBuilderWithBakerEvents(sender, events, effects.getBakerConfigured().getEventsList());
                break;
            case DELEGATION_CONFIGURED:
                updateEventsBuilderWithDelegationEvents(sender, events, effects.getDelegationConfigured().getEventsList());
                break;
            case EFFECT_NOT_SET:
                throw new IllegalArgumentException();
        }
        return builder
                .events(events.build())
                .build();
    }

    /**
     * Helper method for parsing and adding {@link DelegationEvent}s from {@link com.concordium.grpc.v2.AccountTransactionEffects.DelegationConfigured} to the list of events.
     */
    private static void updateEventsBuilderWithDelegationEvents(AccountAddress sender, ImmutableList.Builder<TransactionResultEvent> events, List<DelegationEvent> delegationEvents) {
        delegationEvents.forEach(e -> {
            switch (e.getEventCase()) {
                case DELEGATION_STAKE_INCREASED:
                    events.add(DelegationStakeIncreased.parse(e.getDelegationStakeIncreased(), sender));
                    break;
                case DELEGATION_STAKE_DECREASED:
                    events.add(DelegationStakeDecreased.parse(e.getDelegationStakeDecreased(), sender));
                    break;
                case DELEGATION_SET_RESTAKE_EARNINGS:
                    events.add(DelegationSetRestakeEarnings.parse(e.getDelegationSetRestakeEarnings(), sender));
                    break;
                case DELEGATION_SET_DELEGATION_TARGET:
                    events.add(DelegationSetDelegationTarget.parse(e.getDelegationSetDelegationTarget(), sender));
                    break;
                case DELEGATION_ADDED:
                    events.add(DelegationAdded.parse(e.getDelegationAdded(), sender));
                    break;
                case DELEGATION_REMOVED:
                    events.add(DelegationRemoved.parse(e.getDelegationRemoved(), sender));
                    break;
                case EVENT_NOT_SET:
                    break;
            }
        });
    }

    /**
     * Helper method for parsing and adding {@link BakerEvent}s from {@link com.concordium.grpc.v2.AccountTransactionEffects.BakerConfigured} to the list of events.
     */
    private static void updateEventsBuilderWithBakerEvents(AccountAddress sender, ImmutableList.Builder<TransactionResultEvent> events, List<BakerEvent> bakerEvents) {
        bakerEvents.forEach(e -> {
            switch (e.getEventCase()) {
                case BAKER_ADDED:
                    events.add(BakerAddedResult.parse(e.getBakerAdded()));
                case BAKER_REMOVED:
                    events.add(BakerRemovedResult.parse(e.getBakerRemoved(), sender));
                    break;
                case BAKER_STAKE_INCREASED:
                    events.add(BakerStakeIncreasedResult.parse(e.getBakerStakeIncreased(), sender));
                    break;
                case BAKER_STAKE_DECREASED:
                    events.add(BakerStakeDecreasedResult.parse(e.getBakerStakeDecreased(), sender));
                    break;
                case BAKER_RESTAKE_EARNINGS_UPDATED:
                    events.add(BakerSetRestakeEarningsResult.parse(e.getBakerRestakeEarningsUpdated(), sender));
                    break;
                case BAKER_KEYS_UPDATED:
                    events.add(BakerKeysUpdatedResult.parse(e.getBakerKeysUpdated()));
                    break;
                case BAKER_SET_OPEN_STATUS:
                    events.add(BakerSetOpenStatus.parse(e.getBakerSetOpenStatus(), sender));
                    break;
                case BAKER_SET_METADATA_URL:
                    events.add(BakerSetMetadataURL.parse(e.getBakerSetMetadataUrl(), sender));
                    break;
                case BAKER_SET_TRANSACTION_FEE_COMMISSION:
                    events.add(BakerSetTransactionFeeCommission.parse(e.getBakerSetTransactionFeeCommission(), sender));
                    break;
                case BAKER_SET_BAKING_REWARD_COMMISSION:
                    events.add(BakerSetBakingRewardCommission.parse(e.getBakerSetBakingRewardCommission(), sender));
                    break;
                case BAKER_SET_FINALIZATION_REWARD_COMMISSION:
                    events.add(BakerSetFinalizationRewardCommission.parse(e.getBakerSetFinalizationRewardCommission(), sender));
                    break;
                default: throw new IllegalArgumentException();
            }
        });
    }

    /**
     * Helper method for parsing and adding {@link com.concordium.grpc.v2.ContractTraceElement}s from {@link com.concordium.grpc.v2.AccountTransactionEffects.ContractUpdateIssued} to the list of events.
     */
    private static void updateEventsBuilderWithContractUpdatedEvents(ImmutableList.Builder<TransactionResultEvent> events, List<ContractTraceElement> traceElements) {
        traceElements.forEach(e -> {
            switch (e.getElementCase()) {
                case UPDATED:
                    events.add(ContractUpdated.parse(e.getUpdated()));
                    break;
                case TRANSFERRED:
                    events.add(TransferredResult.parse(e.getTransferred()));
                    break;
                case INTERRUPTED:
                    events.add(InterruptedResult.parse(e.getInterrupted()));
                    break;
                case RESUMED:
                    events.add(ResumedResult.parse(e.getResumed()));
                    break;
                case UPGRADED:
                    events.add(UpgradedResult.parse(e.getUpgraded()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        });
    }
}


