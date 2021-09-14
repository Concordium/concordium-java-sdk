package com.concordium.sdk.responsetypes.transactionstatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TransactionContents {
    @JsonProperty("transferWithMemo")
    TRANSFER_WITH_MEMO,
    @JsonProperty("transferWithScheduleAndMemo")
    TRANSFER_WITH_SCHEDULE_AND_MEMO,
    @JsonProperty("encryptedAmountTransferWithMemo")
    ENCRYPTED_AMOUNT_TRANSFER_WITH_MEMO,
    @JsonProperty("updateCredentials")
    UPDATE_CREDENTIALS,
    @JsonProperty("transferWithSchedule")
    TRANSFER_WITH_SCHEDULE,
    @JsonProperty("transferToPublic")
    TRANSFER_TO_PUBLIC,
    @JsonProperty("encryptedAmountTransfer")
    ENCRYPTED_AMOUNT_TRANSFER,
    @JsonProperty("transferToEncrypted")
    TRANSFER_TO_ENCRYPTED,
    @JsonProperty("updateCredentialKeys")
    UPDATE_CREDENTIAL_KEYS,
    @JsonProperty("deployModule")
    DEPLOY_MODULE,
    @JsonProperty("transfer")
    TRANSFER,
    @JsonProperty("initial")
    INITIAL,
    @JsonProperty("normal")
    NORMAL,
    @JsonProperty("addBaker")
    ADD_BAKER,
    @JsonProperty("updateBakerStake")
    UPDATE_BAKER_STAKE,
    @JsonProperty("updateBakerKeys")
    UPDATE_BAKER_KEYS,
    @JsonProperty("updateBakerRestakeEarnings")
    UPDATE_BAKER_RESTAKE_EARNINGS,
    @JsonProperty("updateProtocol")
    UPDATE_PROTOCOL
}
