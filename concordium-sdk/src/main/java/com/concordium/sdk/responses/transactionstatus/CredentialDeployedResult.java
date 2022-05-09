package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public final class CredentialDeployedResult extends TransactionResultEvent {
    private final String regId;
    private AccountAddress account;

    @JsonCreator
    CredentialDeployedResult(@JsonProperty("regId") String regId,
                             @JsonProperty("account") String account) {
        this.regId = regId;
        if(!Objects.isNull(account)) {
            this.account = AccountAddress.from(account);
        }
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CREDENTIAL_DEPLOYED;
    }
}
