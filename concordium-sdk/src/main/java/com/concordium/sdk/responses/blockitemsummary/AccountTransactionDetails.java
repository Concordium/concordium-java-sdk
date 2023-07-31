package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.transactionstatus.*;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.AccountAddress;
import lombok.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Details of a transaction with a sender account.
 *
 * Users should always check whether the {@link AccountTransactionDetails} is {@link AccountTransactionDetails#isSuccessful()} or not.
 * If this returns false, then one should consult the {@link AccountTransactionDetails#rejectReason} for why the transaction failed.
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
    private final RejectReasonType rejectReason;

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
        val detailsBuilder = AccountTransactionDetails
                .builder()
                .sender(sender)
                .cost(CCDAmount.from(tx.getCost()))
                .successful(true);
        val effects = tx.getEffects();
        switch (effects.getEffectCase()) {
            case NONE:
                detailsBuilder
                        .successful(false)
                        .rejectReason(RejectReasonType.from(effects.getNone().getRejectReason()));
                break;
            case MODULE_DEPLOYED:
                detailsBuilder
                        .type(TransactionResultEventType.MODULE_DEPLOYED)
                        .moduleDeployed(ModuleRef.from(effects.getModuleDeployed().getValue().toByteArray()));
                break;
            case CONTRACT_INITIALIZED:
                detailsBuilder
                        .type(TransactionResultEventType.CONTRACT_INITIALIZED)
                        .contractInitialized(ContractInitializedResult.from(effects.getContractInitialized()));
                break;
            case CONTRACT_UPDATE_ISSUED:
                val updateEvents = effects
                        .getContractUpdateIssued()
                        .getEffectsList()
                        .stream()
                        .map(ContractTraceElement::from)
                        .collect(Collectors.toList());
                detailsBuilder
                        .type(TransactionResultEventType.CONTRACT_UPDATED)
                        .contractUpdated(updateEvents);
                break;
            case ACCOUNT_TRANSFER:
                detailsBuilder
                        .type(TransactionResultEventType.TRANSFERRED)
                        .accountTransfer(TransferredResult.from(effects.getAccountTransfer(), sender));

                break;
            case BAKER_ADDED:
                detailsBuilder
                        .type(TransactionResultEventType.BAKER_ADDED)
                        .bakerAdded(BakerAddedResult.from(effects.getBakerAdded()));
                break;
            case BAKER_REMOVED:
                detailsBuilder
                        .type(TransactionResultEventType.BAKER_REMOVED)
                        .bakerRemoved(BakerRemovedResult.from(effects.getBakerRemoved(), sender));
                break;
            case BAKER_STAKE_UPDATED:
                detailsBuilder
                        .type(TransactionResultEventType.BAKER_STAKE_UPDATED)
                        .bakerStakeUpdated(BakerStakeUpdated.from(effects.getBakerStakeUpdated(), sender));
                break;
            case BAKER_RESTAKE_EARNINGS_UPDATED:
                detailsBuilder
                        .type(TransactionResultEventType.BAKER_SET_RESTAKE_EARNINGS)
                        .bakerRestakeEarningsUpdated(BakerSetRestakeEarningsResult.from(effects.getBakerRestakeEarningsUpdated(), sender));
                break;
            case BAKER_KEYS_UPDATED:
                detailsBuilder
                        .type(TransactionResultEventType.BAKER_KEYS_UPDATED)
                        .bakerKeysUpdated(BakerKeysUpdatedResult.from(effects.getBakerKeysUpdated(), sender));
                break;
            case ENCRYPTED_AMOUNT_TRANSFERRED:
                detailsBuilder
                        .type(TransactionResultEventType.ENCRYPTED_TRANSFER)
                        .encryptedTransfer(EncryptedTransferResult.from(effects.getEncryptedAmountTransferred()));
                break;
            case TRANSFERRED_TO_ENCRYPTED:
                detailsBuilder
                        .type(TransactionResultEventType.ENCRYPTED_SELF_AMOUNT_ADDED)
                        .addedToEncryptedBalance(EncryptedSelfAmountAddedResult.from(effects.getTransferredToEncrypted()));
                break;
            case TRANSFERRED_TO_PUBLIC:
                detailsBuilder
                        .type(TransactionResultEventType.ENCRYPTED_AMOUNTS_REMOVED)
                        .removedFromEncryptedBalance(EncryptedAmountsRemovedResult.from(effects.getTransferredToPublic().getRemoved()));
                break;
            case TRANSFERRED_WITH_SCHEDULE:
                detailsBuilder
                        .type(TransactionResultEventType.TRANSFERRED_WITH_SCHEDULE)
                        .transferredWithSchedule(TransferredWithScheduleResult.from(effects.getTransferredWithSchedule(), sender));
                break;
            case CREDENTIAL_KEYS_UPDATED:
                detailsBuilder
                        .type(TransactionResultEventType.CREDENTIAL_KEYS_UPDATED)
                        .credentialKeysUpdated(CredentialKeysUpdatedResult.from(effects.getCredentialKeysUpdated()));
                break;
            case CREDENTIALS_UPDATED:
                detailsBuilder
                        .type(TransactionResultEventType.CREDENTIALS_UPDATED)
                        .credentialsUpdated(CredentialsUpdatedResult.from(effects.getCredentialsUpdated(), sender));
                break;
            case DATA_REGISTERED:
                detailsBuilder
                        .type(TransactionResultEventType.DATA_REGISTERED)
                        .dataRegistered(DataRegisteredResult.from(effects.getDataRegistered()));
                break;
            case BAKER_CONFIGURED:
                detailsBuilder
                        .type(TransactionResultEventType.BAKER_CONFIGURED)
                        .bakerConfigured(BakerConfigured.from(effects.getBakerConfigured(), sender));
                break;
            case DELEGATION_CONFIGURED:
                detailsBuilder
                        .type(TransactionResultEventType.DELEGATION_CONFIGURED)
                        .delegatorConfigured(DelegatorConfigured.from(effects.getDelegationConfigured(), sender));
                break;
            case EFFECT_NOT_SET:
                throw new IllegalArgumentException("Unrecognized effect.");
        }
        return detailsBuilder.build();
    }

}
