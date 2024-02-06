package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.sdk.crypto.bls.BLSPublicKey;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.transactionstatus.*;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Details of a transaction with a sender account.
 * <p>
 * Users should always check whether the {@link AccountTransactionDetails} is {@link AccountTransactionDetails#isSuccessful()} or not.
 * If this returns false, then one should consult the {@link AccountTransactionDetails#rejectReasonType} for why the transaction failed.
 * If the transaction was successful then first check the type via {@link AccountTransactionDetails#getType()} and use the corresponding
 * getter for getting the concrete event.
 */
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Builder
@Getter
public class AccountTransactionDetails {

    /**
     * Sender of the transaction
     */
    private final AccountAddress sender;

    /**
     * The amount that was deducted from the sender account
     * as a result of this transaction.
     */
    private final CCDAmount cost;

    /**
     * Type of the outcome.
     * Note that the type is only set if the transaction was successfully executed
     * i.e., {@link AccountTransactionDetails#isSuccessful()} returns true.
     */
    private final TransactionResultEventType type;

    /**
     * Present if the transaction failed.
     */
    private final RejectReasonType rejectReasonType;


    /**
     * Present if the transaction failed.
     */
    private final RejectReason rejectReason;

    /**
     * True if the transaction was successfully executed.
     */
    private final boolean successful;

    /**
     * The module reference if a module was deployed if the transaction
     * deployed a module.
     * Present if the transaction was a {@link DeployModule}
     */
    private final ModuleRef moduleDeployed;

    /**
     * The result of a contract being initialized if the transaction
     * initialized a contract.
     * Present if the transaction was a {@link InitContract}
     */
    private final ContractInitializedResult contractInitialized;

    /**
     * The resulting list of events of a contract being updated if the transaction
     * updated a smart contract.
     * Present if the transaction was a {@link UpdateContract}
     */
    private final List<ContractTraceElement> contractUpdated;

    /**
     * The result of an account transfer if the transaction
     * updated was such one.
     * Present if the transaction was a {@link TransferTransaction}
     */
    private final TransferredResult accountTransfer;

    /**
     * The result of a baker being added to the chain.
     * Present if the transaction was a {@link ConfigureBaker}
     */
    private final BakerAddedResult bakerAdded;

    /**
     * The result of a baker being removed from the chain.
     * Present if the transaction was a {@link ConfigureBaker} with an
     * equity capital set to 0.
     */
    private final BakerRemovedResult bakerRemoved;


    /**
     * The result of a baker having its stake updated.
     * Present if the transaction was a {@link ConfigureBaker} with an updated stake.
     */
    private final BakerStakeUpdated bakerStakeUpdated;

    /**
     * The result of a baker having its restake flag updated.
     * Present if the transaction was a {@link ConfigureBaker} with a new re-stake flag.
     */
    private final BakerSetRestakeEarningsResult bakerRestakeEarningsUpdated;

    /**
     * The result of a baker having its keys updated.
     * Present if the transaction was a {@link ConfigureBaker} with new keys.
     */
    private final BakerKeysUpdatedResult bakerKeysUpdated;

    /**
     * The result of a transaction that is transferring CCD with a schedule.
     * Present if the transaction was a {@link TransferScheduleTransaction}.
     */
    private final TransferredWithScheduleResult transferredWithSchedule;

    /**
     * The result of the sender updating keys.
     * Present if the transaction was a {@link UpdateCredentialKeysTransaction}.
     */
    private final CredentialKeysUpdatedResult credentialKeysUpdated;

    /**
     * The result of a credential being updated.
     */
    private final CredentialsUpdatedResult credentialsUpdated;

    /**
     * The result of the sender registering data on the chain.
     * Present if the transaction was a {@link RegisterDataTransaction}.
     */
    private final DataRegisteredResult dataRegistered;

    /**
     * The result of the sender configuring baking.
     * Present if the transaction was a {@link ConfigureBaker}.
     */
    private final BakerConfigured bakerConfigured;

    /**
     * The result of the sender configuring delegation.
     * Present if the transaction was a {@link ConfigureDelegation}.
     */
    private final DelegatorConfigured delegatorConfigured;

    /**
     * The result of the sender sending an encrypted transfer.
     * Present if the transaction was an {@link EncryptedTransfer}.
     */
    private final EncryptedTransferResult encryptedTransfer;

    /**
     * The result of the sender adding CCD to its encrypted balance from its
     * non-encrypted balance.
     * Present if the transaction was a {@link TransferToEncrypted}.
     */
    private final EncryptedSelfAmountAddedResult addedToEncryptedBalance;

    /**
     * The result of the sender subtracting CCD from its encrypted balance to its
     * non-encrypted balance.
     * Present if the transaction was a {@link TransferToPublic}.
     */
    private final EncryptedAmountsRemovedResult removedFromEncryptedBalance;

    public static AccountTransactionDetails from(com.concordium.grpc.v2.AccountTransactionDetails tx) {
        val sender = AccountAddress.from(tx.getSender());
        val detailsBuilder = AccountTransactionDetails.builder().sender(sender).cost(CCDAmount.from(tx.getCost())).successful(true);
        val effects = tx.getEffects();
        switch (effects.getEffectCase()) {
            case NONE:
                val reason = effects.getNone().getRejectReason();
                detailsBuilder.successful(false).rejectReasonType(RejectReasonType.from(reason));
                extractRejectReasonError(detailsBuilder, reason);
                break;
            case MODULE_DEPLOYED:
                detailsBuilder.type(TransactionResultEventType.MODULE_DEPLOYED).moduleDeployed(ModuleRef.from(effects.getModuleDeployed().getValue().toByteArray()));
                break;
            case CONTRACT_INITIALIZED:
                detailsBuilder.type(TransactionResultEventType.CONTRACT_INITIALIZED).contractInitialized(ContractInitializedResult.from(effects.getContractInitialized()));
                break;
            case CONTRACT_UPDATE_ISSUED:
                val updateEvents = effects.getContractUpdateIssued().getEffectsList().stream().map(ContractTraceElement::from).collect(Collectors.toList());
                detailsBuilder.type(TransactionResultEventType.CONTRACT_UPDATED).contractUpdated(updateEvents);
                break;
            case ACCOUNT_TRANSFER:
                detailsBuilder.type(TransactionResultEventType.TRANSFERRED).accountTransfer(TransferredResult.from(effects.getAccountTransfer(), sender));

                break;
            case BAKER_ADDED:
                detailsBuilder.type(TransactionResultEventType.BAKER_ADDED).bakerAdded(BakerAddedResult.from(effects.getBakerAdded()));
                break;
            case BAKER_REMOVED:
                detailsBuilder.type(TransactionResultEventType.BAKER_REMOVED).bakerRemoved(BakerRemovedResult.from(effects.getBakerRemoved(), sender));
                break;
            case BAKER_STAKE_UPDATED:
                detailsBuilder.type(TransactionResultEventType.BAKER_STAKE_UPDATED).bakerStakeUpdated(BakerStakeUpdated.from(effects.getBakerStakeUpdated(), sender));
                break;
            case BAKER_RESTAKE_EARNINGS_UPDATED:
                detailsBuilder.type(TransactionResultEventType.BAKER_SET_RESTAKE_EARNINGS).bakerRestakeEarningsUpdated(BakerSetRestakeEarningsResult.from(effects.getBakerRestakeEarningsUpdated(), sender));
                break;
            case BAKER_KEYS_UPDATED:
                detailsBuilder.type(TransactionResultEventType.BAKER_KEYS_UPDATED).bakerKeysUpdated(BakerKeysUpdatedResult.from(effects.getBakerKeysUpdated(), sender));
                break;
            case ENCRYPTED_AMOUNT_TRANSFERRED:
                detailsBuilder.type(TransactionResultEventType.ENCRYPTED_TRANSFER).encryptedTransfer(EncryptedTransferResult.from(effects.getEncryptedAmountTransferred()));
                break;
            case TRANSFERRED_TO_ENCRYPTED:
                detailsBuilder.type(TransactionResultEventType.ENCRYPTED_SELF_AMOUNT_ADDED).addedToEncryptedBalance(EncryptedSelfAmountAddedResult.from(effects.getTransferredToEncrypted()));
                break;
            case TRANSFERRED_TO_PUBLIC:
                detailsBuilder.type(TransactionResultEventType.ENCRYPTED_AMOUNTS_REMOVED).removedFromEncryptedBalance(EncryptedAmountsRemovedResult.from(effects.getTransferredToPublic().getRemoved()));
                break;
            case TRANSFERRED_WITH_SCHEDULE:
                detailsBuilder.type(TransactionResultEventType.TRANSFERRED_WITH_SCHEDULE).transferredWithSchedule(TransferredWithScheduleResult.from(effects.getTransferredWithSchedule(), sender));
                break;
            case CREDENTIAL_KEYS_UPDATED:
                detailsBuilder.type(TransactionResultEventType.CREDENTIAL_KEYS_UPDATED).credentialKeysUpdated(CredentialKeysUpdatedResult.from(effects.getCredentialKeysUpdated()));
                break;
            case CREDENTIALS_UPDATED:
                detailsBuilder.type(TransactionResultEventType.CREDENTIALS_UPDATED).credentialsUpdated(CredentialsUpdatedResult.from(effects.getCredentialsUpdated(), sender));
                break;
            case DATA_REGISTERED:
                detailsBuilder.type(TransactionResultEventType.DATA_REGISTERED).dataRegistered(DataRegisteredResult.from(effects.getDataRegistered()));
                break;
            case BAKER_CONFIGURED:
                detailsBuilder.type(TransactionResultEventType.BAKER_CONFIGURED).bakerConfigured(BakerConfigured.from(effects.getBakerConfigured(), sender));
                break;
            case DELEGATION_CONFIGURED:
                detailsBuilder.type(TransactionResultEventType.DELEGATION_CONFIGURED).delegatorConfigured(DelegatorConfigured.from(effects.getDelegationConfigured(), sender));
                break;
            case EFFECT_NOT_SET:
                throw new IllegalArgumentException("Unrecognized effect.");
        }
        return detailsBuilder.build();
    }

    private static void extractRejectReasonError(AccountTransactionDetailsBuilder detailsBuilder, com.concordium.grpc.v2.RejectReason reason) {
        switch (reason.getReasonCase()) {
            case MODULE_NOT_WF:
                break;
            case MODULE_HASH_ALREADY_EXISTS:
                detailsBuilder.rejectReason(RejectReasonModuleHashAlreadyExists.builder().moduleRef(ModuleRef.from(reason.getModuleHashAlreadyExists().toByteArray())).build());
                break;
            case INVALID_ACCOUNT_REFERENCE:
                detailsBuilder.rejectReason(RejectReasonInvalidAccountReference.builder().address(AccountAddress.from(reason.getInvalidAccountReference())).build());
                break;
            case INVALID_INIT_METHOD:
                val invalidInitName = InitName.from(reason.getInvalidInitMethod().getInitName().getValue());
                detailsBuilder.rejectReason(RejectReasonInvalidInitMethod.builder().initName(invalidInitName).moduleRef(ModuleRef.from(reason.getInvalidInitMethod().getModuleRef())).build());
                break;
            case INVALID_RECEIVE_METHOD:
                detailsBuilder.rejectReason(RejectReasonInvalidReceiveMethod.builder().moduleRef(ModuleRef.from(reason.getInvalidReceiveMethod().getModuleRef().getValue().toByteArray())).receiveName(ReceiveName.from(reason.getInvalidReceiveMethod().getReceiveName())).build());
                break;
            case INVALID_MODULE_REFERENCE:
                detailsBuilder.rejectReason(RejectReasonInvalidModuleReference.builder().moduleRef(ModuleRef.from(reason.getInvalidModuleReference().toByteArray())).build());
                break;
            case INVALID_CONTRACT_ADDRESS:
                detailsBuilder.rejectReason(RejectReasonInvalidContractAddress.builder().contractAddress(ContractAddress.from(reason.getInvalidContractAddress())).build());
                break;
            case RUNTIME_FAILURE:
                detailsBuilder.rejectReason(new RejectReasonRuntimeFailure());
                break;
            case AMOUNT_TOO_LARGE:
                detailsBuilder.rejectReason(RejectReasonAmountTooLarge.builder().account(AccountAddress.from(reason.getAmountTooLarge().getAddress())).amount(CCDAmount.from(reason.getAmountTooLarge().getAmount())).build());
                break;
            case SERIALIZATION_FAILURE:
                detailsBuilder.rejectReason(new RejectReasonSerializationFailure());
                break;
            case OUT_OF_ENERGY:
                detailsBuilder.rejectReason(new RejectReasonOutOfEnergy());
                break;
            case REJECTED_INIT:
                detailsBuilder.rejectReason(RejectReasonRejectedInit.builder().rejectedInit(reason.getRejectedInit().getRejectReason()).build());
                break;
            case REJECTED_RECEIVE:
                detailsBuilder.rejectReason(RejectReasonRejectedReceive.builder().rejectReason(reason.getRejectedReceive().getRejectReason()).receiveName(ReceiveName.parse(reason.getRejectedReceive().getReceiveName().getValue())).contractAddress(ContractAddress.from(reason.getRejectedReceive().getContractAddress())).parameter(Parameter.from(reason.getRejectedReceive().getParameter())).build());
                break;
            case INVALID_PROOF:
                detailsBuilder.rejectReason(new RejectReasonInvalidProof());
                break;
            case ALREADY_A_BAKER:
                detailsBuilder.rejectReason(RejectReasonAlreadyABaker.builder().bakerId(BakerId.from(reason.getAlreadyABaker().getValue())).build());
                break;
            case NOT_A_BAKER:
                detailsBuilder.rejectReason(RejectReasonNotABaker.builder().accountAddress(AccountAddress.from(reason.getNotABaker())).build());
                break;
            case INSUFFICIENT_BALANCE_FOR_BAKER_STAKE:
                detailsBuilder.rejectReason(new RejectReasonInsufficientBalanceForBakerStake());
                break;
            case STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING:
                detailsBuilder.rejectReason(new RejectReasonStakeUnderMinimumThresholdForBaking());
                break;
            case BAKER_IN_COOLDOWN:
                detailsBuilder.rejectReason(new RejectReasonBakerInCooldown());
                break;
            case DUPLICATE_AGGREGATION_KEY:
                detailsBuilder.rejectReason(RejectReasonDuplicateAggregationKey.builder().publicKey(BLSPublicKey.from(reason.getDuplicateAggregationKey().toByteArray())).build());
                break;
            case NON_EXISTENT_CREDENTIAL_ID:
                detailsBuilder.rejectReason(new RejectReasonNonExistentCredentialID());
                break;
            case KEY_INDEX_ALREADY_IN_USE:
                detailsBuilder.rejectReason(new RejectReasonKeyIndexAlreadyInUse());
                break;
            case INVALID_ACCOUNT_THRESHOLD:
                detailsBuilder.rejectReason(new RejectReasonInvalidAccountThreshold());
                break;
            case INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD:
                detailsBuilder.rejectReason(new RejectReasonInvalidCredentialKeySignThreshold());
                break;
            case INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF:
                detailsBuilder.rejectReason(new RejectReasonInvalidEncryptedAmountTransferProof());
                break;
            case INVALID_TRANSFER_TO_PUBLIC_PROOF:
                detailsBuilder.rejectReason(new RejectReasonInvalidTransferToPublicProof());
                break;
            case ENCRYPTED_AMOUNT_SELF_TRANSFER:
                detailsBuilder.rejectReason(RejectReasonEncryptedAmountSelfTransfer.builder().address(AccountAddress.from(reason.getEncryptedAmountSelfTransfer())).build());
                break;
            case INVALID_INDEX_ON_ENCRYPTED_TRANSFER:
                detailsBuilder.rejectReason(new RejectReasonInvalidIndexOnEncryptedTransfer());
                break;
            case ZERO_SCHEDULEDAMOUNT:
                detailsBuilder.rejectReason(new RejectReasonZeroScheduledAmount());
                break;
            case NON_INCREASING_SCHEDULE:
                detailsBuilder.rejectReason(new RejectReasonNonIncreasingSchedule());
                break;
            case FIRST_SCHEDULED_RELEASE_EXPIRED:
                detailsBuilder.rejectReason(new RejectReasonFirstScheduledReleaseExpired());
                break;
            case SCHEDULED_SELF_TRANSFER:
                detailsBuilder.rejectReason(RejectReasonScheduledSelfTransfer.builder().accountAddress(AccountAddress.from(reason.getScheduledSelfTransfer())).build());
                break;
            case INVALID_CREDENTIALS:
                detailsBuilder.rejectReason(new RejectReasonInvalidCredentials());
                break;
            case DUPLICATE_CRED_IDS:
                detailsBuilder.rejectReason(RejectReasonDuplicateCredIDs.builder().duplicates(reason.getDuplicateCredIds().getIdsList().stream().map(CredentialRegistrationId::from).collect(Collectors.toList())).build());
                break;
            case NON_EXISTENT_CRED_IDS:
                detailsBuilder.rejectReason(RejectReasonNonExistentCredIDs.builder().ids(reason.getNonExistentCredIds().getIdsList().stream().map(CredentialRegistrationId::from).collect(Collectors.toList())).build());
                break;
            case REMOVE_FIRST_CREDENTIAL:
                detailsBuilder.rejectReason(new RejectReasonRemoveFirstCredential());
                break;
            case CREDENTIAL_HOLDER_DID_NOT_SIGN:
                detailsBuilder.rejectReason(new RejectReasonCredentialHolderDidNotSign());
                break;
            case NOT_ALLOWED_MULTIPLE_CREDENTIALS:
                detailsBuilder.rejectReason(new RejectReasonNotAllowedMultipleCredentials());
                break;
            case NOT_ALLOWED_TO_RECEIVE_ENCRYPTED:
                detailsBuilder.rejectReason(new RejectReasonNotAllowedToReceiveEncrypted());
                break;
            case NOT_ALLOWED_TO_HANDLE_ENCRYPTED:
                detailsBuilder.rejectReason(new RejectReasonNotAllowedToHandleEncrypted());
                break;
            case MISSING_BAKER_ADD_PARAMETERS:
                detailsBuilder.rejectReason(new RejectReasonMissingBakerAddParameters());
                break;
            case FINALIZATION_REWARD_COMMISSION_NOT_IN_RANGE:
                detailsBuilder.rejectReason(new RejectReasonFinalizationRewardCommissionNotInRange());
                break;
            case BAKING_REWARD_COMMISSION_NOT_IN_RANGE:
                detailsBuilder.rejectReason(new RejectReasonBakingRewardCommissionNotInRange());
                break;
            case TRANSACTION_FEE_COMMISSION_NOT_IN_RANGE:
                detailsBuilder.rejectReason(new RejectReasonTransactionFeeCommissionNotInRange());
                break;
            case ALREADY_A_DELEGATOR:
                detailsBuilder.rejectReason(RejectReasonAlreadyABaker.builder().bakerId(BakerId.from(reason.getAlreadyABaker())).build());
                break;
            case INSUFFICIENT_BALANCE_FOR_DELEGATION_STAKE:
                detailsBuilder.rejectReason(new RejectReasonInsufficientDelegationStake());
                break;
            case MISSING_DELEGATION_ADD_PARAMETERS:
                detailsBuilder.rejectReason(new RejectReasonMissingDelegationAddParameters());
                break;
            case INSUFFICIENT_DELEGATION_STAKE:
                detailsBuilder.rejectReason(new RejectReasonInsufficientDelegationStake());
                break;
            case DELEGATOR_IN_COOLDOWN:
                detailsBuilder.rejectReason(new RejectReasonDelegatorInCooldown());
                break;
            case NOT_A_DELEGATOR:
                detailsBuilder.rejectReason(RejectReasonNotADelegator.builder()
                        .accountAddress(AccountAddress.from(reason.getNotADelegator()))
                        .build());
                break;
            case DELEGATION_TARGET_NOT_A_BAKER:
                detailsBuilder.rejectReason(RejectReasonDelegationTargetNotABaker.builder()
                        .bakerId(BakerId.from(reason.getDelegationTargetNotABaker()))
                        .build());
                break;
            case STAKE_OVER_MAXIMUM_THRESHOLD_FOR_POOL:
                detailsBuilder.rejectReason(new RejectReasonStakeOverMaximumThresholdForPool());
                break;
            case POOL_WOULD_BECOME_OVER_DELEGATED:
                detailsBuilder.rejectReason(new RejectReasonPoolWouldBecomeOverDelegated());
                break;
            case POOL_CLOSED:
                detailsBuilder.rejectReason(new RejectReasonPoolClosed());
                break;
            case REASON_NOT_SET:
                break;
        }
    }

}
