package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.Credentials;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
public final class CredentialsUpdatedResult extends TransactionResultEvent {
    private AccountAddress account;
    private List<CredentialRegistrationId> newCredIds;
    private List<CredentialRegistrationId> removedCredIds;
    private final int newThreshold;

    @JsonCreator
    CredentialsUpdatedResult(@JsonProperty("account") String account,
                             @JsonProperty("newCredIds") List<String> newCredIds,
                             @JsonProperty("removedCredIds") List<String> removedCredIds,
                             @JsonProperty("newThreshold") String newThreshold) {

        if(!Objects.isNull(account)) {
            this.account = AccountAddress.from(account);
        }
        if (!Objects.isNull(newCredIds)) {
            this.newCredIds = new ArrayList<>();
            for (String newCredId : newCredIds) {
                this.newCredIds.add(CredentialRegistrationId.from(newCredId));
            }
        }
        if(!Objects.isNull(removedCredIds)) {
            this.removedCredIds = new ArrayList<>();
            for (String removedCredId : removedCredIds) {
                this.removedCredIds.add(CredentialRegistrationId.from(removedCredId));
            }
        }
        this.newThreshold = Integer.parseInt(newThreshold);
    }
}
