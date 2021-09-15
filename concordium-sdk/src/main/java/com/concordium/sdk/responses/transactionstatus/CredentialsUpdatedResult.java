package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@ToString
public final class CredentialsUpdatedResult extends TransactionResultEvent {
    private AccountAddress account;
    private final List<String> newCredIds;
    private final List<String> removedCredIds;
    private final int newThreshold;

    @JsonCreator
    CredentialsUpdatedResult(@JsonProperty("account") String account,
                             @JsonProperty("newCredIds") List<String> newCredIds,
                             @JsonProperty("removedCredIds") List<String> removedCredIds,
                             @JsonProperty("newThreshold") String newThreshold) {

        if(!Objects.isNull(account)) {
            this.account = AccountAddress.from(account);
        }
        this.newCredIds = newCredIds;
        this.removedCredIds = removedCredIds;
        this.newThreshold = Integer.parseInt(newThreshold);
    }
}
