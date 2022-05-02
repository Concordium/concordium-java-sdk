package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public final class CredentialDeployedResult extends TransactionResultEvent {
    private final CredentialRegistrationId regId;
    private final AccountAddress account;

    @JsonCreator
    CredentialDeployedResult(@JsonProperty("regId") CredentialRegistrationId regId,
                             @JsonProperty("account") AccountAddress account) {
        this.regId = regId;
        this.account = account;
    }
}
