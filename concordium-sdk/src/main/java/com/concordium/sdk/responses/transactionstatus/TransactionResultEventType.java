package com.concordium.sdk.responses.transactionstatus;

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
    UPDATEA_ENQUEUED,
    @JsonProperty("TransferredWithSchedule")
    TRANSFERRED_WITH_SCHEDULE,
    @JsonProperty("CredentialsUpdated")
    CREDENTIALS_UPDATED,
    @JsonProperty("DataRegistered")
    DATA_REGISTERED,
    @JsonProperty("TransferMemo")
    TRANSFER_MEMO;

    // Convenience methods for do 'safe' casting.
    public <T> T convert(TransactionResultEvent event) {
        if (this != event.getType()) {
            throw new IllegalArgumentException("Unexpected conversion. Expected " + this + " but received " + event.getType());
        }
        return (T) event;
    }

}
