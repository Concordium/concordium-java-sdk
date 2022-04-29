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
    private CredentialRegistrationId regId;
    private AccountAddress account;

    @JsonCreator
    CredentialDeployedResult(@JsonProperty("regId") String regId,
                             @JsonProperty("account") String account) {
        if (!Objects.isNull(regId)) {
            this.regId = CredentialRegistrationId.from(regId);
        }
        if(!Objects.isNull(account)) {
            this.account = AccountAddress.from(account);
        }
    }
}
