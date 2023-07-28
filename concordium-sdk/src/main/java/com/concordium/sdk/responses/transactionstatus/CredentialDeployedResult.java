package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class CredentialDeployedResult implements TransactionResultEvent {
    private final CredentialRegistrationId regId;
    private final AccountAddress account;

    @JsonCreator
    CredentialDeployedResult(@JsonProperty("regId") CredentialRegistrationId regId,
                             @JsonProperty("account") AccountAddress account) {
        this.regId = regId;
        this.account = account;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CREDENTIAL_DEPLOYED;
    }
}
