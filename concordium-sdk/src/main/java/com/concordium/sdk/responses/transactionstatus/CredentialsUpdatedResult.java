package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The credentials have been updated.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public final class CredentialsUpdatedResult implements TransactionResultEvent {

    /**
     * The account which credentials have been updated.
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

    public static CredentialsUpdatedResult from(AccountTransactionEffects.CredentialsUpdated credentialsUpdated, AccountAddress account) {
        return CredentialsUpdatedResult
                .builder()
                .account(account)
                .newCredIds(credentialsUpdated.getNewCredIdsList().stream().map(CredentialRegistrationId::from).collect(Collectors.toList()))
                .removedCredIds(credentialsUpdated.getRemovedCredIdsList().stream().map(CredentialRegistrationId::from).collect(Collectors.toList()))
                .newThreshold(credentialsUpdated.getNewThreshold().getValue())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CREDENTIALS_UPDATED;
    }
}
