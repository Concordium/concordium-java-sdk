package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The credentials have been updated.
 */
@Getter
@ToString
public final class CredentialsUpdatedResult extends TransactionResultEvent {

    /**
     * The account which credentials has been updated.
     */
    private final AccountAddress account;

    /**
     * The new {@link CredentialRegistrationId}`s.
     */
    private final List<CredentialRegistrationId> newCredIds;

    /**
     * The removed {@link CredentialRegistrationId}`s.
     */
    private final List<CredentialRegistrationId> removedCredIds;

    /**
     * The new account threshold
     */
    private final int newThreshold;

    @JsonCreator
    CredentialsUpdatedResult(@JsonProperty("account") AccountAddress account,
                             @JsonProperty("newCredIds") List<String> newCredIds,
                             @JsonProperty("removedCredIds") List<String> removedCredIds,
                             @JsonProperty("newThreshold") String newThreshold) {
        this.account = account;
        this.newCredIds = new ArrayList<>();
        for (String newCredId : newCredIds) {
            this.newCredIds.add(CredentialRegistrationId.from(newCredId));
        }
        this.removedCredIds = new ArrayList<>();
        for (String removedCredId : removedCredIds) {
            this.removedCredIds.add(CredentialRegistrationId.from(removedCredId));
        }
        this.newThreshold = Integer.parseInt(newThreshold);
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CREDENTIALS_UPDATED;
    }
}
