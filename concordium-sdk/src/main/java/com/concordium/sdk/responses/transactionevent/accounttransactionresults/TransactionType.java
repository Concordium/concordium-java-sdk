package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.sdk.responses.transactionstatus.*;

/**
 * Transaction types used with the GRPCv2 API.
 */
public enum TransactionType {
    /**
     * A module was deployed to chain.
     * This event type corresponds to the concrete event {@link ModuleDeployedResult}.
     */
    DEPLOY_MODULE,
    /**
     * A contract was initialized.
     * This event type corresponds to the concrete event {@link ContractInitializedResult}.
     */
    INIT_CONTRACT,
    /**
     * A contract update was issued.
     * This event type corresponds to the concrete event {@link ContractUpdateIssuedResult}.
     */
    UPDATE,
    /**
     * A simple account to account transfer occurred.
     * This event type corresponds to the concrete event {@link AccountTransferResult}.
     */
    TRANSFER,
    /**
     * A baker was added.
     * This event type corresponds to the concrete event {@link BakerAddedResult}.
     */
    ADD_BAKER,
    /**
     * A baker was removed.
     * This event type corresponds to the concrete event {@link BakerRemovedResult}
     */
    REMOVE_BAKER,
    /**
     * An account was deregistered as a baker.
     * This event type corresponds to the concrete event {@link BakerStakeUpdatedResult}.
     */
    UPDATE_BAKER_STAKE,
    /**
     * A baker's setting for restaking earnings was updated.
     * This event type corresponds to the concrete event {@link BakerSetRestakeEarningsResult}.
     */
    UPDATE_BAKER_RESTAKE_EARNINGS,
    /**
     * A baker's keys were updated.
     * This event type corresponds to the concrete event {@link BakerKeysUpdatedResult}.
     */
    UPDATE_BAKER_KEYS,
    /**
     * The keys of a specific credential were updated.
     * This event type corresponds to the concrete event {@link CredentialKeysUpdatedResult}.
     */
    UPDATE_CREDENTIAL_KEYS,
    /**
     * An encrypted amount was transferred.
     * This event type corresponds to the concrete event {@link EncryptedAmountTransferredResult}.
     */
    ENCRYPTED_AMOUNT_TRANSFER,
    /**
     * An acocunt encrypted an amount from public to encrypted balance.
     * This event type corresponds to the concrete event {@link EncryptedSelfAmountAddedResult}.
     */
    TRANSFER_TO_ENCRYPTED,
    /**
     * An account transferred part of its encrypted balance to its public balance.
     * This event type corresponds to the concrete event {@link TransferredToPublicResult}.
     */
    TRANSFER_TO_PUBLIC,
    /**
     * A transfer with schedule was performed.
     * This event type corresponds to the concrete event {@link TransferredWithScheduleResult}.
     */
    TRANSFER_WITH_SCHEDULE,
    /**
     * The credentials have been updated.
     * This event type corresponds to the concrete event {@link CredentialsUpdatedResult}.
     */
    UPDATE_CREDENTIALS,
    /**
     * Some data was registered on the chain.
     * This event type corresponds to the concrete event {@link DataRegisteredResult}.
     */
    REGISTER_DATA,
    /**
     * An account to account transfer with memo occurred.
     * This event type corresponds to the concrete event {@link AccountTransferWithMemoResult}.
     */
    TRANSFER_WITH_MEMO,
    /**
     * An encrypted amount was transferred with a memo.
     * This event type corresponds to the concrete event {@link EncryptedAmountTransferredWithMemoResult}.
     */
    ENCRYPTED_AMOUNT_TRANSFER_WITH_MEMO,
    /**
     * A transfer with schedule and a memo was performed.
     * This event type corresponds to the concrete event {@link TransferredWithScheduleAndMemoResult}.
     */
    TRANSFER_WITH_SCHEDULE_AND_MEMO,
    /**
     * A baker was configured.
     * This event type corresponds to the concrete event {@link BakerConfiguredResult}.
     */
    CONFIGURE_BAKER,
    /**
     * An account configured delegation.
     * This event type corresponds to the concrete event {@link DelegationConfiguredResult}.
     */
    CONFIGURE_DELEGATION,

    /*
      The types bellow used in case where the transaction had no effects.
     */

    /**
     * Nothing happened.
     * This event type corresponds to the concrete event {@link NoneResult}.
     */
    NONE,
    /**
     * Type of failed transaction is unknown.
     */
    NOT_KNOWN,
    ;

    /**
     * Parses {@link com.concordium.grpc.v2.TransactionType} to {@link TransactionType}.
     *
     * @param transactionType {@link com.concordium.grpc.v2.TransactionType} returned by the GRPC V2 API.
     * @return parsed {@link TransactionType}.
     */
    public static TransactionType parse(com.concordium.grpc.v2.TransactionType transactionType) {
        switch (transactionType) {
            case DEPLOY_MODULE: return DEPLOY_MODULE;
            case INIT_CONTRACT: return INIT_CONTRACT;
            case UPDATE: return UPDATE;
            case TRANSFER: return TRANSFER;
            case ADD_BAKER: return ADD_BAKER;
            case REMOVE_BAKER: return REMOVE_BAKER;
            case UPDATE_BAKER_STAKE: return UPDATE_BAKER_STAKE;
            case UPDATE_BAKER_RESTAKE_EARNINGS: return UPDATE_BAKER_RESTAKE_EARNINGS;
            case UPDATE_BAKER_KEYS: return UPDATE_BAKER_KEYS;
            case UPDATE_CREDENTIAL_KEYS: return UPDATE_CREDENTIAL_KEYS;
            case ENCRYPTED_AMOUNT_TRANSFER: return ENCRYPTED_AMOUNT_TRANSFER;
            case TRANSFER_TO_ENCRYPTED: return TRANSFER_TO_ENCRYPTED;
            case TRANSFER_TO_PUBLIC: return TRANSFER_TO_PUBLIC;
            case TRANSFER_WITH_SCHEDULE: return TRANSFER_WITH_SCHEDULE;
            case UPDATE_CREDENTIALS: return UPDATE_CREDENTIALS;
            case REGISTER_DATA: return REGISTER_DATA;
            case TRANSFER_WITH_MEMO: return TRANSFER_WITH_MEMO;
            case ENCRYPTED_AMOUNT_TRANSFER_WITH_MEMO: return ENCRYPTED_AMOUNT_TRANSFER_WITH_MEMO;
            case TRANSFER_WITH_SCHEDULE_AND_MEMO: return TRANSFER_WITH_SCHEDULE_AND_MEMO;
            case CONFIGURE_BAKER: return CONFIGURE_BAKER;
            case CONFIGURE_DELEGATION: return CONFIGURE_DELEGATION;
            default: throw new IllegalArgumentException("Unable to parse TransactionType: " + transactionType);
        }
    }

}
