package com.concordium.sdk.responsetypes.transactionstatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TransactionType {
    @JsonProperty("accountTransaction")
    ACCOUNT_TRANSACTION,
    @JsonProperty("credentialDeploymentTransaction")
    CREDENTIAL_DEPLOYMENT_TRANSACTION,
    @JsonProperty("updateTransaction")
    UPDATE_TRANSACTION
}
