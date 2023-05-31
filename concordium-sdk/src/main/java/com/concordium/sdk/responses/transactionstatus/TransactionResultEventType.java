package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Events emitted by transactions.
 * Should match https://github.com/Concordium/concordium-base/blob/8dcee8746e40d663222aa3b4b04eaa3710e2779e/haskell-src/Concordium/Types/Execution.hs#L736
 */
public enum TransactionResultEventType {
    @JsonProperty("ModuleDeployed")
    MODULE_DEPLOYED,
    @JsonProperty("ContractInitialized")
    CONTRACT_INITIALIZED,
    @JsonProperty("Updated")
    CONTRACT_UPDATED,
    @JsonProperty("Transferred")
    TRANSFERRED,
    @JsonProperty("AccountCreated")
    ACCOUNT_CREATED,
    @JsonProperty("CredentialDeployed")
    CREDENTIAL_DEPLOYED,
    @JsonProperty("BakerAdded")
    BAKER_ADDED,
    @JsonProperty("BakerRemoved")
    BAKER_REMOVED,
    @JsonProperty("BakerStakeIncreased")
    BAKER_STAKE_INCREASED,
    @JsonProperty("BakerStakeDecreased")
    BAKER_STAKE_DECREASED,
    @JsonProperty("BakerSetRestakeEarnings")
    BAKER_SET_RESTAKE_EARNINGS,
    @JsonProperty("BakerKeysUpdated")
    BAKER_KEYS_UPDATED,
    @JsonProperty("CredentialKeysUpdated")
    CREDENTIAL_KEYS_UPDATED,
    @JsonProperty("NewEncryptedAmount")
    NEW_ENCRYPTED_AMOUNT,
    @JsonProperty("EncryptedAmountsRemoved")
    ENCRYPTED_AMOUNTS_REMOVED,
    @JsonProperty("AmountAddedByDecryption")
    AMOUNT_ADDED_BY_DECRYPTION,
    @JsonProperty("EncryptedSelfAmountAdded")
    ENCRYPTED_SELF_AMOUNT_ADDED,
    @JsonProperty("UpdateEnqueued")
    UPDATE_ENQUEUED,
    @JsonProperty("TransferredWithSchedule")
    TRANSFERRED_WITH_SCHEDULE,
    @JsonProperty("CredentialsUpdated")
    CREDENTIALS_UPDATED,
    @JsonProperty("DataRegistered")
    DATA_REGISTERED,
    @JsonProperty("TransferMemo")
    TRANSFER_MEMO,
    @JsonProperty("Interrupted")
    INTERRUPTED,
    @JsonProperty("Resumed")
    RESUMED,
    @JsonProperty("BakerSetOpenStatus")
    BAKER_SET_OPEN_STATUS,
    @JsonProperty("BakerSetMetadataURL")
    BAKER_SET_METADATA_URL,
    @JsonProperty("BakerSetTransactionFeeCommission")
    BAKER_SET_TRANSACTION_FEE_COMMISSION,
    @JsonProperty("BakerSetBakingRewardCommission")
    BAKER_SET_BAKING_REWARD_COMMISSION,
    @JsonProperty("BakerSetFinalizationRewardCommission")
    BAKER_SET_FINALIZATION_REWARD_COMMISSION,
    @JsonProperty("DelegationStakeIncreased")
    DELEGATION_STAKE_INCREASED,
    @JsonProperty("DelegationStakeDecreased")
    DELEGATION_STAKE_DECREASED,
    @JsonProperty("DelegationSetRestakeEarnings")
    DELEGATION_SET_RESTAKE_EARNINGS,
    @JsonProperty("DelegationSetDelegationTarget")
    DELEGATION_SET_DELEGATION_TARGET,
    @JsonProperty("DelegationAdded")
    DELEGATION_ADDED,
    @JsonProperty("DelegationRemoved")
    DELEGATION_REMOVED,
    @JsonProperty("Upgraded")
    UPGRADED,
    @JsonProperty("BakerStakeUpdated")
    BAKER_STAKE_UPDATED,
    @JsonProperty("EncryptedAmountTransferred")
    ENCRYPTED_AMOUNT_TRANSFERRED,
    @JsonProperty("TransfererredToEncrypted")
    TRANSFERRED_TO_ENCRYPTED,
    @JsonProperty("TransfererredPublic")
    TRANSFERRED_TO_PUBLIC,
    @JsonProperty("BakerConfigured")
    BAKER_CONFIGURED,
    @JsonProperty("DelegationConfigured")
    DELEGATION_CONFIGURED,

    TRANSFER_WITH_MEMO,
    ENCRYPTED_AMOUNT_TRANSFERRED_WITH_MEMO,

    CONTRACT_UPDATE_ISSUED,

    TRANSFERRED_WITH_SCHEDULE_AND_MEMO,

    /**
     * No effect other than payment from this transaction.
     */
    NONE,

    /**
     * Type of the failed transaction is not known due to serialization failure.
     */
    NOT_KNOWN;

    /**
     * Parses {@link TransactionType} to {@link TransactionResultEventType}.
     * @param transactionType {@link TransactionType} returned by the GRPC V2 API.
     * @return parsed {@link TransactionResultEventType}.
     */
    public static TransactionResultEventType parse(TransactionType transactionType) {
        switch (transactionType) {
            case DEPLOY_MODULE: return MODULE_DEPLOYED;
            case INIT_CONTRACT: return CONTRACT_INITIALIZED;
            case UPDATE: return CONTRACT_UPDATED;
            case TRANSFER: return TRANSFERRED;
            case ADD_BAKER: return BAKER_ADDED;
            case REMOVE_BAKER: return BAKER_REMOVED;
            case UPDATE_BAKER_STAKE: return BAKER_STAKE_UPDATED;
            case UPDATE_BAKER_RESTAKE_EARNINGS: return BAKER_SET_RESTAKE_EARNINGS;
            case UPDATE_BAKER_KEYS: return BAKER_KEYS_UPDATED;
            case UPDATE_CREDENTIAL_KEYS: return CREDENTIAL_KEYS_UPDATED;
            case ENCRYPTED_AMOUNT_TRANSFER: return ENCRYPTED_AMOUNT_TRANSFERRED;
            case TRANSFER_TO_ENCRYPTED: return TRANSFERRED_TO_ENCRYPTED;
            case TRANSFER_TO_PUBLIC: return TRANSFERRED_TO_PUBLIC;
            case TRANSFER_WITH_SCHEDULE: return TRANSFERRED_WITH_SCHEDULE;
            case UPDATE_CREDENTIALS: return CREDENTIALS_UPDATED;
            case REGISTER_DATA: return DATA_REGISTERED;
            case TRANSFER_WITH_MEMO: return TRANSFER_WITH_MEMO;
            case ENCRYPTED_AMOUNT_TRANSFER_WITH_MEMO: return ENCRYPTED_AMOUNT_TRANSFERRED_WITH_MEMO;
            case TRANSFER_WITH_SCHEDULE_AND_MEMO: return TRANSFERRED_WITH_SCHEDULE_AND_MEMO;
            case CONFIGURE_BAKER: return BAKER_CONFIGURED;
            case CONFIGURE_DELEGATION: return DELEGATION_CONFIGURED;
            case UNRECOGNIZED: return NOT_KNOWN;
            default: throw new IllegalArgumentException("Unable to parse TransactionType: " + transactionType);
        }
    }

    // Convenience methods for doing 'safe' casting.
    public <T> T convert(TransactionResultEvent event) {
        if (this != event.getType()) {
            throw new IllegalArgumentException("Unexpected conversion. Expected " + this + " but received " + event.getType());
        }
        return (T) event;
    }

}
